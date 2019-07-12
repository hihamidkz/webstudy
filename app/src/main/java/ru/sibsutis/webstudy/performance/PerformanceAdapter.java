package ru.sibsutis.webstudy.performance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.sibsutis.webstudy.R;

public class PerformanceAdapter extends ArrayAdapter<Performance> {
    public PerformanceAdapter(Context context, ArrayList<Performance> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Performance lesson = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_performance, parent, false);
        }
        // Lookup view for data population
        TextView tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
        TextView tvType = (TextView) convertView.findViewById(R.id.tvType);
        TextView tvMark = (TextView) convertView.findViewById(R.id.tvMark);
        // Populate the data into the template view using the data object
        tvSubject.setText(lesson.subject);
        tvType.setText(lesson.type);
        tvMark.setText((lesson.mark.equals("Удовлетворительно") ? "Удовл." : lesson.mark));

        switch (lesson.mark) {
            case "Удовлетворительно":
                tvMark.setTextColor(Color.parseColor("#FF2D00"));
                break;
            case "Хорошо":
                tvMark.setTextColor(Color.parseColor("#0000FF"));
                break;
            case "Отлично":
                tvMark.setTextColor(Color.parseColor("#3FCF1F"));
                break;
            case "Зачет":
                tvMark.setTextColor(Color.parseColor("#883AFF"));
                break;
        }

        switch (lesson.type) {
            case "Экзамен":
                tvType.setTextColor(Color.parseColor("#FF7B00"));
                break;
            case "Зачет":
                tvType.setTextColor(Color.parseColor("#883AFF"));
                break;
            case "Расчетно-графическая работа":
                tvType.setTextColor(Color.parseColor("#00CBFF"));
                break;
            case "Реферат":
                tvType.setTextColor(Color.parseColor("#33FF9E"));
                break;
        }

        return convertView;
    }
}