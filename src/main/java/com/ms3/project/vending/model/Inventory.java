package com.ms3.project.vending.model;

import com.ms3.project.vending.utils.Constants;

import com.google.gson.Gson;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inventory {

    private Config config;
    private List<Item> items;

    public Inventory() {
        this.config = null;
        this.items = new ArrayList<>();
    }

    public boolean isConfigInitialized() {
        return this.config != null;
    }

    public void setConfig(Config config) {
        if( this.config == null  ) {
            this.config = config;
        } else if ( config.maxCapacity() < this.items.size() ) {
            throw new IllegalStateException("Not enough space to store all items");
        } else {
            this.config = config;
        }
    }

    public void setItems(List<Item> newItems) {
        if( this.config == null  ) {
            this.items = newItems;
        } else if ( this.config.maxCapacity() < newItems.size() ) {
            throw new IllegalStateException("Not enough space to store all items");
        } else {
            this.items = newItems;
        }
    }

    public Config getConfig() {
        return this.config;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void loadJson(String json) {
        Gson g = new Gson();
        Inventory LoadInventory = g.fromJson(json, Inventory.class);
        if( LoadInventory == null || LoadInventory.config == null || LoadInventory.items == null ) { 
            throw new IllegalStateException("Failed To Map JSON to Class"); 
        }
        this.setConfig(LoadInventory.config);
        this.setItems(LoadInventory.items);
    }

    public void loadJson(Reader jsonFile) {
        Gson g = new Gson();
        Inventory LoadInventory = g.fromJson(jsonFile, Inventory.class);
        if( LoadInventory == null || LoadInventory.config == null || LoadInventory.items == null  ) { 
            throw new IllegalStateException("Failed To Map JSON to Class"); 
        }
        this.setConfig(LoadInventory.config);
        this.setItems(LoadInventory.items);
    }

    public String toJsonString() {
        Inventory SaveInventory = new Inventory();
        SaveInventory.setConfig(this.config);
        SaveInventory.setItems(this.items);
        Gson gson = new Gson();
        return gson.toJson(SaveInventory);
    }


    public String indexToKeyCode(int index) {
        if(this.config == null  ) {
            throw new IllegalStateException("Config or Items are not initialized");
        }
        if( index >= config.maxCapacity() || index < 0 ) {
            throw new IndexOutOfBoundsException("No keycode can exist for this index");
        }
        return String.valueOf(Constants.UPPER_ALPHABET.charAt(index / config.getColumns())) + String.valueOf((index % config.getColumns()) + 1);   
    }

    private int getIndex(String keycode) {

        if(this.config == null  ) {
            throw new IllegalStateException("Config or Items are not initialized");
        }

        if( keycode == null || keycode.length() < 2 ) {
            throw new IllegalArgumentException("One alphabetic and one positive numeric value in keycode to process request.");
        }

        int row = Constants.UPPER_ALPHABET.indexOf(keycode.charAt(0));

        if( row == -1 ) {
            throw new IllegalArgumentException("First value character in keycode needs to be alphabetic.");
        }

        // Can Throw Exception Illegal Format Execeptions;
        int column = Integer.parseInt(keycode.substring(1));

        if( column <= 0 ) {
            throw new IllegalArgumentException("Numeric value in keycode needs to be positive numeric value.");
        }

        if( row > this.config.getRows() ) {
            throw new IndexOutOfBoundsException("Alphabetic Row is out of bounds given: " +
            keycode.charAt(0) +
            ":" +
             String.valueOf(row) +
            " needs to be less than " +
             String.valueOf(this.config.getRows()) 
            );
        }

        if( column > this.config.getColumns() ) {
            throw new IndexOutOfBoundsException("Numeric Column is out of bounds given: " +
             String.valueOf(column) +
            " needs to be less than " +
             String.valueOf(this.config.getRows()) 
            );
        }

        // Machines are 1-indexed columns and we need to map to 0 indexed. 
        return row * this.config.getColumns() + (column - 1);
    }


    public Optional<Item> findByCode(String keycode) {

        int index = this.getIndex(keycode);
        
        if( index >= this.items.size() ) {
            return Optional.empty();
        }

        return Optional.of(this.items.get(index));
        
    }

    public void insertItem(Item newItem) {
        if( this.config == null ) {
            throw new IllegalStateException("Config or Items are not initialized");
        }

        if(  this.items.size() >= this.config.maxCapacity() ) {
            throw new IllegalStateException("Not enough space to store additional items");
        }

        this.items.add(newItem);
    }

    public void updateItem(String keycode, Item updateItem) {
        if( this.config == null  ) {
            throw new IllegalStateException("Config or Items are not initialized");
        }
        this.items.set(this.getIndex(keycode), updateItem);
    }


    public void decrementItem(String keycode) {

        Optional<Item> item = this.findByCode(keycode);
        if( item.isEmpty() ) { 
            throw new IndexOutOfBoundsException("Product does not exist.");
        }
        Item foundItem = item.get();
        if( foundItem.getAmount() < 0 ) {
            throw new IllegalStateException("Item is out of stock.");
        }
        foundItem.setAmount( foundItem.getAmount() - 1 );

        this.items.set(this.getIndex(keycode), foundItem);
    }


}
