package com.example.trendsetter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Comments extends Fragment {

    FirebaseFirestore firebaseFirestore;
    Button add;
    EditText editText;
    String comment;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    List<CommentConstructor> list;
    ListView listViewc;
    String USER_ID,CurrentUserId;
    ImageView back;
    String Profil,ProfilN;
    URL ur = null;
    Bitmap bit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_comments, container, false);

        back=view.findViewById(R.id.backarrow3);
        listViewc = view.findViewById(R.id.listviewC);
        Bundle bundle = getArguments();
        if (bundle!= null){
            USER_ID = bundle.getString("IDOFUSER");
        }
        add = view.findViewById(R.id.addbutton);
        editText = view.findViewById(R.id.comment);

        firebaseFirestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CurrentUserId = firebaseUser.getUid();
        list = new ArrayList<>();
        getImage();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home profile_pic = new Home();
                getFragmentManager().beginTransaction().replace(R.id.Frame, profile_pic).commit();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmnt = editText.getText().toString();
                if (!cmnt.equals("")){

                    addComment(cmnt,CurrentUserId,USER_ID);
                }
                else {
                    Toast.makeText(getActivity(),"you cant add empty comment",Toast.LENGTH_LONG).show();
                }
                editText.setText("");
            }
        });
        getcomments(CurrentUserId,USER_ID);

    return view;
    }



    public  void  getImage (){

        db.collection("user").document(CurrentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if (task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();

                    Profil = documentSnapshot.getString("PictureLink");
                    ProfilN = documentSnapshot.getString("myName");

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

   public  void  addComment(String cmnt,String addinguserid,String gettinguserid) {

       Map<String, Object> newmap = new HashMap<>();
       newmap.put("comment", cmnt);
       newmap.put("addinguserid", addinguserid);
       newmap.put("gettinguserid", gettinguserid);
       newmap.put("timeStamp", FieldValue.serverTimestamp());
       newmap.put("pp",Profil);
       newmap.put("pn",ProfilN);

       if (addinguserid.equals(CurrentUserId)) {
           Toast.makeText(getActivity(), "cannot add comment on own pic", Toast.LENGTH_LONG).show();
       }

       if(!addinguserid.equals(gettinguserid)) {
           db.collection("comments").add(newmap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
               @Override
               public void onSuccess(DocumentReference documentReference) {
                   Toast.makeText(getActivity(), "comment added", Toast.LENGTH_LONG).show();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(getActivity(), "comment failed", Toast.LENGTH_LONG).show();
               }
           });
       }
   }










    public void getcomments(final String My_id , final String U_Id){

        db.collection("comments").orderBy("timeStamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
               list.clear();

               for (QueryDocumentSnapshot doc : value){
                   CommentConstructor commentConstructor = doc.toObject(CommentConstructor.class);


                      if (!commentConstructor.addinguserid.equals(U_Id)) {

                          list.add(commentConstructor);


                      }
                   CommentAdapter commentAdapter = new CommentAdapter(getActivity(),list);
                   listViewc.setAdapter(commentAdapter);
               }


            }
        });
    }




}
