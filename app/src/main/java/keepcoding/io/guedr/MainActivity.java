package keepcoding.io.guedr;

import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    protected static String TAG = MainActivity.class.getCanonicalName();

    protected Button changeToStone;
    protected Button changeToDonkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeToStone = (Button) findViewById(R.id.change_stone_system);
        changeToStone.setOnClickListener(this);

        changeToDonkey = (Button) findViewById(R.id.change_donkey_system);
        changeToDonkey.setOnClickListener(this);

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
