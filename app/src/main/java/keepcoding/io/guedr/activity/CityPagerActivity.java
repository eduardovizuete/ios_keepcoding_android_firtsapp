package keepcoding.io.guedr.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.fragment.ForecastFragment;
import keepcoding.io.guedr.model.Cities;
import keepcoding.io.guedr.model.City;
import keepcoding.io.guedr.model.Forecast;

public class CityPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pager);

        // Vamos a decirle a la actividad que use nuestra toolbar personalizada
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        Cities cities = new Cities();
        CityPagerAdapter adapter = new CityPagerAdapter(getFragmentManager(), cities);

        // le damos al viewpager su adapter para que muestre tantos fragments como diga el modelo
        pager.setAdapter(adapter);
    }

}

class CityPagerAdapter extends FragmentPagerAdapter {

    private Cities mCities;

    public CityPagerAdapter(FragmentManager fm, Cities cities) {
        super(fm);
        mCities = cities;
    }

    @Override
    public Fragment getItem(int position) {
        ForecastFragment fragment = ForecastFragment.newInstance(mCities.getCity(position));

        return fragment;
    }

    @Override
    public int getCount() {
        return mCities.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        City city = mCities.getCity(position);
        String cityName = city.getName();

        return cityName;
    }
}
