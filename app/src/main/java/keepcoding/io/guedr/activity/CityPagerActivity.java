package keepcoding.io.guedr.activity;

import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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
        //toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        // indicar una flecha en la parte superior del toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // recibimos el indice de la ciudad que queremos mostrar
        int cityIndex = getIntent().getIntExtra(EXTRA_CITY_INDEX, 0);

        // añadimos, si hace falta, el CityPagerFragment a nuestra jerarquia
        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(R.id.view_pager_fragment) == null) {
            // hay hueco, y habiendo hueco metemos el fragment
            CityPagerFragment fragment = CityPagerFragment.newInstance(cityIndex);
            fm.beginTransaction()
                    .add(R.id.view_pager_fragment, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superValue = super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            // han pulsado la fecha de back de la ActionBar, finalizamos la actividad
            finish();
            return true;
        }

        return superValue;
    }
}

