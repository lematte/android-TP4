package mpisi2.devtp.tp4;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

public class MyPreferenceFragment extends PreferenceFragment {

    public MyPreferenceFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
