package gongsy.example.com.course;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by Gongsy on 2017/11/21.
 */

public class Login extends Activity{
    private static TrustManager myX509TrustManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    };
    private EditText studentid;
    private EditText password;
    private String id="";
    private String pd="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        Button login=(Button)findViewById(R.id.student_vf);
        studentid=(EditText) findViewById(R.id.studentid);
        password=(EditText)findViewById(R.id.studentpd);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestHttpURLCon();
            }
        });
    }
    private void sendRequestHttpURLCon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection connection = null;
                BufferedReader br = null;
                try {
                    id=studentid.getText().toString();
                    pd=password.getText().toString();
                    URL url = new URL("https://api.mysspku.com/index.php/V1/MobileCourse/Login?username=" + id + "&password=" + pd);
                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, new TrustManager[]{myX509TrustManager}, null);
                    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String arg0, SSLSession arg1) {
                            return true;
                        }
                    });
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    StringBuffer result = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    showResponse(parseJSON(result.toString()));

                } catch (Exception e) {

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private int parseJSON(String response) {
        int errcode = 1;
        try {
            JSONObject jsonObject = new JSONObject(response);
            errcode = jsonObject.getInt("errcode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errcode;
    }

    private void showResponse(final int response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response==0) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("extra_data",id);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "登录名或密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
