package ru.sibsutis.webstudy.parsers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.sibsutis.webstudy.schedule.model.DayOfWeek;
import ru.sibsutis.webstudy.schedule.model.Schedule;
import ru.sibsutis.webstudy.schedule.model.ScheduleCell;

public class SoapScheduleParser {
    public Schedule parse(String soapObject) throws ParseException {
        String[] arr = soapObject.split("Day=anyType\\{");
        if (arr.length < 2) {
            return null;
        }
        Schedule schedule = new Schedule();
        List<DayOfWeek> week = new ArrayList<>();

        String[] line = arr[0].split("\\{");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        schedule.setDateBegin(format.parse(line[2].split("=")[1]));
        String dateEnd = line[2].split("; ")[1];
        schedule.setDateEnd(format.parse(dateEnd.split("=")[1]));
        for (int i = 1; i < 7; i++) {
            DayOfWeek day = new DayOfWeek();
            String[] days = arr[i].split("ScheduleCell=anyType\\{");
            String date = days[0].split("; ")[0];
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            day.setDate(f.parse(date.split("=")[1]));
            String dayOfWeek = days[0].split("; ")[1];
            day.setDay(dayOfWeek.split("=")[1]);
            List<ScheduleCell> list = new ArrayList<>();
            for (int j = 1; j < 7; j++) {
                ScheduleCell sc = new ScheduleCell();
                String dateBegin = days[j].split("; ")[0];
                sc.setDateBegin(format.parse(dateBegin.split("=")[1]));
                dateEnd = days[j].split("; ")[1];
                sc.setDateEnd(format.parse(dateEnd.split("=")[1]));
                String[] lesson = days[j].split("Lesson=anyType\\{");
                if (lesson.length == 1) {
                    list.add(sc);
                    continue;
                }
                String subject = lesson[1].split("; ")[1];
                sc.setSubject(subject.split("=")[1]);
                String lessonType = lesson[1].split("; ")[2];
                sc.setLessonType(lessonType.split("=")[1]);
                String[] teacher = lesson[1].split("Teacher=anyType\\{");
                if (teacher.length > 1) {
                    String teacherName = teacher[1].split("; ")[1];
                    sc.setTeacher(teacherName.split("=")[1]);
                }
                String[] classroom = lesson[1].split("Classroom=anyType\\{");
                if (classroom.length > 1) {
                    String classroomName = classroom[1].split("; ")[1];
                    sc.setClassroom(classroomName.split("=")[1]);
                }
                String[] academicGroup = lesson[1].split("AcademicGroup*\\{");
                if (academicGroup.length > 1) {
                    String academicGroupName = "";
                    for (String ag : academicGroup) {
                        String tmp = ag.split("; ")[1];
                        academicGroupName += tmp.split("=")[1] + " ";
                    }
                    sc.setGroup(academicGroupName);
                }
                list.add(sc);
            }
            day.setCells(list);
            week.add(day);
        }
        schedule.setDays(week);

        return schedule;
    }
}
