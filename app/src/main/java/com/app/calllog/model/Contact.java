package com.app.calllog.model;

/**
 * Created by Akash on 24-May-15.
 */
public class Contact extends ASModel {


    public int id;
    public String name;
    public String number;
    public String emailId;


    public Contact() {

    }

    public Contact(Contact contact) {
        this.id = contact.id;
        this.name = contact.name;
        this.emailId = contact.emailId;
        this.number = contact.number;
    }

    public Contact(int id, String name, String number, String emailId) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public String getNo() {
        return number;
    }

    public String getEmailId() {
        return emailId;
    }


    @Override
    public String getModelName() {
        return Contact.class.getSimpleName();
    }

    @Override
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNo(String number) {
        this.number = number;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", no='" + number + '\'' +
                ", emailId='" + emailId + '\'' +
                '}';
    }

}


