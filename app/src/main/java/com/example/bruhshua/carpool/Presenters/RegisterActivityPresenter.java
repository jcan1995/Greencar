package com.example.bruhshua.carpool.Presenters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.bruhshua.carpool.Model.FirebaseRegisterUser;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.interfaces.RegisterActivityInterface;

/**
 * Created by bruhshua on 11/3/17.
 */

public class RegisterActivityPresenter implements RegisterActivityInterface.Presenter {

    private FirebaseRegisterUser firebaseRegisterUser;
    private RegisterActivityInterface.View mView;

    public RegisterActivityPresenter(RegisterActivityInterface.View view){
        firebaseRegisterUser = new FirebaseRegisterUser(this);
        mView = view;

    }

    public void registerNewUser(User user, String password, Activity activity){
        firebaseRegisterUser.registerNewUserWithFirebase(user,password, activity);
    }

    public void registerNewUser(User user, String password, Uri selectedImage, String filename, Activity activity){
        firebaseRegisterUser.registerNewUserWithFirebase(user,password,selectedImage,filename,activity);
    }


    @Override
    public void registrationisSuccessful(Boolean isSuccessful) {
        if(isSuccessful)
            mView.registrationSuccessful();
        else
            mView.registrationUnsuccessful();
    }
}
