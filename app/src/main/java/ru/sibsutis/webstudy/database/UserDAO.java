package ru.sibsutis.webstudy.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.sibsutis.webstudy.authorization.User;

public class UserDAO extends BaseDaoImpl<User, Integer> {
    protected UserDAO(ConnectionSource connectionSource, Class<User> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public User getUserByParams(String username, String passwd) throws SQLException {
        QueryBuilder<User, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("username", username).and().eq("password", passwd);
        PreparedQuery<User> preparedQuery = queryBuilder.prepare();
        List<User> users = query(preparedQuery);

        return users.get(0);
    }
}
