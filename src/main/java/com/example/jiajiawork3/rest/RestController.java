package com.example.jiajiawork3.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.example.jiajiawork3.utils.ChajiUtils;
import com.taobao.api.ApiException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;

/**
 * @Auther: zhangtao
 * @Date: 2022/4/23 14:37
 * @Description:
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/rest")
public class RestController {

    @RequestMapping(value = "/robots", method = RequestMethod.POST)
    public String helloRobots(@RequestBody(required = false) JSONObject json) throws Exception {
        System.out.println(JSON.toJSONString(json));
        String content = json.getJSONObject("text").getString("content");
        String userId = json.get("senderStaffId").toString();
        String sessionWebhook = json.getString("sessionWebhook");

        System.out.println(content);
        String value = null;
        if (content.contains("找不同")) {
            value = ChajiUtils.chaji(content);
        } else if (content.contains("放个屁")) {
            value = "噗噗噗噗噗,嘿嘿嘿，羞死了";
        } else if (content.contains("谁更臭")) {
            value = "宝宝更臭";
        }


        DingTalkClient client = new DefaultDingTalkClient(sessionWebhook);
        text(client, userId, value);
        return null;
    }

    private void text(DingTalkClient client, String userId, String str) {
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
