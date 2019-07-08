package com.example.trendsetter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
FirebaseFirestore db;
String documentID;
List<MainData> list;
Bitmap bitmap1,bitmap2;
ListView listView;
ImageView imageView;
Button button;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = view.findViewById(R.id.back);
        button = view.findViewById(R.id.logout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Signin profile_pic = new Signin();
                getFragmentManager().beginTransaction().replace(R.id.Frame,profile_pic).commit();

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload profile_pic = new Upload();
                getFragmentManager().beginTransaction().replace(R.id.Frame,profile_pic).commit();
            }
        });
        db = FirebaseFirestore.getInstance();
        Bundle b = getArguments();
        if (b!=null){
            documentID = b.getString("dId");
        }
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT>8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            new getAllImages().execute();
        }
        list = new ArrayList<>();
        listView = view.findViewById(R.id.listview1);
        return  view;
    }



    public  class getAllImages extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            db.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                            Log.d("alldata", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            String PicLink = documentSnapshot.getString("UploadedPicUrl");
                            String WebLink = documentSnapshot.getString("WebsiteLink");
                            String Profile = documentSnapshot.getString("PictureLink");
                            String Name = documentSnapshot.getString("myName");
                            String uid = documentSnapshot.getString("userid");
                            String PicInfo = documentSnapshot.getString("picinfo");

                            double  ratingValue = documentSnapshot.getDouble("rationg");

                            URL url = null;
                            URL url2 = null;

                            try {
                                url = new URL(PicLink);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            try {
                                url2 = new URL(Profile);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            try {
                                bitmap1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                bitmap2 = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            MainData mainData = new MainData(Name, WebLink, bitmap2, bitmap1, uid,PicInfo,ratingValue);
                            list.add(mainData);


                            ImagesAdapter imagesAdapter = new ImagesAdapter(getActivity(), list);
                            listView.setAdapter( imagesAdapter);




                            Log.d("ListData => ", list.toString());


                        }}
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
                }
            });



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }



}
