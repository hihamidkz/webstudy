package ru.sibsutis.webstudy.parsers;

import java.util.ArrayList;
import java.util.List;

import ru.sibsutis.webstudy.authorization.User;

public class SoapUsersParser {
    public List<User> parse(String soapObject) {
        List<User> users = new ArrayList<>();

        String[] arr = soapObject.split("User=anyType\\{");

        for (int i = 1; i < arr.length; i++) {
            User user = new User();
            String[] us = arr[i].split("\\;");
            String[] id = us[0].split("=");
            user.setUserid(id[1]);
            String[] login = us[1].split("=");
            user.setUsername(login[1]);
            String[] password = us[2].split("=");
            user.setPassword(password[1].equals("anyType{}") ? "" : password[1]);
            users.add(user);
        }

        return users;
    }
}
