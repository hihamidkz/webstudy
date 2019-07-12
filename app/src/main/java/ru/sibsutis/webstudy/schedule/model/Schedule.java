package ru.sibsutis.webstudy.schedule.model;

import java.util.Date;
import java.util.List;

/**
 * Класс-модель для расписания
 */
public class Schedule {
    private Date dateBegin;
    private Date dateEnd;
    private List<DayOfWeek> days;

    public Schedule() {

    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<DayOfWeek> getDays() {
        return days;
    }

    public void setDays(List<DayOfWeek> days) {
        this.days = days;
    }
}
