package keepcoding.io.guedr;

import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    protected static String TAG = MainActivity.class.getCanonicalName();

    protected Button changeToStone;
    protected Button changeToDonkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // vamos a obtener una referencia al imageView del offline image
        final ImageView offlineImage = (ImageView) findViewById(R.id.offline_weather_image);

        // esto es muy raro que lo hagamos, pero te puede ayudar a entender
        // que son las clases anonimas en realidad
        changeToStone = (Button) findViewById(R.id.change_stone_system);
        changeToStone.setOnClickListener(new StoneButtonListener(offlineImage));

        // esto es lo que mas probablemente termines haciendo en tu codigo
        changeToDonkey = (Button) findViewById(R.id.change_donkey_system);
        changeToDonkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Me han pedido burro");
                offlineImage.setImageResource(R.drawable.offline_weather2);
            }
        });

        Log.v(TAG, "Hola Amundio, he pasado por OnCreate");
    }

    public void changeSystemClick(View v) {
        Log.v(TAG, "Han llamado a changeSystem");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.v(TAG, "Nos han llamado a onSaveInstanceState");
        outState.putString("clave", "valor");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_stone_system:
                Log.v(TAG, "Me han pedido piedra");
                break;
            case R.id.change_donkey_system:
                Log.v(TAG, "Me han pedido burro");
                break;
            default:
                Log.v(TAG, "No se que me han pedido");
        }


//        if (v.getId() == R.id.change_stone_system) {
//            Log.v(TAG, "Me han pedido piedra");
//        } else if (v.getId() == R.id.change_donkey_system){
//            Log.v(TAG, "Me han pedido burro");
//        } else {
//            Log.v(TAG, "No se que me han pedido");
//        }

    }
}

class StoneButtonListener implements View.OnClickListener {
    private final ImageView offlineImage;

    public StoneButtonListener(ImageView offlineImage) {
        this.offlineImage = offlineImage;
    }


    @Override
    public void onClick(View v) {
        Log.v("Lo que sea", "Me han pedido piedra");
    }
}
