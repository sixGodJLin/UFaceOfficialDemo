package com.uniubi.uface.etherdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.uniubi.uface.etherdemo.activity.AndServerTestActivity;
import com.uniubi.uface.etherdemo.activity.IotReceiveActivity;
import com.uniubi.uface.etherdemo.activity.core.CoreNaviActivity;
import com.uniubi.uface.etherdemo.activity.outdevice.OutDeviceActivity;


/**
 * @author pengchenggao
 */
public class TestActivity extends AppCompatActivity {

    private Button mBtnout,mBtnface,mBtnAnser,mBtnIot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mBtnface = findViewById(R.id.btn_face);
        mBtnout = findViewById(R.id.btn_out);
        mBtnAnser = findViewById(R.id.btn_andserver);
        mBtnIot = findViewById(R.id.btn_iot);
        initEvent();
    }

    private void initEvent() {

        mBtnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this,OutDeviceActivity.class);
                startActivity(intent);
            }
        });
        mBtnface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this,CoreNaviActivity.class);
                startActivity(intent);
            }
        });

        mBtnAnser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this,AndServerTestActivity.class);
                startActivity(intent);
            }
        });
        mBtnIot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("com.uniubi.update.screen");
                intent.putExtra("ort", 2);
                sendBroadcast(intent);

//                Intent intent = new Intent(TestActivity.this,IotReceiveActivity.class);
//                startActivity(intent);
            }
        });
    }


}
