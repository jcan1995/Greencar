package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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

import com.example.bruhshua.carpool.R;

import com.example.bruhshua.carpool.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bruhshua on 5/19/17.
 */

public class RegisterActivity extends Activity{

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference users_ref;
    private final int SELECT_IMAGE = 1234;

    private EditText etEmail;
    private EditText etPassword;
    private EditText etPhoneNumber;
    private EditText etUserName;
    private Button bRegisterUser;
    private ImageView ivProfilePicture;
    private TextView tvAddPicture;

    private Uri selectedImage;
    private String key;
    private String filepath;
    private String filename;
    private ProgressDialog dialog;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        Log.d("LoginActivity","Inside onCreate RegisterActivity");

        mStorageRef = FirebaseStorage.getInstance().getReference();

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance(); //create FirebaseAuth object and get the instance
        users_ref = database.getReference("users");

        etEmail = (EditText) findViewById(R.id.tvRegEmail);
        etPassword = (EditText) findViewById(R.id.tvRegPassword);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etUserName = (EditText) findViewById(R.id.etUserName);
        bRegisterUser = (Button) findViewById(R.id.bRegister);
        bRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        ivProfilePicture = (ImageView) findViewById(R.id.ivAddImage);
        //ivA = (TextView) findViewById(R.id.tvAddPicture);
        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {
            //Todo: send user to the Main Activty
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1234:
                //Do something
                if(requestCode == SELECT_IMAGE && resultCode == RESULT_OK
                        && null != data){
                    selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filepath = cursor.getString(columnIndex);
                    filename = filepath.substring(filepath.lastIndexOf("/")+1);
                    Log.d("addPicTest","filename: " + filename);

                    cursor.close();

                    ImageView ivProfilePicture = (ImageView) findViewById(R.id.ivAddImage);
                    try {
                        ivProfilePicture.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                }else{
                    Log.d("addPicTest","Something else is wrong.");

                }
                break;
            default:
                //Default case
                break;
        }
    }

    public void registerUser(){

        Log.d("RegisterActivity","Inside registerUser");

        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            //if the email text field is empty
            Toast.makeText(getApplicationContext(),"Please Input Your Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //if the password text field is empty
            Toast.makeText(getApplicationContext(),"Please Input Your Password",Toast.LENGTH_LONG).show();
            return;
        }

        dialog = new ProgressDialog(this);
        dialog.setMessage("Registering User...");
        dialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        final String userName = etUserName.getText().toString();
                        String phoneNumber = etPhoneNumber.getText().toString();

                        User newUser = new User(phoneNumber,email,userName,"images/" +userName+ "/"+filename,filepath);

                        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                users_ref.child(key).child(userName).push().child("password").setValue(password);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Map<String, User> users = new HashMap<String, User>();
                        users.put(userName,newUser);

                        key = users_ref.push().getKey();
                        users_ref.child(key).setValue(users);

                        mStorageRef = mStorageRef.child("images/" +userName+ "/"+filename);
                        mStorageRef.putFile(selectedImage)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.d("register_test","Success");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("register_test","Failure");

                                    }
                                });

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        //Todo: add user to database

                    }else{
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration Failed.", Toast.LENGTH_SHORT).show();

                    }

                }
            });

    }

    public void backToLogIn(View view){
        //send user back to the log in activity
        startActivity(new Intent(this,LoginActivity.class));
    }
}
