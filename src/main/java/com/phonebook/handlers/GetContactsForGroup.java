package com.phonebook.handlers;

import com.phonebook.AbstractRequestHandler;
import com.phonebook.Result;
import com.phonebook.model.Model;
import com.phonebook.payloads.Empty;

import java.util.Map;

public class GetContactsForGroup extends AbstractRequestHandler<Empty> {

    public GetContactsForGroup(Model model) {
        super(Empty.class, model);
    }

    @Override
    protected Result processImpl(Empty value, Map<String, String> urlParams) {
        int groupId = Integer.parseInt(urlParams.get(":group_id"));
//        if (!model.existPost(post)) {
//            return new Answer(400);
//        }
            String json = dataToJson(model.getAllContactsForGroup(groupId));
            return Result.ok(json);
    }

}
