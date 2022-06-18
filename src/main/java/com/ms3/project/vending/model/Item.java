package com.ms3.project.vending.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Item {

    private String name;
    private String price;
    private int amount;

    public Item(String name, String price, int amount) {
        this.name = name;
        this.price = price.trim();
        this.amount = amount;
    }

    public String getName() {
        return this.name;
    }

    public String getPrice() {
        return this.price;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price.trim();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getMonetaryAmount() {

        if( this.price.length() == 0 ){
            throw new IllegalArgumentException("Empty String given to Item Price, expects a Non-zero Positive Number: " + price);
        }

        final BigDecimal decimalValue;

        if( this.price.charAt(0) == '$' ){
            decimalValue = new BigDecimal(price.substring(1));
        } else {
            decimalValue = new BigDecimal(price);
        }

        if( decimalValue.signum() != 1 ) {
            throw new IllegalArgumentException("Negative or Zero Price given to Item Price, expects a Non-zero Positive Number: " + price);
        }

        return decimalValue.setScale(2, RoundingMode.HALF_UP);
 
    }

    @Override
    public String toString() {
        return this.getPrice() + " : " + this.getName() +  " : " + String.valueOf(this.getAmount());
    }

}
