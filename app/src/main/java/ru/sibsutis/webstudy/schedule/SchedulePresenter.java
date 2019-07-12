package ru.sibsutis.webstudy.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.GregorianCalendar;

import ru.sibsutis.webstudy.HttpTransportBasicAuthSE;
import ru.sibsutis.webstudy.schedule.model.Schedule;
import ru.sibsutis.webstudy.parsers.SoapScheduleParser;

public final class SchedulePresenter {
    private ScheduleView scheduleView;
    private static SchedulePresenter instance = null;

    private SchedulePresenter(ScheduleView scheduleView) {
        this.scheduleView = scheduleView;
    }

    public static SchedulePresenter getInstance(ScheduleView scheduleView) {
        if (instance == null) {
            instance = new SchedulePresenter(scheduleView);
        }

        return instance;
    }

    public void getSchedule(GregorianCalendar dateBegin,
                            GregorianCalendar dateEnd,
                            Activity currentActivity) {
        new GetSchedule(dateBegin, dateEnd, currentActivity).execute();
    }

    public class GetSchedule extends AsyncTask<String, Void, String> {
        private static final String USERNAME = "WebServices";
        private static final String PASSWORD = "12345";

        private GregorianCalendar dateBegin;
        private GregorianCalendar dateEnd;
        private Activity currentActivity;

        public GetSchedule(GregorianCalendar dateBegin,
                           GregorianCalendar dateEnd,
                           Activity currentActivity) {
            this.dateBegin = dateBegin;
            this.dateEnd = dateEnd;
            this.currentActivity = currentActivity;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) {
                return;
            }
            SoapScheduleParser parser = new SoapScheduleParser();
            Schedule schedule = null;

            try {
                schedule = parser.parse(s);
            } catch (ParseException e) {

            }

            if (schedule == null) {
                return;
            }
            scheduleView.showSchedule(schedule);
        }

        static final String PATH = "http://91.196.247.150:8080/1c/ws/study.1cws";
        static final String NAMESPACE = "http://sgu-infocom.ru/study";
        static final String METHOD_NAME = "GetSchedule";
        static final String SOAP_ACTION = "http://sgu-infocom.ru/study#WebStudy:GetSchedule";

        @Override
        protected String doInBackground(String... params) {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            new MarshalDate().register(envelope);

            SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);

            SharedPreferences spref = currentActivity.getPreferences(Context.MODE_PRIVATE);
            String scheduleObjectId = spref.getString("schedule_object_id", null);
            soapObject.addProperty("ScheduleObjectType", "AcademicGroup");
            soapObject.addProperty("ScheduleObjectId", scheduleObjectId);
            soapObject.addProperty("ScheduleType", "Week");
            soapObject.addProperty("DateBegin", dateBegin.getTime());
            soapObject.addProperty("DateEnd", dateEnd.getTime());
            soapObject.addProperty("UserRef", null);
            soapObject.addProperty("RecordbookRef", null);

            envelope.setOutputSoapObject(soapObject);

            HttpTransportBasicAuthSE httpTransportSE = new HttpTransportBasicAuthSE(PATH, USERNAME, PASSWORD);

            String res = null;

            try {
                httpTransportSE.call(SOAP_ACTION, envelope);
                SoapObject soap = (SoapObject)envelope.getResponse();
                res = soap.toString();
                System.out.println(res);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return res;
        }
    }
}
