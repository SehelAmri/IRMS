package com.example.irms;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.irms.Client.RetrofitClient;
import com.example.irms.Model.AcceptSessionResponse;
import com.example.irms.Model.SessionDetailsResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    String logAuth,logWorkStationID,logUserId,logWorkStationDtlID;
    Bundle bundle;
    TextView AllocatedOrAccepted,hmBusinessEntity,
            hmSessionID,hmGroupDesc,hmSessionDate,
            hmDistrict,hmRegion,hmCurrency;
    Button  hmSessionBtn,hmRetrySession,hmNewCollectionBtn;
    RequestBody sessionDtlBody,saSessionBody;
    Call<SessionDetailsResponse> call,retryCall;
    Call<AcceptSessionResponse>  acceptCall;
    Map<String,String> sessionDetailsParams,saSessionParams;
    LinearLayout noSessionAvailable,currentSession;
    Boolean retryBtnIsClicked = false;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        sessionDetailsParams.put("UserID",logUserId);
        sessionDetailsParams.put("WorkStationID",logWorkStationID);
        sessionDtlBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(sessionDetailsParams)).toString());
        saSessionParams.put("UserID", logUserId);
        saSessionParams.put("WorkStationID", logWorkStationID);
        saSessionParams.put("WorkStationDtlID", logWorkStationDtlID);
        saSessionParams.put("WorkStationName", Build.SERIAL);
          call = RetrofitClient
                .getmInstance()
                .getApi()
                .getDetails(logAuth,sessionDtlBody);
        call.enqueue(sessionDetailsCallBack());

        hmRetrySession.setOnClickListener(view -> {
            retryBtnIsClicked = true;
            retryCall = RetrofitClient
                    .getmInstance()
                    .getApi()
                    .getDetails(logAuth,sessionDtlBody);
            retryCall.enqueue(sessionDetailsCallBack());
        });
        //
        hmSessionBtn.setOnClickListener(view -> {
            saSessionParams.put("SessionID", hmSessionID.getText().toString());
            saSessionBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(saSessionParams)).toString());
            if(hmSessionBtn.getText().equals("ACCEPT")) {
                acceptCall = RetrofitClient
                        .getmInstance()
                        .getApi()
                        .getAcceptDetails(logAuth, saSessionBody);
                acceptCall.enqueue(saSessionCallBack());
                hmNewCollectionBtn.setBackground(getResources().getDrawable(R.drawable.enabled_button));
            }else if(hmSessionBtn.getText().equals("SURRENDER")){
                acceptCall = RetrofitClient
                        .getmInstance()
                        .getApi()
                        .getSurrenderDetails(logAuth, saSessionBody);
                acceptCall.enqueue(saSessionCallBack());
                hmNewCollectionBtn.setBackground(getResources().getDrawable(R.drawable.disabled_button));
            }
        });
        //
        return view;
    }
    public AlertDialog alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setPositiveButton("OK",null);
        AlertDialog createAlertDialog = builder.create();
        createAlertDialog.show();
        return createAlertDialog;
    };
    public Callback<SessionDetailsResponse> sessionDetailsCallBack(){
        Callback<SessionDetailsResponse> callback = new Callback<SessionDetailsResponse>() {
            @Override
            public void onResponse(Call<SessionDetailsResponse> call, Response<SessionDetailsResponse> response) {
                if(response.code() == 200) {
                    if(response.body().getOfficeBusinessEntity() != null) {
                        hmBusinessEntity.setText(response.body().getOfficeBusinessEntity());
                        hmDistrict.setText(response.body().getDefaultDistrictDesc());
                        hmRegion.setText(response.body().getDefaultRegionDesc());
                        hmCurrency.setText(response.body().getDefaultCurrency());
                    }
                    if(response.body().getAllocatedStatus() != null) {
                        if (response.body().getAllocatedStatus().equals("Allocated") || response.body().getAllocatedStatus().equals("Accepted")) {
                            hmSessionID.setText(response.body().getSessionID());
                            hmGroupDesc.setText(response.body().getGroupDesc());
                            hmSessionDate.setText(response.body().getSessionStartAt());
                            if (response.body().getAllocatedStatus().equals("Accepted")) {
                                hmSessionBtn.setText("SURRENDER");
                                AllocatedOrAccepted.setText("ACCEPTED DATE");
                                hmNewCollectionBtn.setBackground(getResources().getDrawable(R.drawable.enabled_button));
                            } else {
                                hmSessionBtn.setText("ACCEPT");
                                AllocatedOrAccepted.setText("ALLOCATED DATE");
                                hmNewCollectionBtn.setBackground(getResources().getDrawable(R.drawable.disabled_button));
                            }
                            currentSession.setVisibility(View.VISIBLE);
                            noSessionAvailable.setVisibility(View.GONE);
                        } else {
                            noSessionAvailable.setVisibility(View.VISIBLE);
                            currentSession.setVisibility(View.GONE);
                            hmNewCollectionBtn.setBackground(getResources().getDrawable(R.drawable.disabled_button));
                        }
                    }else{
                        noSessionAvailable.setVisibility(View.VISIBLE);
                        currentSession.setVisibility(View.GONE);
                        if(response.body().getErrorMessage() != null && retryBtnIsClicked) {
                            alertDialog(response.body().getErrorMessage());
                        }
                        hmNewCollectionBtn.setBackground(getResources().getDrawable(R.drawable.disabled_button));
                    }
                }
            }

            @Override
            public void onFailure(Call<SessionDetailsResponse> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_LONG).show();
                Log.e("error main", "onFailure: " + t.getMessage() );
            }
        };
        return callback;
    }
    public Callback<AcceptSessionResponse> saSessionCallBack(){
        Callback<AcceptSessionResponse> callback = new Callback<AcceptSessionResponse>() {
            @Override
            public void onResponse(Call<AcceptSessionResponse> call, Response<AcceptSessionResponse> response) {
                if(response.body().getAllocatedStatus() != null) {
                    if (response.body().getAllocatedStatus().equals("Accepted")) {
                        hmSessionBtn.setText("SURRENDER");
                        AllocatedOrAccepted.setText("ACCEPTED DATE");
                    } else if (response.body().getAllocatedStatus().equals("Allocated")) {
                        hmSessionBtn.setText("ACCEPT");
                        AllocatedOrAccepted.setText("ALLOCATED DATE");
                    } else{
                        noSessionAvailable.setVisibility(View.VISIBLE);
                        currentSession.setVisibility(View.GONE);
                    }
                }
                if(response.body().getErrorMessage() != null) {
                    alertDialog(response.body().getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<AcceptSessionResponse> call, Throwable t) {
                Log.e("error main", "onFailure: " + t.getMessage() );
            }
        };
        return  callback;
    }
    private void initView(View view){
        bundle = getArguments();
        if(bundle != null) {
            logAuth = bundle.getString("Auth");
            logUserId = bundle.getString("UserID");
            logWorkStationID = bundle.getString("WorkStationID");
            logWorkStationDtlID = bundle.getString("WorkStationDtlID");
        }

        sessionDetailsParams = new HashMap<String, String>();
        saSessionParams = new HashMap<String, String>();
        noSessionAvailable = view.findViewById(R.id.hmNoSession);
        currentSession = view.findViewById(R.id.hmCurrentSession);
        AllocatedOrAccepted = view.findViewById(R.id.hmAcceptedOrAllocated);
        hmSessionBtn = view.findViewById(R.id.hmSessionBtn);
        hmRetrySession = view.findViewById(R.id.hmRetrySession);
        hmBusinessEntity = view.findViewById(R.id.hmBusinessEntity);
        hmNewCollectionBtn = view.findViewById(R.id.hmNewCollectionBtn);
        hmSessionID = view.findViewById(R.id.hmSessionID);
        hmGroupDesc = view.findViewById(R.id.hmGroupDesc);
        hmDistrict = view.findViewById(R.id.hmDistrict);
        hmRegion = view.findViewById(R.id.hmRegion);
        hmCurrency = view.findViewById(R.id.hmCurrency);
        hmSessionDate = view.findViewById(R.id.hmSessionDate);
    }
}
