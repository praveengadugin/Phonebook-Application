package com.phonebook.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface Model {
    int createContact(String fname, String mname, String lname, int gender, Date dob, List<String> emailList, String countryCode, String extension, String contactNumber, List<String> groupList);
    List getAllContacts();
    Optional<Contact> getContact(int contactId);
    void deleteContact(int contactId);
    void updateContact(Contact contact);
    List<Contact> getAllContactsForGroup(int groupId);
    int createGroup(String groupName);
    Optional<Group> getGroup(int groupId);
    void deleteGroup(int groupId);
    void deleteContactFromGroup(int contactId, int groupId);
    List getAllGroups();
}