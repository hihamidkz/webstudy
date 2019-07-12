package ru.sibsutis.webstudy.schedule.model;

import java.util.Date;

/**
 * Класс-модель для расписания, используется для представления одного занятия
 */
public class ScheduleCell {
    private Date dateBegin;
    private Date dateEnd;
    private String subject = "";
    private String teacher = "";
    private String classroom = "";
    private String group = "";
    private String lessonType = "";

    public ScheduleCell() {
    }

    public ScheduleCell(String subject, String teacher, String classroom, String lessonType) {
        this.subject = subject;
        this.teacher = teacher;
        this.classroom = classroom;
        this.lessonType = lessonType;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }
}
