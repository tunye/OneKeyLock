package com.buaa.ct.lockscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 10202 on 2016/4/11.
 */

public class MainActivity extends Activity {

    private static final int MY_REQUEST_CODE = 9999;
    private DevicePolicyManager policyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取设备管理服务
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, LockReceiver.class);

        //判断是否有锁屏权限，若有则立即锁屏并结束自己，若没有则获取权限
        if (policyManager.isAdminActive(componentName)) {
            policyManager.lockNow();
            finish();
        } else {
            activeManage();
        }
        setContentView(R.layout.main); //把这句放在最后，这样锁屏的时候就不会跳出来（闪一下）
    }

    //获取权限
    private void activeManage() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活后才能使用锁屏功能");
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //获取权限成功，立即锁屏并finish自己，否则继续获取权限
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            policyManager.lockNow();
            finish();
        } else {
            activeManage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

