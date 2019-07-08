package com.example.trendsetter;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Pic extends Fragment {
    Button profile, Post;
    String DID;
    FirebaseFirestore db;
    DocumentReference documentReference;
    CircleImageView imageViewl;
    TextView textView;
    FirebaseAuth firebaseAuth;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile__pic, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        DID = firebaseAuth.getCurrentUser().getUid();
        profile = view.findViewById(R.id.changepic);
        Post = view.findViewById(R.id.Postpic);
        db = FirebaseFirestore.getInstance();
        imageViewl =view.findViewById(R.id.profilepic);
        textView  = view.findViewById(R.id.username);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT >8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            getProfile();
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile = new Profile();
                getFragmentManager().beginTransaction().replace(R.id.Frame,profile).commit();
            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Upload upload = new Upload();
                getFragmentManager().beginTransaction().replace(R.id.Frame,upload).commit();
            }
        });




    return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ppp",DID);
        editor.commit();
    }

    public void getProfile(){
        documentReference = db.collection("user").document(DID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        String link = documentSnapshot.getString("PictureLink");
                        String n     = documentSnapshot.getString("myName");
                        URL url = null;

                        try {
                            url = new URL(link);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            imageViewl.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        textView.setText(n);


                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }








}
