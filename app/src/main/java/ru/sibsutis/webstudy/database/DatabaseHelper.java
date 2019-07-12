package ru.sibsutis.webstudy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ru.sibsutis.webstudy.performance.MarkRecord;
import ru.sibsutis.webstudy.authorization.User;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME ="webstudy.db";
    private static final int DATABASE_VERSION = 1;

    private PerformanceDAO performanceDAO = null;
    private UserDAO userDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource){
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, MarkRecord.class);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public PerformanceDAO getPerformanceDAO() throws SQLException{
        if(performanceDAO == null){
            performanceDAO = new PerformanceDAO(getConnectionSource(), MarkRecord.class);
        }
        return performanceDAO;
    }

    public UserDAO getUserDAO() throws SQLException {
        if(userDAO == null){
            userDAO = new UserDAO(getConnectionSource(), User.class);
        }
        return userDAO;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer) {

    }

    @Override
    public void close(){
        super.close();
        performanceDAO = null;
        userDAO = null;
    }
}
