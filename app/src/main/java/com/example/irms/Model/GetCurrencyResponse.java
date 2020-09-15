package com.example.irms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCurrencyResponse {

    @SerializedName("CurrencyCode")
    @Expose
    private String currencyCode;
    @SerializedName("CurrName")
    @Expose
    private String currName;
    @SerializedName("CurrSymbol")
    @Expose
    private String currSymbol;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrName() {
        return currName;
    }

    public void setCurrName(String currName) {
        this.currName = currName;
    }

    public String getCurrSymbol() {
        return currSymbol;
    }

    public void setCurrSymbol(String currSymbol) {
        this.currSymbol = currSymbol;
    }

}