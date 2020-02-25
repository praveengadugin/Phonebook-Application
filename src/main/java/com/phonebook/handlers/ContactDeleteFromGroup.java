package com.phonebook.handlers;

import com.phonebook.AbstractRequestHandler;
import com.phonebook.Constants;
import com.phonebook.Result;
import com.phonebook.model.Contact;
import com.phonebook.model.Group;
import com.phonebook.model.Model;
import com.phonebook.payloads.Empty;

import java.util.Map;
import java.util.Optional;

public class ContactDeleteFromGroup extends AbstractRequestHandler<Empty> {
    private Model model;

    public ContactDeleteFromGroup(Model model) {
        super(Empty.class, model);
        this.model = model;
    }

    @Override
    protected Result processImpl(Empty value, Map<String, String> urlParams) {
        if (!urlParams.containsKey(":contact_id") && !urlParams.containsKey(":group_id") ) {
            throw new IllegalArgumentException();
        }
        int contactId, groupId;
        try {
            contactId = Integer.parseInt(urlParams.get(":contact_id"));
            groupId = Integer.parseInt(urlParams.get(":group_id"));
        } catch (IllegalArgumentException e) {
            return new Result(Constants.HTTP_NOT_FOUND_REQUEST);
        }

        Optional<Contact> contact = model.getContact(contactId);
        Optional<Group> group = model.getGroup(groupId);
        if (!contact.isPresent() || !group.isPresent()) {
            return new Result(Constants.HTTP_NOT_FOUND_REQUEST);
        }
        model.deleteContactFromGroup(contactId, groupId);
        return new Result(Constants.HTTP_SUCCESS_REQUEST);
    }
}