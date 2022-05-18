package com.example.jiajiawork3.utils;

import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;

import java.util.Collections;

/**
 * @program: jiajiawork3
 * @description:
 * @author: chunri
 * @create: 2022-05-18 19:16
 **/
public class DingTalkUtils {

    public static void text(DingTalkClient client, String userId, String str) {
        try {
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            System.out.println(userId);

            text.setContent(" @" + userId + "  \n  " +
                    "报告宝宝，" + str);
            request.setText(text);
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            System.out.println(userId);
            at.setAtUserIds(Collections.singletonList(
                    userId
            ));
            at.setIsAtAll(false);
            request.setAt(at);
            OapiRobotSendResponse response = client.execute(request);
            System.out.println(response.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
