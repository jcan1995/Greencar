package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Presenters.RegisterActivityPresenter;
import com.example.bruhshua.carpool.R;

import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.interfaces.RegisterActivityInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bruhshua on 5/19/17.
 */

public class RegisterActivity extends Activity implements RegisterActivityInterface.View{

    @BindView(R.id.etRegEmail)
    EditText etEmail;

    @BindView(R.id.etRegPassword)
    EditText etPassword;

    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;

    @BindView(R.id.etUserName)
    EditText etUserName;
    private RegisterActivityPresenter registerActivityPresenter;

    private Uri selectedImage;
    private String filepath;
    private String filename;

    private final int SELECT_IMAGE = 1234;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        Log.d("LoginActivity","Inside onCreate RegisterActivity");
        ButterKnife.bind(this);
        registerActivityPresenter = new RegisterActivityPresenter(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1234:
                if(requestCode == SELECT_IMAGE && resultCode == RESULT_OK && null != data){
                    selectedImage = data.getData();//*
                    String picturePath;
                    Cursor cursor = getContentResolver().query(selectedImage,null,null,null,null);
                   if(cursor == null) {
                       picturePath = selectedImage.getPath();
                       cursor.close();
                   }

                    else {
                       cursor.moveToFirst();
                       //int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                       picturePath = cursor.getString(0);
                       cursor.close();
                   }
                    ImageView ivProfilePicture = (ImageView) findViewById(R.id.ivAddImage);
                    filename = picturePath.substring(picturePath.lastIndexOf("/")+1);
                    Log.d("filename:",filename);

                    try {
                        ivProfilePicture.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            default:
                //Default case
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this,LoginActivity.class));

    }

    public void backToLogIn(View view){
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void registerUser(View view){

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String userName = etUserName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please Input Your Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Please Input Your Password",Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User(email, userName, "", phoneNumber, "", 0.0);//Missing downloadUrl and key! setAttributes in Model!
        dialog = new ProgressDialog(this);
        dialog.setMessage("Registering User...");
        dialog.show();

        if(selectedImage == null)
            registerActivityPresenter.registerNewUser(user, password, this);
        else
            registerActivityPresenter.registerNewUser(user, password, selectedImage, filename,this);


    }

    public void getProfilePicture(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }

    @Override
    public void registrationSuccessful() {
        dialog.dismiss();
        Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void registrationUnsuccessful() {
        dialog.dismiss();
        Toast.makeText(getApplicationContext(), "Registration unSuccessful.", Toast.LENGTH_SHORT).show();

    }
}
