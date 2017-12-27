package samplepush2.androidtown.org.iotapplication.helper;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by MinWoo on 2017-11-05.
 */

public class BackHelper {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackHelper(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            // 앱을 완전히 종료하기 위해선 밑에 3줄의 코드를 다 써야한다.
            activity.moveTaskToBack(true);
            activity.finish();
            android.os.Process.killProcess(android.os.Process.myPid());

            toast.cancel();

        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                "\'뒤로\'버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
