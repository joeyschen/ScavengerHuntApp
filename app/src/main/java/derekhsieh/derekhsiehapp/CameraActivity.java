package derekhsieh.derekhsiehapp;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import AsyncTaskRunners.PostImageAsyncTaskRunner;
import AsyncTaskRunners.GetImageAsyncTaskRunner;

public class CameraActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap = null;
    private ImageView cameraImageView;
    // TODO: need to use the actual user and friend for the photos
    private String user;
    private String friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        this.user = extras.getString("username");
        this.friend = extras.getString("friend");

        setContentView(R.layout.activity_camera);

        TextView friendNameTextView = (TextView) findViewById(R.id.friendNameTextView);
        friendNameTextView.setText(friend);

        cameraImageView = (ImageView) findViewById(R.id.imageView);

        Button takeImageButton = (Button) findViewById(R.id.takeImageButton);
        takeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Button saveImageButton = (Button) findViewById(R.id.saveImageButton);
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(v);
                Toast.makeText(getApplicationContext(), "IMAGE SAVED. YISSSS!!!", Toast.LENGTH_SHORT).show();
            }
        });

        Button getImageButton = (Button) findViewById(R.id.getImageButton);
        getImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(v);
                Toast.makeText(getApplicationContext(), "IMAGE RECEIVED. WASSSUP!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveImage(View view) {
        if (imageBitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            String bitmapString = Base64.encodeToString(byteArray, Base64.NO_WRAP | Base64.URL_SAFE | Base64.NO_PADDING);
            PostImageAsyncTaskRunner runner = new PostImageAsyncTaskRunner(getApplicationContext());
            AsyncTask<String, String, Boolean> setAsyncTask = runner.execute(this.user, this.friend, bitmapString);
            try {
                Boolean requests = setAsyncTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void getImage(View view) {
        GetImageAsyncTaskRunner runner = new GetImageAsyncTaskRunner(getApplicationContext());
        AsyncTask<String, String, String> getAsyncTask = runner.execute(this.user, this.friend);
        try {
            String response = getAsyncTask.get();
            //TODO: By default, the user should see the image that is part of that scavenger hunt. Should remove get image button.
            if (response != null)
            {
                if (response.equals("No image")) {
                    Toast.makeText(getApplicationContext(), "No image in database", Toast.LENGTH_SHORT).show();
                } else {
                    byte[] encodeByte = Base64.decode(response, Base64.NO_WRAP | Base64.URL_SAFE | Base64.NO_PADDING);
                    imageBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    cameraImageView.setImageBitmap(imageBitmap);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            cameraImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("image", imageBitmap);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageBitmap = (Bitmap) savedInstanceState.get("image");
        cameraImageView.setImageBitmap(imageBitmap);
    }

    public static void longInfo(String str) {
        if(str.length() > 4000) {
            System.out.println(str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            System.out.println(str);
    }
}