package com.example.jiajiawork3.utils;

import com.dingtalk.api.DefaultDingTalkClient;
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

    public static DefaultDingTalkClient get() {
        return new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=cb379b401501a65a35fdddea861a0c93cc78a951c3d406f1dc7ae9402e46ca29");
    }

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
