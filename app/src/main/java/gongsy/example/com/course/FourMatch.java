package gongsy.example.com.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
 * Created by Gongsy on 2018/1/1.
 */

public class FourMatch extends AppCompatActivity implements View.OnClickListener{
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
    private ImageView mBackBtn;
    private int errcode=1;
    private Button sure;
    private String id="",loc="",id1="",vr1="",id2="",vr2="",id3="",vr3="";
    EditText stuid1,stuvr1,stuid2,stuvr2,stuid3,stuvr3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_match);
        Intent i=getIntent();
        id=i.getStringExtra("id");
        stuid1=(EditText)findViewById(R.id.sid1);
        stuvr1=(EditText)findViewById(R.id.svr1);
        stuid2=(EditText)findViewById(R.id.sid2);
        stuvr2=(EditText)findViewById(R.id.svr2);
        stuid3=(EditText)findViewById(R.id.sid3);
        stuvr3=(EditText)findViewById(R.id.svr3);
        sure=(Button)findViewById(R.id.sure3);
        sure.setOnClickListener(this);
        mBackBtn=(ImageView)findViewById(R.id.title_bak);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final String[] s={"5","13","14","9","8"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s);
        Spinner sp = (Spinner) findViewById(R.id.spinner3);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                loc=s[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
    public void onClick(View v) {
        if(v.getId()==R.id.sure3){
            id1=stuid1.getText().toString();
            vr1=stuvr1.getText().toString();
            id2=stuid2.getText().toString();
            vr2=stuvr2.getText().toString();
            id3=stuid3.getText().toString();
            vr3=stuvr3.getText().toString();
            new Thread(thd).start();
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int e= data.getInt("err");
            if(e==0){
                Intent in=new Intent(FourMatch.this,Success.class);
                in.putExtra("id",id);
                in.putExtra("loc",loc);
                in.putExtra("st","3");
                startActivity(in);
            }else{
                Toast.makeText(FourMatch.this, "未选择成功",Toast.LENGTH_LONG).show();
            }
        }
    };
    Runnable thd = new Runnable() {
        @Override
        public void run() {
            HttpsURLConnection connection = null;
            String lc5,lc13,lc14,lc9,lc8;
            try {
                String ad="https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
                String t="num=1&stuid="+id+"&stu1id="+id1+"&v1code="+vr1+"&stu1id="+id2+"&v1code="+vr2+"&stu1id="+id3+"&v1code="+vr3+"&buildingNo="+loc;
                URL url = new URL(ad);
                SSLContext context = null;
                context = SSLContext.getInstance("TLS");
                context.init(null, new TrustManager[]{myX509TrustManager}, null);
                HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", String.valueOf(t.getBytes().length));
                OutputStream outputStream=connection.getOutputStream();
                outputStream.write(t.getBytes(),0,t.getBytes().length);
                outputStream.close();
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                is.close();
                os.close();
                Log.d("errcode:",""+errcode);
                String result = new String(os.toByteArray());
                try{
                    JSONObject objects = new JSONObject(result);
                    for (int i = 0; i < objects.length(); i++) {
                        errcode=objects.getInt("errcode");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("errcode:",""+errcode);
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putInt("err",errcode);
                msg.setData(data);
                handler.sendMessage(msg);
            } catch (Exception e) {

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

        }
    };
}
