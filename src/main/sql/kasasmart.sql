CREATE USER kasasmart IDENTIFIED BY 'kasasmart';

CREATE DATABASE phonebook;
USE phonebook;

GRANT SELECT, INSERT, UPDATE, DELETE ON phonebook.* TO kasasmart;

DROP TABLE if exists contact;
CREATE TABLE contact (
    contact_id int NOT NULL auto_increment,
    fname varchar(26) NOT NULL,
    mname varchar(26) NOT NULL,
    lname varchar(26) NOT NULL,
    gender int,
    dob date,
    CONSTRAINT PRIMARY KEY (contact_id));

DROP TABLE if exists email;
CREATE TABLE email ( 
email varchar(30), 
contact_id int, 
CONSTRAINT PRIMARY KEY (email, contact_id),
CONSTRAINT FOREIGN KEY (contact_id) REFERENCES contact(contact_id) ON DELETE CASCADE);

DROP TABLE if exists contact_info;
CREATE TABLE contact_info ( 
country_code varchar(5), 
extension varchar(5), 
contact_number varchar(15) not null, 
contact_id int, 
CONSTRAINT PRIMARY KEY (contact_number, contact_id),
CONSTRAINT FOREIGN KEY (contact_id) REFERENCES contact(contact_id) ON DELETE CASCADE);

DROP TABLE if exists c_group;
CREATE TABLE c_group ( 
group_id int not null auto_increment, 
group_name varchar(100) not null, 
CONSTRAINT PRIMARY KEY (group_id));

DROP TABLE if exists contacts_groups;
CREATE TABLE contacts_groups (
contact_id int, 
group_id int, 
CONSTRAINT PRIMARY KEY(contact_id,group_id),
CONSTRAINT FOREIGN KEY (contact_id) REFERENCES contact(contact_id) ON DELETE CASCADE, 
CONSTRAINT FOREIGN KEY (group_id) REFERENCES c_group(group_id) ON DELETE CASCADE);



