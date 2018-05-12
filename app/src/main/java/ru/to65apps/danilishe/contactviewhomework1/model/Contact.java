package ru.to65apps.danilishe.contactviewhomework1.model;

import android.net.Uri;

public class Contact {
    String id, name, phone, email;
    Uri imageUri;

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

    public void setImageUri(Uri imageUri) {this.imageUri = imageUri;}

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

    public Uri getImageUri() {return imageUri;}
}
