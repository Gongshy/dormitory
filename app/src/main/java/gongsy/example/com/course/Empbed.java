package gongsy.example.com.course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

public class Empbed extends AppCompatActivity {
    TextView bd5,bd13,bd14,bd9,bd8;
    private ImageView mBackBtn;
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
        setContentView(R.layout.emp_bed);
        bd5=(TextView)findViewById(R.id.bd5);
        bd13=(TextView)findViewById(R.id.bd13);
        bd14=(TextView)findViewById(R.id.bd14);
        bd9=(TextView)findViewById(R.id.bd9);
        bd8=(TextView)findViewById(R.id.bd8);
        mBackBtn=(ImageView)findViewById(R.id.title_bk);
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
            String b5 = data.getString("5");
            String b13 = data.getString("13");
            String b14 = data.getString("14");
            String b9 = data.getString("9");
            String b8 = data.getString("8");
            bd5.setText(b5);
            bd13.setText(b13);
            bd14.setText(b14);
            bd9.setText(b9);
            bd8.setText(b8);
        }
    };

    Runnable thd=new Runnable() {
        @Override
        public void run() {

            final String address="https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender=2";
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
        String bed5="",bed13="",bed14="",bed9="",bed8="";
        int errcode=1;
        try{
            JSONObject objects = new JSONObject(responseStr);
            for (int i = 0; i < objects.length(); i++) {

                errcode=objects.getInt("errcode");
                String data=objects.getString("data") ;
                JSONObject object = new JSONObject(data);
                for (int j = 0; j < object.length(); j++){
                    bed5=object.getString("5");
                    bed13=object.getString("13");
                    bed14=object.getString("14");
                    bed9=object.getString("9");
                    bed8=object.getString("8");

                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("5", bed5);
        data.putString("13", bed13);
        data.putString("14", bed14);
        data.putString("9", bed9);
        data.putString("8", bed8);
        msg.setData(data);
        handler.sendMessage(msg);
    }

}
