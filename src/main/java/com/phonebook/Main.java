package com.phonebook;

import com.phonebook.handlers.*;
import com.phonebook.model.Model;
import com.phonebook.sql2omodel.Sql2oModel;
import org.sql2o.Sql2o;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.ResourceBundle;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        Spark.staticFileLocation("/UI");
        ResourceBundle rb = ResourceBundle.getBundle("db-credentials");
        Properties prop = new Properties();
        try {
            prop.load(Main.class.getClassLoader().getResourceAsStream("db-credentials.properties"));

        } catch (IOException e) {
            System.exit(1);
            e.printStackTrace();
        }

        Sql2o sql2o = new Sql2o("jdbc:mysql://" + prop.getProperty("host") + ":" + prop.getProperty("port") + "/" + prop.getProperty("database"),
                prop.getProperty("username"), prop.getProperty("password"));

        Model model = new Sql2oModel(sql2o);
        Spark.exception(Exception.class, (e, request, response) -> {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            System.err.println(sw.getBuffer().toString());
        });

        get("/hello", (req, res) -> "Hello World");

        get("/contact", new ContactAllFetch(model));

        post("/contact", new ContactCreate(model));

        delete("/contact/:contact_id", new ContactDelete(model));

        get("/contact/:contact_id", new GetSingleContact(model));

        put("/contact/:contact_id", new ContactEdit(model));

        get("/contact/group/:group_id", new GetContactsForGroup(model));

        post("/group", new GroupCreate(model));

        delete("/group/:group_id", new GroupDelete(model));

        get("/group", new GroupAllFetch(model));

        delete("/contact/:contact_id/:group_id", new ContactDeleteFromGroup(model));

        get("/alive", (Request request, Response response) -> {
            return "ok";
        });
    }
}
