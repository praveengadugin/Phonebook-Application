package com.phonebook.payloads;

import com.phonebook.Constants;
import com.phonebook.Validable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Contact implements Validable {


    private String fname;
    private String mname;
    private String lname;
    private int gender;
    @Setter(AccessLevel.NONE)
    private Date dob;
    @Getter(AccessLevel.NONE)
    private Map<Integer,String> emails = new HashMap<Integer,String>();
    private String countryCode;
    private String extension;
    private String contactNumber;
    @Getter(AccessLevel.NONE)
    private Map<Integer,String> groups = new HashMap<Integer,String>();

    public List<String> getEmailList() {
        return (List<String>) this.emails.values().stream()
                .collect(Collectors.toList());
    }

    public List<String> getGroupList() {
        return (List<String>) this.groups.values().stream()
                .collect(Collectors.toList());
    }

    public void setDob(String dob) {
        try {
            this.dob = new SimpleDateFormat(Constants.DATEFORMAT).parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid() {
        boolean isValidContact = false;
        try{
            if(countryCode != null)
                Integer.parseInt(countryCode);
            if(extension != null)
                Integer.parseInt(extension);
            Long.parseLong(contactNumber);
            isValidContact = true;
        }catch(NumberFormatException nfe){
            isValidContact = false;
        }
        return fname != null && !fname.isEmpty() && isValidContact;
    }
}
