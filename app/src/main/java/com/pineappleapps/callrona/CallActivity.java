package com.pineappleapps.callrona;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pineappleapps.callrona.database.AppDatabase;
import com.pineappleapps.callrona.database.AppExecutors;

import java.util.Date;

public class CallActivity extends AppCompatActivity {


    Button roll_button;
    ImageButton statistics, contact_list;

    ProgressBar rolling_progress;
    TextView rolling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        final AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());

        roll_button = findViewById(R.id.roll_contacts);
        statistics = findViewById(R.id.view_statistics);
        contact_list = findViewById(R.id.view_contact);

        rolling_progress = findViewById(R.id.rolling_progress);
        rolling = findViewById(R.id.rolling_text);

        rolling_progress.setVisibility(View.INVISIBLE);
        rolling.setVisibility(View.INVISIBLE);


        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statisticsIntent = new Intent(CallActivity.this, StatisticsActivity.class);

                startActivity(statisticsIntent);
            }
        });

        contact_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(CallActivity.this, ContactsActivity.class);

                startActivity(contactIntent);
            }
        });

        roll_button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                rolling_progress.setVisibility(View.VISIBLE);
                rolling.setVisibility(View.VISIBLE);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    try {
                                        Thread.sleep(5000);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    Cursor cursor = mDb.query(
                                            "SELECT * FROM contacts ORDER BY RANDOM() LIMIT 1", null);
                                    if (cursor.getCount() > 0) {

                                        if (cursor.moveToFirst()) {
                                            while (!cursor.isAfterLast()) {

                                                String contact_name = cursor.getString(cursor.getColumnIndex("contact_name"));
                                                String contact_phone = cursor.getString(cursor.getColumnIndex("phone_number"));
                                                String number_of_calls = cursor.getString(cursor.getColumnIndex("number_of_calls"));
                                                String date_called = cursor.getString(cursor.getColumnIndex("date_called"));

                                                Log.e("Results", contact_name);
                                                Log.e("Results", contact_phone);
                                                Log.e("Results", number_of_calls);
                                                Log.e("Results", date_called);

                                                rolling_progress.setVisibility(View.INVISIBLE);
                                                rolling.setVisibility(View.INVISIBLE);

                                                SharedPreferences pref = getApplicationContext().getSharedPreferences("CallRona", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = pref.edit();

                                                editor.putString("contact_name", contact_name);
                                                editor.putString("contact_phone", contact_phone);


                                                editor.apply(); // commit changes

                                                Intent random = new Intent(CallActivity.this, SearchRandomContacts.class);

                                                startActivity(random);
                                                overridePendingTransition(R.anim.slide_down, R.anim.slide_up);

                                                cursor.moveToNext();

                                            }
                                        }
                                    } else{
                                        rolling_progress.setVisibility(View.INVISIBLE);
                                        rolling.setVisibility(View.INVISIBLE);
                                        Toast.makeText(CallActivity.this, "No contacts available! Add contacts to roll.", Toast.LENGTH_LONG).show();

                                    }


                                }
                            });

                        }
                    });
                }
        });



    }

    private static long sayBackPress;

    @Override
    public void onBackPressed() {

        if (sayBackPress + 2000 > System.currentTimeMillis()) {

            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            startActivity(a);
            super.onBackPressed();
        } else {
            Toast.makeText(CallActivity.this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
            sayBackPress = System.currentTimeMillis();

        }

    }
}




