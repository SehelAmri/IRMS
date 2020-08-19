package com.example.irms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irms.Client.RetrofitClient;
import com.example.irms.Model.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
Button logLangBtn,logSignIn;
EditText logName,logPassword;
TextView logHelp;
TextWatcher emptyField;
RequestBody body;
Call<LoginResponse> call;
String serialNum,logNameTxt,logPassTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_login);
        initView();
        logLangBtn.setOnClickListener(view ->  { setOnChangeLanguage(); });
        logHelp.setOnClickListener(view -> alertDialog(R.string.logHelpDetails));
         textWatcher();
        logPassword.addTextChangedListener(emptyField);
        logName.addTextChangedListener(emptyField);
        logSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               logNameTxt = logName.getText().toString();
                logPassTxt = logPassword.getText().toString();
                String creds = String.format("%s:%s", logNameTxt, logPassTxt);
                final String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserName",logNameTxt);
                params.put("MachineName", Build.SERIAL);
                params.put("Password",logPassTxt);

                body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(params)).toString());

                call = RetrofitClient
                        .getmInstance()
                        .getApi()
                        .createUser(auth,body);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if(response.code() == 200) {
                            if (response.body().getErrorMessage() == null) {
                                if(response.body().getUserID() != null) {
                                    Intent intent = new Intent(Login.this, Home.class);
                                    intent.putExtra("UserID", response.body().getUserID());
                                    intent.putExtra("UserName", response.body().getUserName());
                                    intent.putExtra("Auth", auth);
                                    intent.putExtra("WorkStationID", response.body().getWorkStationID());
                                    intent.putExtra("WorkStationDtlID", response.body().getWorkStationDtlID());
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                              alertDialog(R.string.logNot_auth_user);
                            }
                        }
                        else {
                            alertDialog(R.string.logUser_not_available);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                       Toast.makeText(Login.this,t.getMessage(),Toast.LENGTH_LONG).show();
                       Log.e("loginError",t.getMessage());
                    }
                });
            }
        });
    }
    private void setOnChangeLanguage(){
        String[] languageOptions = {"Kiswahili","English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Login.this);
        mBuilder.setTitle(R.string.logSelectLang);
        mBuilder.setSingleChoiceItems(languageOptions, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    //Swahili
                    setLocale("sw");
                    recreate();
                }else if(i == 1){
                    setLocale("en");
                    recreate();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    private void setLocale(String lang) {
        Resources res = Login.this.getResources();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        res.updateConfiguration(config, dm);
        //Save Data to Preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My Lang",lang);
        editor.apply();
    }
    private void initView(){
        logLangBtn = findViewById(R.id.logLangBtn);
        logSignIn = findViewById(R.id.logSignIn);
        logName = findViewById(R.id.logName);
        logPassword = findViewById(R.id.logPassword);
        logHelp =  findViewById(R.id.logHelp);
        serialNum = Build.SERIAL;
    }
    public void loadLocale(){
        SharedPreferences savedLang = getSharedPreferences("Settings",MODE_PRIVATE);
        String language = savedLang.getString("My Lang","");
        setLocale(language);
    }
    public void textWatcher(){
        emptyField = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailInput = logName.getText().toString().trim();
                String passInput = logPassword.getText().toString().trim();

                logSignIn.setClickable(!emailInput.isEmpty() && !passInput.isEmpty());
                logSignIn.setEnabled(!emailInput.isEmpty() && !passInput.isEmpty());
                if(!emailInput.isEmpty() && !passInput.isEmpty()) {
                    logSignIn.setBackground(getResources().getDrawable(R.drawable.enabled_button));
                }else {
                    logSignIn.setBackground(getResources().getDrawable(R.drawable.disabled_button));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
    public AlertDialog alertDialog(int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage(message).setPositiveButton("OK",null);
       AlertDialog createAlertDialog = builder.create();
       createAlertDialog.show();
       return createAlertDialog;
    };


}