package com.example.fypapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button selectImg;
    Button selectMoody;
    ImageView imageView;
    ImageView imageView2;
    Button createMoody;
    TextView textView1,textView2;
    String img1,img2;

    private String Document_img1 = "";
    public static final String KEY_User_Document1 = "doc1";

    private String URL = "http://172.16.12.161:80/FYP/uploadImage.php";

    //PyObject obj,obj2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        selectImg = (Button) findViewById(R.id.moodyBtn);
        selectMoody = (Button) findViewById(R.id.selectMoody);
        createMoody = (Button) findViewById(R.id.createMoody);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        textView1 = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage(MainActivity.this);
            }
        });



        selectMoody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage2(MainActivity.this);
            }
        });



        createMoody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void selectImage(Context context) {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which].equals("Take Photo")){
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "000001.jpg");
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(takePicture,0);
                }
                else if(options[which].equals("Choose from Gallery")){
                    Intent gallery1 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery1, 1);
                }
                else if(options[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void selectImage2(Context context) {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which].equals("Take Photo")){
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "000001.jpg");
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(takePicture,0);
                }
                else if(options[which].equals("Choose from Gallery")){
                    Intent gallery1 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery1, 2);
                }
                else if(options[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();

        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b,Base64.DEFAULT);
//        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
//        return Document_img1;
    }

    public String imageName(String picturePath){
        String[] arrOfStr = picturePath.split("/");

        String imageName = null;
        for (String a : arrOfStr)

            imageName = a;
        return imageName;


    }

    private void uploadImage(String image , TextView textview) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Log.d("response", "onResponse: " + Response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("errorResponse", "onErrorResponse: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("image",image);
                params.put("name", textview.getText().toString());

                return params;
            }
        };

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    File f = new File(Environment.getExternalStorageDirectory().toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("000001.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        Bitmap bitmap;
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                        bitmap=getResizedBitmap(bitmap, 200);
                        imageView.setImageBitmap(bitmap);
                        BitMapToString(bitmap);
                        String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";

                        f.delete();
                        OutputStream outFile = null;
                        File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                        try {
                            outFile = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                            outFile.flush();
                            outFile.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:

                    Uri selectedImage = data.getData();



                    Bitmap thumbnail=null;
                    try {
                        InputStream inputStream=getContentResolver().openInputStream(data.getData());
                        thumbnail=BitmapFactory.decodeStream(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Log.d("picture", "Picture Path: "+ picturePath);
                    textView1.setText(imageName(picturePath));
//                    Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);

                    Log.d("thumbnail", "thumbnail: "+ thumbnail);
                    thumbnail = getResizedBitmap(thumbnail, 400);

                    img1 = BitMapToString(thumbnail);
                    Log.d("bitmap", "thumbnail: "+ thumbnail);
                    imageView.setImageBitmap(thumbnail);

                    uploadImage(img1,textView1);

                    break;

                case 2:
                    Uri selectedImage2 = data.getData();

                    Bitmap thumbnail2 = null;
                    try {
                        InputStream inputStream=getContentResolver().openInputStream(data.getData());
                        thumbnail2 =BitmapFactory.decodeStream(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    String[] filePath2 = { MediaStore.Images.Media.DATA };
                    Cursor c2 = getContentResolver().query(selectedImage2,filePath2, null, null, null);
                    c2.moveToFirst();
                    int columnIndex2 = c2.getColumnIndex(filePath2[0]);
                    String picturePath2 = c2.getString(columnIndex2);
                    c2.close();

                    textView2.setText(imageName(picturePath2));
//                    Bitmap thumbnail2 = (BitmapFactory.decodeFile(picturePath2));

                    thumbnail2=getResizedBitmap(thumbnail2, 400);
                    img2 = BitMapToString(thumbnail2);
                    Log.d("bitmap", "thumbnail: "+ thumbnail2);
                    imageView2.setImageBitmap(thumbnail2);

                    uploadImage(img2 , textView2);

                    break;
            }

        }
    }


    @Override
    public void onClick(View v) {

        //uploadImage();
    }
}