package com.pineappleapps.callrona.database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pineappleapps.callrona.model.Contact;

import java.util.List;

@Dao
public interface ContactsDao {

        @Query("SELECT * FROM contacts")
        List<Contact> getContactsList();
        @Insert
        void insertContact(Contact contact);
        @Update
        void updateContact(Contact contact);
        @Delete
        void deleteContact(Contact contact);

}