package keepcoding.io.guedr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ForecastActivity extends AppCompatActivity {
    public static final String PREFERENCE_SHOW_CELSIUS = "showCelsius";

    protected static String TAG = ForecastActivity.class.getCanonicalName();
    private static final int ID_OPCION_1 = 1;
    private static final int ID_OPCION_2 = 2;

    private static final int REQUEST_UNITS = 1;
    protected boolean mShowCelsius = true;
    private Forecast mForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // creamos el
        // modelo
        mForecast = new Forecast(25, 10, 35, "Soleado con alguna nube", R.drawable.ico_01);

        // recuperamos el valor que habiamos guardado para mShowCelsius en disco
        mShowCelsius = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PREFERENCE_SHOW_CELSIUS, true);

        updateForecast();
    }

    private void updateForecast() {
        // accedemos a las vistas de la interfaz
        ImageView forecastImage = (ImageView) findViewById(R.id.forecast_image);
        TextView maxTempText = (TextView) findViewById(R.id.max_temp);
        TextView minTempText = (TextView) findViewById(R.id.min_temp);
        TextView humidityText = (TextView) findViewById(R.id.humidity);
        TextView forecastDescription = (TextView) findViewById(R.id.forecast_description);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // metodo de insertar opciones de menu por codigo directo
        //menu.add(0, ID_OPCION_1, 0, "Opcion de Menu 1");
        //menu.add(0, ID_OPCION_2, 0, "Opcion de Menu 2");

        // metodo de insertar opciones de menu usando un recurso de menus
        getMenuInflater().inflate(R.menu.menu_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superReturn = super.onOptionsItemSelected(item);

        //if (item.getItemId() == ID_OPCION_1) {
        //    Log.v(TAG, "Se ha pulsado la opcion 1");
        //} else if (item.getItemId() == ID_OPCION_2) {
        //    Log.v(TAG, "Se ha pulsado la opcion 2");
        //}

        if (item.getItemId() == R.id.menu_show_settings) {
            Log.v(TAG, "Se ha pulsado la opcion de ajustes");

            // creamos intent explicito para abrir la pantalla de ajustes
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UNITS) {
            // estamos volviendo de la pantalla de SettingsActivity
            // miro como ha ido el resultado
            if (resultCode == RESULT_OK) {
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

                Snackbar.make(findViewById(android.R.id.content), snackBarText, Snackbar.LENGTH_LONG)
                        .setAction("Deshacer", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // deshacemos los cambios
                                mShowCelsius = oldShowCelsius;
                                PreferenceManager.getDefaultSharedPreferences(ForecastActivity.this)
                                        .edit()
                                        .putBoolean(PREFERENCE_SHOW_CELSIUS, mShowCelsius)
                                        .apply();
                                updateForecast();
                            }
                        })
                        .show();

                // persistimos las preferencias del usuario respecto a las unidades
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putBoolean(PREFERENCE_SHOW_CELSIUS, mShowCelsius);
//                editor.apply();

                PreferenceManager.getDefaultSharedPreferences(this)
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, mShowCelsius)
                        .apply();

                updateForecast();
            } else if (resultCode == RESULT_CANCELED) {
                // no hago nada porque el usuario ha cancelado la seleccion de unidades
            }
        }

    }
}
