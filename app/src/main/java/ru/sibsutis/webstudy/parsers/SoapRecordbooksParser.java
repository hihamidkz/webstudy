package ru.sibsutis.webstudy.parsers;

import android.util.Pair;

public class SoapRecordbooksParser {
    public Pair<String, String> parse(String soapObject) {
        String[] arr1 = soapObject.split("AcademicGroupCompoundKey=");
        String[] arr2 = arr1[1].split(";");
        String[] arr3 = soapObject.split("Recordbook=anyType\\{");
        String[] arr4 = arr3[1].split(";");
        String[] arr5 = arr4[0].split("=");

        return new Pair<>(arr2[0], arr5[1]);
    }
}
