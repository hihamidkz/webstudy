package ru.sibsutis.webstudy.performance;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import ru.sibsutis.webstudy.HttpTransportBasicAuthSE;
import ru.sibsutis.webstudy.database.PerformanceDAOService;
import ru.sibsutis.webstudy.parsers.SoapPerformanceParser;

public final class PerformancePresenter {
    private PerformanceView performanceView;
    private static PerformancePresenter instance;

    private final String[] TERMS = {"Первый семестр",
            "Второй семестр",
            "Третий семестр",
            "Четвертый семестр",
            "Пятый семестр",
            "Шестой семестр",
            "Седьмой семестр",
            "Восьмой семестр"};

    private PerformancePresenter(PerformanceView performanceView) {
        this.performanceView = performanceView;
    }

    public static PerformancePresenter getInstance(PerformanceView performanceView) {
        if (instance == null) {
            instance = new PerformancePresenter(performanceView);
        }

        return instance;
    }

    public void getPerformance(View view, Activity currentActivity, int pageNumber) {
        SharedPreferences spref = currentActivity.getPreferences(Context.MODE_PRIVATE);
        boolean performance = spref.getBoolean("performance", false);

        if (!performance) {
            new GetEducationalPerformance(currentActivity, view).execute(String.valueOf(pageNumber));
        } else {
            List<MarkRecord> marks = null;
            try {
                 marks = new PerformanceDAOService().getPerformanceByTerm(TERMS[pageNumber]);
            } catch (SQLException e) {
            }

            performanceView.showPerformance(marks, view);
        }
    }

    class GetEducationalPerformance extends AsyncTask<String, Void, String> {
        private final String NAMESPACE = "http://sgu-infocom.ru/study";
        private final String METHOD_NAME = "GetEducationalPerformance";
        private final String PATH = "http://91.196.247.150:8080/1c/ws/study.1cws";
        private final String SOAP_ACTION = "http://sgu-infocom.ru/study#WebStudy:GetEducationalPerformance";
        private final String USERNAME = "WebServices";
        private final String PASSWORD = "12345";

        private Activity activity;
        private View view;

        private int pageNumber;

        public GetEducationalPerformance(Activity activity, View view) {
            this.activity = activity;
            this.view = view;
        }

        @Override
        public void onPostExecute(String s) {
            if (s == null) {
                return;
            }
            SoapPerformanceParser parser = new SoapPerformanceParser();
            List<List<MarkRecord>> marks = parser.parse(s);

            SharedPreferences spref = activity.getPreferences(Context.MODE_PRIVATE);

            // Костыль для избежания дублирования записей в БД
            if (!spref.getBoolean("performance", false)) {
                try {
                    new PerformanceDAOService().saveAll(marks);
                } catch (SQLException e) {
                }
            }


            spref.edit().putBoolean("performance", true).commit();

            List<MarkRecord> list = marks.get(pageNumber);

            performanceView.showPerformance(list, view);
        }

        @Override
        protected String doInBackground(String... params) {
            pageNumber = Integer.parseInt(params[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);

            SharedPreferences spref = activity.getPreferences(Context.MODE_PRIVATE);
            String userId = spref.getString("userid", null);
            String recordbookId = spref.getString("recordbook_id", null);

            soapObject.addProperty("UserId", userId);
            soapObject.addProperty("RecordbookId", recordbookId);
            envelope.setOutputSoapObject(soapObject);

            HttpTransportBasicAuthSE httpTransportSE = new HttpTransportBasicAuthSE(PATH, USERNAME, PASSWORD);

            String res = null;
            try {
                httpTransportSE.call(SOAP_ACTION, envelope);
                SoapObject soap = (SoapObject)envelope.getResponse();
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
