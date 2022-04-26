package com.example.jiajiawork3.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.example.jiajiawork3.dao.AutoAnswerDao;
import com.example.jiajiawork3.entity.AutoAnswer;
import com.example.jiajiawork3.utils.ChajiUtils;
import com.example.jiajiawork3.utils.StringUtils;
import com.taobao.api.ApiException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: zhangtao
 * @Date: 2022/4/23 14:37
 * @Description:
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/rest")
public class RestController {
    @Resource
    private AutoAnswerDao answerDao;

    @RequestMapping(value = "/robots", method = RequestMethod.POST)
    public String helloRobots(@RequestBody(required = false) JSONObject json) throws Exception {
        System.out.println(JSON.toJSONString(json));
        String content = json.getJSONObject("text").getString("content");
        String userId = json.get("senderStaffId").toString();
        String sessionWebhook = json.getString("sessionWebhook");

        System.out.println(content);
        String value;
        if (content.contains("找不同")) {
            value = ChajiUtils.chaji(content);
        } else if (content.contains("你要记住")) {
            String question = StringUtils.subString(content, "说", "的时候");
            String answer = StringUtils.subStringEnd(content, "回复");
            // 条件封装
            QueryWrapper<AutoAnswer> wrapper = new QueryWrapper<>();
            wrapper.eq("question", question);
            Integer count = answerDao.selectCount(wrapper);
            if (count == null || count == 0) {
                AutoAnswer autoAnswer = new AutoAnswer();
                autoAnswer.setQuestion(question);
                autoAnswer.setAnswer(answer);
                answerDao.insert(autoAnswer);
            } else {
                // 条件封装
                QueryWrapper<AutoAnswer> updateWrapper = new QueryWrapper<>();
                updateWrapper.eq("question", question);
                AutoAnswer autoAnswer = new AutoAnswer();
                autoAnswer.setAnswer(answer);
                answerDao.update(autoAnswer, updateWrapper);
            }
            value = "好的主人";
        } else {
            // 条件封装
            value = queryByQuestion(content, userId);
        }


        DingTalkClient client = new DefaultDingTalkClient(sessionWebhook);
        text(client, userId, value);
        return null;
    }

    public String queryByQuestion(String content, String userId) {
        String value;
        QueryWrapper<AutoAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question", content.trim());
        List<AutoAnswer> autoAnswers = answerDao.selectList(queryWrapper);
        String defaultAnswer = null;
        String specifyAnswer = null;
        for (AutoAnswer autoAnswer : autoAnswers) {
            if (userId.equals(autoAnswer.getQuestionId())) {
                specifyAnswer = autoAnswer.getAnswer();
                break;
            } else {
                defaultAnswer = autoAnswer.getAnswer();
            }
        }
        value = org.springframework.util.StringUtils.isEmpty(specifyAnswer) ? (org.springframework.util.StringUtils.isEmpty(defaultAnswer) ? "emmm智商不够用了" : defaultAnswer) : specifyAnswer;
        return value;
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
