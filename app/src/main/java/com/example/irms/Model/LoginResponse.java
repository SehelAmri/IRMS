package com.example.irms.Model;

public class LoginResponse {
    String UserName;
    String WorkStationDtlID;
    String UserID;
    String WorkStationID;
    String Token;
    String ErrorMessage;

    public String getUserName() {
        return UserName;
    }

    ;

    public String getWorkStationID() {
        return WorkStationID;
    }

    ;

    public String getUserID() {
        return UserID;
    }

    ;

    public String getToken() {
        return Token;
    }

    ;

    public String getWorkStationDtlID() {
        return WorkStationDtlID;
    }

    ;

    public String getErrorMessage() {
        return ErrorMessage;
    }

    ;
}
