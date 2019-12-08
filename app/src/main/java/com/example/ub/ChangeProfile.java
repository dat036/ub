package com.example.ub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ChangeProfile extends AppCompatActivity implements View.OnClickListener {
    private final String gd = "";
    private String emailReceive ="kdat036@gmail.com";
    private String getData ="Email";
    private EditText edtFName;
    private EditText edtJob;
    private EditText edtLName;
    private EditText edtBirthDate;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private TextView txtScore;
    private TextView txtMember;
    private Button btnChangePass;
    private Button btnSave;
    private ImageView ava;


    private static final Pattern NAME_PATTERN =
            Pattern.compile(
                    "[a-zA-Z]"
            );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        reflect();
        if(rbMale.isChecked()){
            String gd = "Male";
        }
        else{
            String gd= "Female";
        }
        btnSave.setOnClickListener(this);
        edtBirthDate.setOnClickListener(this);
//        Intent intent = getIntent();
////        emailReceive = intent.getStringExtra(getData);
        getUserData("http://192.168.1.7/ub/getUser.php",emailReceive);
    }
    private void reflect(){
        edtFName = (EditText) findViewById(R.id.edt_fullname);
        edtLName = (EditText) findViewById(R.id.edt_lastName);
        edtJob = (EditText) findViewById(R.id.edt_job);
        edtBirthDate = (EditText) findViewById(R.id.edt_birthDay);
        rbMale = (RadioButton) findViewById(R.id.btn_male);
        rbFemale = (RadioButton) findViewById(R.id.btn_female);
//        txtMember = (TextView) findViewById(R.id.txtMember);
        txtScore = (TextView) findViewById(R.id.txtScore);
//        btnChangePass = (Button) findViewById(R.id.btn_changePass);
        btnSave = (Button) findViewById(R.id.btn_save);
        ava = (ImageView) findViewById(R.id.imgAva);
    }
    private void getUserData(String url , String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Log.d("AAA",emailReceive);
                                Log.d("AAA",object.getString("user_email"));
                                if (object.getString("user_email").equals(emailReceive)) {
                                    Log.d("ABC","ASS");
                                    edtFName.setText(object.getString("user_firstName"));
                                    edtLName.setText(object.getString("user_lastName"));
                                    edtBirthDate.setText(object.getString("user_birthDate"));
                                    edtJob.setText(object.getString("user_job"));
                                    if (object.getString("user_gender").equals("Male")) {
                                        rbMale.setChecked(true);
                                    } else
                                        rbFemale.setChecked(true);
                                    String x = String.valueOf(object.getInt("user_score"));
                                    Log.d("AAA",x);
                                    txtScore.append(x);
                                }
                                Toast.makeText(ChangeProfile.this, "Load dữ liệu thành công", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btn_changePass: {
//                Toast.makeText(this, "ABC", Toast.LENGTH_SHORT).show();
//                break;
//            }
            case R.id.btn_save: {
//                upload("http://192.168.1.7/ub/updateProfile.php");
                Toast.makeText(this, "Change Succesfuld", Toast.LENGTH_SHORT).show();
                break;

            }
            case R.id.edt_birthDay: {
                chonNgay();
                break;

            }

        }
    }
    private void chonNgay(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Toast.makeText(ChangeProfile.this, simpleDateFormat.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                edtBirthDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year,month,day);
        datePickerDialog.show();
    }

    private void upload(String url){
        final String gender = gd;
        final String fName = edtFName.getText().toString().trim();
        final String lName = edtLName.getText().toString();
        final String job = edtJob.getText().toString();
        final String birhtdate = edtBirthDate.getText().toString();
//        if (edtFName.getText().toString().isEmpty() || edtLName.getText().toString().isEmpty() || edtJob.getText().toString().isEmpty() || edtBirthDate.getText().toString().isEmpty()) {
//            Toast.makeText(getApplicationContext(), "You must type all inputs", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!NAME_PATTERN.matcher(fName).matches()) {
//            Toast.makeText(getApplicationContext(), "Please enter a valid first name", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!NAME_PATTERN.matcher(lName).matches()) {
//            Toast.makeText(getApplicationContext(), "Please enter a valid last name", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!NAME_PATTERN.matcher(job).matches()) {
//            Toast.makeText(getApplicationContext(), "Please enter a valid job", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (passwordInput.contains(" ")) {
//            Toast.makeText(getApplicationContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
//            Toast.makeText(getApplicationContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if ((password.getText().toString()).length() < 6) {
//            Toast.makeText(getApplicationContext(), "Password must be 6 -20 characters", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
//            Toast.makeText(getApplicationContext(), "Password and Confirm Password don't match", Toast.LENGTH_SHORT).show();
//            return;
//        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim() == "Success"){
                            Toast.makeText(ChangeProfile.this, "Update complete", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstName",fName);
                params.put("lastName",lName);
                params.put("gender",gender);
                params.put("birthDate",birhtdate);
                params.put("job",job);
                params.put("email",emailReceive);


                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}


//        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.names().get(0).equals("success")) {
//                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
//                    } else if (jsonObject.names().get(0).equals("error")) {
//                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), jsonObject.getString("type"), Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> hashMap = new HashMap<String, String>();
//                hashMap.put("firstname", firstname.getText().toString());
//                hashMap.put("lastname", lastname.getText().toString());
//                hashMap.put("email", email.getText().toString().trim());
//                hashMap.put("password", password.getText().toString());
//
//                return hashMap;
//            }
//        };
//        requestQueue.add(request);
//
//    }
//});