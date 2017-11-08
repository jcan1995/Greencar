package com.example.bruhshua.carpool.interfaces;

import com.example.bruhshua.carpool.Model.User;

/**
 * Created by bruhshua on 11/3/17.
 */

public interface LoginActivityInterface {

    interface View{
        void attemptLogin(User user);

    }
    interface Presenter{
        void loadUser(User user);
    }
}
