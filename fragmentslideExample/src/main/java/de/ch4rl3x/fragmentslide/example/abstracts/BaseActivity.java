package de.ch4rl3x.fragmentslide.example.abstracts;

import java.util.ArrayList;

import de.ch4rl3x.fragmentslide.AbstractBaseActivity;
import de.ch4rl3x.fragmentslide.example.utilities.Mask;
import de.ch4rl3x.fragmentslide.example.R;
import de.ch4rl3x.fragmentslide.example.fragments.NavigationDrawerFragment;
import de.ch4rl3x.fragmentslide.interfaces.IMask;


public class BaseActivity extends AbstractBaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected int setTitleRes() {
        return getCurrentMask().getTitle();
    }

    @Override
    public IMask findMaskeByName(String arg0) {
        return Mask.findByName(arg0);
    }

    @Override
    public int[] setFragmentContainer() {
        return findFragmentContainer();
    }

    private int[] findFragmentContainer() {
        ArrayList<Integer> list = new ArrayList<Integer>();

        if (findViewById(R.id.container1) != null) {
            list.add(Integer.valueOf(R.id.container1));
        }

        if (findViewById(R.id.container2) != null) {
            list.add(Integer.valueOf(R.id.container2));
        }

        if (findViewById(R.id.container3) != null) {
            list.add(Integer.valueOf(R.id.container3));
        }

        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    @Override
    public int[] setSeparartor() {
        return findSeparator();
    }

    private int[] findSeparator() {
        ArrayList<Integer> list = new ArrayList<Integer>();

        if (findViewById(R.id.separator1) != null) {
            list.add(Integer.valueOf(R.id.separator1));
        }

        if (findViewById(R.id.separator2) != null) {
            list.add(Integer.valueOf(R.id.separator2));
        }

        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    @Override
    protected int setToolbarID() {
        return R.id.toolbar;
    }

    @Override
    protected int setDrawerLayoutID() {
        return R.id.drawer_layout;
    }

    @Override
    protected int setNavigationDrawerID() {
        return R.id.navigation_drawer;
    }

    @Override
    public void onNavigationDrawerClosing() {
        if (getCurrentMask() != null) {
            refreshActionBar();
            setActionBarTitle();
        }
    }

    @Override
    public void onNavigationDrawerOpening() {
        if (getCurrentMask() != null) {
            setActionBarTitle();
        }
    }
}
