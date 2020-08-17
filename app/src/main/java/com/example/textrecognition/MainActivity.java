package com.example.textrecognition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class MainActivity extends AppCompatActivity {

    private Button cameraButton;
    private final static int REQUEST_cAMERA=123;
    private FirebaseVisionTextRecognizer textRecognizer;
    FirebaseVisionImage image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        cameraButton=findViewById(R.id.button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureintent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePictureintent,REQUEST_cAMERA);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_cAMERA && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            recognizeMyText(bitmap);
        }
    }

    private void recognizeMyText(Bitmap bitmap) {
        image=FirebaseVisionImage.fromBitmap(bitmap);
        try {
            textRecognizer= FirebaseVision
                    .getInstance()
                    .getOnDeviceTextRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        String result=firebaseVisionText.getText();
                        if(result.isEmpty()){
                            Toast.makeText(MainActivity.this,"NO text Detected",Toast.LENGTH_LONG).show();

                        }
                        else{
                            Intent intent=new Intent(MainActivity.this,result.class);
                            intent.putExtra(textRecognition.RESULT_TEXT,result);
                            startActivity(intent);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}