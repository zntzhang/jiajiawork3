package com.example.jiajiawork3.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.example.jiajiawork3.dao.AutoAnswerDao;
import com.example.jiajiawork3.entity.AutoAnswer;
import com.example.jiajiawork3.utils.CacheUtils;
import com.example.jiajiawork3.utils.ChajiUtils;
import com.example.jiajiawork3.utils.DingTalkUtils;
import com.example.jiajiawork3.utils.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        } else if (content.contains("喝水了") || content.contains("喝了")) {
            CacheUtils.set("flag", "false");
            value = "宝宝好乖，爱宝宝";
        } else if (content.contains("没喝水")) {
            CacheUtils.set("flag", "true");
            value = "好的主人，我继续问";
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
        DingTalkUtils.text(client, userId, value);
        return null;
    }

    public String queryByQuestion(String content, String userId) {
        String value;
        QueryWrapper<AutoAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question", content.trim());
        List<AutoAnswer> autoAnswers = answerDao.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(autoAnswers)) {
            Set<String> all = answerDao.selectList(new QueryWrapper<>()).stream().map(AutoAnswer::getQuestion).collect(Collectors.toSet());
            String questionStr = "\r\n 找不同（接龙时使用）" +
                    "\r\n 喝了 " +
                    "\r\n 没喝水" +
                    "\r\n 你要记住"
                    + String.join("\r\n", all);
            value = "emmm智商不够用了,我会这些：" + questionStr;
        } else {
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
            value = !org.springframework.util.StringUtils.isEmpty(specifyAnswer) ? specifyAnswer : defaultAnswer;
        }
        return value;
    }


}
