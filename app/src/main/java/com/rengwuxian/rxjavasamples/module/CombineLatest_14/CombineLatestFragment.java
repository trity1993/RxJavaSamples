package com.rengwuxian.rxjavasamples.module.CombineLatest_14;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rengwuxian.rxjavasamples.BaseFragment;
import com.rengwuxian.rxjavasamples.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func3;

import static android.text.TextUtils.isEmpty;
import static android.util.Patterns.EMAIL_ADDRESS;

/**
 * combineLatest的使用进行对表达验证控制
 * Created by trity on 18/5/16.
 */
public class CombineLatestFragment extends BaseFragment {

    @Bind(R.id.et_email)
    EditText etEmail;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.et_pwd_confirm)
    EditText etPwdConfirm;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private Observable<CharSequence> emailChangeObservable;
    private Observable<CharSequence> passwordChangeObservable;
    private Observable<CharSequence> passwordConfirmChangeObservable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combine_latest, container, false);
        ButterKnife.bind(this, view);

        emailChangeObservable = Observable.create(new TextViewTextOnSubscribe(etEmail)).skip(1);
        passwordChangeObservable = Observable.create(new TextViewTextOnSubscribe(etPwd)).skip(1);
        passwordConfirmChangeObservable = Observable.create(new TextViewTextOnSubscribe(etPwdConfirm)).skip(1);
        combineLatestEvents();
        return view;
    }

    private void combineLatestEvents() {
        subscription = Observable.combineLatest
                (emailChangeObservable, passwordChangeObservable, passwordConfirmChangeObservable, new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence newEmail, CharSequence newPassword, CharSequence passwordConfirm) {
                        boolean emailValid = !isEmpty(newEmail) &&
                                EMAIL_ADDRESS.matcher(newEmail).matches();
                        if (!emailValid) {
                            etEmail.setError("邮箱不合法");
                        }

                        boolean passValid = !isEmpty(newPassword);
                        if (!passValid) {
                            etPwd.setError("密码不为空");
                        }

                        boolean passConfirmValid = !isEmpty(passwordConfirm) && TextUtils.equals(newPassword,passwordConfirm);

                        if (!passConfirmValid) {
                            etPwdConfirm.setError("两次密码需要一致");
                        }

                        return emailValid && passValid && passConfirmValid;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(aBoolean){
                            btnSubmit.setBackgroundColor(Color.RED);
                        }else{
                            btnSubmit.setBackgroundColor(Color.GRAY);
                        }
                    }
                });
    }

    @OnClick(R.id.btn_submit)
    public void onClick(View view){
        Toast.makeText(getActivity(),"click",Toast.LENGTH_SHORT).show();
    }


    /*@OnTextChanged({R.id.et_email, R.id.et_pwd, R.id.et_pwd_confirm})
    public void onTextChanager(CharSequence c) {

    }*/


    @Override
    protected int getContentRes() {
        return 0;
    }

    @Override
    protected int getTitleRes() {
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
