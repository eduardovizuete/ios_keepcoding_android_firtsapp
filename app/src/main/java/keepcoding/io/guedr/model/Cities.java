package keepcoding.io.guedr.model;

import java.util.LinkedList;
import java.util.List;

import keepcoding.io.guedr.R;

public class Cities {

    private static Cities mInstance;

    private LinkedList<City> mCities;

    public static Cities getInstance() {
        if (mInstance == null) {
            // no existe una instancia estatica de la clase, la creo
            mInstance = new Cities();
        }

        return mInstance;
    }

    public Cities() {
        mCities = new LinkedList<>();
        mCities.add(new City("Madrid", new Forecast(25, 10, 35, "Soleado con alguna nube", R.drawable.ico_02)));
        mCities.add(new City("Jaén", new Forecast(36, 23, 19, "Sol a tope", R.drawable.ico_01)));
        mCities.add(new City("Quito", new Forecast(30, 15, 40, "Arcoiris", R.drawable.ico_10)));
    }

    public City getCity(int index) {
        return mCities.get(index);
    }

    public LinkedList<City> getCities() {
        return mCities;
    }

    public int getCount() {
        return mCities.size();
    }
}
