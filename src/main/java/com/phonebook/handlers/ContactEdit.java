package com.phonebook.handlers;

import com.phonebook.AbstractRequestHandler;
import com.phonebook.Constants;
import com.phonebook.Result;
import com.phonebook.model.ContactInfo;
import com.phonebook.model.Model;
import com.phonebook.payloads.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContactEdit extends AbstractRequestHandler<Contact> {

    private Model model;

    public ContactEdit(Model model) {
        super(Contact.class, model);
        this.model = model;
    }

    @Override
    protected Result processImpl(Contact value, Map<String, String> urlParams) {
        if (!urlParams.containsKey(":contact_id")) {
            throw new IllegalArgumentException();
        }
        int contactId;
        try {
            contactId = Integer.parseInt(urlParams.get(":contact_id"));
        } catch (IllegalArgumentException e) {
            return new Result(Constants.HTTP_NOT_FOUND_REQUEST);
        }

        Optional<com.phonebook.model.Contact> contact = model.getContact(contactId);
        if (!contact.isPresent()) {
            return new Result(Constants.HTTP_NOT_FOUND_REQUEST);
        }
        if(value.isValid()){
            contact.get().setFname(value.getFname());
            contact.get().setMname(value.getMname());
            contact.get().setLname(value.getLname());
            contact.get().setGender(value.getGender());
            contact.get().setDob(value.getDob());
            contact.get().setContactInfo(new ContactInfo(value.getCountryCode(), value.getExtension(), value.getContactNumber(), contactId));
            contact.get().setDob(value.getDob());
            List<String> emails = new ArrayList<String>();
            emails.addAll(value.getEmailList());
            contact.get().setEmails(emails);
            List<String> groups = new ArrayList<String>();
            groups.addAll(value.getGroupList());
            contact.get().setGroups(groups);
        }
        model.updateContact(contact.get());
        return new Result(Constants.HTTP_SUCCESS_REQUEST);
    }
}
