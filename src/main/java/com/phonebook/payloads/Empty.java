package com.phonebook.payloads;

import com.phonebook.Validable;

public class Empty implements Validable {
    @Override
    public boolean isValid() {
        return true;
    }
}
