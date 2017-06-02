package keepcoding.io.guedr.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.activity.SettingsActivity;
import keepcoding.io.guedr.model.Forecast;

public class ForecastFragment extends Fragment {

    public static final String PREFERENCE_SHOW_CELSIUS = "showCelsius";

    protected static String TAG = ForecastFragment.class.getCanonicalName();
    private static final int ID_OPCION_1 = 1;
    private static final int ID_OPCION_2 = 2;

    private static final int REQUEST_UNITS = 1;
    protected boolean mShowCelsius = true;
    private Forecast mForecast;
    private View mRoot;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRoot = inflater.inflate(R.layout.fragment_forecast, container, false);

        // creamos el modelo de pega
        mForecast = new Forecast(25, 10, 35, "Soleado con alguna nube", R.drawable.ico_01);

        // recuperamos el valor que habiamos guardado para mShowCelsius en disco
        mShowCelsius = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PREFERENCE_SHOW_CELSIUS, true);

        updateForecast();

        return mRoot;
    }

    private void updateForecast() {
        // accedemos a las vistas de la interfaz
        ImageView forecastImage = (ImageView) mRoot.findViewById(R.id.forecast_image);
        TextView maxTempText = (TextView) mRoot.findViewById(R.id.max_temp);
        TextView minTempText = (TextView) mRoot.findViewById(R.id.min_temp);
        TextView humidityText = (TextView) mRoot.findViewById(R.id.humidity);
        TextView forecastDescription = (TextView) mRoot.findViewById(R.id.forecast_description);

        // calculamos las temperaturas en funcion de las unidades
        float maxTemp = 0;
        float minTemp = 0;
        String unitsToShow = null;
        if (mShowCelsius) {
            maxTemp = mForecast.getMaxTemp(Forecast.CELSIUS);
            minTemp = mForecast.getMinTemp(Forecast.CELSIUS);
            unitsToShow = "ºC";
        } else {
            maxTemp = mForecast.getMaxTemp(Forecast.FARENHEIT);
            minTemp = mForecast.getMinTemp(Forecast.FARENHEIT);
            unitsToShow = "F";
        }

        // Actualizamos la vista con el modelo
        forecastImage.setImageResource(mForecast.getIcon());
        maxTempText.setText(getString(R.string.max_temp_format, maxTemp, unitsToShow));
        minTempText.setText(getString(R.string.min_temp_format, minTemp, unitsToShow));
        humidityText.setText(getString(R.string.hunidity_format, mForecast.getHumidity()));
        forecastDescription.setText(mForecast.getDescription());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // metodo de insertar opciones de menu usando un recurso de menus
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superReturn = super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.menu_show_settings) {
            Log.v(TAG, "Se ha pulsado la opcion de ajustes");

            // creamos intent explicito para abrir la pantalla de ajustes
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            // pasamos datos a la siguiente actividad: las unidades
            if (mShowCelsius) {
                settingsIntent.putExtra(SettingsActivity.EXTRA_UNITS, R.id.celsius_rb);
            } else {
                settingsIntent.putExtra(SettingsActivity.EXTRA_UNITS, R.id.farenheit_rb);
            }

            // esto lo usariamos si la actividad destino no devolviera datos
            //startActivity(settingsIntent);

            // esto lo usamos porque SettingsActivity devuelve datos que nos interesan
            startActivityForResult(settingsIntent, REQUEST_UNITS);
            return true;
        }

        return superReturn;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UNITS) {
            // estamos volviendo de la pantalla de SettingsActivity
            // miro como ha ido el resultado
            if (resultCode == Activity.RESULT_OK) {
                final boolean oldShowCelsius = mShowCelsius; // por si acaso el usuario se arrepiente
                String snackBarText = null;

                // todo ha ido bien, hago caso a los datos de entrada
                // (la opcion por defecto aqui es absurda... pero hay que rellenarla)
                int optionSelected = data.getIntExtra(SettingsActivity.EXTRA_UNITS, R.id.farenheit_rb);
                if (optionSelected == R.id.farenheit_rb) {
//                    Toast.makeText(this, "Se ha seleccionado Farenheit", Toast.LENGTH_LONG).show();
                    mShowCelsius = false;
                    snackBarText = "Se ha seleccionado Farenheit";
                } else {
//                    Toast.makeText(this, "Se ha seleccionado Celsius", Toast.LENGTH_LONG).show();
                    snackBarText = "Se ha seleccionado Celsius";
                    mShowCelsius = true;
                }

                if (getView() != null) {
                    Snackbar.make(getView(), snackBarText, Snackbar.LENGTH_LONG)
                            .setAction("Deshacer", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // deshacemos los cambios
                                    mShowCelsius = oldShowCelsius;
                                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                                            .edit()
                                            .putBoolean(PREFERENCE_SHOW_CELSIUS, mShowCelsius)
                                            .apply();
                                    updateForecast();
                                }
                            })
                            .show();
                }

                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, mShowCelsius)
                        .apply();

                updateForecast();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // no hago nada porque el usuario ha cancelado la seleccion de unidades
            }
        }
    }
}
