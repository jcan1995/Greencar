package com.example.bruhshua.carpool.interfaces;

/**
 * Created by bruhshua on 11/3/17.
 */

public interface RegisterActivityInterface {

    interface View{
        void registrationSuccessful();
        void registrationUnsuccessful();

    }

    interface Presenter{
        void registrationisSuccessful(Boolean isSuccessful);

    }

}
