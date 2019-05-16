package com.uniubi.uface.etherdemo;

import android.app.Application;

import com.uniubi.andserver.EtherAndServerManager;
import com.uniubi.ether.Ether;
import com.uniubi.iot.DeviceStatusListener;
import com.uniubi.iot.EtherIotManager;
import com.uniubi.uface.ether.UfaceEtherOutDevice;
import com.uniubi.uface.ether.andserver.handler.AbstractEtherRequestHandler;
import com.uniubi.uface.ether.config.AlgorithmOptions;
import com.uniubi.uface.ether.config.AndServerOptions;
import com.uniubi.uface.ether.config.CommonOptions;
import com.uniubi.uface.ether.config.ServiceOptions;
import com.uniubi.uface.ether.config.configenum.algorithm.DataSourceFormat;
import com.uniubi.uface.ether.config.configenum.algorithm.FaceOrientation;
import com.uniubi.uface.ether.config.configenum.service.AliveLevel;
import com.uniubi.uface.ether.config.configenum.service.RecoMode;
import com.uniubi.uface.ether.config.configenum.service.RecoPattern;
import com.uniubi.uface.ether.config.configenum.service.ScenePhotoRecResult;
import com.uniubi.uface.ether.config.configenum.service.ScenePhotoType;
import com.uniubi.uface.ether.config.configenum.service.ScenePhotoWholeResult;
import com.uniubi.uface.ether.config.configenum.service.WorkMode;
import com.uniubi.uface.ether.core.cvhandle.FaceHandler;
import com.uniubi.uface.ether.utils.AppLog;
import com.uniubi.uface.etherdemo.serverhandle.ChangeRocModeHandler;
import com.uniubi.uface.etherdemo.serverhandle.FaceCreateHandler;
import com.uniubi.uface.etherdemo.serverhandle.FaceDeleteHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * version:
 * email: cfj950221@163.com
 *
 * @author caojun
 * @date 2018/8/7
 */

public class EtherApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AlgorithmOptions algorithmOptions = AlgorithmOptions.newBuilder()
                .withDataSourceFormat(DataSourceFormat.CV_PIX_FMT_NV21)
                .withDataSourceWidth(640)
                .withDataSourceHeight(480)
                .withFaceOrientation(FaceOrientation.CV_FACE_UP)
                .withFilterMaxFace(false)
                .withFilterFaceAngle(true)
                .withFilterOutScreen(true)
                .withFaceVerifyThreshold(62)
                .withMinFaceVerifyScore(57)
                .withFaceDetectThreadNum(2)
                .withMinFacePixel(96)
                .withMaxFaceNumber(-1)
                .withMaxAliveCount(6)
                .withAliveThreadNumber(1)
                .withFaceVerifyDistance(88)
                .withAliveMinFaceSize(88)
                .withVerifyStrangerCount(3)
                .withIsContinueVerify(false)
                .withOccupancyArea(0.4f)
                .withMaxAliveCount(10)
                .build();

        FaceCreateHandler faceCreateHandler = new FaceCreateHandler("/faceCreate");
        FaceDeleteHandler faceDeleteHandler = new FaceDeleteHandler("/faceDelete");
        ChangeRocModeHandler rocModeHandler = new ChangeRocModeHandler("/changeReco");
        List<AbstractEtherRequestHandler> handlers = new ArrayList<>();
        handlers.add(faceCreateHandler);
        handlers.add(faceDeleteHandler);
        handlers.add(rocModeHandler);

        ServiceOptions serviceOptions = new ServiceOptions.Builder()
                .withRecoMode(RecoMode.LOCALONLY)
                .withRecoPattern(RecoPattern.VERIFY)
                .withAliveLevel(AliveLevel.HARD)
                .withWorkMode(WorkMode.OFFLINE)
                .withScenePhotoRecResult(ScenePhotoRecResult.NON)
                .withScenePhotoWholeResult(ScenePhotoWholeResult.SUCCESS)
                .build();

        AndServerOptions andServerOptions = AndServerOptions.newBuilder()
                .withOfflineServerHandlers(handlers)
                .withOfflineServerPort(8090)
                .withOfflineServerTimeout(15)
                .withIsRegisterWebsite(true)
                .withIsExternalStorage(true)
                .withOfflineServerWebsitePath("").build();
//        CommonOptions commonOptions = new CommonOptions("***", "***", "***");

                CommonOptions commonOptions = new CommonOptions("84E0F42087C7607A", "F4D5EBBB99BF4C02A79CBF36725B6D45", "50642AE915144550AEE3F0140840BB05");

        Ether ether = new Ether.Builder(commonOptions)
                .withAlgorithmOptions(algorithmOptions)
                .withServiceOptions(serviceOptions)
                .withAndServerOptions(andServerOptions)
                .build();
        ether.init(this);

        //iot通道
//        EtherIotManager.getInstance().init(new DeviceStatusListener() {
//            @Override
//            public void deviceEnable() {
//
//            }
//
//            @Override
//            public void deviceDisable() {
//
//            }
//        });
        //启动andserver服务
        EtherAndServerManager.getInstance().startAndServer(this);

        AppLog.e("hwcode "+ FaceHandler.getHwCode());
    }


}
