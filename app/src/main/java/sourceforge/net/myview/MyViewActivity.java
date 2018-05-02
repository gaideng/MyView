package sourceforge.net.myview;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import sourceforge.net.myview.view.MyCircle;
import sourceforge.net.myview.viewgroup.GestureLockGroup;

public class MyViewActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private static final String TAG = "MyViewActivity";
    private ViewGroup viewGroup;
    private MyCircle myCircle1;
    private GestureLockGroup mGestureLockViewGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view);
        Log.i(TAG, "onCreate: 实例" + this);
        Log.i(TAG, "onCreate: ");
        viewGroup = (ViewGroup) findViewById(R.id.viewGroup);
        myCircle1 = (MyCircle) findViewById(R.id.myCircle1);
        mGestureLockViewGroup = (GestureLockGroup) findViewById(R.id.id_gestureLockViewGroup);
        mGestureLockViewGroup.setAnswer(new int[] { 1, 2, 3, 4,5 });
        mGestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockGroup.OnGestureLockViewListener()
                {

                    @Override
                    public void onUnmatchedExceedBoundary()
                    {
                        Toast.makeText(MyViewActivity.this, "错误5次...",
                                Toast.LENGTH_SHORT).show();
                        mGestureLockViewGroup.setUnMatchExceedBoundary(5);
                    }

                    @Override
                    public void onGestureEvent(boolean matched)
                    {
                        Toast.makeText(MyViewActivity.this, matched+"",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBlockSelected(int cId)
                    {
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        myCircle1.stopThread();

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG, "onSaveInstanceState: 2");
        outPersistentState.putInt("key",3333);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.i(TAG, "onCreate: 2");
        if (persistentState != null){
            Log.i(TAG, "onCreate: " + persistentState.getInt("key"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: 1");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        myCircle1.startThread();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(TAG, "getMeasuredWidth: " + viewGroup.getMeasuredWidth());
        Log.i(TAG, "getWidth: " + viewGroup.getWidth());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged: ");
    }

    public void onCall(View view) {
        ((TextView) view).setGravity(Gravity.CENTER);

//        call1((TextView) view);
//        call2((TextView) view);
//        call3();
        startActivity(new Intent(this,MainActivity.class));
//        EasyPermissions.requestPermissions(this,"用于拨打电话",100, Manifest.permission.CALL_PHONE);
    }

    private void call3() {
        Intent intent = new Intent("my.intent.action.Call");
//        intent.setType("text/user");
//        intent.setData(Uri.parse("dg:123"));
        intent.setDataAndType(Uri.parse("dg:123"),"text/user");
        startActivity(intent);
    }

    private void call2() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:10086"));
//        startActivity(intent);
    }

    private void call1(TextView view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + view.getText()));
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 100){
            if (perms.contains(Manifest.permission.CALL_PHONE)){
                call2();
                Log.i(TAG, "onPermissionsGranted: 获得权限");
            }
            Log.i(TAG, "onPermissionsGranted: 未获得权限");
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        StringBuffer sb = new StringBuffer();
        for (String str : perms){
            sb.append(str);
            sb.append("\n");
        }
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("好")
                    .setNegativeButton("不行")
                    .build()
                    .show();
        }
    }
}
