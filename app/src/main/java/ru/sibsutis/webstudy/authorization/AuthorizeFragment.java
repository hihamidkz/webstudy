package ru.sibsutis.webstudy.authorization;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import ru.sibsutis.webstudy.HttpTransportBasicAuthSE;
import ru.sibsutis.webstudy.R;
import ru.sibsutis.webstudy.database.UserDAOService;
import ru.sibsutis.webstudy.parsers.SoapRecordbooksParser;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AuthorizeFragment extends Fragment {

    protected Button signIn;
    protected EditText userName;
    protected EditText password;
    private SharedPreferences spref;

    public AuthorizeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorize, container, false);
        NavigationView navView = getActivity().findViewById(R.id.nav_view);
        View hView = navView.getHeaderView(0);
        TextView navUser = hView.findViewById(R.id.nav_name);

        signIn = view.findViewById(R.id.sign_in);
        userName = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);

        signIn.setOnClickListener((View v)-> {
            String username = userName.getText().toString();
            String passwd = null;
            try {
                passwd = SHA1(password.getText().toString()).toUpperCase();
            } catch (NoSuchAlgorithmException e) {
            }

            navUser.setText(username);

            User user = null;
            try {
                user = new UserDAOService().getUserByParams(username, passwd);
            } catch (SQLException e) {
            }
            String userId = user.getUserid();
            new GetRecordbooks(getActivity()).execute(userId);
            spref = getActivity().getPreferences(Context.MODE_PRIVATE);
            spref.edit().putString("userid", userId).commit();
            spref.edit().putString("username", username).commit();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return view;
    }

    private String SHA1(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] result = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    class GetRecordbooks extends AsyncTask<String, Void, String> {
        private final String NAMESPACE = "http://sgu-infocom.ru/study";
        private final String METHOD_NAME = "GetRecordbooks";
        private final String PATH = "http://91.196.247.150:8080/1c/ws/study.1cws";
        private final String SOAP_ACTION = "http://sgu-infocom.ru/study#WebStudy:GetRecordbooks";
        private final String USERNAME = "WebServices";
        private final String PASSWORD = "12345";

        private Activity activity;

        public GetRecordbooks(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onPostExecute(String s) {
            if (s == null) {
                return;
            }
            SoapRecordbooksParser parser = new SoapRecordbooksParser();
            Pair<String, String> recordbooks = parser.parse(s);
            SharedPreferences spref = activity.getPreferences(Context.MODE_PRIVATE);

            spref.edit().putString("schedule_object_id", recordbooks.first).commit();
            spref.edit().putString("recordbook_id", recordbooks.second).commit();
        }

        @Override
        protected String doInBackground(String... params) {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);

            String userId = params[0];

            soapObject.addProperty("UserId", userId);
            envelope.setOutputSoapObject(soapObject);

            HttpTransportBasicAuthSE httpTransportSE = new HttpTransportBasicAuthSE(PATH, USERNAME, PASSWORD);

            String res = "";

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
