package sourceforge.net.myview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ThirdActivity extends BaseActivity {
    private static final String TAG = "ThirdActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Log.i(TAG, "onCreate: " + getTaskId());
    }

    public void onBtn(View view) {
        startActivity(new Intent(this,FourActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
