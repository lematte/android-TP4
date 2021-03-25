package mpisi2.devtp.tp4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.os.Bundle;

public class MyPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        MyPreferenceFragment fragment = new MyPreferenceFragment();

        FragmentTransaction ft =  getFragmentManager().beginTransaction();

        ft.add(R.id.mycontent, fragment).commit();
    }
}