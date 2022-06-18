package com.ms3.project.vending.model;

import com.ms3.project.vending.utils.Constants;

public class Config {
    
    private int rows;
    private int columns;

    public Config(int rows, int columns) {

        if(rows <= 0 || rows > Constants.UPPER_ALPHABET.length()) {
            throw new IllegalArgumentException(
                "Config rows can not represent row need a positive non-zero value and less than " +
                String.valueOf(Constants.UPPER_ALPHABET.length()) +
                "to represent the current alphabet: " +
                Constants.UPPER_ALPHABET
            );
        }

        if( columns <= 0 ) {
            throw new IllegalArgumentException("Config columns can needs to be a positive non-zero value");
        }

        this.rows = rows;
        this.columns = columns;
    }


    public int getRows() {
        return this.rows;
    }
    
    public int getColumns() {
        return this.columns;
    }

    public int maxCapacity() {
        return this.rows * this.columns;
    }

    public void setRows(int rows) {

        if(rows <= 0 || rows > Constants.UPPER_ALPHABET.length()) {
            throw new IllegalArgumentException(
                "Config rows can not represent row need a positive non-zero value and less than " +
                String.valueOf(Constants.UPPER_ALPHABET.length()) +
                "to represent the current alphabet: " +
                Constants.UPPER_ALPHABET
            );
        }

        this.rows = rows;
    }

    public void setColumns(int columns) {

        if( columns <= 0 ) {
            throw new IllegalArgumentException("Config columns needs to be a positive non-zero value");
        }

        this.columns = columns;
    }

}
