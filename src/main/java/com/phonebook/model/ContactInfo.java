package com.phonebook.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfo {

    private String country_code;
    private String extension;
    private String contact_number;
    private int contact_id;


}
