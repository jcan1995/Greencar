package com.example.bruhshua.carpool.Model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.bruhshua.carpool.Activities.LoginActivity;
import com.example.bruhshua.carpool.interfaces.RegisterActivityInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by bruhshua on 11/3/17.
 */

public class FirebaseRegisterUser {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference users_ref;
    private StorageReference mStorageRef;

    private RegisterActivityInterface.Presenter mPresenter;

    public FirebaseRegisterUser(RegisterActivityInterface.Presenter presenter){
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance(); //create FirebaseAuth object and get the instance
        users_ref = database.getReference("users");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mPresenter = presenter;

    }

    public void registerNewUserWithFirebase(final User user, String password, final Uri selectedImage, final String filename, Activity activity){
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    final String key = users_ref.push().getKey();

                    mStorageRef = mStorageRef.child("images/" +user.getUserName()+ "/"+filename);
                    mStorageRef.putFile(selectedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    @SuppressWarnings("VisibleForTests")
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    user.setKey(key);
                                    user.setDownloadUrl(downloadUrl.toString());
                                    users_ref.child(key).setValue(user);

                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(user.getUserName())
                                            .setPhotoUri(selectedImage)
                                            .build();

                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mPresenter.registrationisSuccessful(true);
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mPresenter.registrationisSuccessful(false);

                                }
                            });

                }
            }
        });
    }

    public void registerNewUserWithFirebase(final User user, String password, Activity activity){

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    final String key = users_ref.push().getKey();

                    user.setKey(key);
                    users_ref.child(key).setValue(user);
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(user.getUserName())
                            .build();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mPresenter.registrationisSuccessful(true);

                        }
                    });
                }else{
                    mPresenter.registrationisSuccessful(false);

                }
            }
        });
    }
}
