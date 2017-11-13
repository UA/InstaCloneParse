package com.ua.instacloneparse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {

    ArrayList<String> names;
    ArrayList<Bitmap> images;
    ArrayList<String> comments;

    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        listView = view.findViewById(R.id.listView);

        names = new ArrayList<>();
        images = new ArrayList<>();
        comments = new ArrayList<>();

        final PostAdapter adapter = new PostAdapter(getActivity(),R.layout.custom_view,names,images,comments);
        listView.setAdapter(adapter);

        ParseQuery<ParseObject> query = new ParseQuery<>("Posts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null){
                    Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }else{
                    if(objects.size()>0){
                        for (final ParseObject object : objects) {

                            ParseFile parseFile = object.getParseFile("image");
                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e != null){
                                        Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                    }else{
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                        images.add(bitmap);
                                        names.add(object.getString("username"));
                                        comments.add(object.getString("comment"));
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }

                }
            }
        });

        return view;
    }
}
