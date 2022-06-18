package com.ms3.project.vending.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public enum MoneyEnum {

    PENNY(new BigDecimal("0.01")),
    NICKEL(new BigDecimal("0.05")),
    QUARTER(new BigDecimal("0.25")),
    DOLLAR(new BigDecimal("1.00")),
    FIVE_DOLLAR(new BigDecimal("5.00")),
    TEN_DOLLAR(new BigDecimal("10.00")),
    TWENTY_DOLLAR(new BigDecimal("20.00"));
    
    public static final List<MoneyEnum> BIG_TO_SMALL = new ArrayList<>(
        Arrays.asList(
            TWENTY_DOLLAR,
            TEN_DOLLAR,
            FIVE_DOLLAR,
            DOLLAR,
            QUARTER,
            NICKEL,
            PENNY
        )
    );

    private BigDecimal value;
    
    MoneyEnum(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue(){ return this.value; }
    
}
