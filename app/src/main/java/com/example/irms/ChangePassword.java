package com.example.irms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.irms.Client.RetrofitClient;
import com.example.irms.Model.ChangePasswordResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
EditText mCurrentPassword,mNewPassword,mConfirmPassword;
String verNewPassword,verConfirmPassword,verOldPassword,logToken;
Bundle bundle;
Button mChangePassword;
RequestBody body;
Call<ChangePasswordResponse> call;
Boolean currentBool,newBool,confirmBool;
TextWatcher emptyField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle(R.string.fa_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentPassword = findViewById(R.id.cp_current_password);
        mNewPassword = findViewById(R.id.cp_new_password);
        mConfirmPassword = findViewById(R.id.cp_confirm_password);
        mChangePassword = findViewById(R.id.cp_change_passBtn);
        bundle = getIntent().getExtras();
        if(bundle != null){
         logToken = bundle.getString("Token");
        }

        currentBool = false;
        newBool     = false;
        confirmBool = false;

        mCurrentPassword.addTextChangedListener(textWatcher(0));
        mNewPassword.addTextChangedListener(textWatcher(1));
        mConfirmPassword.addTextChangedListener(textWatcher(2));
        mChangePassword.setOnClickListener(this::onClick);

    }

public TextWatcher textWatcher(int iterate){
   emptyField = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if((i-i1) >= 0){
                 if(iterate == 0){
                     currentBool = true;
                 }else if(iterate == 1){
                     newBool = true;
                 }else if(iterate == 2){
                     confirmBool = true;
                 }
                }else{
                    if(iterate == 0){
                        currentBool = false;
                    }else if(iterate == 1){
                        newBool = false;
                    }else if(iterate == 2){
                        confirmBool = false;
                    }
                }
                mChangePassword.setEnabled(currentBool && newBool && confirmBool);
                mChangePassword.setClickable(currentBool && newBool && confirmBool);
                if(currentBool && newBool && confirmBool){
                    mChangePassword.setBackground(getResources().getDrawable(R.drawable.enabled_button));
                }else{
                    mChangePassword.setBackground(getResources().getDrawable(R.drawable.disabled_button));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    return emptyField;
}

    public AlertDialog alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
        builder.setMessage(message).setPositiveButton(R.string.ALERT_OK,null);
        AlertDialog createAlertDialog = builder.create();
        createAlertDialog.show();
        return createAlertDialog;
    };
    public AlertDialog alertDialogDefined(int message,int title, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
        builder.setTitle(title).setMessage(message).setPositiveButton(R.string.ALERT_OK,listener).setNegativeButton(R.string.ALERT_CANCEL,null);
        AlertDialog createAlertDialog = builder.create();
        createAlertDialog.show();
        return createAlertDialog;
    };

public Callback<ChangePasswordResponse>  changePasswordResponseCallback(){
    Callback<ChangePasswordResponse> returnValue = new Callback<ChangePasswordResponse>() {
        @Override
        public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
            if(response.code() == 200) {
                  if(response.body().getConfirmationMessage() != null){
                  if(response.body().getConfirmationMessage().equals("Password Changed successfully")){
                      AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                      builder.setMessage("Password changed successfully").setPositiveButton(R.string.ALERT_OK, (dialogInterface, i) -> {
                          Intent intent = new Intent(ChangePassword.this, Login.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                      });
                      AlertDialog createAlertDialog = builder.create();
                      createAlertDialog.show();
                  }
                  }else if(response.body().getErrorMessage() != null){
                      alertDialog(response.body().getErrorMessage());
                  }
            }else{
                alertDialog(getString(R.string.try_again));
            }
        }

        @Override
        public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {

        }
    };
    return  returnValue;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cp_change_passBtn:
                verNewPassword = mNewPassword.getText().toString();
                verConfirmPassword = mConfirmPassword.getText().toString();
                verOldPassword = mCurrentPassword.getText().toString();
                if(verConfirmPassword.equals(verNewPassword)){
                    alertDialogDefined(R.string.alert_change_pass_confirm_msg,R.string.fa_change_password, (dialogInterface, i) -> {
                        Map<String,String> params = new HashMap<>();
                        params.put("OldPassword",verOldPassword);
                        params.put("NewPassword",verNewPassword);
                        params.put("ConfirmPassword",verConfirmPassword);
                        body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(params)).toString());
                        call = RetrofitClient
                                .getmInstance()
                                .getApi()
                                .getChangePassword(logToken,body);
                        call.enqueue(changePasswordResponseCallback());
                    });
                }else{
                    mConfirmPassword.setError(getString(R.string.cp_passwords_dont_match));
                }
                break;
        }
    }
}