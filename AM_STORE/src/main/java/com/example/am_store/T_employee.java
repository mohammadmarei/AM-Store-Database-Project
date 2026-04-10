package com.example.am_store;

public class T_employee {
    private int id;
    private String name;
    private String email;
    private String password;
    private double salary;
    private String role;
    public static int loggedInEmployeeId;
    public T_employee(int id, String name, String email, String password, double salary, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.salary = salary;
        this.role = role;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getSalary() {
        return salary;
    }

    public int getId() { return id; }
    public String getRole() { return role; }

}
