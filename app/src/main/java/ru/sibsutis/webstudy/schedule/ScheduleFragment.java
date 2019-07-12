package ru.sibsutis.webstudy.schedule;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ru.sibsutis.webstudy.R;
import ru.sibsutis.webstudy.schedule.model.DayOfWeek;
import ru.sibsutis.webstudy.schedule.model.Lesson;
import ru.sibsutis.webstudy.schedule.model.Schedule;
import ru.sibsutis.webstudy.schedule.model.ScheduleCell;

public class ScheduleFragment extends Fragment implements ScheduleView {
    private final String[] DAYS = { "Пн", "Вт", "Ср", "Чт", "Пт", "Сб" };

    private List<ListView> lv = new ArrayList<>();
    private Button change1;
    private Button change2;
    private TextView text1;
    private TextView text2;
    private GregorianCalendar dateBegin = new GregorianCalendar();
    private GregorianCalendar dateEnd = new GregorianCalendar();

    private SchedulePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = SchedulePresenter.getInstance(this);
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        setup(view);

        presenter.getSchedule(dateBegin, dateEnd, getActivity());

        return view;
    }

    private void setup(View view) {
        change1 = view.findViewById(R.id.changeBegin);
        change2 = view.findViewById(R.id.changeEnd);
        text1 = view.findViewById(R.id.dateBegin);
        text2 = view.findViewById(R.id.dateEnd);

        change1.setOnClickListener(new Listener());
        change2.setOnClickListener(new Listener());

        lv.add(view.findViewById(R.id.tab1));
        lv.add(view.findViewById(R.id.tab2));
        lv.add(view.findViewById(R.id.tab3));
        lv.add(view.findViewById(R.id.tab4));
        lv.add(view.findViewById(R.id.tab5));
        lv.add(view.findViewById(R.id.tab6));

        setCurrentDates();

        TabHost tabHost = view.findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(DAYS[0]);
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(DAYS[1]);
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator(DAYS[2]);
        tabSpec.setContent(R.id.tab3);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator(DAYS[3]);
        tabSpec.setContent(R.id.tab4);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag5");
        tabSpec.setIndicator(DAYS[4]);
        tabSpec.setContent(R.id.tab5);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag6");
        tabSpec.setIndicator(DAYS[5]);
        tabSpec.setContent(R.id.tab6);
        tabHost.addTab(tabSpec);
    }

    private void setCurrentDates() {
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;
        String text = "";

        int dayOfWeek = (Calendar.MONDAY);
        while (calendar.get(Calendar.DAY_OF_WEEK) != dayOfWeek){
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        text = "Дата начала: " + day + "." + (month + 1) + "." + year;
        text1.setText(text);
        dateBegin.set(year, month, day);

        calendar.add(Calendar.DAY_OF_MONTH, 6);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        text = "Дата окончания: " + day + "." + (month + 1) + "." + year;
        text2.setText(text);
        dateEnd.set(year, month, day);
    }

    @Override
    public void showSchedule(Schedule schedule) {
        List<DayOfWeek> d = schedule.getDays();
        for (int i = 0; i < d.size(); i++) {
            ArrayList<Lesson> lessons = new ArrayList<>();
            SubjectsAdapter adapter = new SubjectsAdapter(getContext(), lessons);
            lv.get(i).setAdapter(adapter);
            List<ScheduleCell> sc = d.get(i).getCells();
            for (int j = 0; j < sc.size(); j++) {
                String text = sc.get(j).getSubject() + "\n" + sc.get(j).getLessonType();
                if (sc.get(j).getSubject().equals("")) {
                    text = "";
                }

                DateFormat formatter = new SimpleDateFormat("HH:mm");
                String time = formatter.format(sc.get(j).getDateBegin()) + "-\n"
                        + formatter.format(sc.get(j).getDateEnd());
                Lesson lesson = new Lesson(time,
                        text,
                        sc.get(j).getClassroom());
                adapter.add(lesson);
            }
        }
    }

    private class Listener implements View.OnClickListener {
        Calendar calendar;
        DatePickerDialog datePickerDialog;
        int year, month, day;

        @Override
        public void onClick(View v) {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            Calendar start = new GregorianCalendar(calendar.get(Calendar.YEAR) - 1,
                    Calendar.JANUARY, 1);
            Calendar end = new GregorianCalendar(calendar.get(Calendar.YEAR),
                    Calendar.DECEMBER, 31);

            List<Calendar> mondaysList = new ArrayList<>();

            int dayOfWeek = (v.getId() == R.id.changeBegin ? Calendar.MONDAY : Calendar.SUNDAY);
            while (start.before(end)){
                if (start.get(Calendar.DAY_OF_WEEK) == dayOfWeek){
                    Calendar newCal = Calendar.getInstance();
                    newCal.set(Calendar.YEAR, start.get(Calendar.YEAR));
                    newCal.set(Calendar.MONTH, start.get(Calendar.MONTH));
                    newCal.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH));
                    mondaysList.add(newCal);
                }
                start.add(Calendar.DAY_OF_MONTH, 1);
            }

            Calendar[] mondays = mondaysList.toArray(new Calendar[0]);
            datePickerDialog = DatePickerDialog.newInstance(new DateSetListener(v.getId()), year, month, day);
            datePickerDialog.setSelectableDays(mondays);
            datePickerDialog.setThemeDark(false);
            datePickerDialog.showYearPickerFirst(false);
            datePickerDialog.setAccentColor(Color.parseColor("#0072BA"));
            datePickerDialog.setTitle(v.getId() == R.id.changeBegin ? "Дата начала" : "Дата окончания");
            datePickerDialog.show(getActivity().getFragmentManager(), "Date");
        }
    }

    private class DateSetListener implements DatePickerDialog.OnDateSetListener {
        int id;

        public DateSetListener(int id) {
            this.id = id;
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int month, int day) {
            switch (id) {
                case R.id.changeBegin:
                    String text = "Дата начала: " + day + "." + (month + 1) + "." + year;
                    text1.setText(text);
                    dateBegin.set(year, month, day);
                    break;
                case R.id.changeEnd:
                    String txt = "Дата окончания: " + day + "." + (month + 1) + "." + year;
                    text2.setText(txt);
                    dateEnd.set(year, month, day);
                    break;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.add(Calendar.DAY_OF_MONTH, id == R.id.changeBegin ? 6 : -6);

            switch (id) {
                case R.id.changeBegin:
                    String text = "Дата окончания: " +
                            calendar.get(Calendar.DAY_OF_MONTH) + "." +
                            (calendar.get(Calendar.MONTH) + 1) + "." +
                            calendar.get(Calendar.YEAR);
                    text2.setText(text);
                    dateEnd.set(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH));
                    break;
                case R.id.changeEnd:
                    String txt = "Дата начала: " +
                            calendar.get(Calendar.DAY_OF_MONTH) + "." +
                            (calendar.get(Calendar.MONTH) + 1) + "." +
                            calendar.get(Calendar.YEAR);
                    text1.setText(txt);
                    dateBegin.set(calendar.get(Calendar.YEAR),
                                  calendar.get(Calendar.MONTH) + 1,
                                  calendar.get(Calendar.DAY_OF_MONTH));
                    break;
            }
            presenter.getSchedule(dateBegin, dateEnd, getActivity());
        }
    }
}
