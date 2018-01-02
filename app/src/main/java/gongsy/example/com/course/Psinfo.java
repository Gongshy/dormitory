package gongsy.example.com.course;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
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
 * Created by Gongsy on 2017/11/22.
 */

public class Psinfo extends AppCompatActivity {
    String id="";
    private ImageView mBackBtn;
    TextView stuid,stunm,stusx,stuvr,stulc,stugd;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_info);
        Intent intent=getIntent();
        id=intent.getStringExtra("extra");
        stuid=(TextView)findViewById(R.id.stuid);
        stunm=(TextView)findViewById(R.id.stunm);
        stusx=(TextView)findViewById(R.id.stusx);
        stuvr=(TextView)findViewById(R.id.stuvr);
        stulc=(TextView)findViewById(R.id.stulc);
        stugd=(TextView)findViewById(R.id.stugd);
        mBackBtn=(ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new Thread(thd).start();

    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String nm = data.getString("nm");
            String sx = data.getString("sx");
            String vr = data.getString("vr");
            String lc = data.getString("lc");
            String gd = data.getString("gd");
            stuid.setText(id);
            stunm.setText(nm);
            stusx.setText(sx);
            stuvr.setText(vr);
            stulc.setText(lc);
            stugd.setText(gd);
        }
    };
    Runnable thd=new Runnable() {
        @Override
        public void run() {

            final String address="https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid="+id;
            HttpURLConnection con=null;
            try{
                URL url=new URL(address);
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, new TrustManager[]{myX509TrustManager}, null);
                HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
                con=(HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(8000);
                con.setReadTimeout(8000);
                InputStream in=con.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = in.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                in.close();
                os.close();
                String responseStr = new String(os.toByteArray());
                json(responseStr);
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                if(con!=null){
                    Log.d("id","非空");
                    con.disconnect();
                }else{
                    Log.d("id","空");
                }
            }

        }
    };
    private void json(String responseStr){
        String nm="",sx="",vr="",lc="",gd="";
        int errcode=1;
        try{
            JSONObject objects = new JSONObject(responseStr);
            for (int i = 0; i < objects.length(); i++) {

                errcode=objects.getInt("errcode");
                String data=objects.getString("data") ;
                JSONObject object = new JSONObject(data);
                for (int j = 0; j < object.length(); j++){
                    nm=object.getString("name");
                    sx=object.getString("gender");
                    vr=object.getString("vcode");
                    lc=object.getString("location");
                    gd=object.getString("grade");

                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("nm", nm);
        data.putString("sx", sx);
        data.putString("vr", vr);
        data.putString("lc", lc);
        data.putString("gd", gd);
        msg.setData(data);
        handler.sendMessage(msg);
    }
    private void queryPsInfo(String studentId){
        final String address="https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?studentid="+studentId;
        Log.d("id",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                try{
                    URL url=new URL(address);
                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, new TrustManager[]{myX509TrustManager}, null);
                    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String arg0, SSLSession arg1) {
                            return true;
                        }
                    });
                    con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in=con.getInputStream();
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    int len = 0;
                    byte buffer[] = new byte[1024];
                    while ((len = in.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                    in.close();
                    os.close();

                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    if(con!=null){
                        Log.d("id","非空");
                        con.disconnect();
                    }else{
                        Log.d("id","空");
                    }
                }
            }
        }).start();
    }
}
