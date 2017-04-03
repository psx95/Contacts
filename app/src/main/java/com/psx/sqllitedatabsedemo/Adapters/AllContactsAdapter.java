package com.psx.sqllitedatabsedemo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.psx.sqllitedatabsedemo.Model.Contact;
import com.psx.sqllitedatabsedemo.Model.ViewContact;
import com.psx.sqllitedatabsedemo.R;

import java.util.List;
import java.util.Random;

/**
 * Created by Pranav on 26-03-2017.
 */

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.CardViewHolder> {

    private Context context;
    private List<Contact> contactList;

    public AllContactsAdapter (Context context, List<Contact> contactList){
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact,parent,false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }


    @Override
    public void onBindViewHolder(CardViewHolder holder, final int position) {
        String str="";
        holder.contact_name.setText(contactList.get(position).getFirst_name()+" "+contactList.get(position).getLast_name());
        holder.contact_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ViewContact.class);
                intent.putExtra("id",contactList.get(position).getId());
                context.startActivity(intent);
            }
        });
        String contactname = holder.contact_name.getText().toString();
        String[] splitted = contactname.split(" ");
        Log.d("CHECK",contactname);
        if (splitted.length > 1){
            char x = splitted[0].charAt(0);
            str+= Character.toString(x);
            if (splitted[1].length()>0){
                char y = splitted[1].charAt(0);
                str+= Character.toString(y);
            }
        }
        else {
            char x = splitted[0].charAt(0);
            str += Character.toString(x);
        }
        Log.d("TEST",str+ " "+ contactname + splitted[0] + splitted[0].charAt(0));
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(10)
                .endConfig()
                .round();
        TextDrawable textDrawable = builder.build(str,colorGenerator.getRandomColor());
        holder.image_holder.setImageDrawable(textDrawable);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView contact_name;
        private ImageView image_holder;
        private String contactname;

        public CardViewHolder(View itemView) {
            super(itemView);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            image_holder = (ImageView) itemView.findViewById(R.id.imageView_contact);
        }
    }
}
