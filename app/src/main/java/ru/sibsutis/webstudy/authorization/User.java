package ru.sibsutis.webstudy.authorization;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class User {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "userid", dataType = DataType.STRING)
    private String userid;
    @DatabaseField(columnName = "username", dataType = DataType.STRING)
    private String username;
    @DatabaseField(columnName = "password", dataType = DataType.STRING)
    private String password;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
