package com.psx.sqllitedatabsedemo.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.psx.sqllitedatabsedemo.Helper.DatabaseHandler;
import com.psx.sqllitedatabsedemo.R;

public class ViewContact extends AppCompatActivity {

    private TextView textView_name;
    private TextView textView_number;
    private TextView textView_email;
    private ImageView imageView_pic;
    private Intent intent;
    private DatabaseHandler databaseHandler;
    private Context context;
    private ActionBar actionBar;
    private String title;
    private ColorGenerator colorGenerator;
    private TextDrawable.IBuilder builder;
    private int chosen_color;
    private char x,y;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        title = "View Contact";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(s);
        final Drawable arrow = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_back);
        arrow.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.md_black_1000), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(arrow);
        textView_name = (TextView) findViewById(R.id.text_name);
        textView_email = (TextView) findViewById(R.id.text_email);
        textView_number = (TextView) findViewById(R.id.text_phone);
        imageView_pic = (ImageView) findViewById(R.id.imageView21);
        intent = getIntent();
        context = this;
        databaseHandler = new DatabaseHandler(context);
        Contact contact = databaseHandler.getContact(intent.getIntExtra("id",0));
        if (contact.getFirst_name() == null){
            textView_email.setText("Unnamed");
            flag = 1;
        }
        else if (contact.getLast_name() == null){
            textView_name.setText((contact.getFirst_name().toString()));
            x = contact.getFirst_name().charAt(0);
        }
        else{
            if (contact.getFirst_name().length()>0){
                x = contact.getFirst_name().charAt(0);
            }
            if (contact.getLast_name().length()>0){
                y = contact.getLast_name().charAt(0);
            }
            Log.d("STRING",x+""+y);
            textView_name.setText((contact.getFirst_name().toString()+" "+contact.getLast_name().toString()));
        }
        if (contact.getEmail_id() == null){
            textView_email.setText("No Email");
        }
        else {
            textView_email.setText(contact.getEmail_id().toString());
        }
        if (contact.getPhone_number() == null){
            textView_number.setText("No  Number");
        }
        else {
            textView_number.setText(contact.getPhone_number().toString());
        }
        colorGenerator = ColorGenerator.MATERIAL;
        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(3)
                .endConfig()
                .round();
        chosen_color = colorGenerator.getRandomColor();
        String str = "",str2="";
        str += Character.toString(x);
        str2 += Character.toString(y);
        if (x!='\u0000' && y!='\u0000' && flag!=1){
            str.concat(str2);
            Log.d("STRING",str+ str2+" Y is "+y+" last name is"+contact.getLast_name());
            TextDrawable textDrawable = builder.build(str+str2, chosen_color);
            imageView_pic.setImageDrawable(textDrawable);
        }
        else if (y == '\u0000'){
            TextDrawable textDrawable = builder.build(str, chosen_color);
            imageView_pic.setImageDrawable(textDrawable);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
