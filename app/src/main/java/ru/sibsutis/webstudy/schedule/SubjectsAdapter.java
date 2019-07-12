package ru.sibsutis.webstudy.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.sibsutis.webstudy.R;
import ru.sibsutis.webstudy.schedule.model.Lesson;

public class SubjectsAdapter extends ArrayAdapter<Lesson> {
    public SubjectsAdapter(Context context, ArrayList<Lesson> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Lesson lesson = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lesson, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvLesson);
        TextView tvClass = (TextView) convertView.findViewById(R.id.tvClass);

        tvName.setText(lesson.time);
        tvHome.setText(lesson.subject);
        tvClass.setText(lesson.classroom);

        return convertView;
    }
}