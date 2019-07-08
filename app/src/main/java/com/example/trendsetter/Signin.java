package com.example.trendsetter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class Signin extends Fragment {
    EditText email,password;
    Button signin,newcomer;
    String DocumentID1;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    public Signin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_signin, container, false);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs111",Context.MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("done",false);
        if (b== true){
            Profile_Pic p = new Profile_Pic();
            getFragmentManager().beginTransaction().replace(R.id.Frame,p).commit();
        }

        email = view.findViewById(R.id.Email);
        password = view.findViewById(R.id.Password);
        signin = view.findViewById(R.id.signin);
        newcomer = view.findViewById(R.id.newcomer);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        auth = FirebaseAuth.getInstance();

        newcomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup signup = new Signup();
                getFragmentManager().beginTransaction().replace(R.id.Frame, signup).commit();
            }
        });



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(getActivity(),"All fields are required",Toast.LENGTH_LONG).show();
                }else {
                    auth.signInWithEmailAndPassword(txt_email,txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                         checkEmailVerification();
                                    }else {
                                        Toast.makeText(getActivity(),"Username or Password is incorrect",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
        return view;
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = auth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if (emailflag){
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs111", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("done",true);
            editor.commit();






            Profile home = new Profile();
            getFragmentManager().beginTransaction().replace(R.id.Frame, home).commit();
        }else {
            Toast.makeText(getActivity(),"Verify your Email",Toast.LENGTH_SHORT).show();
            auth.signOut();
        }
    }
}
