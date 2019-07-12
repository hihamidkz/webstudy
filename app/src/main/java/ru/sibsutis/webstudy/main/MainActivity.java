package ru.sibsutis.webstudy.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import ru.sibsutis.webstudy.authorization.AuthorizeFragment;
import ru.sibsutis.webstudy.database.HelperFactory;
import ru.sibsutis.webstudy.R;
import ru.sibsutis.webstudy.schedule.ScheduleFragment;
import ru.sibsutis.webstudy.performance.ViewPagerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    private final String USER_ID = "userid";
    private final String FIRST_RUN = "firstrun";

    private SharedPreferences spref;
    private ProgressBar progressBar;
    private MainPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HelperFactory.setHelper(getApplicationContext());

        presenter = new MainPresenter(this);
        spref = getPreferences(MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);

        if (spref.getBoolean(FIRST_RUN, true)) {
            presenter.firstRun();
        } else if (spref.getString(USER_ID, null) == null) {
            presenter.authorize();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            spref = getPreferences(MODE_PRIVATE);
            spref.edit().putString(USER_ID, null).commit();
            presenter.onExit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (spref.getString(USER_ID, null) == null) {
            return false;
        }

        if (id == R.id.schedule) {
            presenter.getSchedule();
        } else if (id == R.id.performance) {
            presenter.getPerformance();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
     public void showSchedule() {
        Fragment fragment = new ScheduleFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void showPerformance() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("performance");
        if (fragment == null) {
            fragment = new ViewPagerFragment();
            String tag = fragment.getTag();
            System.out.println("Tag " + tag);
        }
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment, "performance")
                       .commit();
    }

    @Override
    public void showAuthorization() {
        Fragment fragment = new AuthorizeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment)
                       .commit();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
