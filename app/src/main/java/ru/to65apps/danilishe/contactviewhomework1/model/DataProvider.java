package ru.to65apps.danilishe.contactviewhomework1.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataProvider {
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
            ContactsContract.Contacts.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Photo.PHOTO_URI};

    private static DataProvider ourInstance;
    private List<Contact> mContacts;

    public static DataProvider getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DataProvider();
            ourInstance.updateContacts(context);
        }
        return ourInstance;
    }

    private DataProvider() {
    }

    public void updateContacts(Context context) {
        mContacts = new ArrayList<>();
        try (Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                PROJECTION, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC")) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                    String contact_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME));
                    Contact contact = new Contact(contact_id, contact_name);

                    if (mContacts.contains(contact)) {
                        contact = mContacts.get(mContacts.indexOf(contact));
                    } else {
                        mContacts.add(contact);
                    }

                    updateContactDetails(cursor, contact);
                } while (cursor.moveToNext());
            }
            Collections.sort(mContacts);
        }
        Log.d(getClass().getSimpleName(), "loaded " + mContacts.size() + " contacts");
    }

    private void updateContactDetails(Cursor cursor, Contact contact) {
        switch (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.Data.MIMETYPE))) {
            case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                contact.addPhone(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                break;
            case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                contact.addEmail(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
                break;
            case ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE:
                contact.setImageUri(Uri.parse(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_URI))));
                break;
        }
    }

    public List<Contact> getContacts() {
        return mContacts;
    }

    public Contact getContact(String contactId) {
        for (Contact contact : mContacts) {
            if (contact.getId().equals(contactId))
                return contact;
        }
        return null;
    }
}
