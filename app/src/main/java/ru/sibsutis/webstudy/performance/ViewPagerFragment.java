package ru.sibsutis.webstudy.performance;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ru.sibsutis.webstudy.R;
import ru.sibsutis.webstudy.performance.PerformanceFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerFragment extends Fragment {

    static final int PAGE_COUNT = 8;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    public ViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        pager = view.findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        return view;
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PerformanceFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Семестр " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment created = (Fragment) super.instantiateItem(container, position);

            return created;
        }

    }
}
