package com.example.billzg.pillremindervol2.model;

public class Pills {

    private String name;
    private double quantity;
    private String when; //this is the time actually


    public Pills() {
    }

    public Pills(String name, double quantity,String when) {
        this.name = name;
        this.quantity = quantity;
        this.when = when;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }
}