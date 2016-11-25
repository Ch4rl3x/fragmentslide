package de.ch4rl3x.fragmentslide.example.utilities;

import de.ch4rl3x.fragmentslide.example.R;
import de.ch4rl3x.fragmentslide.example.abstracts.BaseFragment;
import de.ch4rl3x.fragmentslide.example.fragments.Blue;
import de.ch4rl3x.fragmentslide.example.fragments.Red;
import de.ch4rl3x.fragmentslide.example.fragments.Green;
import de.ch4rl3x.fragmentslide.example.fragments.BlueSubMask;
import de.ch4rl3x.fragmentslide.example.fragments.BlueSubMaskWithRedTheme;
import de.ch4rl3x.fragmentslide.interfaces.IMask;
import de.ch4rl3x.fragmentslide.interfaces.ITheme;

/**
 * Created by Ch4rl3x on 25.11.2016.
 */
public enum Mask implements IMask {

    Blue(null, Blue.class, Theme.ThemeBlue, R.layout.fragment_blue, -1),
    BlueSubMask(Blue, BlueSubMask.class, Theme.ThemeBlue, R.layout.fragment_main_sub_mask, R.string.blueSubMask),
    BlueSubMaskWithRedTheme(Blue, BlueSubMaskWithRedTheme.class, Theme.ThemeRed, R.layout.fragment_main_sub_mask_with_red_theme, R.string.blueSubMaskWithRedTheme),
    Green(null, Green.class, Theme.ThemeGreen, R.layout.fragment_green,  R.string.greenMask),
    Red(null, Red.class, Theme.ThemeRed, R.layout.fragment_red, R.string.redMask);


    private Mask parentView;
    private Class<? extends BaseFragment> fragClass;
    private Theme theme;
    private int layout;
    private int title;
    private boolean hideMenuToggle;

    public static final String INTENT_KEY_MASKE = "maske";
    public static final int MASKEN_LEVEL = 3;

    Mask(Mask parentView, Class<? extends BaseFragment> fragClass, Theme theme, int layout, int title, boolean hideMenuToggle) {
        this.parentView = parentView;
        this.fragClass = fragClass;
        this.theme = theme;
        this.layout = layout;
        this.title = title;
        this.hideMenuToggle = hideMenuToggle;
    }

    Mask(Mask parentView, Class<? extends BaseFragment> fragClass, Theme theme, int layout, int title) {
        this.parentView = parentView;
        this.fragClass = fragClass;
        this.theme = theme;
        this.layout = layout;
        this.title = title;
        this.hideMenuToggle = false;
    }

    @Override
    public Mask getParentView() {
        return parentView;
    }

    @Override
    public Class<? extends BaseFragment> getFragmentClass() {
        return fragClass;
    }

    @Override
    public ITheme getTheme() {
        return theme;
    }

    @Override
    public int getLayout() {
        return layout;
    }

    @Override
    public int getTitle() {
        return title;
    }

    @Override
    public boolean isHideMenuToggle() {
        return hideMenuToggle;
    }

    /**
     * Liefert die Views anhand des Namens des Enums
     *
     * @param viewName der Name
     * @return die Views
     */
    public static Mask findByName(String viewName) {
        Mask result = null;
        for(Mask maske : values()) {
            if(maske.name().equals(viewName)) {
                result = maske;
            }
        }
        return result;
    }

    @Override
    public int getEbene() {
        int ebene = 0;
        Mask maske = this;
        while((maske = maske.getParentView()) != null) {
            ebene++;
        }
        return ebene;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int getAppTitle() {
        return R.string.app_name;
    }
}
