package com.phonebook.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class Contact {
    private int contact_id;
    private String fname;
    private String mname;
    private String lname;
    private int gender;
    private Date dob;
    private List<String> emails;
    private ContactInfo contactInfo;
    private List<String> groups;
}
