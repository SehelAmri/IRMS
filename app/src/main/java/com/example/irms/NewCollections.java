package com.example.irms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.irms.Client.RetrofitClient;
import com.example.irms.Model.GetCategoryResponse;
import com.example.irms.Model.GetCurrencyResponse;
import com.example.irms.Model.SessionDetailsResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCollections extends Fragment {
    Spinner nc_currency_spinner, nc_category_spinner;
    String logToken, defaultBEID;
    Callback<List<GetCurrencyResponse>> currency_callback;
    LinearLayout layout_no_access, layout_accepted;
    Call<List<GetCategoryResponse>> category_call;
    ArrayList<String> currency_list, category_list;
    RequestBody category_request_body;
    Bundle bundle;
    Call<List<GetCurrencyResponse>> currency_call;
    Call sessionDtlCall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_collections, container, false);
        nc_currency_spinner = view.findViewById(R.id.nc_currency_spinner);
        nc_category_spinner = view.findViewById(R.id.nc_category_spinner);
        layout_accepted = view.findViewById(R.id.new_collection_accepted);
        layout_no_access = view.findViewById(R.id.new_collection_no_access);
        bundle = getArguments();
        if (bundle != null) {
            logToken = bundle.getString("Token");
        }

        sessionDtlCall = RetrofitClient
                .getmInstance()
                .getApi()
                .getDetails(logToken);
        sessionDtlCall.enqueue(sessionDetailsCallBack());

        return view;
    }

    public void nc_spinners(Spinner nc_spinner, ArrayList<String> currency_list) {
        ArrayAdapter<String> array_adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, currency_list);
        nc_spinner.setAdapter(array_adapter);
    }

    public Callback<List<GetCurrencyResponse>> currencyResponseCallBack() {
        currency_callback = new Callback<List<GetCurrencyResponse>>() {
            @Override
            public void onResponse(Call<List<GetCurrencyResponse>> call, Response<List<GetCurrencyResponse>> response) {
                for (GetCurrencyResponse list : response.body()) {
                    if (list.getCurrencyCode() != null) {
                        currency_list.add(list.getCurrencyCode());
                    }
                }
                nc_spinners(nc_currency_spinner, currency_list);
            }

            @Override
            public void onFailure(Call<List<GetCurrencyResponse>> call, Throwable t) {

            }
        };
        return currency_callback;
    }

    public Callback<List<GetCategoryResponse>> categoryResponseCallBack() {
        Callback<List<GetCategoryResponse>> callback = new Callback<List<GetCategoryResponse>>() {
            @Override
            public void onResponse(Call<List<GetCategoryResponse>> call, Response<List<GetCategoryResponse>> response) {
                for (GetCategoryResponse getCategoryResponse : response.body()) {
                    if (getCategoryResponse.getCategoryName() != null) {
                        category_list.add(getCategoryResponse.getCategoryName());
                    }
                }
                nc_spinners(nc_category_spinner, category_list);
            }

            @Override
            public void onFailure(Call<List<GetCategoryResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        return callback;
    }

    public Callback<SessionDetailsResponse> sessionDetailsCallBack() {
        Callback<SessionDetailsResponse> callback = new Callback<SessionDetailsResponse>() {
            @Override
            public void onResponse(Call<SessionDetailsResponse> call, Response<SessionDetailsResponse> response) {
                if (response.body().getAllocatedStatus() != null) {
                    if (response.body().getAllocatedStatus().equals("Accepted")) {
                        if (response.body().getDefaultBEID() != null) {
                            defaultBEID = response.body().getDefaultBEID();
                            category_function();
                            currency_function();
                        }
                        layout_accepted.setVisibility(View.VISIBLE);
                        layout_no_access.setVisibility(View.GONE);
                    } else {
                        layout_accepted.setVisibility(View.GONE);
                        layout_no_access.setVisibility(View.VISIBLE);
                    }
                } else {
                    layout_accepted.setVisibility(View.GONE);
                    layout_no_access.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SessionDetailsResponse> call, Throwable t) {

            }
        };
        return callback;
    }

    public void category_function() {
        category_list = new ArrayList<>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("BEID", defaultBEID);
        category_request_body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());

        category_call = RetrofitClient
                .getmInstance()
                .getApi()
                .getCategory(logToken, category_request_body);
        category_call.enqueue(categoryResponseCallBack());
    }

    public void currency_function() {
        currency_list = new ArrayList<>();
        currency_call = RetrofitClient
                .getmInstance()
                .getApi()
                .getCurrencyCode(logToken);
        currency_call.enqueue(currencyResponseCallBack());
    }

}
