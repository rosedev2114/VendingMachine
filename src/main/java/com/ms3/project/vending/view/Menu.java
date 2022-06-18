package com.ms3.project.vending.view;

import com.ms3.project.vending.model.Inventory;
import com.ms3.project.vending.model.Transcation;
import com.ms3.project.vending.model.MoneyEnum;

import java.io.IOException;

public final class Menu {
    
    private static void displayJsonBanner() {
        System.out.println("=====================");
        System.out.println("==== JSON IMPORT ====");
        System.out.println("=====================");
        System.out.println("");
    }

    private static void displayVendingBanner() {
        System.out.println("======================");
        System.out.println("======= VENDING ======");
        System.out.println("======================");
        System.out.println("");
    }

    private static void displayAuditBanner() {
        System.out.println("=====================");
        System.out.println("======= AUDIT =======");
        System.out.println("=====================");
        System.out.println("");
    }

    private static void displayMoneyBanner() {
        System.out.println("=====================");
        System.out.println("======= MONEY =======");
        System.out.println("=====================");
        System.out.println("");
    }


    public static void displayJsonMenu() {
        clearConsole();
        displayJsonBanner();
    }

    public static void displayVendingMenu(Inventory curInventory, Transcation curTranscation) {
        clearConsole();
        displayVendingBanner();

        if( curInventory.isConfigInitialized() == false ){
            System.out.println("Inventory is currently not configured.");
        }

        int itemsLength =  curInventory.getItems().size();

        for(int i = 0; i < itemsLength; i++ ){
            System.out.println(curInventory.indexToKeyCode(i) + ": " + curInventory.getItems().get(i).toString());
        }

        System.out.println("\nCurrent Balance: $" + curTranscation.getBalance().toString() + "\n");
    }

    public static void displayMoneyMenu(Transcation curTranscation) {
        clearConsole();
        displayMoneyBanner();

        for( int i = 0; i < MoneyEnum.BIG_TO_SMALL.size(); i++ ) {
            MoneyEnum money = MoneyEnum.BIG_TO_SMALL.get(i);
            System.out.println(String.valueOf((i + 1)) + ": $" + money.getValue().toString());    
        }

        System.out.println("\nCurrent Balance: $" + curTranscation.getBalance().toString() + "\n");
    }

    public static void displayAuditMenu() {
        clearConsole();
        displayAuditBanner();
    }

    public final static void clearConsole()
    {
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                new ProcessBuilder("clear").inheritIO().start().waitFor(); 
        } 
        catch (IOException | InterruptedException ex) {

        }
    }
    


}
