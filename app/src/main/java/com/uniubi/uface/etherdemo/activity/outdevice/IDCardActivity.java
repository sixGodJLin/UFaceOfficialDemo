package com.uniubi.uface.etherdemo.activity.outdevice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uniubi.uface.ether.core.cvhandle.FaceHandler;
import com.uniubi.uface.ether.outdevice.bean.CardInfo;
import com.uniubi.uface.ether.outdevice.utils.IDSerialPortReader;
import com.uniubi.uface.ether.outdevice.utils.IDUsbReader;
import com.uniubi.uface.etherdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IDCardActivity extends AppCompatActivity {

    FaceHandler faceHandler;
    @BindView(R.id.btn_serialport)
    Button btn_serialport;
    @BindView(R.id.btn_zkt)
    Button btn_zkt;
    @BindView(R.id.btn_zkt_release)
    Button btn_zkt_release;
    @BindView(R.id.btn_back)
    Button btn_back;
    @BindView(R.id.btn_clear)
    Button btn_clear;
    @BindView(R.id.tv_info)
    TextView tv_info;
    @BindView(R.id.iv)
    ImageView iv;
    private static final String TAG = "IDCard";
    private Thread usbReadThread;
    private Thread serialPortReadThread;
    private CardInfo cardInfo;
    private boolean isReading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card);
        ButterKnife.bind(this);
        faceHandler = new FaceHandler();
        faceHandler.init();
    }

    @OnClick({R.id.btn_serialport, R.id.btn_zkt, R.id.btn_zkt_release, R.id.btn_back, R.id.tv_info, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_serialport:
                IDSerialPortReader.getInstance().open("/dev/ttyS1");
                serialPortReadThread = new Thread(new ReadIDSerialPortRunnable());
                serialPortReadThread.start();
                break;
            case R.id.btn_zkt:
                IDUsbReader.getInstance().open();
                usbReadThread = new Thread(new ReadIDUSBRunnable());
                usbReadThread.start();
                break;
            case R.id.btn_zkt_release:
                //中控等usb释放资源
                if (usbReadThread != null) {
                    usbReadThread.interrupt();
                }
                if (serialPortReadThread != null) {
                    serialPortReadThread.interrupt();
                }
                IDUsbReader.getInstance().close();
                IDSerialPortReader.getInstance().close();
                break;
            case R.id.iv:
                break;
            case R.id.btn_clear:
                //清除信息
                tv_info.setText("");
                iv.setImageBitmap(null);
                break;
            case R.id.btn_back:
                isReading = true;
                finish();
                IDUsbReader.getInstance().close();
                break;
            default:
                break;

        }
    }


    private class ReadIDUSBRunnable implements Runnable {

        @Override
        public void run() {
            while (true && !isReading) {
                cardInfo = IDUsbReader.getInstance().readIdCard();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cardInfo != null) {
                            tv_info.setText(cardInfo.toString());
                            iv.setImageBitmap(cardInfo.getPhoto());
                        }
                    }
                });
            }
        }
    }

    private class ReadIDSerialPortRunnable implements Runnable {

        @Override
        public void run() {
            while (true && !isReading) {
                cardInfo = IDSerialPortReader.getInstance().readIdCard();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cardInfo != null) {
                            tv_info.setText(cardInfo.toString());
                            iv.setImageBitmap(cardInfo.getPhoto());
                        }
                    }
                });
            }
        }
    }
}
