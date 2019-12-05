package com.example.ub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Checkout extends AppCompatActivity implements View.OnClickListener{
    private  String Percent = "percent";
    private int percent = 0;
    private RadioButton No0Percent;
    private RadioButton No50Percent;
    private RadioButton No100Percent;
    private TextView txt_material;
    private TextView txt_size;
    private TextView txt_people;
    private CheckBox cb_material;
    private CheckBox cb_size;
    private CheckBox cb_people;
    private String waste_Address = "89Le Loi";
    private EditText edtNote;
    private Button btn_checkout;
    private ImageView imgeCheckout;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap FixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
    String User_id = "user_id";
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String ConvertImage;
    String nameImageCheckout = "";
    String name_userId = "";
    HttpURLConnection httpURLConnection;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;
    int RC;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    boolean check = true;
    String urlCheckin = "http://192.168.1.7/ub/checkout.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_checkout);
        byteArrayOutputStream = new ByteArrayOutputStream();
        anhXa();
        getWasteData("http://192.168.1.7/ub/getWaste.php",waste_Address);
        btn_checkout.setOnClickListener(this);
        imgeCheckout.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(Checkout.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 5);
            }
        }
    }

    public void anhXa() {
        btn_checkout = (Button) findViewById(R.id.btn_checkout);
        imgeCheckout = (ImageView) findViewById(R.id.imgeCheckout);
        txt_material = (TextView) findViewById(R.id.txtMaterial);
        txt_size = (TextView) findViewById(R.id.txtSize);
        txt_people = (TextView) findViewById(R.id.txtPeople);
        cb_material = (CheckBox) findViewById(R.id.tb_material);
        cb_size = (CheckBox) findViewById(R.id.tb_size);
        cb_people = (CheckBox) findViewById(R.id.tb_people);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imgeCheckout.setImageBitmap(FixBitmap);
                    btn_checkout.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Checkout.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            imgeCheckout.setImageBitmap(FixBitmap);
            btn_checkout.setVisibility(View.VISIBLE);
//              saveImage(thumbnail);
            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public void UploadImageToServer() {
        if(cb_material.isChecked()){
            percent +=33;
        }
        if(cb_people.isChecked()){
            percent +=33;
        }
        if(cb_size.isChecked()){
            percent +=33;
        }
        final String pc = String.valueOf(percent);
        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT).toString();

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                Toast.makeText(Checkout.this, string1, Toast.LENGTH_LONG).show();
                edtNote.setText(string1);
            }

            @Override
            protected String doInBackground(Void... params) {

                Checkout.ImageProcessClass imageProcessClass = new Checkout.ImageProcessClass();

                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put(User_id, name_userId);
                HashMapParams.put(ImageName, ConvertImage);
                HashMapParams.put(ImageTag, nameImageCheckout);
                HashMapParams.put(Percent,pc);

                String FinalData = imageProcessClass.ImageHttpRequest(urlCheckin, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgeCheckout : {
                showPictureDialog();
                break;
            }
            case R.id.btn_checkout: {
                Toast.makeText(this, "Check out sucessful", Toast.LENGTH_SHORT).show();
//                nameImageCheckout = "abc";
//                UploadImageToServer();
//                break;
            }
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_checkout: {
//                /* xét tên ảnh từ name wastes
//                 xét tên ảnh từ name wastes + time
//                 */
//                nameImageCheckout = "abc";
//                UploadImageToServer();
////                Intent intent = new Intent();
////                intent.putExtra(codeCheck,"1");
//                break;
//            }
//            case R.id.imgeCheckout: {
//                showPictureDialog();
//                break;
//            }
//        }
//    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null) {

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&;");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilder.toString();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(Checkout.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getWasteData(String link, final String waste_address) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, link, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                if (object.getString("waste_adress").equals(waste_address)){
                                        txt_material.setText(object.getString("waste_material"));
                                        txt_size.setText(String.valueOf(object.getString("waste_size")));
                                        txt_people.setText(String.valueOf(object.getInt("waste_people")));
                                }
                                Toast.makeText(Checkout.this, "Load dữ liệu thành công", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}

