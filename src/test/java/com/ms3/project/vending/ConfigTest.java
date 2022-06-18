package com.ms3.project.vending;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import com.ms3.project.vending.model.Config;
import com.ms3.project.vending.utils.Constants;

public class ConfigTest {
    
    @Test
    public void testNormal() {
        Config config = new Config(1,2);
        assertEquals(config.getColumns(), 2);
        assertEquals(config.getRows(), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAlphabetOverflow() {
        new Config(Constants.UPPER_ALPHABET.length() + 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAlphabetOverflow() {
        Config config = new Config(1, 1);
        config.setRows(Constants.UPPER_ALPHABET.length() + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegRow() {
        new Config(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegCol() {
        new Config(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroRow() {
        new Config(0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroCol() {
        new Config(1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegRow() {
        Config config = new Config(1, 1);
        config.setRows(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegCol() {
        Config config = new Config(1, 1);
        config.setColumns(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetZeroRow() {
        Config config = new Config(1, 1);
        config.setRows(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetZeroCol() {
        Config config = new Config(1, 1);
        config.setColumns(0);
    }

}
