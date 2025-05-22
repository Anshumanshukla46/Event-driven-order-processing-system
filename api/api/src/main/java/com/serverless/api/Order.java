package com.serverless.api;

public class Order {

    private String id;
    private String product;

    public Order(){

    }

    public Order(String id, String product) {
        this.id = id;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
