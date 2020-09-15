package com.example.irms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCategoryResponse {
    @SerializedName("CategoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("CategoryDescription")
    @Expose
    private String categoryDescription;

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

}
