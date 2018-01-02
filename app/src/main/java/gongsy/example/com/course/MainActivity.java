package gongsy.example.com.course;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Intent intent=getIntent();
        id=intent.getStringExtra("extra_data");
        Button Ps_info=(Button)findViewById(R.id.psinfo);
        Ps_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,Psinfo.class);
                intent.putExtra("extra",id);
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
        Button Sel_dorm=(Button)findViewById(R.id.slt_dorm);
        Sel_dorm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,MatchSelect.class);
                intent.putExtra("extra",id);
                startActivity(intent);
            }
        });
    }
}
