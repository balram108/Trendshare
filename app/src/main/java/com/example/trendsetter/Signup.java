package com.example.trendsetter;


import android.media.MediaPlayer;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Signup extends Fragment {
    EditText fullname,enteremail,password;
    Button register;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseFirestore db;
    Map<String,Object>map;
    String docId;




    public Signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_signup, container, false);

        fullname=view.findViewById(R.id.fullname);
        enteremail=view.findViewById(R.id.Enteremail);
        password=view.findViewById(R.id.Password);
        register=view.findViewById(R.id.register);
        db = FirebaseFirestore.getInstance();
        map = new HashMap<>();

        auth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_fullname = fullname.getText().toString();
                String txt_enteremail = enteremail.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_fullname) || TextUtils.isEmpty(txt_enteremail) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(getActivity(),"All fields are required",Toast.LENGTH_LONG).show();
                }else if (txt_password.length() < 6){
                    Toast.makeText(getActivity(),"Password must be atleast 6 characters", Toast.LENGTH_LONG).show();
                }else {
                    register(txt_enteremail,txt_password);
                }

            }
        });


        return view;
    }

    private void register(final String enteremail, final String password){

        auth.createUserWithEmailAndPassword(enteremail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                           sendEmailVerification();

                        }
                    }
                });
    }

    private  void sendEmailVerification(){
        FirebaseUser firebaseUser = auth.getCurrentUser();
        docId = auth.getCurrentUser().getUid();
        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        auth.signOut();


                        String name =  fullname.getText().toString();
                        map.put("myName",name);
                        db.collection("user").document(docId).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("doc id :",docId);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Failed");
                            }
                        });




                        Signin signin = new Signin();
                        getFragmentManager().beginTransaction().replace(R.id.Frame, signin).commit();
                    }else {
                        Toast.makeText(getActivity(),"Verification mail has'nt been sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
