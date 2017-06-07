package keepcoding.io.guedr.activity;

import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.fragment.CityPagerFragment;
import keepcoding.io.guedr.model.Cities;

public class CityPagerActivity extends AppCompatActivity {

    public static final String EXTRA_CITY_INDEX = "EXTRA_CITY_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pager);

        // Vamos a decirle a la actividad que use nuestra toolbar personalizada
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        // recibimos el indice de la ciudad que queremos mostrar
        int cityIndex = getIntent().getIntExtra(EXTRA_CITY_INDEX, 0);

        // añadimos, si hace falta, el CityPagerFragment a nuestra jerarquia
        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(R.id.fragment_city_pager) == null) {
            // hay hueco, y habiendo hueco metemos el fragment
            CityPagerFragment fragment = CityPagerFragment.newInstance(cityIndex);
            fm.beginTransaction()
                    .add(R.id.fragment_city_pager, fragment)
                    .commit();
        }
    }
}

