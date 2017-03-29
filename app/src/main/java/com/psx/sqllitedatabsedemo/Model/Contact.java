package com.psx.sqllitedatabsedemo.Model;

/**
 * Created by Pranav on 21-03-2017.
 */

public class Contact {
    // A simple POJO class for saving a contact

    String first_name;
    String last_name;
    String email_id;
    String phone_number;
    int id;

    public Contact ()
    {
        // required, empty constructor
    }

    public Contact(int id, String first_name, String last_name, String email_id, String phone_number){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_id = email_id;
        this.phone_number = phone_number;
        this.id = id;
    }

    public Contact(String first_name, String last_name, String email_id, String phone_number){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_id = email_id;
        this.phone_number = phone_number;
    }

    // getters
    public String getFirst_name ()
    {
        return this.first_name;
    }
    public String getLast_name ()
    {
        return  this.last_name;
    }
    public String getEmail_id ()
    {
        return this.email_id;
    }
    public int getId (){
        return this.id;
    }

    public String getPhone_number (){
        return this.phone_number;
    }

    //setters
    public void setEmail_id (String email_id){
        this.email_id = email_id;
    }

    public void setFirst_name (String first_name){
        this.first_name = first_name;
    }

    public void setLast_name (String last_name){
        this.last_name = last_name;
    }
    public void setPhone_number (String phone_number){
        this.phone_number = phone_number;
    }
    public void setId (int id){
        this.id = id;
    }

}
