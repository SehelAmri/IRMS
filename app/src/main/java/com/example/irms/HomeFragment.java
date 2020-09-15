package com.example.irms;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.irms.Client.RetrofitClient;
import com.example.irms.Model.AcceptSessionResponse;
import com.example.irms.Model.SessionDetailsResponse;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener{
    String logToken,logWorkStationID,logUserId,logWorkStationDtlID;
    Bundle bundle;
    TextView AllocatedOrAccepted,hmBusinessEntity,
            hmSessionID,hmGroupDesc,hmSessionDate,
            hmDistrict,hmRegion,hmCurrency;
    Button  hmSessionBtn,hmRetrySession,hmNewCollectionBtn;
    Fragment selectedCollectionsFragment;
    private Context mContext;
    Call<SessionDetailsResponse> call,retryCall;
    Call<AcceptSessionResponse>  acceptCall;
    LinearLayout noSessionAvailable,currentSession;
    Boolean retryBtnIsClicked = false;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
          call = RetrofitClient
                .getmInstance()
                .getApi()
                .getDetails(logToken);
        call.enqueue(sessionDetailsCallBack());

        hmRetrySession.setOnClickListener(view -> {
            retryBtnIsClicked = true;
            retryCall = RetrofitClient
                    .getmInstance()
                    .getApi()
                    .getDetails(logToken);
            retryCall.enqueue(sessionDetailsCallBack());
        });
        //
        hmSessionBtn.setOnClickListener(view -> {
            if(hmSessionBtn.getText().equals(getString(R.string.hmAcceptSession))) {
                acceptCall = RetrofitClient
                        .getmInstance()
                        .getApi()
                        .getAcceptDetails(logToken);
                acceptCall.enqueue(saSessionCallBack());
            }else if(hmSessionBtn.getText().equals(getString(R.string.hmSurrenderSession))){
                acceptCall = RetrofitClient
                        .getmInstance()
                        .getApi()
                        .getSurrenderDetails(logToken);
                acceptCall.enqueue(saSessionCallBack());
            }
        });
        hmNewCollectionBtn.setOnClickListener(this);
        //
        return view;
    }
    public AlertDialog alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setPositiveButton(R.string.ALERT_OK,null);
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
                                hmSessionBtn.setText(R.string.hmSurrenderSession);
                                AllocatedOrAccepted.setText(R.string.hmAcceptedDate);
                                hmNewCollectionBtn.setBackgroundResource(R.drawable.enabled_button);
                                hmNewCollectionBtn.setEnabled(true);
                            } else {
                                hmSessionBtn.setText(R.string.hmAcceptSession);
                                AllocatedOrAccepted.setText(R.string.hmAllocatedDate);
                                hmNewCollectionBtn.setBackgroundResource(R.drawable.disabled_button);
                                hmNewCollectionBtn.setEnabled(false);
                            }
                            currentSession.setVisibility(View.VISIBLE);
                            noSessionAvailable.setVisibility(View.GONE);
                        } else {
                            noSessionAvailable.setVisibility(View.VISIBLE);
                            currentSession.setVisibility(View.GONE);
                            hmNewCollectionBtn.setBackgroundResource(R.drawable.disabled_button);
                            hmNewCollectionBtn.setEnabled(false);
                        }
                    }else{
                        noSessionAvailable.setVisibility(View.VISIBLE);
                        currentSession.setVisibility(View.GONE);
                        if(response.body().getErrorMessage() != null && retryBtnIsClicked) {
                            alertDialog(response.body().getErrorMessage());
                        }
                        hmNewCollectionBtn.setBackgroundResource(R.drawable.disabled_button);
                        hmNewCollectionBtn.setEnabled(false);
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
                        hmSessionBtn.setText(R.string.hmSurrenderSession);
                        AllocatedOrAccepted.setText(R.string.hmAcceptedDate);
                        hmNewCollectionBtn.setBackgroundResource(R.drawable.enabled_button);
                        hmNewCollectionBtn.setEnabled(true);
                    } else if (response.body().getAllocatedStatus().equals("Allocated")) {
                        hmSessionBtn.setText(R.string.hmAcceptSession);
                        AllocatedOrAccepted.setText(R.string.hmAllocatedDate);
                        hmNewCollectionBtn.setBackgroundResource(R.drawable.disabled_button);
                        hmNewCollectionBtn.setEnabled(false);

                    } else{
                        noSessionAvailable.setVisibility(View.VISIBLE);
                        currentSession.setVisibility(View.GONE);
                        hmNewCollectionBtn.setBackgroundResource(R.drawable.disabled_button);
                        hmNewCollectionBtn.setEnabled(false);
                    }
                }else{
                    alertDialog(getString(R.string.hmUnableToChangeCurrentSession));
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
            logToken = bundle.getString("Token");
            logUserId = bundle.getString("UserID");
            logWorkStationID = bundle.getString("WorkStationID");
            logWorkStationDtlID = bundle.getString("WorkStationDtlID");
        }

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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.hmNewCollectionBtn:
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.hm_bottom_navigation);
                selectedCollectionsFragment = new CollectionsFragment();
                selectedCollectionsFragment.setArguments(bundle);
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,selectedCollectionsFragment)
                        .commit();
        }
    }
}
