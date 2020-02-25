package com.phonebook.sql2omodel;

import com.phonebook.model.Contact;
import com.phonebook.model.ContactInfo;
import com.phonebook.model.Group;
import com.phonebook.model.Model;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Sql2oModel implements Model {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public int createContact(String fname, String mname, String lname, int gender, Date dob, List<String> emailList, String countryCode, String extension, String contactNumber, List<String> groupList) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("insert into contact(fname, mname, lname, gender, dob) VALUES (:fname, :mname, :lname, :gender, :dob)")
                    .addParameter("fname", fname)
                    .addParameter("mname", mname)
                    .addParameter("lname", lname)
                    .addParameter("gender", gender)
                    .addParameter("dob", dob)
                    .executeUpdate();
            int contactId = conn.createQuery("select LAST_INSERT_ID()").executeScalar(Integer.class);
            emailList.forEach((emailId) ->
                    conn.createQuery("insert into email(email, contact_id) VALUES (:email_id, :contact_id)")
                            .addParameter("email_id", emailId)
                            .addParameter("contact_id", contactId)
                            .executeUpdate());
            conn.createQuery("insert into contact_info(country_code, extension, contact_number, contact_id) VALUES (:country_code, :extension, :contact_number, :contact_id)")
                    .addParameter("country_code", countryCode)
                    .addParameter("extension", extension)
                    .addParameter("contact_number", contactNumber)
                    .addParameter("contact_id", contactId)
                    .executeUpdate();
            groupList.forEach((groupId) ->
                    conn.createQuery("insert into contacts_groups(contact_id, group_id) VALUES (:contact_id, :group_id)")
                            .addParameter("contact_id", contactId)
                            .addParameter("group_id", Integer.parseInt(groupId))
                            .executeUpdate());
            conn.commit();
            return contactId;
        }
    }

    @Override
    public Optional<Contact> getContact(int contactId) {
        try (Connection conn = sql2o.beginTransaction()) {
            List<Contact> contacts = conn.createQuery("select * from contact where contact_id=:contact_id")
                    .addParameter("contact_id", contactId)
                    .executeAndFetch(Contact.class);

            if (contacts.size() == 0) {
                return Optional.empty();
            } else if (contacts.size() == 1) {
                Contact contact = contacts.get(0);
                contact.setEmails(getEmailsFor(conn, contactId));
                contact.setGroups(getGroupsFor(conn, contactId));
                contact.setContactInfo(getContactInfoFor(conn, contactId));
                conn.commit();
                return Optional.of(contact);
            } else {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void deleteContact(int contactId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("delete from contact where contact_id=:contact_id")
                    .addParameter("contact_id", contactId)
                    .executeUpdate();
        }
    }

    @Override
    public void updateContact(Contact contact) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("update contact set fname=:fname, mname=:mname, lname=:lname, gender=:gender, dob=:dob where contact_id=:contact_id")
                    .addParameter("fname", contact.getFname())
                    .addParameter("mname", contact.getMname())
                    .addParameter("lname", contact.getLname())
                    .addParameter("gender", contact.getGender())
                    .addParameter("dob", contact.getDob())
                    .addParameter("contact_id", contact.getContact_id())
                    .executeUpdate();
            conn.createQuery("update contact_info set country_code=:country_code, extension=:extension, contact_number=:contact_number where contact_id=:contact_id")
                    .addParameter("country_code", contact.getContactInfo().getCountry_code())
                    .addParameter("extension", contact.getContactInfo().getExtension())
                    .addParameter("contact_number", contact.getContactInfo().getContact_number())
                    .addParameter("contact_id", contact.getContact_id())
                    .executeUpdate();
            conn.createQuery("delete from email where contact_id=:contact_id")
                    .addParameter("contact_id", contact.getContact_id())
                    .executeUpdate();
            conn.createQuery("delete from contacts_groups where contact_id=:contact_id")
                    .addParameter("contact_id", contact.getContact_id())
                    .executeUpdate();
            contact.getEmails().forEach((emailId) ->
                    conn.createQuery("insert into email(email, contact_id) VALUES (:email_id, :contact_id)")
                            .addParameter("email_id", emailId)
                            .addParameter("contact_id", contact.getContact_id())
                            .executeUpdate());
            contact.getGroups().forEach((groupId) ->
                    conn.createQuery("insert into contacts_groups(contact_id, group_id) VALUES (:contact_id, :group_id)")
                            .addParameter("contact_id", contact.getContact_id())
                            .addParameter("group_id", Integer.parseInt(groupId))
                            .executeUpdate());
            conn.commit();
        }
    }

    @Override
    public List getAllContacts() {
        try (Connection conn = sql2o.open()) {
            List<Contact> contacts = conn.createQuery("select * from contact")
                    .executeAndFetch(Contact.class);
            contacts.forEach((contact) -> {contact.setEmails(getEmailsFor(conn, contact.getContact_id())); contact.setGroups(getGroupsFor(conn, contact.getContact_id())); contact.setContactInfo(getContactInfoFor(conn, contact.getContact_id()));});
            return contacts;
        }
    }

    @Override
    public List<Contact> getAllContactsForGroup(int groupId) {
        try (Connection conn = sql2o.beginTransaction()) {
            List<Contact> contacts = new ArrayList<Contact>();
            List<Integer> contactIds = conn.createQuery("select contact_id from contacts_groups where group_id=:group_id")
                    .addParameter("group_id", groupId)
                    .executeAndFetch(Integer.class);
            System.out.println(contactIds);
            contactIds.forEach((contactId) -> contacts.add(getContact(contactId).get()));
            conn.commit();
            return contacts;
        }
    }

    @Override
    public int createGroup(String groupName){
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("insert into c_group(group_name) VALUES (:group_name)")
                    .addParameter("group_name", groupName)
                    .executeUpdate();
            int groupId = conn.createQuery("select LAST_INSERT_ID()").executeScalar(Integer.class);
            conn.commit();
            return groupId;
        }
    }

    @Override
    public Optional<Group> getGroup(int groupId) {
        try (Connection conn = sql2o.open()) {
            List<Group> groups = conn.createQuery("select * from c_group where group_id=:group_id")
                    .addParameter("group_id", groupId)
                    .executeAndFetch(Group.class);
            if (groups.size() == 0) {
                return Optional.empty();
            } else if (groups.size() == 1) {
                return Optional.of(groups.get(0));
            } else {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void deleteGroup(int groupId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("delete from c_group where group_id=:group_id")
                    .addParameter("group_id", groupId)
                    .executeUpdate();
        }
    }

    @Override
    public void deleteContactFromGroup(int contactId, int groupId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("delete from contacts_groups where contact_id=:contact_id and group_id=:group_id")
                    .addParameter("contact_id", contactId)
                    .addParameter("group_id", groupId)
                    .executeUpdate();
        }
    }

    @Override
    public List getAllGroups() {
        try (Connection conn = sql2o.open()) {
            List<Group> groups = conn.createQuery("select * from c_group")
                    .executeAndFetch(Group.class);
            return groups;
        }
    }

    private List<String> getEmailsFor(Connection conn, int contactId) {
        return conn.createQuery("select email from email where contact_id=:contact_id")
                .addParameter("contact_id", contactId)
                .executeAndFetch(String.class);
    }

    private List<String> getGroupsFor(Connection conn, int contactId) {
        return conn.createQuery("select group_id from contacts_groups where contact_id=:contact_id")
                .addParameter("contact_id", contactId)
                .executeAndFetch(String.class);
    }

    private ContactInfo getContactInfoFor(Connection conn, int contactId){
        List<ContactInfo> contactInfoList = conn.createQuery("select * from contact_info where contact_id=:contact_id")
                .addParameter("contact_id", contactId)
                .executeAndFetch(ContactInfo.class);
        if(!contactInfoList.isEmpty()){
            return contactInfoList.get(0);
        }else{
            return new ContactInfo();
        }
    }

}
