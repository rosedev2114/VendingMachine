package com.ms3.project.vending;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.*;

import com.ms3.project.vending.model.Item;
import com.ms3.project.vending.model.MoneyEnum;
import com.ms3.project.vending.model.Transcation;

public class TransactionTest {
    
    @Test
    public void testAddPenny() {
        Transcation transcation = new Transcation();
        transcation.addMoney(MoneyEnum.PENNY);
        assertEquals(transcation.getBalance(), MoneyEnum.PENNY.getValue());
    }
    @Test
    public void testAddNickel() {
        Transcation transcation = new Transcation();
        transcation.addMoney(MoneyEnum.NICKEL);
        assertEquals(transcation.getBalance(), MoneyEnum.NICKEL.getValue());
    }
    @Test
    public void testAddQuarter() {
        Transcation transcation = new Transcation();
        transcation.addMoney(MoneyEnum.QUARTER);
        assertEquals(transcation.getBalance(), MoneyEnum.QUARTER.getValue());
    }
    @Test
    public void testAddDollar() {
        Transcation transcation = new Transcation();
        transcation.addMoney(MoneyEnum.DOLLAR);
        assertEquals(transcation.getBalance(), MoneyEnum.DOLLAR.getValue());
    }
    @Test
    public void testAddFiveDollar() {
        Transcation transcation = new Transcation();
        transcation.addMoney(MoneyEnum.FIVE_DOLLAR);
        assertEquals(transcation.getBalance(), MoneyEnum.FIVE_DOLLAR.getValue());
    }
    @Test
    public void testAddTenDollar() {
        Transcation transcation = new Transcation();
        transcation.addMoney(MoneyEnum.TEN_DOLLAR);
        assertEquals(transcation.getBalance(), MoneyEnum.TEN_DOLLAR.getValue());
    }
    @Test
    public void testAddTwentyDollar() {
        Transcation transcation = new Transcation();
        transcation.addMoney(MoneyEnum.TWENTY_DOLLAR);
        assertEquals(transcation.getBalance(), MoneyEnum.TWENTY_DOLLAR.getValue());
    }
    @Test(expected = IllegalStateException.class)
    public void testInvalidFunds() {
        Transcation transcation = new Transcation();
        Item item = new Item("Test","$1.50",1);
        transcation.purchase(item);
    }
    @Test(expected = IllegalStateException.class)
    public void testOutOfStockItem() {
        Transcation transcation = new Transcation();
        Item item = new Item("Test","$1.50",0);
        transcation.purchase(item);
    }
    @Test
    public void testClearBalance() {
        Transcation transcation = new Transcation();
        transcation.addMoney(MoneyEnum.TWENTY_DOLLAR);
        transcation.clearBalance();
        assertEquals(transcation.getBalance(), new BigDecimal("0.00"));
    }
}
