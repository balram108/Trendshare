package com.example.trendsetter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1 ;
    ImageView imageView;
    Button UploadPic;
    String doc,photoLink;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    Task<Uri> url;
    FirebaseUser firebaseUser;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = view.findViewById(R.id.image1);
        UploadPic = view.findViewById(R.id.Upload);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();




            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.image1:
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                            break;
                    }
                }
            });






            UploadPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                     Profile_Pic upload = new Profile_Pic();
                     getFragmentManager().beginTransaction().replace(R.id.Frame, upload).commit();


                }
            });


            return view;

    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id",doc);
        editor.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

              if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK  && data != null && data.getData() !=null) {
                 Uri selectedImage = data.getData();
                 imageView.setImageURI(selectedImage);
                  new sendProfilePic().execute();
             }
    }







    public class  sendProfilePic extends AsyncTask<Void,Void,Void>{

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            imageView.setDrawingCacheEnabled(false);
            byte [] data = baos.toByteArray();

            final String path = "userImage/" + UUID.randomUUID() + ".png";

            final StorageReference storageReference = firebaseStorage.getReference(path);
            final UploadTask uploadTask = storageReference.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                    Log.d("tsak done:",path);

                    url = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    url.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                          photoLink = uri.toString();
                          doc = mAuth.getCurrentUser().getUid();

                            Map<String,String> newmap = new HashMap<>();
                            newmap.put("PictureLink",photoLink);

                            db.collection("user").document(doc).set(newmap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    System.out.println(doc);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("failed");
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("failed");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("failed");
                }
            });


            return null;
        }


    }











}
