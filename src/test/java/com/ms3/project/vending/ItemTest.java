package com.ms3.project.vending;

import com.ms3.project.vending.model.Item;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

public class ItemTest {

    @Test(expected = NumberFormatException.class)
    public void testInvalidPrice() {
        new Item("Test","BAD PRICE",1).getMonetaryAmount();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPrice() {
        new Item("Test","",1).getMonetaryAmount();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroPrice()  {
        new Item("Test","0.00",1).getMonetaryAmount();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegPrice()  {
        new Item("Test","-$1.00",1).getMonetaryAmount();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegPriceTwo()  {
        new Item("Test","$-1.00",1).getMonetaryAmount();
    }

    @Test
    public void testReduceLargePrecision() {
        BigDecimal value = new Item("Test","2.000001",1).getMonetaryAmount();
        assertEquals(value, new BigDecimal("2.00"));
    }

    @Test
    public void testSettersGetters() { 
        Item item = new Item("Test","$1.00",1);
        item.setName("Test2");
        item.setAmount(2);
        item.setPrice("$2.00");
        assertEquals(item.getName(), "Test2");
        assertEquals(item.getAmount(), 2);
        assertEquals(item.getPrice(), "$2.00");
    }


}
