package keepcoding.io.guedr.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.LinkedList;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.activity.DetailActivity;
import keepcoding.io.guedr.activity.SettingsActivity;
import keepcoding.io.guedr.adapter.ForecasteRecyclerViewAdapter;
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
    private RecyclerView mList;

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

        // accedemos al RecyclerView con findViewById
        mList = (RecyclerView) mRoot.findViewById(R.id.forecast_list);

        // le decimos como debe visualizar el RecyclerView (su LayoutManager)
        //mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.recycler_columns)));

        // le decimos como debe animarse el RecyclerView
        mList.setItemAnimator(new DefaultItemAnimator());

        // por ultimo, un RecyclerView necesita un adapter
        // esto lo haremos en updateForecast

        updateForecast();

        return mRoot;
    }

    private void updateForecast() {

        // Accedemos al modelo
        final LinkedList<Forecast> forecast = mCity.getForecast();

        // accedemos al viewSwitcher
        final ViewSwitcher viewSwitcher = (ViewSwitcher) mRoot.findViewById(R.id.view_switcher);
        viewSwitcher.setInAnimation(getActivity(), android.R.anim.fade_in);
        viewSwitcher.setOutAnimation(getActivity(), android.R.anim.fade_out);

        if (forecast == null) {
            AsyncTask<City, Integer, LinkedList<Forecast>> weatherDownloader = new AsyncTask<City, Integer, LinkedList<Forecast>>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    viewSwitcher.setDisplayedChild(LOADING_VIEW_INDEX); // mostramos el progressbar
                }

                @Override
                protected LinkedList<Forecast> doInBackground(City... params) {
                    publishProgress(50);
                    return downloadForecast(params[0]);
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onCancelled(LinkedList<Forecast> forecast) {
                    super.onCancelled();
                }

                @Override
                protected void onPostExecute(LinkedList<Forecast> forecast) {

                    if (forecast != null) {
                        // no ha habido errores descargando la informacion del tiempo, actualizo la interfaz
                        mCity.setForecast(forecast);

                        updateForecast();

                        viewSwitcher.setDisplayedChild(FORECAST_VIEW_INDEX);
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle(R.string.error);
                        alertDialog.setMessage(R.string.couldnt_download_weather);
                        alertDialog.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateForecast();
                            }
                        });

                        alertDialog.show();
                    }
                }
            };

            weatherDownloader.execute(mCity);

            return;
        }

        // asignamos el adapter al RecyclerView
        ForecasteRecyclerViewAdapter adapter = new ForecasteRecyclerViewAdapter(forecast, mShowCelsius);

        // nos suscribimos a las pulsaciones de las tarjetas del RecyclerView
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mList.getChildAdapterPosition(v);
                Forecast forecastDetail = forecast.get(position);

                // lanzamos la actividad de detalle (ojo, esto seria mejor lanzarlo desde la actividad
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_FORECAST, forecastDetail);
                intent.putExtra(DetailActivity.EXTRA_SHOWCELSIUS, mShowCelsius);
                startActivity(intent);
            }
        });
        mList.setAdapter(adapter);
    }

    private LinkedList<Forecast> downloadForecast(City city) {
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

            // nos descargamos todos los dias de la prediccion
            LinkedList<Forecast> forecasts = new LinkedList<>();

            for (int i = 0; i < list.length(); i++) {
                JSONObject today = list.getJSONObject(i);
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

                Forecast forecast = new Forecast(max, min, humidity, description, iconResource);
                forecasts.add(forecast);
            }

            Thread.sleep(2000);

            return forecasts;
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
