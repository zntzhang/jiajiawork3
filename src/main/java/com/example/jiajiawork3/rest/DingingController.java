package com.example.jiajiawork3.rest;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.example.jiajiawork3.consts.CommonConst;
import com.example.jiajiawork3.dao.AutoAnswerDao;
import com.example.jiajiawork3.dao.RainbowPiDao;
import com.example.jiajiawork3.domain.AutoAnswer;
import com.example.jiajiawork3.domain.RainbowPi;
import com.example.jiajiawork3.domain.RainbowPiResponse;
import com.example.jiajiawork3.domain.oa.OALateTimeDataResult;
import com.example.jiajiawork3.schedule.DingDingSchedule;
import com.example.jiajiawork3.utils.CacheUtils;
import com.example.jiajiawork3.utils.ChajiUtils;
import com.example.jiajiawork3.utils.DingTalkUtils;
import com.example.jiajiawork3.utils.StringUtils;
import com.example.jiajiawork3.utils.excel.CalculateWorkV2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Auther: zhangtao
 * @Date: 2022/4/23 14:37
 * @Description:
 */
@RestController
@RequestMapping(value = "/rest")
public class DingingController implements InitializingBean {
    @Resource
    private AutoAnswerDao answerDao;
    @Resource
    private RainbowPiDao rainbowPiDao;
    @Resource
    private ResourceLoader resourceLoader;

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public String importExcel(MultipartFile excelFile, Integer num, HttpServletResponse response) throws Exception {
        CalculateWorkV2.doExcel(excelFile.getInputStream(), num, response);
        return "success";
    }

    @RequestMapping(value = "/excel/template", method = RequestMethod.POST)
    public String importExcel(HttpServletResponse response) throws Exception {
        InputStream inputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            String filename = "example.xls";
            String path = "/home/zntzhang/excel/example.xls";
            org.springframework.core.io.Resource resource = resourceLoader.getResource(path);
            inputStream = resource.getInputStream();

            response.setContentType("application/vnd.ms-excel");
            response.addHeader("charset", "utf-8");
            response.addHeader("Pragma", "no-cache");
            response.setHeader("Content-Disposition", "attachment;filename=" + "test" + filename);
            servletOutputStream = response.getOutputStream();
            IoUtil.copy(inputStream, servletOutputStream);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            此处记得关闭输出Servlet流
            IoUtil.close(servletOutputStream);
            IoUtil.close(inputStream);
        }
        return "success";
    }

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
        } else if (content.contains("喝水了") || content.contains("喝了") || content.contains("放个屁")) {
            CacheUtils.set("flag", "false");
            String result = HttpUtil.get("http://api.tianapi.com/caihongpi/index?key=e713c19e916f111d29375e31f838880e");
            RainbowPiResponse rainbowPiResponse = JSON.parseObject(result, RainbowPiResponse.class);
            if (rainbowPiResponse != null && Objects.equals(rainbowPiResponse.getCode(), 200)) {
                value = rainbowPiResponse.getNewslist().get(0).getContent();
                RainbowPi rainbowPi = new RainbowPi();
                rainbowPi.setContent(value);
                rainbowPi.setCreated(new Date());
                rainbowPiDao.insert(rainbowPi);
            } else {
                value = "宝宝好乖，爱宝宝";
            }
        } else if (content.contains("没喝水")) {
            CacheUtils.set("flag", "true");
            value = "好的主人，我继续问";
        } else if (content.contains("早安")) {
            value = DingDingSchedule.getZaoAnContent();
        } else if (content.contains("迟到")) {
            value = late(content);
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

    public String late(String content) {
        String cookie = StringUtils.subStringEnd(content, "|");
        System.out.println(cookie);
        String format = DateUtil.format(DateUtil.date(), "yyyy-MM");
        String json = String.format("{\"url\":\"/attendance/detail-new\",\"method\":\"HTTP_POST\",\"paramMap\":{\"date\":\"%s\",\"userId\":3330}}", format);
        String result = HttpRequest.post("http://oaplus.raycloud.com/oldAPI/reqOld")
                .body(json)
                .header("Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzMzMwIn0.p8SbXmR5IBYWNL69i1Xu_KQND17rd9F7uJH1j3OX-jg")
                .cookie("JSESSIONID=" + (org.springframework.util.StringUtils.isEmpty(cookie) ? "289887390E4F6C97020E22931EB0BDCB" : cookie))
                .execute().body();
        System.out.println(result);
        JSONObject jsonObject = JSON.parseObject(result);
        Integer status = jsonObject.getInteger("status");
        if (!Objects.equals(status, 200)) {
            return jsonObject.getString("data");
        }
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject result1 = data.getJSONObject("result");
        JSONArray data1 = result1.getJSONArray("data");
        List<OALateTimeDataResult> oaLateTimeDataResults1 = data1.toJavaList(OALateTimeDataResult.class);
        int sum = 0;
        for (OALateTimeDataResult oaLateTimeDataResult : oaLateTimeDataResults1) {
            sum += oaLateTimeDataResult.getLateTime();
        }
        return format + "迟到" + sum + "分钟" + (sum > 100 ? "已超" : "未超" + "100分钟");
    }

    public String queryByQuestion(String content, String userId) {

        String result = HttpUtil.get(String.format("https://apis.tianapi.com/robot/index?key=e713c19e916f111d29375e31f838880e&question=%s", content.trim()));
        JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject != null && jsonObject.getInteger("code") == 200) {
            return jsonObject.getJSONObject("result").getString("reply");

        }
        return "么么哒";
    }


    /**
     * 每次发布后给宝宝提示发布了什么
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        DingTalkUtils.text(DingTalkUtils.get(), CommonConst.BAOBAO_ID, "海绵宝宝重启成功");

    }
}
