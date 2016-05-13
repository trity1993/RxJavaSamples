// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples;

import android.app.AlertDialog;
import android.app.Fragment;

import butterknife.OnClick;
import rx.Subscription;

public abstract class BaseFragment extends Fragment {
    protected final String TAG=getClass().getSimpleName();
    protected Subscription subscription;

    @OnClick(R.id.tipBt)
    public void tip() {
        AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setMessage(getContentRes())
                .create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    protected abstract int getContentRes();

    protected abstract int getTitleRes();
}
