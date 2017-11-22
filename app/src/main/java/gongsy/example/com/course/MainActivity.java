package gongsy.example.com.course;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Button Log_in=(Button)findViewById(R.id.login);
        Log_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });
        Button Ps_info=(Button)findViewById(R.id.psinfo);
        Ps_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,Psinfo.class);
                startActivity(intent);
            }
        });
        Button Emp_bed=(Button)findViewById(R.id.emp_bd);
        Emp_bed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,Empbed.class);
                startActivity(intent);
            }
        });
    }
}
