package com.example.jiajiawork3.schedule;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.example.jiajiawork3.consts.CommonConst;
import com.example.jiajiawork3.domain.ZaoAnResponse;
import com.example.jiajiawork3.utils.CacheUtils;
import com.example.jiajiawork3.utils.DingTalkUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @program: jiajiawork3
 * @description:
 * @author: chunri
 * @create: 2022-05-18 16:39
 **/
@Component
public class DingDingSchedule {

    @PostConstruct
    private void init() {
        CacheUtils.set("flag", "true");
    }

    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void job() {
        if (Objects.equals(CacheUtils.get("flag"), "true") && timeCalendar()) {
            DingTalkUtils.text(DingTalkUtils.get(), CommonConst.BAOBAO_ID, "宝宝喝水了吗?");
        } else {
            try {
                Thread.sleep(1000 * 60 * 60 * 2);
                CacheUtils.set("flag", "true");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Scheduled(cron = "0 0 8 * * ? *")
    public void job2() {

        String result = HttpUtil.get("https://apis.tianapi.com/zaoan/index?key=e713c19e916f111d29375e31f838880e");
        ZaoAnResponse zaoAnResponse = JSON.parseObject(result, ZaoAnResponse.class);
        String content;
        if (zaoAnResponse != null && Objects.equals(zaoAnResponse.getCode(), 200)) {
            content = zaoAnResponse.getResult().getContent();
        } else {
            content = "宝宝好乖，爱宝宝";
        }
        DingTalkUtils.text(DingTalkUtils.get(), CommonConst.BAOBAO_ID, content);
    }

    //判断是否在规定的时间内签到 nowTime 当前时间 beginTime规定开始时间 endTime规定结束时间
    public static boolean timeCalendar() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        try {
            Date nowTime = df.parse(df.format(new Date()));

            //上午的规定时间
            Date amBeginTime = df.parse("8:00");
            Date amEndTime = df.parse("19:00");

            //设置当前时间
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);
            //设置开始时间
            Calendar amBegin = Calendar.getInstance();
            amBegin.setTime(amBeginTime);//上午开始时间
            //设置结束时间
            Calendar amEnd = Calendar.getInstance();
            amEnd.setTime(amEndTime);//上午结束时间

            LocalDate now = LocalDate.now();
            DayOfWeek dayOfWeek = now.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                // return false;
            }
            //处于开始时间之后，和结束时间之前的判断
            return date.after(amBegin) && date.before(amEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }


}
