package com.ua.instacloneparse;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment implements View.OnClickListener {

    ImageView imageView;
    EditText comment;
    Button upload;
    Bitmap bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        imageView = view.findViewById(R.id.imageView);
        comment = view.findViewById(R.id.comment);
        upload = view.findViewById(R.id.upload);
        upload.setOnClickListener(this);
        imageView.setOnClickListener(this);

        return view;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView:
                chooseImage();
                break;
            case R.id.upload:
                uploadImage();
                break;
        }
    }

    private void uploadImage() {
        String commentText = comment.getText().toString();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] bytes = outputStream.toByteArray();
        ParseFile parseFile = new ParseFile("image.png",bytes);

        ParseObject parseObject = new ParseObject("Posts");
        parseObject.put("image",parseFile);
        parseObject.put("comment",commentText);
        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),"Post shared",Toast.LENGTH_LONG).show();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment uploadFragment = new FeedFragment();
                    fragmentTransaction.replace(R.id.frameLayout,uploadFragment);
                    fragmentTransaction.commit();
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void chooseImage() {
        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode == 2){
           if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(intent,1);

           }
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == getActivity().RESULT_OK  && data != null){
            Uri uri = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
