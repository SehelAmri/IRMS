package com.example.irms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.sip.SipSession;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lucasurbas.listitemview.ListItemView;

import java.util.Locale;

public class AccountFragment extends Fragment implements View.OnClickListener{
    TextView tvName,tvWorkstation;
    ListItemView log_out,set_en,set_sw,change_password;
    BottomNavigationView bottomNavigationView;
    String logUserName,logToken;
    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.fragment_account,container,false);
     initView(view);
     tvName.setText(logUserName);
     tvWorkstation.setText(Build.SERIAL);
     set_en.setOnClickListener(this);
     set_sw.setOnClickListener(this);
     change_password.setOnClickListener(this);
     log_out.setOnClickListener(this);
     return view;
    }
    public void initView(View view){
        tvName = view.findViewById(R.id.act_account_name);
        tvWorkstation = view.findViewById(R.id.act_account_workstation);
        set_en = view.findViewById(R.id.set_en);
        set_sw = view.findViewById(R.id.set_sw);
        log_out = view.findViewById(R.id.log_out);
        change_password = view.findViewById(R.id.change_password);
        bottomNavigationView = getActivity().findViewById(R.id.hm_bottom_navigation);
        bundle = getArguments();
        if(bundle != null){
            logUserName = bundle.getString("UserName");
            logToken = bundle.getString("Token");
        }
    }
    public AlertDialog alertDialog(int message,int title, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setPositiveButton(R.string.ALERT_OK,listener).setNegativeButton(R.string.ALERT_CANCEL,null);
        AlertDialog createAlertDialog = builder.create();
        createAlertDialog.show();
        return createAlertDialog;
    };
    private void setLocale(String lang) {
        Resources res =getActivity().getResources();
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
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My Lang",lang);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_en:
                setLocale("en");
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                getActivity().recreate();
                break;
            case R.id.set_sw:
                setLocale("sw");
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                getActivity().recreate();
                break;
            case R.id.log_out:
                alertDialog(R.string.alert_logout_confirm_msg, R.string.alert_logout_confirm_title, (dialogInterface, i) -> {
                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
                break;
            case R.id.change_password:
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                intent.putExtra("Token",logToken);
                startActivity(intent);
                break;
        }
    }
}

