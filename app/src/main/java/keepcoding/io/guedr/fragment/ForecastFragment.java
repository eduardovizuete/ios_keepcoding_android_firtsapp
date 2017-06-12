package keepcoding.io.guedr.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.activity.SettingsActivity;
import keepcoding.io.guedr.model.City;
import keepcoding.io.guedr.model.Forecast;

public class ForecastFragment extends Fragment {

    public static final String PREFERENCE_SHOW_CELSIUS = "showCelsius";

    protected static String TAG = ForecastFragment.class.getCanonicalName();

    private static final int ID_OPCION_1 = 1;
    private static final int ID_OPCION_2 = 2;

    private static final String ARG_CITY = "city";
    private static final int REQUEST_UNITS = 1;
    private static final int LOADING_VIEW_INDEX = 0;
    private static final int FORECAST_VIEW_INDEX = 1;

    protected boolean mShowCelsius = true;
    private City mCity;
    private View mRoot;

    public static ForecastFragment newInstance(City city) {
        ForecastFragment fragment = new ForecastFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_CITY, city);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            // recuperamos el modelo que nos pasen como argumento
            mCity = (City) getArguments().getSerializable(ARG_CITY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRoot = inflater.inflate(R.layout.fragment_forecast, container, false);

        // recuperamos el valor que habiamos guardado para mShowCelsius en disco
        mShowCelsius = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PREFERENCE_SHOW_CELSIUS, true);

        updateForecast();

        return mRoot;
    }

    private void updateForecast() {
        // accedemos a las vistas de la interfaz
        TextView cityName = (TextView) mRoot.findViewById(R.id.city);
        ImageView forecastImage = (ImageView) mRoot.findViewById(R.id.forecast_image);
        TextView maxTempText = (TextView) mRoot.findViewById(R.id.max_temp);
        TextView minTempText = (TextView) mRoot.findViewById(R.id.min_temp);
        TextView humidityText = (TextView) mRoot.findViewById(R.id.humidity);
        TextView forecastDescription = (TextView) mRoot.findViewById(R.id.forecast_description);

        // establecemos el nombre de la ciudad
        cityName.setText(mCity.getName());

        // Accedemos al modelo
        Forecast forecast = mCity.getForecast();

        // accedemos al viewSwitcher
        final ViewSwitcher viewSwitcher = (ViewSwitcher) mRoot.findViewById(R.id.view_switcher);
        viewSwitcher.setInAnimation(getActivity(), android.R.anim.fade_in);
        viewSwitcher.setOutAnimation(getActivity(), android.R.anim.fade_out);

        if (forecast == null) {
            AsyncTask<City, Integer, Forecast> weatherDownloader = new AsyncTask<City, Integer, Forecast>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    viewSwitcher.setDisplayedChild(LOADING_VIEW_INDEX); // mostramos el progressbar
                }

                @Override
                protected Forecast doInBackground(City... params) {
                    publishProgress(50);
                    return downloadForecast(params[0]);
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onCancelled(Forecast forecast) {
                    super.onCancelled();
                }

                @Override
                protected void onPostExecute(Forecast forecast) {

                    if (forecast != null) {
                        // no ha habido errores descargando la informacion del tiempo, actualizo la interfaz
                        mCity.setForecast(forecast);

                        updateForecast();

                        viewSwitcher.setDisplayedChild(FORECAST_VIEW_INDEX);
                    }
                }
            };

            weatherDownloader.execute(mCity);

            return;
        }

        // calculamos las temperaturas en funcion de las unidades
        float maxTemp = 0;
        float minTemp = 0;
        String unitsToShow = null;
        if (mShowCelsius) {
            maxTemp = forecast.getMaxTemp(Forecast.CELSIUS);
            minTemp = forecast.getMinTemp(Forecast.CELSIUS);
            unitsToShow = "ºC";
        } else {
            maxTemp = forecast.getMaxTemp(Forecast.FARENHEIT);
            minTemp = forecast.getMinTemp(Forecast.FARENHEIT);
            unitsToShow = "F";
        }

        // Actualizamos la vista con el modelo
        forecastImage.setImageResource(forecast.getIcon());
        maxTempText.setText(getString(R.string.max_temp_format, maxTemp, unitsToShow));
        minTempText.setText(getString(R.string.min_temp_format, minTemp, unitsToShow));
        humidityText.setText(getString(R.string.hunidity_format, forecast.getHumidity()));
        forecastDescription.setText(forecast.getDescription());
    }

    private Forecast downloadForecast(City city) {
        URL url = null;
        InputStream input = null;

        try {
            // nos descargamos la informacion del tiempo a machete
            url = new URL(String.format("http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&lang=sp&units=metric&appid=9e654b60f16d59bce6afc1b7208bcebf", city.getName()));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            byte data[] = new byte[1024];
            int downloadedBytes;
            input = con.getInputStream();
            StringBuilder sb = new StringBuilder();
            while ((downloadedBytes = input.read(data)) != -1) {
                sb.append(new String(data, 0, downloadedBytes));
            }

            // Analizamos los datos para convertirlos de JSON a algo que podamos manejar en codigo
            JSONObject jsonRoot = new JSONObject(sb.toString());
            JSONArray list = jsonRoot.getJSONArray("list");
            JSONObject today = list.getJSONObject(0);
            float max = (float) today.getJSONObject("temp").getDouble("max");
            float min = (float) today.getJSONObject("temp").getDouble("min");
            float humidity = (float) today.getDouble("humidity");
            String description = today.getJSONArray("weather").getJSONObject(0).getString("description");
            String iconString = today.getJSONArray("weather").getJSONObject(0).getString("icon");

            // convertimos el texto iconString a drawable
            iconString = iconString.substring(0, iconString.length() - 1);
            int iconInt = Integer.parseInt(iconString);
            int iconResource = R.drawable.ico_01;

            switch (iconInt) {
                case 1: iconResource = R.drawable.ico_01; break;
                case 2: iconResource = R.drawable.ico_02; break;
                case 3: iconResource = R.drawable.ico_03; break;
                case 4: iconResource = R.drawable.ico_04; break;
                case 9: iconResource = R.drawable.ico_09; break;
                case 10: iconResource = R.drawable.ico_10; break;
                case 11: iconResource = R.drawable.ico_11; break;
                case 13: iconResource = R.drawable.ico_13; break;
                case 50: iconResource = R.drawable.ico_50; break;
            }

            Thread.sleep(5000);

            return new Forecast(max, min, humidity, description, iconResource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return  null;
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
