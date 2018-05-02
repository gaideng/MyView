package sourceforge.net.myview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class FourActivity extends BaseActivity {
    private static final String TAG = "FourActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        Log.i(TAG, "onCreate: " + getTaskId());
    }

    public void onBtn(View view) {
//        startActivity(new Intent(this,FirstActivity.class));
        CollectorActivity.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
