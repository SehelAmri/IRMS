package com.example.irms.Model;

public class SessionDetailsResponse {
    String GroupDesc,OfficeBusinessEntity,AllocatedStatus,DefaultCurrency,
            DefaultDistrictDesc,SessionID,SessionStartAt,ErrorMessage,DefaultRegionDesc;

    public String getGroupDesc(){
    return GroupDesc;
    };
    public String getOfficeBusinessEntity(){
        return OfficeBusinessEntity;
    };
    public String getAllocatedStatus(){
        return AllocatedStatus;
    };
    public String getDefaultCurrency(){
        return DefaultCurrency;
    }
    public String getDefaultDistrictDesc(){
        return DefaultDistrictDesc;
    }
    public String getDefaultRegionDesc(){
        return DefaultRegionDesc;
    }
    public String getErrorMessage(){
        return ErrorMessage;
    };
    public  String getSessionID(){return SessionID;}
    public String getSessionStartAt(){return SessionStartAt;}
}
