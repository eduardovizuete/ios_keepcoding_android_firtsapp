package keepcoding.io.guedr.model;

import java.io.Serializable;
import java.util.LinkedList;

public class City implements Serializable {

    private String mName;
    private LinkedList<Forecast> mForecast;

    public City(String name, LinkedList<Forecast> forecast) {
        mName = name;
        mForecast = forecast;
    }

    public City(String name) {
        this(name, null);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public LinkedList<Forecast> getForecast() {
        return mForecast;
    }

    public void setForecast(LinkedList<Forecast> forecast) {
        mForecast = forecast;
    }

    @Override
    public String toString() {
        return getName();
    }
}
