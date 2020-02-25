package com.phonebook.handlers;

import com.phonebook.AbstractRequestHandler;
import com.phonebook.Constants;
import com.phonebook.Result;
import com.phonebook.model.Model;
import com.phonebook.payloads.Contact;

import java.util.Map;

public class ContactCreate extends AbstractRequestHandler<Contact> {

    private Model model;

    public ContactCreate(Model model) {
        super(Contact.class, model);
        this.model = model;
    }

    @Override
    protected Result processImpl(Contact value, Map<String, String> urlParams) {
        int contactId = model.createContact(value.getFname(), value.getMname(), value.getLname(), value.getGender(), value.getDob(), value.getEmailList(), value.getCountryCode(), value.getExtension(), value.getContactNumber(), value.getGroupList());
        return new Result(Constants.HTTP_CREATED_REQUEST, contactId+"");
    }

}
