package samplepush2.androidtown.org.iotapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String HELLO = "MyIID";

    @Override
    public void onTokenRefresh() {
        Log.d(HELLO, "onTokenRefresh() 호출됨.");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(HELLO, "Refreshed Token : " + refreshedToken);

        sendRegistrationToServer(refreshedToken);

    }
    public void sendRegistrationToServer(String token) {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .build();

        Request request = new Request.Builder()
                .url("http://ec2-13-124-194-75.ap-northeast-2.compute.amazonaws.com:8080/project/postToken")    // 페이지 경로를 입력
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}