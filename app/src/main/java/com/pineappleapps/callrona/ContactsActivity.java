package com.pineappleapps.callrona;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pineappleapps.callrona.adaptor.ContactAdaptor;
import com.pineappleapps.callrona.database.AppDatabase;
import com.pineappleapps.callrona.database.AppExecutors;
import com.pineappleapps.callrona.model.Contact;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 24;

    private ContactAdaptor mAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        FloatingActionButton getContact = findViewById(R.id.add_contact);

        getContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
                // Code here executes on main thread after user presses button
            }
        });

        RecyclerView mRecyclerView = findViewById(R.id.contact_list_view);
        final TextView number_of_contacts = findViewById(R.id.number_of_contacts);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ContactAdaptor(this);
        mRecyclerView.setAdapter(mAdapter);
        mDb = AppDatabase.getInstance(getApplicationContext());

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Cursor cursor = mDb.query(
                                        "SELECT * FROM contacts", null);

                                if (cursor.getCount() > 0) {

                                    int number_available = cursor.getCount();

                                    number_of_contacts.setText(number_available + " contacts available");


                                } else {

                                    number_of_contacts.setText("0 contacts available");

                                }
                            }
                        });
                    }
                } catch (InterruptedException e) { }
            }
        };

        t.start();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(@NotNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                int position = viewHolder.getAdapterPosition();
                                List<Contact> tasks = mAdapter.getTasks();
                                mDb.contactsDao().deleteContact(tasks.get(position));
                                retrieveTasks();
                                Toast.makeText(ContactsActivity.this, "Contact removed!", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
            }


        }).attachToRecyclerView(mRecyclerView);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            if (requestCode == RESULT_PICK_CONTACT) {
                Cursor cursor = null;
                try {
                    String phoneNo = null;
                    String contactName = null;
                    Uri uri = data.getData();
                    assert uri != null;
                    cursor = getContentResolver().query(uri, null, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    phoneNo = cursor.getString(phoneIndex);

                    int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    contactName = cursor.getString((nameIndex));

                    final String finalContactName = contactName;
                    final String finalPhoneNo = phoneNo;

                    final String currentTime = String.valueOf(Calendar.getInstance().getTime());

                    final String number_of_calls = "0";

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                      public void run() {
                       runOnUiThread(new Runnable() {
                        @Override
                           public void run() {
                            Cursor cursor = mDb.query(
                          "SELECT phone_number FROM contacts where phone_number = " + "'" + finalPhoneNo + "'", null);
                          if (cursor.getCount() > 0) {
                           Toast.makeText(ContactsActivity.this, finalContactName + " already exists!.", Toast.LENGTH_LONG).show();
                           } else {
                          Contact contact = new Contact(finalContactName, finalPhoneNo, number_of_calls, currentTime);
                        mDb.contactsDao().insertContact(contact);
                        retrieveTasks();
                  Toast.makeText(ContactsActivity.this, finalContactName + " successfully added!", Toast.LENGTH_LONG).show();
                }
            }
          });
        }
            });

                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            Log.e("ContactsActivity", "Failed to pick contact");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    private void retrieveTasks() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final List<Contact> contacts = mDb.contactsDao().getContactsList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.setTasks(contacts);
                    }
                });
            }
        });

    }

}
