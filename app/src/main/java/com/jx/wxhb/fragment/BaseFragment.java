package com.jx.wxhb.fragment;

import android.support.v4.app.*;
import android.view.View;
import android.view.ViewGroup;

/**
 * Desc
 * Created by Jun on 2017/2/16.
 */

public abstract class BaseFragment extends Fragment {

    protected void addFragment(Fragment fragment, int wrap){
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(wrap,fragment).commitAllowingStateLoss();
    }

    protected void hideParentView() {
        if (isAdded() && getActivity() != null) {
            try {
                ((ViewGroup) this.getView().getParent()).setVisibility(View.GONE);
            } catch (ClassCastException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    protected void showParentView() {
        if (isAdded() && getActivity() != null) {
            try {
                ((ViewGroup) this.getView().getParent()).setVisibility(View.VISIBLE);
            } catch (ClassCastException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
