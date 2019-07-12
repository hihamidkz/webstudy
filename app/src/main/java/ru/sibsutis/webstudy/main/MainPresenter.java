package ru.sibsutis.webstudy.main;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import ru.sibsutis.webstudy.authorization.AuthorizeFragment;
import ru.sibsutis.webstudy.HttpTransportBasicAuthSE;
import ru.sibsutis.webstudy.R;
import ru.sibsutis.webstudy.database.UserDAOService;
import ru.sibsutis.webstudy.authorization.User;
import ru.sibsutis.webstudy.parsers.SoapUsersParser;

public class MainPresenter {
    private MainView mainView;

    MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    public void firstRun() {
        mainView.showProgressBar();
        new GetUsers((MainActivity)mainView).execute();
    }

    public void authorize() {
        mainView.showAuthorization();
    }

    public void onExit() {
        mainView.showAuthorization();
    }

    public void getSchedule() {
        mainView.showSchedule();
    }

    public void getPerformance() {
        mainView.showPerformance();
    }

    void onDestroy() {
        mainView = null;
    }

    class GetUsers extends AsyncTask<String, Void, String> {
        final String NAMESPACE = "http://sgu-infocom.ru/study";
        final String PATH = "http://91.196.247.150:8080/1c/ws/study.1cws";
        final String METHOD_NAME = "GetUsers";
        final String SOAP_ACTION = "http://sgu-infocom.ru/study#WebStudy:GetUsers";
        final String USERNAME = "WebServices";
        final String PASSWORD = "12345";
        final String FIRST_RUN = "firstrun";

        MainActivity activity;
        String res = "";

        ProgressBar progressBar;

        public GetUsers(MainActivity a) {
            this.activity = a;
            progressBar = activity.findViewById(R.id.progressBar);
        }

        @Override
        protected void onPostExecute(String s) {
            SoapUsersParser parser = new SoapUsersParser();
            List<User> list = parser.parse(res);

            try {
                new UserDAOService().saveAll(list);
            } catch (SQLException e) {
            }
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            SharedPreferences spref = activity.getPreferences(activity.MODE_PRIVATE);
            if (spref.getBoolean(FIRST_RUN, true)) {
                spref.edit().putBoolean(FIRST_RUN, false).commit();
            }
            Fragment fragment = new AuthorizeFragment();
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        @Override
        protected String doInBackground(String... params) {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);

            envelope.setOutputSoapObject(soapObject);

            HttpTransportBasicAuthSE httpTransportSE = new HttpTransportBasicAuthSE(PATH, USERNAME, PASSWORD);

            try {
                httpTransportSE.call(SOAP_ACTION, envelope);
                SoapObject soap =(SoapObject)envelope.getResponse();
                res = soap.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return res;
        }
    }

}
