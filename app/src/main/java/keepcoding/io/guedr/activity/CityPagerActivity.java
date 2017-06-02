package keepcoding.io.guedr.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.fragment.ForecastFragment;
import keepcoding.io.guedr.model.Cities;
import keepcoding.io.guedr.model.City;
import keepcoding.io.guedr.model.Forecast;

public class CityPagerActivity extends AppCompatActivity {

    private ViewPager mPager;
    private Cities mCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pager);

        // Vamos a decirle a la actividad que use nuestra toolbar personalizada
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mCities = new Cities();
        CityPagerAdapter adapter = new CityPagerAdapter(getFragmentManager(), mCities);

        // le damos al viewpager su adapter para que muestre tantos fragments como diga el modelo
        mPager.setAdapter(adapter);

        // vamos a enterarnos de cuando cambia de pagina el viewpager
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateCityInfo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateCityInfo(0);
    }

    private void updateCityInfo(int position) {
        String cityName = mCities.getCity(position).getName();
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(cityName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_pager, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superReturn = super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.previous) {
            // movemos el pager hacia atras
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            return true;
        } else if (item.getItemId() == R.id.next) {
            // movemos el pager hacia adelante
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            return true;
        }

        return superReturn;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuPrev = menu.findItem(R.id.previous);
        MenuItem menuNext = menu.findItem(R.id.next);

        menuPrev.setEnabled(mPager.getCurrentItem() > 0);
        menuNext.setEnabled(mPager.getCurrentItem() < mCities.getCount() - 1);

//        if (mPager.getCurrentItem() > 0) {
//            // puedo ir hacia atras
//            menuPrev.setEnabled(true);
//        } else {
//            menuPrev.setEnabled(false);
//        }
//
//        if (mPager.getCurrentItem() < mCities.getCount() - 1) {
//            // puedo ir hacia adelante
//            menuNext.setEnabled(true);
//        } else {
//            menuNext.setEnabled(false);
//        }

        return true;
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
