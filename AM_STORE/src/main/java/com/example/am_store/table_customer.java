package com.example.am_store;

public class table_customer {
    private int id;
    private String name;
    private String phone;
    public table_customer(int id,String name,  String phone) {
        this.id=id;
        this.name = name;
        this.phone =phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getPhone() { return phone; }

}
