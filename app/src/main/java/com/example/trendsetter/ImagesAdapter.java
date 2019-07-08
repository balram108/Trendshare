package com.example.trendsetter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class ImagesAdapter extends BaseAdapter {


    String link;
    Activity activity;
    List<MainData> list;
    public ImagesAdapter(FragmentActivity activity, List<MainData> list) {
        this.activity = activity;
        this.list     = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolders{
        ImageView imageView;
        RatingBar ratingBar;
        Button button;
        ImageView imageView1,imageView2;
        TextView textView,textView1,textView2,textView3;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolders viewHolders = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = (View) inflater.inflate(R.layout.image_item,null);

            viewHolders = new ViewHolders();

            viewHolders.imageView = convertView.findViewById(R.id.imageView22);
            viewHolders.ratingBar =  convertView.findViewById(R.id.rating);
            viewHolders.textView  =  convertView.findViewById(R.id.username);
            viewHolders.textView1  = convertView.findViewById(R.id.urlink);
            viewHolders.textView2  = convertView.findViewById(R.id.userdanaam);
            viewHolders.textView3  = convertView.findViewById(R.id.Productinfo);
            viewHolders.imageView1 = convertView.findViewById(R.id.profilepic);
            viewHolders.imageView2 = convertView.findViewById(R.id.review);


            final MainData mainData = list.get(position);
            
            viewHolders.ratingBar.setRating((float) mainData.getRatingStar());
            viewHolders.imageView.setImageBitmap(mainData.getBitmap2());
            viewHolders.textView.setText(mainData.getName().toUpperCase());
            viewHolders.textView1.setText(mainData.getWebLink());
            viewHolders.textView3.setText(mainData.getINfo());
            viewHolders.textView2.setText(mainData.getName().toUpperCase());
            viewHolders.imageView1.setImageBitmap(mainData.getBitmap1());

          viewHolders.textView1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String link = mainData.getWebLink();
                  Uri uri = Uri.parse(link); // missing 'http://' will cause crashed
                  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                  activity.startActivity(intent);
              }
          });


            convertView.setTag(viewHolders);

             viewHolders.imageView2.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     AppCompatActivity  activity = (AppCompatActivity) v.getContext();
                     Comments comments = new Comments();
                     Bundle  bundle    = new Bundle();
                     bundle.putString("IDOFUSER",mainData.getUID());
                     comments.setArguments(bundle);
                     activity.getSupportFragmentManager().beginTransaction().replace(R.id.Frame,comments).commit();
                 }
             });

          /*  viewHolders.button.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     AppCompatActivity  activity = (AppCompatActivity) v.getContext();
                     Profile_Pic profile_pic = new Profile_Pic();
                     Bundle  bundle    = new Bundle();
                     bundle.putString("IDOFUSER",mainData.getUID());
                     profile_pic.setArguments(bundle);
                     activity.getSupportFragmentManager().beginTransaction().replace(R.id.Frame,profile_pic).commit();

                 }
             });*/

        }
        else {
            viewHolders = (ViewHolders) convertView.getTag();
        }

        return convertView;
    }




}
