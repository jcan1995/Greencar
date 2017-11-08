package com.example.bruhshua.carpool.Presenters;

import com.example.bruhshua.carpool.Model.FirebaseAuthenticate;
import com.example.bruhshua.carpool.Model.FirebaseLogin;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.interfaces.SplashScreenInterface;

/**
 * Created by bruhshua on 11/7/17.
 */

public class SplashScreenPresenter implements SplashScreenInterface.Presenter  {

    private SplashScreenInterface.View mView;
    private FirebaseAuthenticate firebaseAuthenticate;

    public SplashScreenPresenter(SplashScreenInterface.View view){
        mView = view;
        firebaseAuthenticate = new FirebaseAuthenticate(this);
    }

    public void attemptLogin(){
        firebaseAuthenticate.checkAuth();
    }


    @Override
    public void notAuthenticated() {
        mView.toLoginPage();
    }

    @Override
    public void authenticated(User authUser) {

        mView.toHomePage(authUser);
    }
}
