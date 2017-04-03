package com.psx.sqllitedatabsedemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.psx.sqllitedatabsedemo.Helper.DatabaseHandler;
import com.psx.sqllitedatabsedemo.Model.Contact;

public class AddNewContact extends AppCompatActivity {

    private ActionBar actionBar;
    private String title;
    private EditText editText_name;
    private EditText editText_phone;
    private EditText editText_email;
    private Context context;
    private String temp,str="";
    private String[] splitted;
    private ImageView imageView;
    private DatabaseHandler databaseHandler;
    private ColorGenerator colorGenerator;
    private TextDrawable.IBuilder builder;
    private int chosen_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        title = "Add New Contact";
        context = this;
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(s);
        databaseHandler = new DatabaseHandler(context);
        //final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        final Drawable arrow = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_back);
        arrow.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.md_black_1000), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(arrow);
        imageView = (ImageView) findViewById(R.id.imageView2);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
        editText_name.addTextChangedListener(textWatcher);
        colorGenerator = ColorGenerator.MATERIAL;
        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(3)
                .endConfig()
                .round();
        chosen_color = colorGenerator.getRandomColor();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            //after -length of charcters that are going to replace count characters beginning at start in charSequence
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            //before-length of text that has just been replaced by count characters beginning at start within charSequence

        }

        @Override
        public void afterTextChanged(Editable editable) {
            temp = editText_name.getText().toString();
            if (temp.length() > 0) {
/*                splitted = temp.split(" ");
                if (splitted.length > 1) {
                    char x = splitted[0].charAt(0);
                    char y = splitted[1].charAt(0);
                    str += Character.toString(x);
                    str += Character.toString(y);
                } else {
                    char x = splitted[0].charAt(0);
                    str += Character.toString(x);
                }*/
                if (temp.length() == 1){
                    str += temp;
                    TextDrawable textDrawable = builder.build(str, chosen_color);
                    imageView.setImageDrawable(textDrawable);
                }
                else if (temp.indexOf(" ")==temp.length()-1){
                    char x  = temp.charAt(0);
                    str = Character.toString(x);
                    TextDrawable textDrawable = builder.build(str, chosen_color);
                    imageView.setImageDrawable(textDrawable);
                }
                else {
                    if (temp.indexOf(" ")==temp.length()-2){
                        char x = temp.charAt(temp.length()-1);
                        str += Character.toString(x);
                        TextDrawable textDrawable = builder.build(str, chosen_color);
                        imageView.setImageDrawable(textDrawable);
                    }
                }
            }
            else{
                imageView.setImageResource(R.drawable.ic_user_image_with_black_background);
                str = "";
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home :
                this.finish();
                return true;
            case R.id.action_save_contact:
                //code to save contact to database
                saveContact();
                Toast.makeText(context,"Contact Saved Successfully",Toast.LENGTH_SHORT).show();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_contact,menu);
        return true;
    }

    public void saveContact (){
        String whole_name = editText_name.getText().toString();
        String [] splitted = whole_name.split(" ");
        String first = "", last = "";
        if (splitted.length > 1){
            first = splitted[0];
            last = whole_name.substring(whole_name.indexOf(32));
        }
        else {
            first = splitted[0];
            last = "";
        }
        Contact contact = new Contact(first,last,editText_email.getText().toString(),editText_phone.getText().toString());
        databaseHandler.addContact(contact);
    }
}
