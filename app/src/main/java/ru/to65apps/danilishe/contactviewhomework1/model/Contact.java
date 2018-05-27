package ru.to65apps.danilishe.contactviewhomework1.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Contact implements Comparable<Contact> {
    private String id, name;
    private List<String> mPhones = new ArrayList<>();
    private List<String> mEmails = new ArrayList<>();
    private Uri imageUri;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id) &&
                Objects.equals(name, contact.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Contact(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addPhone(String phones) {
        mPhones.add(phones);
    }

    public void addEmail(String email) {
        mEmails.add(email);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getPhones() {
        return mPhones;
    }

    public List<String> getEmails() {
        return mEmails;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    @Override
    public int compareTo(@NonNull Contact o) {
        return this.getName().compareTo(o.getName()) +
                37 * this.getId().compareTo(o.getId());
    }
}
