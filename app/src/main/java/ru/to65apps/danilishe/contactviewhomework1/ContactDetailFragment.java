package ru.to65apps.danilishe.contactviewhomework1;

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
import ru.to65apps.danilishe.contactviewhomework1.model.DataProvider;

public class ContactDetailFragment extends Fragment {

    public static final String ITEM_ID = "item_id";
    private final DataProvider dp = DataProvider.getInstance(getContext());

    private Contact mItem;

    public ContactDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ITEM_ID)) {
            String contactId = getArguments().getString(ITEM_ID);

            mItem = dp.getContact(contactId);

            CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);

        if (mItem != null) {
            TextView phonesField = (TextView) rootView.findViewById(R.id.contact_detail_phone);
            TextView emailsField = (TextView) rootView.findViewById(R.id.contact_detail_email);

            for (String phone : mItem.getPhones()) {
                phonesField.append("\n" + phone);
            }

            for (String email : mItem.getEmails()) {
                emailsField.append("\n" + email);
            }

            if (mItem.getImageUri() != null) {
                ImageView contactIcon = getActivity().findViewById(R.id.contactIcon);
                contactIcon.setVisibility(View.VISIBLE);
                contactIcon.setImageURI(mItem.getImageUri());
            }
        }

        return rootView;
    }
}
