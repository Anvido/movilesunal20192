package com.example.sqliteapp.model;

public class Company {
    private long compId;
    private String compName;
    private String compWebPage;
    private int compNumber;
    private String compEmail;
    private String compProducts;
    private String compServices;
    private String compClassification;

    public  Company(){}

    public Company(long compId, String compName, String compWebPage, int compNumber, String compEmail, String compProducts, String compServices, String compClassification) {
        this.compId = compId;
        this.compName = compName;
        this.compWebPage = compWebPage;
        this.compNumber = compNumber;
        this.compEmail = compEmail;
        this.compProducts = compProducts;
        this.compServices = compServices;
        this.compClassification = compClassification;
    }

    public long getCompId() {
        return compId;
    }

    public void setCompId(long compId) {
        this.compId = compId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompWebPage() {
        return compWebPage;
    }

    public void setCompWebPage(String compWebPage) {
        this.compWebPage = compWebPage;
    }

    public int getCompNumber() {
        return compNumber;
    }

    public void setCompNumber(int compNumber) {
        this.compNumber = compNumber;
    }

    public String getCompEmail() {
        return compEmail;
    }

    public void setCompEmail(String compEmail) {
        this.compEmail = compEmail;
    }

    public String getCompProducts() {
        return compProducts;
    }

    public void setCompProducts(String compProducts) {
        this.compProducts = compProducts;
    }

    public String getCompServices() {
        return compServices;
    }

    public void setCompServices(String compServices) {
        this.compServices = compServices;
    }

    public String getCompClassification() {
        return compClassification;
    }

    public void setCompClassification(String compClassification) {
        this.compClassification = compClassification;
    }

    @Override
    public String toString() {
        return  "Id: " + compId +
                "\n Empresa: " + compName +
                "\n Web Page: " + compWebPage +
                "\n Telefono: " + compNumber +
                "\n Email: " + compEmail +
                "\n Productos: " + compProducts +
                "\n Servicios: " + compServices +
                "\n Clasificación: " + compClassification;
    }
}
