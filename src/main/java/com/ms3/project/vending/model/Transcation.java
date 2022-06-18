package com.ms3.project.vending.model;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class Transcation {
 
    private BigDecimal balance; 

    public Transcation() {
        this.balance = new BigDecimal("0.00").setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public boolean validateTranscation(Item order) {
        return this.validateItem(order) && this.validateBalance(order);
    }

    public boolean validateItem(Item order) {
        return order.getAmount() > 0;
    }

    public boolean validateBalance(Item order) {
        BigDecimal tempCost = order.getMonetaryAmount().setScale(2, RoundingMode.HALF_UP);
        BigDecimal tempBalance = this.balance.setScale(2, RoundingMode.HALF_UP);
        return tempBalance.subtract(tempCost).signum() != -1;        
    }

    public BigDecimal clearBalance() {
        BigDecimal tempBalance = this.balance.setScale(2, RoundingMode.HALF_UP);
        this.balance = new BigDecimal(0.00).setScale(2);
        return tempBalance;
    }

    public boolean purchase(Item order) {
        if( this.validateItem(order) == false ) {
            throw new IllegalStateException("Could not process the order, the item is out of stock.");
        }
        if( this.validateBalance(order) == false ) {
            throw new IllegalStateException("Could not proces the order, insufficient funds.");
        }

        BigDecimal cost = order.getMonetaryAmount().setScale(2, RoundingMode.HALF_UP);
        this.balance = this.balance.setScale(2, RoundingMode.HALF_UP).subtract(cost);
        return true;
    }

    public void addMoney(MoneyEnum money) {
        this.balance = balance.add(money.getValue());
    }

    @Override
    public String toString() {
        return "Current Balance: $" + this.balance.toString(); 
    }

}
