package ru.sibsutis.webstudy.performance;

import android.view.View;

import java.util.List;

public interface PerformanceView {
    void showPerformance(List<MarkRecord> marks, View view);
}
