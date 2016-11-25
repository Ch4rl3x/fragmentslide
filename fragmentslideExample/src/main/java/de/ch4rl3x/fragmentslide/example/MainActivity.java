package de.ch4rl3x.fragmentslide.example;

import android.os.Bundle;

import de.ch4rl3x.fragmentslide.example.abstracts.BaseActivity;
import de.ch4rl3x.fragmentslide.example.fragments.NavigationDrawerFragment;
import de.ch4rl3x.fragmentslide.example.utilities.Mask;


public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startMask(Mask.Blue, true);
    }
}
