package ru.sibsutis.webstudy.performance;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Класс-модель оценок, используется при получении успеваемости
 */
@DatabaseTable(tableName = "performance")
public class MarkRecord {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "subject", dataType = DataType.STRING)
    private String subject;
    @DatabaseField(columnName = "mark", dataType = DataType.STRING)
    private String mark;
    @DatabaseField(columnName = "type", dataType = DataType.STRING)
    private String typeOfTheControl;
    @DatabaseField(columnName = "term", dataType = DataType.STRING)
    private String term;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getTypeOfTheControl() {
        return typeOfTheControl;
    }

    public void setTypeOfTheControl(String typeOfTheControl) {
        this.typeOfTheControl = typeOfTheControl;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
