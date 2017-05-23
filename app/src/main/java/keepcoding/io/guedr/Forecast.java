package keepcoding.io.guedr;

public class Forecast {
    private float mMaxTemp;
    private float mMinTemp;
    private float mHumidity;
    private String mDescription;
    private int mIcon;

    public Forecast(float maxTemp, float minTemp, float humidity, String description, int icon) {
        mMaxTemp = maxTemp;
        mMinTemp = minTemp;
        mHumidity = humidity;
        mDescription = description;
        mIcon = icon;
    }

    public float getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        mMaxTemp = maxTemp;
    }

    public float getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(float minTemp) {
        mMinTemp = minTemp;
    }

    public float getHumidity() {
        return mHumidity;
    }

    public void setHumidity(float humidity) {
        mHumidity = humidity;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }
}
