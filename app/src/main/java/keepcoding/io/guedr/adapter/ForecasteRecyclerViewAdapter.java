package keepcoding.io.guedr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.model.Forecast;

public class ForecasteRecyclerViewAdapter extends RecyclerView.Adapter<ForecasteRecyclerViewAdapter.ForecastViewHolder> {

    private LinkedList<Forecast> mForecasts;
    private boolean mShowCelsius;

    public ForecasteRecyclerViewAdapter(LinkedList<Forecast> forecast, boolean showCelsius) {
        super();
        mForecasts = forecast;
        mShowCelsius = showCelsius;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        holder.bindForecast(mForecasts.get(position), mShowCelsius);
    }

    @Override
    public int getItemCount() {
        return mForecasts.size();
    }

    protected class ForecastViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mForecastImage;
        private final TextView mMaxTempText;
        private final TextView mMinTempText;
        private final TextView mHumidityText;
        private final TextView mForecastDescription;

        public ForecastViewHolder(View itemView) {
            super(itemView);

            // accedemos a las vistas de la interfaz
            mForecastImage = (ImageView) itemView.findViewById(R.id.forecast_image);
            mMaxTempText = (TextView) itemView.findViewById(R.id.max_temp);
            mMinTempText = (TextView) itemView.findViewById(R.id.min_temp);
            mHumidityText = (TextView) itemView.findViewById(R.id.humidity);
            mForecastDescription = (TextView) itemView.findViewById(R.id.forecast_description);
        }

        public void bindForecast(Forecast forecast, boolean showCelsius) {
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

            // Accedemos al contexto
            Context context = mForecastImage.getContext();

            // Actualizamos la vista con el modelo
            mForecastImage.setImageResource(forecast.getIcon());
            mMaxTempText.setText(context.getString(R.string.max_temp_format, maxTemp, unitsToShow));
            mMinTempText.setText(context.getString(R.string.min_temp_format, minTemp, unitsToShow));
            mHumidityText.setText(context.getString(R.string.hunidity_format, forecast.getHumidity()));
            mForecastDescription.setText(forecast.getDescription());
        }
    }
}
