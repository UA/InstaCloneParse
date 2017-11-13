package com.ua.instacloneparse;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ugur on 12.11.2017.
 */

public class PostAdapter extends ArrayAdapter {
    private final ArrayList<String> names;
    private final ArrayList<Bitmap> images;
    private final ArrayList<String> comments;
    private Activity context;

    public PostAdapter(@NonNull Activity context, int resource, ArrayList<String> names, ArrayList<Bitmap> images, ArrayList<String> comments) {
        super(context, resource, names);
        this.names = names;
        this.images = images;
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view,null,true);
        TextView username = customView.findViewById(R.id.username);
        ImageView imageview = customView.findViewById(R.id.imageView);
        TextView comment = customView.findViewById(R.id.comment);
        username.setText(names.get(position));
        imageview.setImageBitmap(images.get(position));
        comment.setText(comments.get(position));
        return customView;
    }
}
