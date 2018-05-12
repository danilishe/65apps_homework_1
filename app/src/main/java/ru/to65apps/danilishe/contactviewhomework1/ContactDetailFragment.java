package ru.to65apps.danilishe.contactviewhomework1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.to65apps.danilishe.contactviewhomework1.model.Contact;

public class ContactDetailFragment extends Fragment {

    public static final String ITEM_ID = "item_id";

    private Contact mItem;

    public ContactDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ITEM_ID)) {
            mItem = ContactListActivity.CONTACTS.get(getArguments().getString(ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contact_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            String text = "Имя: " + (mItem.getName() != null ? mItem.getName() : "Нет имени") +
                    "\nТелефон: " + (mItem.getPhone() != null ? mItem.getPhone() : "нет телефона") +
                    "\nПочта: " + (mItem.getEmail() != null ? mItem.getEmail() : "нет почты");

            ((TextView) rootView.findViewById(R.id.contact_detail)).setText(text);

            if (mItem.getImageUri() != null) {
                ImageView contactIcon = (ImageView) getActivity().findViewById(R.id.contactIcon);
                contactIcon.setVisibility(View.VISIBLE);
                contactIcon.setImageURI(mItem.getImageUri());
            }
        }

        return rootView;
    }
}
