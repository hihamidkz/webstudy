package ru.sibsutis.webstudy.schedule.model;

import java.util.Date;
import java.util.List;

/**
 * Класс-модель для расписания, используется для представления одного дня недели
 */
public class DayOfWeek {
    private Date date;
    private String day;
    private List<ScheduleCell> cells;

    public DayOfWeek() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<ScheduleCell> getCells() {
        return cells;
    }

    public void setCells(List<ScheduleCell> cells) {
        this.cells = cells;
    }
}
