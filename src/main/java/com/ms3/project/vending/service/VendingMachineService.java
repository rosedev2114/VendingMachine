package com.ms3.project.vending.service;

import com.google.gson.Gson;
import com.ms3.project.vending.model.Inventory;
import com.ms3.project.vending.model.Item;
import com.ms3.project.vending.model.MenuState;
import com.ms3.project.vending.model.MoneyEnum;
import com.ms3.project.vending.model.Transcation;
import com.ms3.project.vending.view.Menu;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

public class VendingMachineService {
    
    private static final Inventory INVENTORY = new Inventory();
    private static final Transcation TRANSCATION = new Transcation();
    private static final Scanner SCANNER = new Scanner(System.in);

    // Attempts to initalize the config inside of <java_execution_path>/<.vending.json>
    private static final Path LOG_PATH = Paths.get(System.getProperty("user.dir"),"logs.txt");
    private static final Path SAVE_PATH = Paths.get(System.getProperty("user.dir"),".vending.json");

    private static String INPUT = "";
    private static MenuState STATE = MenuState.VENDING;

    private static final HashMap<String, MenuState> INPUT_TO_MENU = new HashMap<>() {{
        put("/", MenuState.VENDING);
        put("+", MenuState.ADD_MONEY);
        put("-", MenuState.MONEY_REFUND);
        put("#", MenuState.CONFIG);
        put("=", MenuState.AUDIT);
        put("X", MenuState.EXIT);
    }};


    public VendingMachineService() { 

        // Add logger to 
        writeLog("NEW BOOT: Balance: $" + TRANSCATION.getBalance());

        if( Files.exists(SAVE_PATH) == false ) {

            writeLog("Warning: Config does not exist on boot.");
            STATE = MenuState.CONFIG; // Config is not created yet. Recover With new Config File Prompt

            return;
        }
        
        boolean loadSucessful = false;

        try {

            Reader reader = Files.newBufferedReader(SAVE_PATH);
            INVENTORY.loadJson(reader);

        } catch(Exception ex) { 

            writeLog("Failed to Load Config JSON:" + ex);
            STATE = MenuState.CONFIG; // Failed to either, read-file, parse-json or invalid data format. Recover With new Config File Prompt.

        }

        if( loadSucessful ) {
            saveJson();
        }

    }

    private static void anyKeyPrompt() {
        System.out.println("Press any key to continue");
        SCANNER.nextLine().toUpperCase();
    }

    private static void saveJson() {
        try (PrintWriter out = new PrintWriter(new FileWriter(SAVE_PATH.toFile()))) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(INVENTORY);
            out.write(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeLog(String msg) {
        try {
            Files.write(LOG_PATH, (msg + "\n").getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE); 
        } catch( IOException e ){ 
            e.printStackTrace();
        }
    }

    public void start() {

        while(true) {

            if( STATE == MenuState.EXIT ) {
                System.out.println("Exiting...");
                break;
            }

            switch(STATE) {

                case VENDING:

                    if( INVENTORY.isConfigInitialized() == false ){
                        STATE = MenuState.CONFIG;
                        break;
                    }

                    Menu.displayVendingMenu(INVENTORY, TRANSCATION);
                    System.out.println("[A1 ... ] Enter key code to purchase [+] Add Money [-] Refund Money [=] Audit Logs [#] Config [x] Exit Menu");
                    INPUT = SCANNER.nextLine().toUpperCase();

                    if( INPUT_TO_MENU.containsKey(INPUT) ){
                        STATE = INPUT_TO_MENU.get(INPUT);
                        break;
                    }

                    try {

                        Optional<Item> itemResult = INVENTORY.findByCode(INPUT);

                        if( itemResult.isEmpty() ){
                            System.out.println("That shelf is currently empty.");
                            anyKeyPrompt(); 
                        }

                        Item item = itemResult.get();

                        if( TRANSCATION.validateItem(item) == false ) {
                            System.out.println("That shelf is currently out-of-stock");
                            anyKeyPrompt();
                            break;
                        }
                        if( TRANSCATION.validateBalance(item) == false ){
                            System.out.println("Insufficient Funds");
                            anyKeyPrompt();
                            break;
                        }

                        String previousBalance = TRANSCATION.getBalance().toString();
                        TRANSCATION.purchase(item);
                        INVENTORY.decrementItem(INPUT);
                        
                        writeLog("PURCHASE: Name: " +
                            item.getName() +
                            " Price: " +
                            item.getPrice() +
                            " Amount Left: " +
                            String.valueOf(item.getAmount()) +
                            " Starting Balance: $" +
                            previousBalance +
                            " Ending Balance: $" +
                            TRANSCATION.getBalance().toString()
                        );
                       
                        saveJson();
 
                    } catch (Exception ex) {
                        System.out.println("Invalid input, not a valid menu choice." + ex);
                        anyKeyPrompt();
                    }

                    break;

                case ADD_MONEY:

                    if( INVENTORY.isConfigInitialized() == false ){
                        STATE = MenuState.CONFIG;
                        break;
                    }

                    Menu.displayMoneyMenu(TRANSCATION);

                    System.out.println("[1 ... ] Enter code to add money [/] Make Purchase [-] Refund Money [=] Audit Logs [#] Config [x] Exit Menu");
                    INPUT = SCANNER.nextLine().toUpperCase();

                    if( INPUT_TO_MENU.containsKey(INPUT) ){
                        STATE = INPUT_TO_MENU.get(INPUT);
                        break;
                    }
                    else {

                        try {
                            int parseValue = Integer.parseInt(INPUT);

                            if( parseValue <= 0 ){
                                System.out.println("Negative non-zero number given, please enter correct value.");
                                break;
                            }
                        
                            parseValue = parseValue - 1; // one-indexed to zero-indexed. 

                            if( parseValue > MoneyEnum.BIG_TO_SMALL.size() ){
                                System.out.println("Value to too large, please enter correct value.");
                                break;                                
                            }

                            String previousBalance = TRANSCATION.getBalance().toString();
                           
                            MoneyEnum money = MoneyEnum.BIG_TO_SMALL.get(parseValue);
                           
                            TRANSCATION.addMoney(money);

                            writeLog("ADDED MONEY: Added Balance: $" + String.valueOf(money.getValue()) + " Previous Balance: $" + previousBalance + " Current Balance: $" + TRANSCATION.getBalance().toString());

                        } catch(NumberFormatException ex) {
                            System.out.println("Invalid input, not a valid menu choice.");
                            break;
                        }

                    }

                    break;


                case MONEY_REFUND:

                    if( INVENTORY.isConfigInitialized() == false ){
                        STATE = MenuState.CONFIG;
                        break;
                    }

                    BigDecimal refundAmount = TRANSCATION.clearBalance();
                    Menu.displayMoneyMenu(TRANSCATION);

                    writeLog("REFUNDED: Refunded Balance: $" + refundAmount.toString() + " Current Balance: $" + TRANSCATION.getBalance().toString());

                    System.out.println("Refund: $" + refundAmount.toString() + "\n");
                    System.out.println("Press any key to continue.");

                    SCANNER.nextLine().toUpperCase();

                    STATE = MenuState.ADD_MONEY;

                    break;                
    
                case AUDIT:

                    Menu.displayAuditMenu();

                    if( LOG_PATH.toFile().exists() ) {
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(LOG_PATH.toFile()));
                            String line;
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    System.out.println("\n[/] Make Purchase [+] Add Money [-] Refund Money [#] Config [x] Exit");
                    
                    INPUT = SCANNER.nextLine().toUpperCase();

                    if( INPUT_TO_MENU.containsKey(INPUT) ){
                        STATE = INPUT_TO_MENU.get(INPUT);
                        break;
                    }

                    break;

                case CONFIG:

                    Menu.displayJsonMenu();

                    if( INVENTORY.isConfigInitialized() == false ) {

                        System.out.println("Config needs to be set before we can begin vending.");
                        System.out.println("1: File Path\n2: String/Text\n");
                        System.out.println("[x] Exit Menu");

                        INPUT = SCANNER.nextLine().toUpperCase();

                        if( INPUT.equals("X") ) { STATE = MenuState.EXIT; break; }

                    }
                    else {

                        System.out.println("1: File Path\n2: String/Text\n");
                        System.out.println("[/] Make Purchase [+] Add Money [-] Refund Money [=] Audit Logs [x] Exit Menu");
                        INPUT = SCANNER.nextLine().toUpperCase();

                        if( INPUT_TO_MENU.containsKey(INPUT) ){
                            STATE = INPUT_TO_MENU.get(INPUT);
                            break;
                        }

                    }

                    if( INPUT.equals("1") ) {
                        STATE = MenuState.CONFIG_FILE;
                    }
                    else if( INPUT.equals("2") ) {
                        STATE = MenuState.CONFIG_TEXT;
                    }
                    else {
                        System.out.println("Invalid Input.");
                        anyKeyPrompt();
                    }

                    break;

                case CONFIG_FILE:

                    Menu.displayJsonMenu();

                    System.out.println("[x] Back");
                    System.out.println("Absolute Path Here: ");

                    INPUT = SCANNER.nextLine();

                    if( INPUT.toUpperCase().equals("X") ) {

                        STATE = MenuState.CONFIG;
                        break;

                    } else {

                        File tempFile = new File(INPUT);

                        boolean loadSucessful = false;

                        if( tempFile.exists() && tempFile.isFile() ) {
                            try {

                                Reader reader = Files.newBufferedReader(Paths.get(tempFile.getAbsolutePath()));
                                INVENTORY.loadJson(reader);
                                STATE = MenuState.VENDING;
                                loadSucessful = true;

                            } catch(Exception ex) { 

                                System.out.println("Failed to Load Config JSON:" + ex);
                                writeLog("Failed to Load Config JSON:" + ex);
                                STATE = MenuState.CONFIG; // Failed to either, read-file, parse-json or invalid data format. Recover With new Config File Prompt.

                            }
                            if( loadSucessful ) {
                                saveJson();
                            }
                        } else {

                            System.out.println("Could not find file at described location.");
                            anyKeyPrompt();

                        }
                    }

                    break;

                case CONFIG_TEXT:

                    Menu.displayJsonMenu();

                    System.out.println("[x] Back");
                    System.out.println("JSON Text Here: ");

                    INPUT = SCANNER.nextLine();

                    if( INPUT.toUpperCase().equals("X") ) {

                        STATE = MenuState.CONFIG;
                        break;

                    } else {
                        boolean loadSucessful = false;

                        try {

                            INVENTORY.loadJson(INPUT);
                            STATE = MenuState.VENDING;
                            loadSucessful = true;

                        } catch(Exception ex) { 

                            System.out.println("Failed to Load Config JSON:" + ex);
                            writeLog("Failed to Load Config JSON:" + ex);
                            anyKeyPrompt();

                        }

                        if( loadSucessful ){
                            saveJson();
                        }

                    }
                    
                    break;

                case EXIT:
                    break;

                default:
                    System.err.println("INVALID/IMPOSSIBLE STATE");
                    STATE = MenuState.EXIT;
                    break;
            }
        }
    }

}
