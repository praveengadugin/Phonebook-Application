package com.phonebook.payloads;

import com.phonebook.Validable;
import lombok.Data;

@Data
public class Group implements Validable {

    private String groupName;

    public boolean isValid() {
        return groupName != null && !groupName.isEmpty();
    }
}
