package com.phonebook.handlers;

import com.phonebook.AbstractRequestHandler;
import com.phonebook.Constants;
import com.phonebook.Result;
import com.phonebook.model.Model;
import com.phonebook.payloads.Group;

import java.util.Map;

public class GroupCreate extends AbstractRequestHandler<Group> {

    private Model model;

    public GroupCreate(Model model) {
        super(Group.class, model);
        this.model = model;
    }

    @Override
    protected Result processImpl(Group value, Map<String, String> urlParams) {
        int groupId = model.createGroup(value.getGroupName());
        return new Result(Constants.HTTP_CREATED_REQUEST, groupId+"");
    }
}
