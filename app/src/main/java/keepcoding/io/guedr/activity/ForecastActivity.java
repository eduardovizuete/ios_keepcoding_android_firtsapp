package keepcoding.io.guedr.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.fragment.CityListFragment;
import keepcoding.io.guedr.fragment.CityPagerFragment;
import keepcoding.io.guedr.model.Cities;
import keepcoding.io.guedr.model.City;

public class ForecastActivity extends AppCompatActivity implements CityListFragment.OnCityselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // chuleta para saber detalles del dispositivo real que esta ejecutando esta aplicacion
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int dpWidth = (int) (width / metrics.density);
        int dpHeight = (int) (height / metrics.density);
        String model = Build.MODEL;
        int dpi = metrics.densityDpi;

        setContentView(R.layout.activity_forecast);

        // cargamos a mano el fragment
        FragmentManager fm = getFragmentManager();

        // averiguamos que interfaz hemos cargado
        // eso lo averiguamos si en la interfaz tenemos un framelayout concreto
        if (findViewById(R.id.city_list_fragment) != null) {
            // hemos cargado una interfaz que tiene el hueco para el fragment CityListFragment
            // comprobamos primero que no tenemos ya añadido el fragment a nuestra jerarquia
            if (fm.findFragmentById(R.id.city_list_fragment) == null) {
                // no esta añadido, lo añadire con una transaccion de fragments
                // creo la instancia del nuevo fragment
                Cities cities = Cities.getInstance();
                CityListFragment fragment = CityListFragment.newInstance(cities.getCities());

                fm.beginTransaction()
                        .add(R.id.city_list_fragment, fragment)
                        .commit();
            }
        }

        if (findViewById(R.id.view_pager_fragment) != null) {
            // hemos cargado una interfaz que tiene el hueco para el fragment CityPagerFragment
            // comprobamos primero que no tenemos ya añadido el fragment
            if (fm.findFragmentById(R.id.view_pager_fragment) == null) {
                fm.beginTransaction()
                        .add(R.id.view_pager_fragment, CityPagerFragment.newInstance(0))
                        .commit();
            }
        }
    }

    @Override
    public void onCitySelected(City city, int position) {
        // vamos a ver que fragments tenemos cargados en la interfaz
        FragmentManager fm = getFragmentManager();
        CityPagerFragment cityPagerFragment = (CityPagerFragment) fm.findFragmentById(R.id.view_pager_fragment);

        if (cityPagerFragment != null) {
            // tenemos un pager, vamos a ver como podemos decirle que cambie de ciudad
            cityPagerFragment.moveToCity(position);
        } else {
            // no tenemos un pager, lazamos la actividad CityPagerActivity
            Intent intent = new Intent(this, CityPagerActivity.class);
            intent.putExtra(CityPagerActivity.EXTRA_CITY_INDEX, position);
            startActivity(intent);
        }
    }
}
