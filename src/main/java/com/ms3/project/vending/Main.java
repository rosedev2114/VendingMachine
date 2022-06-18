package com.ms3.project.vending;

import com.ms3.project.vending.service.VendingMachineService;

public class Main {

    public static void main(String[] args) {
        VendingMachineService service = new VendingMachineService();
        service.start();
    }

}