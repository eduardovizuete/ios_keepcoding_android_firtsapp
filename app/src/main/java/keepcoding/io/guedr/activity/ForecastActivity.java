package keepcoding.io.guedr.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.fragment.CityListFragment;
import keepcoding.io.guedr.model.Cities;
import keepcoding.io.guedr.model.City;

public class ForecastActivity extends AppCompatActivity implements CityListFragment.OnCityselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // cargamos a mano el fragment
        FragmentManager fm = getFragmentManager();

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

    @Override
    public void onCitySelected(City city, int position) {
        Intent intent = new Intent(this, CityPagerActivity.class);
        intent.putExtra(CityPagerActivity.EXTRA_CITY_INDEX, position);
        startActivity(intent);
    }
}
