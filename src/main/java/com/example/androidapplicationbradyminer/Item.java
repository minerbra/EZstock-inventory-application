package com.example.androidapplicationbradyminer;

import java.io.Serializable;

public class Item implements Serializable {
    private long id;
    private String name;
    private int quantity;

    // Default constructor
    public Item() {}

    public Item(long id, String name) {
        this.id = id;
        this.name = name;
        this.quantity = 0;
    }

    public Item(long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        this.quantity = Math.max(0, this.quantity - 1);
    }
}


