package com.example.bruhshua.carpool.Presenters;

import android.app.Activity;

import com.example.bruhshua.carpool.Model.FirebaseLogin;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.interfaces.LoginActivityInterface;

/**
 * Created by bruhshua on 11/1/17.
 */

public class LoginActivityPresenter implements LoginActivityInterface.Presenter {

    private FirebaseLogin firebase;
    private LoginActivityInterface.View mView;

    public LoginActivityPresenter(LoginActivityInterface.View view){
        firebase = new FirebaseLogin(this);
        mView = view;
    }

    public void login(String email, String password, Activity activity){
        firebase.login(email,password,activity);
    }

    @Override
    public void loadUser(User user) {
        mView.attemptLogin(user);
    }

}
