package samplepush2.androidtown.org.iotapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.Map;

import samplepush2.androidtown.org.iotapplication.helper.BackHelper;
import samplepush2.androidtown.org.iotapplication.model.Person;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public BackHelper backHelper;
    private EditText editText1,editText2;
    private Button button;
    //
    static String strJson = "";
    static int result_code;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar= getSupportActionBar();

        editText1=(EditText)findViewById(R.id.editText1);
        actionBar.hide();
        editText2=(EditText)findViewById(R.id.editText2);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        //
        result_code=0;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                LoginActivity.HttpAsyncTask1 httpTask = new LoginActivity.HttpAsyncTask1(LoginActivity.this);
                httpTask.execute("http://13.124.194.75:8080/project/appLogin",
                        editText1.getText().toString(),editText2.getText().toString());
                break;
        }
    }

    //
    private class HttpAsyncTask1 extends AsyncTask<String, Void, String> {

        private LoginActivity loginAct;

        HttpAsyncTask1(LoginActivity loginActivity) {
            this.loginAct = loginActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            person = new Person();
            person.setID(urls[1]);
            person.setPass(urls[2]);


            return POST(urls[0], person);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            strJson = result;

            loginAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result_code = Integer.parseInt(strJson);
                    if(result_code == 100) {
                        Toast.makeText(getApplicationContext(),"로그인 되셨습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userID",editText1.getText().toString());
                        startActivity(intent);
                    }
                    if(result_code == 200) {
                        showAlertDialog();
                        //Toast.makeText(getApplicationContext(),"ID/PW를 확인해주세요.",Toast.LENGTH_LONG).show();
                    }


                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    //
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
            String user_Pass = (String) person.getPass();

            String link = url;
            String data = URLEncoder.encode("user_ID", "UTF-8") + "=" + URLEncoder.encode(user_ID, "UTF-8");
            data += "&" + URLEncoder.encode("user_Password", "UTF-8") + "=" + URLEncoder.encode(user_Pass, "UTF-8");


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

    public void showAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("경고");
        builder.setIcon(R.drawable.alert);
        builder.setMessage("아이디 와 비밀번호를 확인해주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        } );
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        backHelper.onBackPressed();
    }
}
