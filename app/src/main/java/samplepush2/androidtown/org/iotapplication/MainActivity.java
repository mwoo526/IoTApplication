package samplepush2.androidtown.org.iotapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import samplepush2.androidtown.org.iotapplication.adapter.TrashAdapter;
import samplepush2.androidtown.org.iotapplication.helper.BackHelper;
import samplepush2.androidtown.org.iotapplication.model.Person;
import samplepush2.androidtown.org.iotapplication.model.Trash;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener,CompoundButton.OnCheckedChangeListener{

    BackHelper backHelper;
    List<Trash> list;
    TrashAdapter adapter;

    final static String TAG = "AndroidNodeJS";

    private ListView listView;
    private ImageButton imageButton;
    private ToggleButton toggle;
    JSONObject jObject;
    String led_Color, trash_Amount, artik_ID, trash_Location;
    String checkId;
    private String ip="http://13.124.194.75:8080/project";
    static String strJson = "";
    static int result_code=0;
    Person person;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        checkId=intent.getStringExtra("userID");
        list=new ArrayList<Trash>();
        adapter=new TrashAdapter(this,R.layout.list_item,list);
        listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        imageButton=(ImageButton)findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);

        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setBackgroundResource(R.drawable.togglebutton2);
        toggle.setOnCheckedChangeListener(this);

        backHelper=new BackHelper(this);


        String regId = FirebaseInstanceId.getInstance().getToken();

        //MainActivity.HttpAsyncTask1 httpTask = new MainActivity.HttpAsyncTask1(this);
        // FCM 등록 ID 전송 과정(user_ID와 함께)
        //httpTask.execute("http://13.124.194.75:8080/project/postToken",checkId,regId);

    }

    public void addData(){
        // Request Json 요청 코드
        StringRequest request = new StringRequest(
                Request.Method.GET, ip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        transformJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
       //adapter.add(new Trash(R.drawable.red,"우촌관 3층","90%"));
        //adapter.add(new Trash(R.drawable.green,"상상관 2층","30%"));
    }


    public void transformJson(String data) {
        try {
            JSONArray jarray = new JSONArray(data);
            for (int i = 0; i < jarray.length(); i++) {
                jObject = jarray.getJSONObject(i);
                led_Color=jObject.getString("led_id");
                trash_Amount=jObject.getString("trash_Amount");
                artik_ID=jObject.getString("artik_ID");
                trash_Location=jObject.getString("trash_Location");

                Log.d("Hello",led_Color + trash_Amount + artik_ID + trash_Location );
                int trash = Integer.parseInt(trash_Amount);
                int total=3000;
                if(led_Color.equals("1")&&trash<1800){
                    trash=trash/total*100;
                    adapter.add(new Trash(R.drawable.green,trash_Location, trash+"%"));
                }
                else if(led_Color.equals("2")&&trash<2650){
                    trash=trash/total*100;
                    adapter.add(new Trash(R.drawable.yellow,trash_Location, trash+"%"));
                }
                else if(led_Color.equals("3")){
                    trash=trash/total*100;
                    adapter.add(new Trash(R.drawable.red,trash_Location, trash+"%"));
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButton:
                Intent intent= new Intent(getApplicationContext(),HansungActivity.class);
                startActivity(intent);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.listView:
                Trash item = adapter.getItem(i);
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra("image",item.getImage());
                intent.putExtra("location",item.getLocation());
                intent.putExtra("amount",item.getAmount());
                startActivity(intent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        // The toggle is enabled
        if(isChecked){
            buttonView.setBackgroundResource(R.drawable.togglebutton);
           MainActivity.HttpAsyncTask1 httpTask = new MainActivity.HttpAsyncTask1(this);
            httpTask.execute("http://13.124.194.75:8080/project/postApp",checkId);
        }
        // The toggle is disabled
        else{
            buttonView.setBackgroundResource(R.drawable.togglebutton2);
            adapter.clear();
        }
    }

    private class HttpAsyncTask1 extends AsyncTask<String, Void, String> {

        private MainActivity mainAct;

        HttpAsyncTask1(MainActivity mainActivity) {
            this.mainAct = mainActivity;
        }

        //앱에서 서버로 데이터 전송
        @Override
        protected String doInBackground(String... urls) {

            person = new Person();
            person.setID(urls[1]);
           // person.setToken(urls[2]);
            Log.d("Hello",urls[1]);


            return POST(urls[0], person);
        }
        // onPostExecute displays the results of the AsyncTask.
        //서버로부터 온 결과값을 처리
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            strJson = result;
            Log.d("Hello",strJson);

            mainAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String[] data = strJson.split("\\}");
                    for(int i=0;i<data.length-1;i++) {
                        Log.d("Hello",data[i]);
                        String[] json = data[i].split("&#034;");
                        Log.d("Hello", json[3] + " / " + json[7] + " / " + json[11] + " / " + json[15]);

                        artik_ID=json[3];
                        trash_Location= json[7];
                        led_Color= json[11];
                        trash_Amount=json[15];

                        Log.d("Hello",led_Color + trash_Amount + artik_ID + trash_Location );
                        double trash = Double.parseDouble(trash_Amount);
                        double total=3000;
                        if(led_Color.equals("1")&&trash<1800){
                            trash=trash/total*100;
                            String stringTrash=String.format("%.1f",trash);
                            adapter.add(new Trash(R.drawable.green,trash_Location, stringTrash+"%"));
                        }
                        else if(led_Color.equals("2")&&trash<2650){
                            trash=trash/total*100;
                            String stringTrash=String.format("%.1f",trash);
                            adapter.add(new Trash(R.drawable.yellow,trash_Location, stringTrash+"%"));
                        }
                        else if(led_Color.equals("3")){
                            trash=trash/total*100;
                            String stringTrash=String.format("%.1f",trash);
                            adapter.add(new Trash(R.drawable.red,trash_Location, stringTrash+"%"));
                        }
                    }
                   // transformJson(result);
                   /* if(strJson.equals("100")) {
                        Toast.makeText(getApplicationContext(),"토큰을 받았습니다.",Toast.LENGTH_SHORT).show();

                    }
                    if(strJson.equals("200")) {

                        Toast.makeText(getApplicationContext(),"토큰을 전송받지못했습니다.",Toast.LENGTH_LONG).show();
                    }


                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
            });
        }
    }

    // POST방식으로 데이터 전송
    public static String POST(String url, Person person) {
        InputStream is = null;
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) urlCon.openConnection();

            String json = "";


            httpCon.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            httpCon.setDoInput(true);
            httpCon.setDoOutput(true);
            httpCon.setUseCaches(false);
            httpCon.setRequestMethod("POST");
            httpCon.setAllowUserInteraction(true);

            // Http Header Setting

            String user_ID = (String) person.getID();
            String user_token = (String) person.getToken();

            String link = url;
            String data = URLEncoder.encode("user_ID", "UTF-8") + "=" + URLEncoder.encode(user_ID, "UTF-8");
            //data += "&" + URLEncoder.encode("user_Token", "UTF-8") + "=" + URLEncoder.encode(user_token, "UTF-8");

            Log.d("Hello",data);

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(httpCon.getOutputStream(), "UTF-8"));
            pw.write(data);
            pw.flush();
            // receive response as inputStream
            try {
                is = httpCon.getInputStream();
                // convert inputstream to string
                if (is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpCon.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    //
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;

    }


    @Override
    public void onBackPressed() {
        backHelper.onBackPressed();
    }

}
