package ru.sibsutis.webstudy.parsers;

import java.util.ArrayList;
import java.util.List;

import ru.sibsutis.webstudy.performance.MarkRecord;

public class SoapPerformanceParser {
    public List<List<MarkRecord>> parse(String soapObject) {
        List<List<MarkRecord>> marks = new ArrayList<>();

        String[] arr = soapObject.split("MarkRecord=anyType\\{");

        String currentTerm = "Первый семестр";
        String recordsTerm;
        int i = 1;
        while(i < arr.length) {
            List<MarkRecord> list = new ArrayList<>();
            do {
                MarkRecord record = new MarkRecord();
                String[] subject = arr[i].split("Subject=");
                record.setSubject(subject[1].split(";")[0]);

                String[] mark = arr[i].split("Mark=");
                if (mark[1].split(";")[0].equals("anyType{}")) {
                    record.setMark("");
                } else {
                    record.setMark(mark[1].split(";")[0]);
                }

                String[] typeOfTheControl = arr[i].split("TypeOfTheControl=");
                record.setTypeOfTheControl(typeOfTheControl[1].split(";")[0]);

                String[] term = arr[i].split("Term=");
                record.setTerm(term[1].split(";")[0]);
                recordsTerm = record.getTerm();

                if (recordsTerm.equals(currentTerm)) {
                    list.add(record);
                    i++;
                }
            } while (currentTerm.equals(recordsTerm) && i < arr.length);
            marks.add(list);
            currentTerm = recordsTerm;
        }

        return marks;
    }
}
