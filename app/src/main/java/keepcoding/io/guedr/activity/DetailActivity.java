package keepcoding.io.guedr.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.model.Forecast;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_FORECAST = "EXTRA_FORECAST";
    public static final String EXTRA_SHOWCELSIUS = "EXTRA_SHOWCELSIUS";

    private ImageView mForecastImage;
    private TextView mMaxTempText;
    private TextView mMinTempText;
    private TextView mHumidityText;
    private TextView mForecastDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // accedemos a los datos de entrada de la actividad anterior
        Forecast forecast = (Forecast) getIntent().getSerializableExtra(EXTRA_FORECAST);
        boolean showCelsius = getIntent().getBooleanExtra(EXTRA_SHOWCELSIUS, true);

        // accedemos a las vistas de la interfaz
        mForecastImage = (ImageView) findViewById(R.id.forecast_image);
        mMaxTempText = (TextView) findViewById(R.id.max_temp);
        mMinTempText = (TextView) findViewById(R.id.min_temp);
        mHumidityText = (TextView) findViewById(R.id.humidity);
        mForecastDescription = (TextView) findViewById(R.id.forecast_description);

        // calculamos las temperaturas en funcion de las unidades
        float maxTemp = 0;
        float minTemp = 0;
        String unitsToShow = null;
        if (showCelsius) {
            maxTemp = forecast.getMaxTemp(Forecast.CELSIUS);
            minTemp = forecast.getMinTemp(Forecast.CELSIUS);
            unitsToShow = "ºC";
        } else {
            maxTemp = forecast.getMaxTemp(Forecast.FARENHEIT);
            minTemp = forecast.getMinTemp(Forecast.FARENHEIT);
            unitsToShow = "F";
        }

        // Actualizamos la vista con el modelo
        mForecastImage.setImageResource(forecast.getIcon());
        mMaxTempText.setText(getString(R.string.max_temp_format, maxTemp, unitsToShow));
        mMinTempText.setText(getString(R.string.min_temp_format, minTemp, unitsToShow));
        mHumidityText.setText(getString(R.string.hunidity_format, forecast.getHumidity()));
        mForecastDescription.setText(forecast.getDescription());
    }
}
