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
        mCities.add(new City("Madrid"));
        mCities.add(new City("Jaén"));
        mCities.add(new City("Quito"));
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
