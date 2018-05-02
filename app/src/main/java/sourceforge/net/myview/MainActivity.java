package sourceforge.net.myview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,EasyPermissions.PermissionCallbacks{
    Button btnScan;
    TextView tvResult;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Main" + this);
        btnScan = (Button) findViewById(R.id.btnScan);
        tvResult = (TextView) findViewById(R.id.tvResult);
        btnScan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnScan:
//                EasyPermissions.requestPermissions(this,"用于扫描二维码",100, android.Manifest.permission.CAMERA);
                startActivity(new Intent(this,MyViewActivity.class));
                break;
        }
    }

    private void scan() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        Collection<String> myList =  new ArrayList<>();
        myList.addAll(IntentIntegrator.QR_CODE_TYPES);
        myList.addAll(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setDesiredBarcodeFormats(myList);
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setPrompt("请扫描二维码"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);//是否保留扫码成功时候的截图
        integrator.initiateScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null && resultCode == RESULT_OK) {
            String result = scanResult.getContents();
            Log.e("HYN", result);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        switch (requestCode){
            case 100:
                scan();
                Toast.makeText(this, "已获取相机权限", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "已获取WRITE_EXTERNAL_STORAGE和WRITE_EXTERNAL_STORAGE权限", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//处理权限名字字符串
        StringBuffer sb = new StringBuffer();
        for (String str : perms){
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 2,sb.length(),"");

        switch (requestCode){
            case 100:
                Toast.makeText(this, "已拒绝权限" + perms.get(0), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "已拒绝WRITE_EXTERNAL_STORAGE和WRITE_EXTERNAL_STORAGE权限"+ perms.get(0), Toast.LENGTH_SHORT).show();
                break;
        }
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问" , Toast.LENGTH_SHORT).show();
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
