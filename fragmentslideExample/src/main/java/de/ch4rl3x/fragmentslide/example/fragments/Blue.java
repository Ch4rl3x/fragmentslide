package de.ch4rl3x.fragmentslide.example.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import de.ch4rl3x.fragmentslide.example.R;
import de.ch4rl3x.fragmentslide.example.abstracts.BaseFragment;
import de.ch4rl3x.fragmentslide.example.utilities.Mask;
import de.ch4rl3x.fragmentslide.fragment.AbstractBaseFragment;
import de.ch4rl3x.fragmentslide.interfaces.IOnRevertClickListener;
import de.ch4rl3x.fragmentslide.interfaces.IRevertToastRunnable;
import de.ch4rl3x.fragmentslide.runnable.AbstractRevertToastRunnable;
import de.ch4rl3x.fragmentslide.runnable.RevertClickListener;

public class Blue extends BaseFragment implements View.OnClickListener {

    @Override
    public void onFragmentStarted(Bundle initialData) {
        super.onFragmentStarted(initialData);

        findViewById(R.id.subMaskMain).setOnClickListener(this);
        findViewById(R.id.subMaskMainWithRedTheme).setOnClickListener(this);

        enableFabButton(android.R.drawable.ic_menu_delete, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRevertToast("Revert Text", new AbstractRevertToastRunnable() {
                    @Override
                    protected void innerRun() {
                        Toast.makeText(getActiviyAbstract(), "Reverable Runnable runs now!", Toast.LENGTH_LONG).show();
                    }
                }, new RevertClickListener() {

                    @Override
                    public void onRevertClicked(View view) {
                        Toast.makeText(getActiviyAbstract(), "On Revert clicked!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.subMaskMain:
                startMask(Mask.BlueSubMask);
                break;
            case R.id.subMaskMainWithRedTheme:
                startMask(Mask.BlueSubMaskWithRedTheme);
                break;
        }
    }


}


