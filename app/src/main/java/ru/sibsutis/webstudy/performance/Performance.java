package ru.sibsutis.webstudy.performance;

/**
 * Класс-модель для PerformanceAdapter, нужен для отображения успеваемости в ViewPager
 */
public class Performance {
    public String subject;
    public String type;
    public String mark;

    public Performance(String subject, String mark, String type) {
        this.subject = subject;
        this.mark = mark;
        this.type = type;
    }
}
