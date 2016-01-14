package com.mgl.volleystats.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mgl.volleystats.R;
import com.mgl.volleystats.api.VolleyStatsApi;
import com.mgl.volleystats.base.Constants;
import com.mgl.volleystats.base.Credentials;
import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.base.VolleyStatApplication;
import com.mgl.volleystats.models.Player;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by goofyahead on 10/25/15.
 */
public class CreatePlayerActivity extends AppCompatActivity {

    private static final String TAG = CreatePlayerActivity.class.getName();
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @InjectView(R.id.player_image)
    ImageView picture;
    @InjectView(R.id.save)
    Button save;
    @InjectView(R.id.player_name)
    EditText playerName;
    @InjectView(R.id.player_number)
    EditText playerNumber;
    @InjectView(R.id.player_position)
    Spinner playerPosition;


    @Inject
    VolleyStatsApi api;
    @Inject
    VolleyPrefs prefs;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private File mCurrentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_player_activity);

        ButterKnife.inject(this);
        ((VolleyStatApplication) getApplication()).inject(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(CreatePlayerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreatePlayerActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Log.d(TAG, "should show explanation on permissions");

            } else {
                ActivityCompat.requestPermissions(CreatePlayerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        Log.d(TAG, "is ready:" + isExternalStorageWritable());

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Current file size " + mCurrentFile.length());

                TypedFile file = new TypedFile("application/octet-stream", new File(mCurrentFile.getAbsolutePath()));

                api.createPlayer(new Player(prefs.getTeamId(), playerName.getText().toString(), Integer.parseInt(playerNumber.getText().toString()), playerPosition.getSelectedItem().toString()), file, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.d(TAG, "SUCCESS");
                        CreatePlayerActivity.this.finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "FAILURE "  + error.getMessage());
                    }
                });

                galleryAddPic();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        File photoFile = null;
        photoFile = getOutputMediaFile();

        if (photoFile != null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            takePictureIntent.putExtra("crop", "true");
            takePictureIntent.putExtra("outputX", 350);
            takePictureIntent.putExtra("outputY", 350);
            takePictureIntent.putExtra("aspectX", 1);
            takePictureIntent.putExtra("aspectY", 1);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private File getOutputMediaFile(){

        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Camera Guide", "Required media storage does not exist");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constants.VOLLEY_STATS_FOLDER + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        mCurrentFile = mediaFile;
        return mediaFile;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(mCurrentFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentFile.getAbsolutePath(), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/360, photoH/360);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentFile.getAbsolutePath(), bmOptions);
            Bitmap dstBmp;

            if (bitmap.getWidth() >= bitmap.getHeight()){

                dstBmp = Bitmap.createBitmap(
                        bitmap,
                        bitmap.getWidth()/2 - bitmap.getHeight()/2,
                        0,
                        bitmap.getHeight(),
                        bitmap.getHeight()
                );

            }else{

                dstBmp = Bitmap.createBitmap(
                        bitmap,
                        0,
                        bitmap.getHeight()/2 - bitmap.getWidth()/2,
                        bitmap.getWidth(),
                        bitmap.getWidth()
                );
            }

            OutputStream fOut = null;
//            File file = new File(path, "FitnessGirl"+Contador+".jpg"); // the File to save to
            try {
                fOut = new FileOutputStream(mCurrentFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            dstBmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate

        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            // do not forget to close the stream
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Picasso.with(CreatePlayerActivity.this).load(mCurrentFile).into(picture);

//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            picture.setImageBitmap(imageBitmap);
        } else {
            Log.d(TAG, "something wrong with pic? " + resultCode);
        }
    }
}
