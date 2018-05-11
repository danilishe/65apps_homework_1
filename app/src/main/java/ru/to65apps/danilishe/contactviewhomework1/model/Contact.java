package ru.to65apps.danilishe.contactviewhomework1.model;

public class Contact {
    String id, name, phone, email;

    public Contact(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
