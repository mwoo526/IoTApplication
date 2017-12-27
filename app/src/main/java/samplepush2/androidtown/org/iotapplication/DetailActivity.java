package samplepush2.androidtown.org.iotapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView1,textView2,textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("IoTrash");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        int image =intent.getIntExtra("image",0);
        String location =intent.getStringExtra("location");
        String amount =intent.getStringExtra("amount");

        imageView=(ImageView)findViewById(R.id.imageView);
        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);
        textView3=(TextView)findViewById(R.id.textView3);


        imageView.setImageResource(image);
        textView1.setText(location);
        textView2.setText(amount);
        if(image==R.drawable.green)
            textView3.setText("양호합니다 . 비우시지 않으셔도 되요^^");
        if(image==R.drawable.yellow)
            textView3.setText("쓰레기가 조금 차여있네요. 하지만 괜찮아요 **");
        if(image==R.drawable.red)
            textView3.setText("어이쿠 꽉차있네요. 비우셔야 할꺼 같에요!!");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
