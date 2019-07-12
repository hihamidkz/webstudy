package ru.sibsutis.webstudy.database;

import java.sql.SQLException;
import java.util.List;

import ru.sibsutis.webstudy.authorization.User;

public class UserDAOService {
    public void saveAll(List<User> users) throws SQLException {
        UserDAO dao = null;
        try {
            dao = HelperFactory.getHelper().getUserDAO();
        } catch (SQLException e) {
        }

        for (User u : users) {
            dao.create(u);
        }
    }

    public User getUserByParams(String username, String passwd) throws SQLException {
        UserDAO dao = null;
        try {
            dao = HelperFactory.getHelper().getUserDAO();
        } catch (SQLException e) {
        }

        return dao.getUserByParams(username, passwd);
    }
}
