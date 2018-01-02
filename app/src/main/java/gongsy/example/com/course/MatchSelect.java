package gongsy.example.com.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Gongsy on 2017/12/31.
 */

public class MatchSelect extends AppCompatActivity {
    String id;
    ListView list;
    private ImageView mBackBtn;
    private String[] data={"单人","两人","三人","四人"};
    private ArrayAdapter<String> adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_select);
        Intent intent=getIntent();
        id=intent.getStringExtra("extra");
        list=(ListView)findViewById(R.id.match_list);
        adapter=new ArrayAdapter<String>(MatchSelect.this,android.R.layout.simple_list_item_1,data);
        mBackBtn=(ImageView)findViewById(R.id.title_bak);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {
                int k=position;
                if(k==0){
                    Intent i=new Intent(MatchSelect.this,SingleMatch.class);
                    i.putExtra("ex",id);
                    startActivity(i);
                }
                if(k==1){Intent i=new Intent(MatchSelect.this,DoubleMatch.class);
                    i.putExtra("id",id);
                    startActivity(i);
                }
                if(k==2){Intent i=new Intent(MatchSelect.this,ThreeMatch.class);
                    i.putExtra("id",id);
                    startActivity(i);
                }
                if(k==3){Intent i=new Intent(MatchSelect.this,FourMatch.class);
                    i.putExtra("id",id);
                    startActivity(i);
                }


            }
        });
    }
}
