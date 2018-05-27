package ru.to65apps.danilishe.contactviewhomework1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;

import ru.to65apps.danilishe.contactviewhomework1.model.DataProvider;

public class ContactListActivity extends AppCompatActivity {
    public static String TAG = ContactListActivity.class.getSimpleName();


    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "start");
        setContentView(R.layout.activity_contact_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.contact_detail_container) != null) {
            mTwoPane = true;
        }

        Log.d(TAG, "start asking permissions");
        askPermissions();

    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            RecyclerView recyclerView = findViewById(R.id.contact_list);
            assert recyclerView != null;
            ContactAdapter adapter = new ContactAdapter(this, DataProvider.getInstance(this).getContacts(), mTwoPane);
            Log.d(TAG, "adapter created");
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }


}
