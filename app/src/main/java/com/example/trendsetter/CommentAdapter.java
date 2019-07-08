package com.example.trendsetter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CommentAdapter extends BaseAdapter {

    Bitmap bit;
    List<CommentConstructor> list;
    Activity activity;

    public CommentAdapter(FragmentActivity activity, List<CommentConstructor> list) {
        this.activity = activity;
        this.list = list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.comment_design,null);

            viewHolder = new ViewHolder();

            viewHolder.textView = convertView.findViewById(R.id.textcomment);
            viewHolder.textView1 = convertView.findViewById(R.id.uernamecomment);
            viewHolder.imageView = convertView.findViewById(R.id.commentpic);

            CommentConstructor commentConstructor = list.get(position);


            viewHolder.textView.setText(commentConstructor.getComment());
            viewHolder.textView1.setText(commentConstructor.getPn().toUpperCase());



            String path = commentConstructor.getPp();
            URL url = null;
            try {
                url = new URL(path);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bit = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            viewHolder.imageView.setImageBitmap(bit);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        return convertView;
    }


    public  class  ViewHolder{
        ImageView imageView;
        TextView textView,textView1;
    }
}
