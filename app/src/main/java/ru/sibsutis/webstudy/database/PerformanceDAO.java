package ru.sibsutis.webstudy.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.sibsutis.webstudy.performance.MarkRecord;

public class PerformanceDAO extends BaseDaoImpl {
    protected PerformanceDAO(ConnectionSource connectionSource, Class<MarkRecord> dataClass)
            throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<MarkRecord> getPerformanceByTerm(String term) throws SQLException {
        QueryBuilder<MarkRecord, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("term", term);
        PreparedQuery<MarkRecord> preparedQuery = queryBuilder.prepare();
        List<MarkRecord> list = query(preparedQuery);

        return list;
    }

    public Long getCount() throws SQLException {
        QueryBuilder<MarkRecord, Integer> queryBuilder = queryBuilder();
        queryBuilder.setCountOf(true);
        queryBuilder.where().eq("term", "Первый семестр");
        PreparedQuery<MarkRecord> preparedQuery = queryBuilder.prepare();
        return countOf(preparedQuery);
    }
}
