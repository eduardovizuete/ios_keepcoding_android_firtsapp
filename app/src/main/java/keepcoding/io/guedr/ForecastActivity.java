package keepcoding.io.guedr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastActivity extends AppCompatActivity {
    protected static String TAG = ForecastActivity.class.getCanonicalName();
    private static final int ID_OPCION_1 = 1;
    private static final int ID_OPCION_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // creamos el modelo
        Forecast forecast = new Forecast(25, 10, 35, "Soleado con alguna nube", R.drawable.ico_01);
        setForecast(forecast);
    }

    private void setForecast(Forecast forecast) {
        // accedemos a las vistas de la interfaz
        ImageView forecastImage = (ImageView) findViewById(R.id.forecast_image);
        TextView maxTemp = (TextView) findViewById(R.id.max_temp);
        TextView minTemp = (TextView) findViewById(R.id.min_temp);
        TextView humidity = (TextView) findViewById(R.id.humidity);
        TextView forecastDescription = (TextView) findViewById(R.id.forecast_description);

        // Actualizamos la vista con el modelo
        forecastImage.setImageResource(forecast.getIcon());
        maxTemp.setText(getString(R.string.max_temp_format, forecast.getMaxTemp()));
        minTemp.setText(getString(R.string.min_temp_format, forecast.getMinTemp()));
        humidity.setText(getString(R.string.hunidity_format, forecast.getHumidity()));
        forecastDescription.setText(forecast.getDescription());
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

            // intent explicito
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);

            return true;
        }

        return superReturn;
    }
}
