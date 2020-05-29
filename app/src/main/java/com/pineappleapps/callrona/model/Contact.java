package com.pineappleapps.callrona.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "contacts")
public class Contact {

        @PrimaryKey(autoGenerate = true)
        private int id;
        @ColumnInfo(name = "contact_name")
        private String contact_name;
        @ColumnInfo(name = "phone_number")
        private String contact_phone;
        @ColumnInfo(name = "number_of_calls")
        private String number_of_calls;
        @ColumnInfo(name = "date_called")
        private String date_called;

        public Contact(int id, String contact_name, String contact_phone, String number_of_calls, String date_called){

            this.id = id;
            this.contact_name = contact_name;
            this.contact_phone = contact_phone;
            this.number_of_calls = number_of_calls;
            this.date_called = date_called;
        }

        @Ignore
        public Contact(String contact_name, String contact_phone, String number_of_calls, String date_called) {

            this.contact_name = contact_name;
            this.contact_phone = contact_phone;
            this.number_of_calls = number_of_calls;
            this.date_called = date_called;
        }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name= contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getNumber_of_calls() {
            return number_of_calls;
    }

    public void setNumber_of_calls(String number_of_calls) {
        this.number_of_calls = number_of_calls;
    }

    public String getDate_called() {
        return date_called;
    }

    public void setDate_called(String date_called) {
        this.date_called = date_called;
    }
}