package com.example.bruhshua.carpool.interfaces;

import com.example.bruhshua.carpool.Model.User;

/**
 * Created by bruhshua on 11/7/17.
 */

public interface SplashScreenInterface {

    interface View{
        void toLoginPage();
        void toHomePage(User authUser);

    }

    interface Presenter{
        void notAuthenticated();
        void authenticated(User authUser);
    }
}
