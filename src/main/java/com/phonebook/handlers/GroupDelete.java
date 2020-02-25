package com.phonebook.handlers;

import com.phonebook.AbstractRequestHandler;
import com.phonebook.Constants;
import com.phonebook.Result;
import com.phonebook.model.Group;
import com.phonebook.model.Model;
import com.phonebook.payloads.Empty;

import java.util.Map;
import java.util.Optional;

public class GroupDelete extends AbstractRequestHandler<Empty> {
    private Model model;

    public GroupDelete(Model model) {
        super(Empty.class, model);
        this.model = model;
    }

    @Override
    protected Result processImpl(Empty value, Map<String, String> urlParams) {
        if (!urlParams.containsKey(":group_id")) {
            throw new IllegalArgumentException();
        }
        int groupId;
        try {
            groupId = Integer.parseInt(urlParams.get(":group_id"));
        } catch (IllegalArgumentException e) {
            return new Result(Constants.HTTP_NOT_FOUND_REQUEST);
        }

        Optional<Group> group = model.getGroup(groupId);
        if (!group.isPresent()) {
            return new Result(Constants.HTTP_NOT_FOUND_REQUEST);
        }
        model.deleteGroup(groupId);
        return new Result(Constants.HTTP_SUCCESS_REQUEST);
    }
}
