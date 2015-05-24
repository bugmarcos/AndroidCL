package com.app.calllog.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.app.calllog.R;

/**
 * Created by Akash on 24-May-15.
 */
public class BaseFragment extends Fragment {


    protected void setFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }

    protected void setFragmentWithBackstack(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(getActivity().getClass().getCanonicalName())
                .commit();
    }

    protected void clearBackstack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

}
