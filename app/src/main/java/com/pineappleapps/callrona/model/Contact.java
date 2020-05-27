package com.pineappleapps.callrona.model;

import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {

        @PrimaryKey(autoGenerate = true)
        private int id;
        @ColumnInfo(name = "contact_name")
        private String contact_name;
        @ColumnInfo(name = "phone_number")
        private String contact_phone;

        public Contact(int id, String contact_name, String contact_phone){

            this.id = id;
            this.contact_name = contact_name;
            this.contact_phone = contact_phone;
        }

        @Ignore
        public Contact(String contact_name, String contact_phone) {

            this.contact_name = contact_name;
            this.contact_phone = contact_phone;
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


}