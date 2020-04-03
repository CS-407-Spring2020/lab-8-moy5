package com.uw.lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(null);
    }

    public void upload(View view) {

        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imgRef = storageRef.child("images/capture.jpg");

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.capture);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imgByteStream = baos.toByteArray();

            UploadTask uploadTask = imgRef.putBytes(imgByteStream);
            uploadTask.addOnFailureListener((exception) -> {
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("ImageUpload", "Image successfully uploaded to Firebase");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Error", "Image upload failed.");
        }
    }

    public void downloadAndSet(View view) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imgRef = storageRef.child("images/capture.jpg");

        final ImageView imageView = findViewById(R.id.imageView);
        final long ONE_MEGABYTE = 1024 * 1024;

        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
                Log.i("Error", "Image Download failed");
            }
    });
}

}
