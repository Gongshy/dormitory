package gongsy.example.com.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

/**
 * Created by Gongsy on 2017/12/31.
 */

public class Success extends AppCompatActivity {
    TextView did,dloc,dst;
    String id="",loc="",st="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        loc=intent.getStringExtra("loc");
        st=intent.getStringExtra("st");
        did=(TextView)findViewById(R.id.did);
        dloc=(TextView)findViewById(R.id.dloc);
        dst=(TextView)findViewById(R.id.dst);
        new Thread(thd).start();
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String deid = data.getString("id");
            String deloc = data.getString("loc");
            String dest = data.getString("st");
            did.setText(deid);
            dloc.setText(deloc);
            dst.setText(dest);
        }
    };

    Runnable thd=new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("id", id);
            data.putString("loc", loc);
            data.putString("st", st);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
}
