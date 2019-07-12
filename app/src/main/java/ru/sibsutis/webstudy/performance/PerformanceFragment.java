package ru.sibsutis.webstudy.performance;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


import ru.sibsutis.webstudy.R;

public class PerformanceFragment extends Fragment implements PerformanceView {
    private static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private int pageNumber;

    private ListView lv;
    private PerformancePresenter presenter;

    public static PerformanceFragment newInstance(int page) {
        PerformanceFragment pageFragment = new PerformanceFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);
        presenter = PerformancePresenter.getInstance(this);

        presenter.getPerformance(view, getActivity(), pageNumber);

        return view;
    }

    @Override
    public void showPerformance(List<MarkRecord> marks, View view) {
        lv = view.findViewById(R.id.lv);
        ArrayList<Performance> l = new ArrayList<>();
        PerformanceAdapter adapter = new PerformanceAdapter(view.getContext(), l);
        lv.setAdapter(adapter);

        for (int i = 0; i < marks.size(); i++) {
            Performance p = new Performance(marks.get(i).getSubject(),
                                            marks.get(i).getMark(),
                                            marks.get(i).getTypeOfTheControl());
            adapter.add(p);
        }

        Performance p = new Performance("", "", "");
        adapter.add(p);
    }
}