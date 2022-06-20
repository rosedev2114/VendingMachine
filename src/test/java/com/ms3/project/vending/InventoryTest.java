package com.ms3.project.vending;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.*;

import com.ms3.project.vending.model.Inventory;
import com.ms3.project.vending.model.Item;

import java.util.Optional;

public class InventoryTest {
    
    @Test
    public void testInitCheck() {
        Inventory inv = new Inventory();
        assertFalse( inv.isConfigInitialized() );
    }

    @Test
    public void testInit() {
        Inventory inv = new Inventory();
        String json = """
        {
            \"config\": {
                \"rows\": 4,
                \"columns\": \"8\"
            },
            \"items\": [{\"name\": \"Snickers\",\"amount\": 10,\"price\": \"$1.35\"}]
        }
        """;

        inv.fromJson(json);
        assertEquals(inv.getConfig().getRows(), 4);
        assertEquals(inv.getConfig().getColumns(), 8);

        Item item = inv.getItems().get(0);

        assertEquals(item.getName(),"Snickers");
        assertEquals(item.getAmount(),10);
        assertEquals(item.getPrice(),"$1.35");
    }


    @Test
    public void testIndexToKeycode() {
        Inventory inv = new Inventory();
        String json = """
        {
            \"config\": {
                \"rows\": 4,
                \"columns\": \"8\"
            },
            \"items\": [{\"name\": \"Snickers\",\"amount\": 10,\"price\": \"$1.35\"}]
        }
        """;
        inv.fromJson(json);
        assertEquals(inv.indexToKeyCode(0), "A1");
    }

    @Test
    public void testKeycodeToIndexFound() {
        Inventory inv = new Inventory();
        String json = """
        {
            \"config\": {
                \"rows\": 4,
                \"columns\": \"8\"
            },
            \"items\": [{\"name\": \"Snickers\",\"amount\": 10,\"price\": \"$1.35\"}]
        }
        """;
        inv.fromJson(json);
        Optional<Item> item = inv.findByCode("A1");
        assertTrue(item.isPresent());
    }


    @Test
    public void testKeycodeToIndexEmpty() {
        Inventory inv = new Inventory();
        String json = """
        {
            \"config\": {
                \"rows\": 4,
                \"columns\": \"8\"
            },
            \"items\": [{\"name\": \"Snickers\",\"amount\": 10,\"price\": \"$1.35\"}]
        }
        """;
        inv.fromJson(json);
        Optional<Item> item = inv.findByCode("A3");
        assertTrue(item.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testTooManyItems() {
        Inventory inv = new Inventory();
        String json = """
        {
            \"config\": {
                \"rows\": 1,
                \"columns\": \"1\"
            },
            \"items\": [{\"name\": \"Snickers\",\"amount\": 10,\"price\": \"$1.35\"}, {\"name\": \"Snickers\",\"amount\": 10,\"price\": \"$1.35\"}]
        }
        """;
        inv.fromJson(json);
    }

    @Test
    public void testDecrementItem() {
        Inventory inv = new Inventory();
        String json = """
        {
            \"config\": {
                \"rows\": 1,
                \"columns\": \"1\"
            },
            \"items\": [{\"name\": \"Snickers\",\"amount\": 10,\"price\": \"$1.35\"}]
        }
        """;
        inv.fromJson(json);
        inv.decrementItem("A1");
        Optional<Item> item = inv.findByCode("A1");
        assertEquals(item.get().getAmount(), 9);
    }

    @Test
    public void testUpdateItem() {
        Inventory inv = new Inventory();
        String json = """
        {
            \"config\": {
                \"rows\": 1,
                \"columns\": \"1\"
            },
            \"items\": [{\"name\": \"Snickers\",\"amount\": 10,\"price\": \"$1.35\"}]
        }
        """;
        inv.fromJson(json);
        Item overwriteItem = new Item("Snickers","$1.50",10); 
        inv.updateItem("A1",overwriteItem);
        Optional<Item> item = inv.findByCode("A1");
        assertEquals(item.get().getPrice(), "$1.50");
    }

    @Test
    public void testInsertItem() {
        Inventory inv = new Inventory();
        String json = """
        {
            \"config\": {
                \"rows\": 1,
                \"columns\": \"1\"
            },
            \"items\": []
        }
        """;
        inv.fromJson(json);
        Item insertItem = new Item("Snickers","$1.50",10); 
        inv.insertItem(insertItem);
        Optional<Item> item = inv.findByCode("A1");
        assertEquals(item.get().getPrice(), "$1.50");
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertItemTooMany() {
        Inventory inv = new Inventory();
        String json = """
        {
            \"config\": {
                \"rows\": 1,
                \"columns\": \"1\"
            },
            \"items\": []
        }
        """;
        inv.fromJson(json);
        Item insertItem = new Item("Snickers","$1.50",10); 
        inv.insertItem(insertItem);
        inv.insertItem(insertItem);
    }
}
