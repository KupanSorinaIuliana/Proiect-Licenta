package com.example.licenta.model;

public class User {
    private int id, age;
    private String username, email, password, nume, prenume;
    public int getId() {

        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setAge(int age){this.age=age;}
    public void setEmail(String email) {
        this.email = email;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPrenume() {
        return prenume;
    }
    public int getAge(){return age;}
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getNume() {
        return nume;
    }
}
