package ru.to65apps.danilishe.contactviewhomework1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.to65apps.danilishe.contactviewhomework1.model.Contact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

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

    ContactAdapter(ContactListActivity parent, List<Contact> items, boolean twoPane) {
        mContacts = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.bind(contact);
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
            contactIcon = view.findViewById(R.id.contact_icon);
            contactName = view.findViewById(R.id.contact_name);
            contactPhone = view.findViewById(R.id.contact_phone);
        }

        public void bind(Contact contact) {
            Uri uri = contact.getImageUri();
            if (uri != null)
                contactIcon.setImageURI(uri);
            else
                contactIcon.setImageResource(R.mipmap.ic_launcher);

            String name = contact.getName();
            if (name == null) {
                contactName.setTextColor(Color.LTGRAY);
                contactName.setText(R.string.no_name);
            } else {
                contactName.setTextColor(Color.GRAY);
                contactName.setText(name);
            }

            List<String> phones = contact.getPhones();
            if (!phones.isEmpty()) {
                contactPhone.setTextColor(Color.GRAY);
                contactPhone.setText(phones.get(0));
            } else {
                contactPhone.setTextColor(Color.LTGRAY);
                contactPhone.setText(R.string.no_phone);
            }

            itemView.setTag(contact);
            itemView.setOnClickListener(mOnClickListener);
        }
    }
}
