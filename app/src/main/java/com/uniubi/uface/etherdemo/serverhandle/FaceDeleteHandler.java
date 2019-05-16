package com.uniubi.uface.etherdemo.serverhandle;

import android.text.TextUtils;

import com.uniubi.uface.ether.andserver.handler.AbstractEtherRequestHandler;
import com.uniubi.uface.ether.core.EtherFaceManager;
import com.uniubi.uface.ether.db.impl.OfflineFaceInfoImpl;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;

import java.io.IOException;
import java.util.Map;

/**
 * description:
 * version:
 * email: cfj950221@163.com
 *
 * @author caojun
 * @date 2018/8/15
 */

public class FaceDeleteHandler extends AbstractEtherRequestHandler {

    public FaceDeleteHandler(String url) {
        super(url);
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parseParams(request);
        if (!params.containsKey("faceId")||!params.containsKey("personId")) {
            response(response, "Please fill in whole params");
            return;
        }
        String faceId = params.get("faceId");
        String personId = params.get("personId");
        if (TextUtils.isEmpty(faceId)) {
            return;
        }
        boolean isDelete = OfflineFaceInfoImpl.getFaceInfoImpl().deleteByFaceId(faceId);
        if (isDelete){
            EtherFaceManager.getInstance().removeFeatureFromLib(personId,faceId);
            response(response,"删除人脸成功");
        }else {
            response(response,"删除人脸失败");
        }

    }

    private void response(HttpResponse response, String info) {
        StringEntity stringEntity = new StringEntity(info, "utf-8");
        response.setStatusCode(200);
        response.setEntity(stringEntity);
    }
}
