package ru.sibsutis.webstudy.schedule.model;

/**
 * Класс-модель для SubjectsAdapter, используется для отоюражения расписания в ListView
 */
public class Lesson {
    public String time;
    public String subject;
    public String classroom;

    public Lesson(String time, String subject, String classroom) {
        this.time = time;
        this.subject = subject;
        this.classroom = classroom;
    }
}
