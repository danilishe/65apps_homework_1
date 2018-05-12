package ru.to65apps.danilishe.contactviewhomework1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.to65apps.danilishe.contactviewhomework1.model.Contact;

import static android.provider.ContactsContract.CommonDataKinds.Contactables;
import static android.provider.ContactsContract.CommonDataKinds.Email;
import static android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactListActivity extends AppCompatActivity {

    public static final String[] PROJECTION = new String[]{
            Phone._ID,
            Phone.RAW_CONTACT_ID,
            ContactsContract.Contacts.Data.MIMETYPE,
            Contactables.DISPLAY_NAME,
            Phone.NUMBER,
            ContactsContract.CommonDataKinds.Photo.PHOTO_URI};
    private boolean mTwoPane;
    public final static Map<String, Contact> CONTACTS = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.contact_detail_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.contact_list);
        assert recyclerView != null;

        askPermissions();


        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void askPermissions() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, loadContacts(), mTwoPane));
    }

    public List<Contact> loadContacts() {
        if (CONTACTS.isEmpty()) {
            final Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    PROJECTION, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    String contact_id = cursor.getString(cursor.getColumnIndex(Phone.RAW_CONTACT_ID));
                    Contact contact = CONTACTS.get(contact_id);
                    if (contact == null) {
                        String name = cursor.getString(cursor.getColumnIndex(Contactables.DISPLAY_NAME));
                        contact = new Contact(name + contact_id, name);
                        CONTACTS.put(name + contact_id, contact);
                    }
                    switch (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.Data.MIMETYPE))) {
                        case Phone.CONTENT_ITEM_TYPE:
                            contact.setPhone(cursor.getString(cursor.getColumnIndex(Phone.NUMBER)));
                            break;
                        case Email.CONTENT_ITEM_TYPE:
                            contact.setEmail(cursor.getString(cursor.getColumnIndex(Email.ADDRESS)));
                            break;
                        case ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE:
                            contact.setImageUri(
                                    Uri.parse(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_URI))));
//                            Cursor query = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,
//                                    ContactsContract.Contacts.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "' AND " + Phone.RAW_CONTACT_ID + "=" + contact_id,
//                                    null, null);
//                            if (query == null) break;
//                            query.moveToFirst();
//                            int column = 0;
//                            for (String col : query.getColumnNames()) {
//                                System.out.println(col + "=" + query.getString(column++));
//                            }
//                            query.close();
//                            System.out.println("=========");

//                            Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
//                                    .parseLong(contact_id));
//                            Uri uri = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//                            contact.setImageUri(uri);
                            break;
                    }

//                    // debug
//                    System.out.println(Arrays.asList(cursor.getColumnNames()));
//                    for (String name : cursor.getColumnNames()) {
//                        System.out.println(name + "=" + cursor.getString(cursor.getColumnIndex(name)));
//                    }
//                    System.out.println("========");

                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return new ArrayList<>(CONTACTS.values());
    }

    private String getFirstPhone(String id) {
        Cursor phoneCursor = getContentResolver().query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, Phone.DISPLAY_NAME + "='" + id + "'", null, null);
        String firstPhone = "no phone number";
        if (phoneCursor != null && phoneCursor.getCount() > 0) {
            phoneCursor.moveToFirst();
            firstPhone = phoneCursor.getString(0);
            phoneCursor.close();
        }
        return firstPhone;
    }

    private String getFirstEmail(String id) {
        Cursor emailCursor = getContentResolver().query(Email.CONTENT_URI, new String[]{Email.ADDRESS}, Email.DISPLAY_NAME + "='" + id + "'", null, null);
        String firstEmail = "no email address";
        if (emailCursor != null && emailCursor.getCount() > 0) {
            emailCursor.moveToFirst();
            firstEmail = emailCursor.getString(0);
            emailCursor.close();
        }
        return firstEmail;
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ContactListActivity mParentActivity;
        private final List<Contact> mContacts;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact item = (Contact) view.getTag();
                if (mTwoPane) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactDetailFragment.ITEM_ID, item.getId());

                    ContactDetailFragment fragment = new ContactDetailFragment();
                    fragment.setArguments(bundle);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contact_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ContactDetailActivity.class);
                    intent.putExtra(ContactDetailFragment.ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ContactListActivity parent,
                                      List<Contact> items,
                                      boolean twoPane) {
            mContacts = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            Contact contact = mContacts.get(position);

            Uri uri = contact.getImageUri();
            if (uri != null)
                holder.contactIcon.setImageURI(uri);
            else
                holder.contactIcon.setImageResource(R.mipmap.ic_launcher);

            String name = contact.getName();
            if (name == null) {
                holder.contactName.setTextColor(Color.GRAY);
                holder.contactName.setText("Нет имени");
            } else {
                holder.contactName.setText(name);
            }

            String phone = contact.getPhone();
            if (phone == null) {
                holder.contactPhone.setText("нет телефона");
                holder.contactPhone.setTextColor(Color.GRAY);
            } else {
                holder.contactPhone.setText(phone);
            }

            holder.itemView.setTag(contact);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView contactIcon;
            final TextView contactName;
            final TextView contactPhone;

            ViewHolder(View view) {
                super(view);
                contactIcon = (ImageView) view.findViewById(R.id.contact_icon);
                contactName = (TextView) view.findViewById(R.id.contact_name);
                contactPhone = (TextView) view.findViewById(R.id.contact_phone);
            }
        }
    }
}
