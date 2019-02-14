package com.example.kayjean.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
        import android.view.animation.AccelerateDecelerateInterpolator;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
        import android.widget.TextView;

import java.io.File;
import java.util.Random;

public class Chart extends AppCompatActivity {

    private SparkView sparkView;
    private RandomizedAdapter adapter;
    private TextView scrubInfoTextView;
    private String label;
    private Bitmap rotatedBmp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        label = extras.getString("PRODUCT_NAME");
        setInitialImage(label);
        //setInitialImage("test");

        sparkView = findViewById(R.id.sparkview);

        adapter = new RandomizedAdapter();
        sparkView.setAdapter(adapter);
        sparkView.setScrubListener(new SparkView.OnScrubListener() {
            @Override
            public void onScrubbed(Object value) {
                if (value == null) {
                    scrubInfoTextView.setText(R.string.scrub_empty);
                } else {
                    scrubInfoTextView.setText(getString(R.string.scrub_format, value));
                }
            }
        });

        findViewById(R.id.random_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setdata();
            }
        });

        ((CheckBox)findViewById(R.id.fillCheckBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    sparkView.setFillType(SparkView.FillType.DOWN);
                    sparkView.setFillShader();
                }
                else
                    sparkView.setFillType(SparkView.FillType.NONE);
            }
        });
    }

    private void setInitialImage(String label) {
        setCurrentImage(label);
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void setCurrentImage(String label) {

        final ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);

        //File imgFile = new  File("/storage/emulated/0/Android/data/com.example.kayjean.camera/files/pic.jpg");
        File imgFile = new  File(label);
        if(imgFile.exists()){

            Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap croppedBmp = Bitmap.createBitmap(bm, bm.getWidth()/10, bm.getHeight()/2, bm.getWidth()*11/20, bm.getHeight()/8);
            rotatedBmp = RotateBitmap(croppedBmp,180);
            imageView.setImageBitmap(rotatedBmp);

        };
/*
        // /storage/emulated/0/Android/data/com.example.kayjean.camera/files/pic.jpg
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
        Bitmap croppedBmp = Bitmap.createBitmap(bm, bm.getWidth()/10, bm.getHeight()/2, bm.getWidth()*11/20, bm.getHeight()/8);
        rotatedBmp = RotateBitmap(croppedBmp,180);
        imageView.setImageBitmap(rotatedBmp);
*/
    }

    public class RandomizedAdapter extends SparkAdapter {
        private float[] yData;
        private final Random random;

        public RandomizedAdapter() {
            random = new Random();
            yData = new float[50];
            randomize();
        }

        public void setdata()
        {
            yData =new float[rotatedBmp.getWidth()];
            float maxluminance = 0f;

            for( int w = 0 ; w < rotatedBmp.getWidth() ; w++ )
            {
                maxluminance = 0f;
                for( int h = 0 ; h < rotatedBmp.getHeight() ; h++ )
                {
                    int color = rotatedBmp.getPixel(w,h);
// extract each color component
                    int red   = (color >>> 16) & 0xFF;
                    int green = (color >>>  8) & 0xFF;
                    int blue  = (color >>>  0) & 0xFF;

//https://github.com/D-Speiser/OCR/blob/master/src/GrayscaleConverter.java
// calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                    //float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;
                    //float luminance = (red + green + blue) / 3;
                    float luminance = (red * 0.299f + green * 0.587f + blue * 0.114f) / 255;

                    if( luminance > maxluminance )
                        maxluminance = luminance;
                }
                yData[w] = maxluminance;
            }

            notifyDataSetChanged();
        }

        public void randomize() {
            for (int i = 0, count = yData.length; i < count; i++) {
                //yData[i] = random.nextFloat();
                yData[i] = 0;
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return yData.length;
        }

        @NonNull
        @Override
        public Object getItem(int index) {
            return yData[index];
        }

        @Override
        public float getY(int index) {
            return yData[index];
        }
    }
}