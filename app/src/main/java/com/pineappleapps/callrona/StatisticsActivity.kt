package com.pineappleapps.callrona

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.pineappleapps.callrona.database.AppDatabase
import com.pineappleapps.callrona.database.AppExecutors
import kotlinx.android.synthetic.main.activity_statistics.*
import java.util.*


class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Statistik"
        setContentView(R.layout.activity_statistics)


        val mDb = AppDatabase.getInstance(applicationContext)

        val currentTime = Calendar.getInstance().time.toString()

        AppExecutors.getInstance().diskIO().execute {
            runOnUiThread {



                val cursor: Cursor = mDb.query(
                    "SELECT SUM (number_of_calls) FROM contacts", null
                )
                if (cursor.count > 0) {
                    val mArrayList = mutableListOf<String>()
                    if (cursor.moveToFirst()) {

                        while (!cursor.isAfterLast) {

                            mArrayList.add(cursor.getString(0))

                            cursor.moveToNext()
                        }
                        Log.e("CALLS-IN-TOTAL:", mArrayList.toString())
                        calls_in_total.setText(mArrayList.get(0))

                    }
                } else {
                    Toast.makeText(
                        this@StatisticsActivity,
                        "No contacts available! Add contacts to roll.",
                        Toast.LENGTH_LONG
                    ).show()
                }




                val cursor2: Cursor = mDb.query(
                    "SELECT SUM (number_of_calls) FROM contacts where date_called > '$currentTime' - 7 ", null
                )
                if (cursor2.count > 0) {
                    val mArrayList = mutableListOf<String>()

                    if (cursor2.moveToFirst()) {
                        while (!cursor2.isAfterLast) {

                            mArrayList.add(cursor2.getString(0))

                            cursor2.moveToNext()

                        }
                        Log.e("CALLS-THIS-WEEK:", mArrayList.toString())
                        calls_this_week.setText(mArrayList.get(0))
                    }
                } else {
                    Toast.makeText(
                        this@StatisticsActivity,
                        "No contacts available! Add contacts to roll.",
                        Toast.LENGTH_LONG
                    ).show()
                }




                val cursor3: Cursor = mDb.query(
                    "SELECT contact_name FROM contacts ORDER BY number_of_calls DESC LIMIT 4", null
                )
                if (cursor3.count > 0) {
                    val mArrayList = mutableListOf<String>()


                    if (cursor3.moveToFirst()) {

                        while (!cursor3.isAfterLast) {

                            mArrayList.add(cursor3.getString(cursor3.getColumnIndex("contact_name")))

                            cursor3.moveToNext()
                        }
                        Log.e("CHART-NUMBERS:", mArrayList.toString())

                        try {
                            top_name_4.text = mArrayList[0]
                        } catch (e: IndexOutOfBoundsException){
                            top_name_4.text = "N/A"
                        }
                        try {
                            top_name_3.text = mArrayList[1]
                        } catch (e: IndexOutOfBoundsException){
                            top_name_3.text = "N/A"
                        }
                        try {
                            top_name_2.text = mArrayList[2]
                        } catch (e: IndexOutOfBoundsException){
                            top_name_2.text = "N/A"
                        }

                        try {
                            top_name_1.text = mArrayList[3]
                        } catch (e: IndexOutOfBoundsException){
                            top_name_1.text = "N/A"
                        }


                    }
                } else {
                    Toast.makeText(
                        this@StatisticsActivity,
                        "No contacts available! Add contacts to roll.",
                        Toast.LENGTH_LONG
                    ).show()
                }


                val cursor4: Cursor = mDb.query(
                    "SELECT number_of_calls FROM contacts ORDER BY number_of_calls DESC LIMIT 4", null
                )
                if (cursor4.count > 0) {
                    val mArrayList = mutableListOf<String>()

                    if (cursor4.moveToFirst()) {

                        while (!cursor4.isAfterLast) {

                            mArrayList.add(cursor4.getString(cursor4.getColumnIndex("number_of_calls")))
                            cursor4.moveToNext()
                        }
                        Log.e("CHART-NUMBERS:", mArrayList.toString())


                        try {
                            top_no_4.text = mArrayList[0]
                        } catch (e: IndexOutOfBoundsException){
                            top_no_4.text = "N/A"
                        }
                        try {
                            top_no_3.text = mArrayList[1]
                        } catch (e: IndexOutOfBoundsException){
                            top_no_3.text = "N/A"
                        }
                        try {
                            top_no_2.text = mArrayList[2]
                        } catch (e: IndexOutOfBoundsException){
                            top_no_2.text = "N/A"
                        }

                        try {
                            top_no_1.text = mArrayList[3]
                        } catch (e: IndexOutOfBoundsException){
                            top_no_1.text = "N/A"
                        }



                    }
                } else {
                    Toast.makeText(
                        this@StatisticsActivity,
                        "No contacts available! Add contacts to roll.",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
        }


    }


}
