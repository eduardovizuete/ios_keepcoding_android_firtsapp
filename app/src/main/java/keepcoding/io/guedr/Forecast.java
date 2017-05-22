package keepcoding.io.guedr;

public class Forecast {
    private float nMaxTemp;
    private float nMinTemp;
    private float mHumidity;
    private String mDescription;
    private int mIcon;


    public Forecast(float nMaxTemp, float nMinTemp, float mHumidity, String mDescription, int mIcon) {
        this.nMaxTemp = nMaxTemp;
        this.nMinTemp = nMinTemp;
        this.mHumidity = mHumidity;
        this.mDescription = mDescription;
        this.mIcon = mIcon;
    }

    public float getMaxTemp() {
        return nMaxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.nMaxTemp = maxTemp;
    }

    public float getMinTemp() {
        return nMinTemp;
    }

    public void setMinTemp(float minTemp) {
        this.nMinTemp = minTemp;
    }

    public float getHumidity() {
        return mHumidity;
    }

    public void setHumidity(float humidity) {
        this.mHumidity = humidity;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }
}
