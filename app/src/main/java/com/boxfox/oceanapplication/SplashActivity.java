package com.boxfox.oceanapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.boxfox.oceanapplication.login.data.UserData;
import com.boxfox.oceanapplication.login.facebook.FBLoginUtil;
import com.buffaloes.oceanapplication.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 200;
    private LoginButton login_facebook;
    private View login_facebook_fake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermission();
        FBLoginUtil.print_facebook_keyhash(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_splash);
        login_facebook_fake = findViewById(R.id.btn_splash_facebook_login_fake);
        login_facebook = findViewById(R.id.btn_splash_facebook_login);
        checkUserData();
        initLoginButton();
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.NFC) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                }
            } else {
                next();
            }
        } else {
            next();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                next();
            } else {
                AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
                ab.setMessage("펴미션을 허용하지 않으면 Schooler를 사용하실 수 없습니다!");
                ab.setPositiveButton("확인", null);
                ab.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                ab.show();
            }
        }
    }

    private void next() {
        startActivity(new Intent(this, CameraActivity.class));
    }

    private void checkUserData() {
        Realm.init(this);
        if (UserData.getDefaultUser() != null) {
            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void initLoginButton() {
        login_facebook_fake.setOnClickListener((v) -> login_facebook.performClick());
        FBLoginUtil.initButton(this, login_facebook, (result, data) -> {

        });
    }

}