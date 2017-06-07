package keepcoding.io.guedr.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.model.Cities;
import keepcoding.io.guedr.model.City;

public class CityPagerFragment extends Fragment {

    private static  final String ARG_INITIAL_CITY_INDEX = "ARG_INITIAL_CITY_INDEX";

    private ViewPager mPager;
    private Cities mCities;
    private int mInitialCityIndex = 0;

    public static CityPagerFragment newInstance(int cityIndex) {
        CityPagerFragment fragment = new CityPagerFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_INITIAL_CITY_INDEX, cityIndex);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mInitialCityIndex = getArguments().getInt(ARG_INITIAL_CITY_INDEX, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_city_pager, container, false);

        mPager = (ViewPager) root.findViewById(R.id.view_pager);
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

        moveToCity(mInitialCityIndex);
        updateCityInfo(mInitialCityIndex);

        return root;
    }

    private void moveToCity(int cityIndex) {
        mPager.setCurrentItem(cityIndex);
    }

    private void updateCityInfo(int position) {
        String cityName = mCities.getCity(position).getName();
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
            ActionBar toolbar = parentActivity.getSupportActionBar();
            if (toolbar != null) {
                toolbar.setTitle(cityName);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_pager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superReturn = super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.previous) {
            // movemos el pager hacia atras
            moveToCity(mPager.getCurrentItem() - 1);

            return true;
        } else if (item.getItemId() == R.id.next) {
            // movemos el pager hacia adelante
            moveToCity(mPager.getCurrentItem() + 1);

            return true;
        }

        return superReturn;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
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

