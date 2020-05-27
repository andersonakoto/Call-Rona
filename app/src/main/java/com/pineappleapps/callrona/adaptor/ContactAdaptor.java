package com.pineappleapps.callrona.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pineappleapps.callrona.R;
import com.pineappleapps.callrona.database.AppDatabase;
import com.pineappleapps.callrona.model.Contact;

import java.util.List;

public class ContactAdaptor extends RecyclerView.Adapter<ContactAdaptor.MyViewHolder> {
    private Context context;
    private List<Contact> mContactList;

    public ContactAdaptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_contact_view, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdaptor.MyViewHolder myViewHolder, int i) {
        myViewHolder.contact_name.setText(mContactList.get(i).getContact_name());
        myViewHolder.contact_phone.setText(mContactList.get(i).getContact_phone());

    }

    @Override
    public int getItemCount() {
        if (mContactList == null) {
            return 0;
        }
        return mContactList.size();

    }

    public void setTasks(List<Contact> personList) {
        mContactList = personList;
        notifyDataSetChanged();
    }

    public List<Contact> getTasks() {

        return mContactList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView contact_name, contact_phone;
        AppDatabase mDb;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            mDb = AppDatabase.getInstance(context);
            contact_name = itemView.findViewById(R.id.contacts_item);
            contact_phone= itemView.findViewById(R.id.contacts_item_phone);


        }
        }
    }
