package com.example.am_store;

import java.util.Date;

public class table_order {
    private String Status;
    private int id;
    private int e_id;
    private int totalprice;
    private String Customer_name;
    private String Customer_phone;
    private Date date;

    public table_order(int id,int e_id,String customer_name, int totalprice, String status,String customer_phone, Date date) {
        this.id = id;
        this.e_id=e_id;
        this.Status = status;
        this.totalprice = totalprice;
        this.Customer_name = customer_name;
        this.Customer_phone = customer_phone;
        this.date = date;
    }
    public String getStatus() {
        return Status;
    }

    public int getId() {
        return id;
    }
    public int getE_id() {
        return e_id;
    }

    public int getTotalprice() {
        return totalprice;
    }


    public String getCustomer_phone() {
        return Customer_phone;
    }
    public String getCustomer_name() {
        return Customer_name;
    }
    public String getstatus() {
        return Status;
    }


    public Date getDate() {
        return date;
    }

    public void setId(int orderId) {
    }
}
