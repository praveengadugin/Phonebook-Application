package com.phonebook.handlers;

import com.phonebook.AbstractRequestHandler;
import com.phonebook.Result;
import com.phonebook.model.Model;
import com.phonebook.payloads.Empty;

import java.util.Map;

public class GroupAllFetch  extends AbstractRequestHandler<Empty> {

    public GroupAllFetch(Model model) {
        super(Empty.class, model);
    }

    @Override
    protected Result processImpl(Empty value, Map<String,String> urlParams) {
        String json = dataToJson(model.getAllGroups());
        return Result.ok(json);
    }

}