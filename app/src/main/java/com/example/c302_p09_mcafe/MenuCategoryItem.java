package com.example.c302_p09_mcafe;
import java.io.Serializable;

public class MenuCategoryItem implements Serializable {
    private String id;
    private String categoryId;
    private String description;
    private double unitPrice;

    public MenuCategoryItem(String id, String categoryId, String description, double unitPrice) {
        this.id = id;
        this.categoryId = categoryId;
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return description;
    }
}
