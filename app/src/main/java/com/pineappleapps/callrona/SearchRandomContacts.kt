package com.pineappleapps.callrona

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pineappleapps.callrona.database.AppDatabase
import com.pineappleapps.callrona.database.AppExecutors
import kotlinx.android.synthetic.main.activity_view_roll.*
import java.util.*


class SearchRandomContacts : AppCompatActivity() {

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_roll);


        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        window.setLayout((width * 0.85).toInt(), (height * 0.7).toInt())
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val params = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = 0
        val lp = window.attributes
        lp.dimAmount = 0.4f
        window.attributes = lp
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        val pref = applicationContext.getSharedPreferences(
            "CallRona",
            Context.MODE_PRIVATE
        )
        val mDb = AppDatabase.getInstance(applicationContext)

        val contact_name = pref.getString("contact_name", null) // getting String
        val contact_phone = pref.getString("contact_phone", null) // getting String

        random_pick.setText(contact_name + " has been picked.")

        call_chosen.setOnClickListener {
            val currentTimehere = Calendar.getInstance().time.toString()

            AppExecutors.getInstance().diskIO().execute {
                runOnUiThread {

                    val cursor: Cursor = mDb.query(
                        "UPDATE contacts SET number_of_calls = number_of_calls + 1, date_called = '$currentTimehere' WHERE phone_number = '$contact_phone'", null
                    )
                    if (cursor.count > 0) {
                        if (cursor.moveToFirst()) {
                            while (!cursor.isAfterLast) {
                                val contact_name =
                                    cursor.getString(cursor.getColumnIndex("contact_name"))
                                val contact_phone =
                                    cursor.getString(cursor.getColumnIndex("phone_number"))
                                val number_of_calls =
                                    cursor.getString(cursor.getColumnIndex("number_of_calls"))
                                val date_called =
                                    cursor.getString(cursor.getColumnIndex("date_called"))
                                Log.e("Results", contact_name)
                                Log.e("Results", contact_phone)
                                Log.e("Results", number_of_calls)
                                Log.e("Results", date_called)

                                overridePendingTransition(
                                    R.anim.slide_down,
                                    R.anim.slide_up
                                )
                                cursor.moveToNext()
                            }
                        }
                    }
                }
            }

            val call = Intent(Intent.ACTION_DIAL)
            call.data = Uri.parse("tel:$contact_phone")
            startActivity(call)
        }

        roll_again.setOnClickListener {
            val pref = applicationContext.getSharedPreferences(
                "CallRona",
                Context.MODE_PRIVATE
            )
            val editor = pref.edit()


            onBackPressed()
            super.onBackPressed()
            editor.clear();
            editor.apply(); // commit changes
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.anim.slide_up)
    }

     override fun onTouchEvent(event: MotionEvent): Boolean {
         if (event.action == MotionEvent.ACTION_OUTSIDE) {
             overridePendingTransition(0, R.anim.slide_up)
         }
         return true

     }
}