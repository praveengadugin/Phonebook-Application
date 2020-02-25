package com.phonebook;

import java.util.Map;

public interface RequestHandler<V extends Validable> {
    Result process(V value, Map<String, String> urlParams);
}

