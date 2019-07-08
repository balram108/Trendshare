package com.example.trendsetter;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class Upload extends Fragment {



    Uri uri;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    ImageView imageView1,back;
    Button choosefile,Complete,get;
    private static final int RESULT_LOAD_IMAGE = 1;
    Task<Uri> url;
    String documentId,picLink;
    FirebaseUser firebaseUser;
    String photoLink;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    EditText editTextl,editText2;
    RatingBar rbar;





    public Upload() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_upload, container, false);

        back=view.findViewById(R.id.backarrow);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        documentId = firebaseAuth.getCurrentUser().getUid();
        editTextl=view.findViewById(R.id.urlLink);
        editText2=view.findViewById(R.id.info);
        imageView1 = view.findViewById(R.id.imageView);
        choosefile = view.findViewById(R.id.choosepic);
        Complete = view.findViewById(R.id.Finish);
        rbar = view.findViewById(R.id.rating);
        get      = view.findViewById(R.id.getData);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // choosing file from gallery

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile_Pic profile_pic = new Profile_Pic();
                getFragmentManager().beginTransaction().replace(R.id.Frame, profile_pic).commit();
            }
        });
        choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.choosepic:
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                        break;
                }
            }
        });

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Home home = new Home();
                getFragmentManager().beginTransaction().replace(R.id.Frame,home).commit();

            }
        });

        Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new uploadImages().execute();
            }
        });


    get.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Home gd = new Home();

            Bundle newBundle = new Bundle();
            newBundle.putString("dId",documentId);
            gd.setArguments(newBundle);

            getFragmentManager().beginTransaction().replace(R.id.Frame,gd).commit();
        }
    });
    return view;
    }


    public float onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {

        final int numStars = ratingBar.getNumStars();
        final float rate =  ratingBar.getRating();
        final float ratingBarStepSize = ratingBar.getStepSize();

        return rate;
    }




// getting file from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK  && data != null && data.getData() !=null) {
            uri = data.getData();
            imageView1.setImageURI(uri);
        }
    }




    public  class  uploadImages extends AsyncTask<Void,Void,Void>{

    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(Void... voids) {
        imageView1.setDrawingCacheEnabled(true);
        imageView1.buildDrawingCache();
        Bitmap bitmap = imageView1.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        imageView1.setDrawingCacheEnabled(false);
        byte [] data = baos.toByteArray();

        final String path = "userAllImage/" + UUID.randomUUID() + ".png";

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
                        picLink = uri.toString();
                        String UrlOfImage = editTextl.getText().toString();
                        String id = firebaseUser.getUid();
                        String info = editText2.getText().toString();
                        float rating =  onRatingChanged(rbar,0,true);


                        Log.d("rating Value ", String.valueOf((float) rating));


                        Map<String,Object> newMap = new HashMap<>();
                        newMap.put("UploadedPicUrl",picLink);
                        newMap.put("WebsiteLink",UrlOfImage);
                        newMap.put("picinfo",info);
                        newMap.put("userid",id);
                        newMap.put("rationg",rating);

                        db.collection("user").document(documentId).set(newMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(),"data merged Successfully",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
            }
        });


        return null;
    }
}


}
