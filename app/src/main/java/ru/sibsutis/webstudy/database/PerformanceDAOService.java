package ru.sibsutis.webstudy.database;

import java.sql.SQLException;
import java.util.List;

import ru.sibsutis.webstudy.performance.MarkRecord;

public class PerformanceDAOService {
    public List<MarkRecord> getPerformanceByTerm(String term) throws SQLException {
        PerformanceDAO dao = null;
        try {
            dao = HelperFactory.getHelper().getPerformanceDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<MarkRecord> marks = dao.getPerformanceByTerm(term);

        return marks;
    }

    public void saveAll(List<List<MarkRecord>> marks) throws SQLException {
        PerformanceDAO dao = null;
        try {
            dao = HelperFactory.getHelper().getPerformanceDAO();
        } catch (SQLException e) {
        }

        for (List<MarkRecord> l : marks) {
            for (MarkRecord r : l) {
                dao.create(r);
            }
        }
    }
}
