package keepcoding.io.guedr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        findViewById(R.id.accept_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptSettings();
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSettings();
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.units_rg);
        radioGroup.check(R.id.farenheit_rb);
    }

    private void cancelSettings() {
    }

    private void acceptSettings() {
    }
}
