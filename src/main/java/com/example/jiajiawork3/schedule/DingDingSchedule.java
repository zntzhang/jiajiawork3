package com.example.jiajiawork3.schedule;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.example.jiajiawork3.utils.CacheUtils;
import com.example.jiajiawork3.utils.DingTalkUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private static String BAOBAO_ID = "3458263417750496";

    @PostConstruct
    private void init() {
        CacheUtils.set("flag", "true");
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void job() {
        if (Objects.equals(CacheUtils.get("flag"), "true") && timeCalendar()) {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=cb379b401501a65a35fdddea861a0c93cc78a951c3d406f1dc7ae9402e46ca29");
            DingTalkUtils.text(client, BAOBAO_ID, "宝宝喝水了吗?");
        } else {
            try {
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    //判断是否在规定的时间内签到 nowTime 当前时间 beginTime规定开始时间 endTime规定结束时间
    public static boolean timeCalendar() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        try {
            Date nowTime = df.parse(df.format(new Date()));

            //上午的规定时间
            Date amBeginTime = df.parse("7:00");
            Date amEndTime = df.parse("23:00");

            //设置当前时间
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);
            //设置开始时间
            Calendar amBegin = Calendar.getInstance();
            amBegin.setTime(amBeginTime);//上午开始时间
            //设置结束时间
            Calendar amEnd = Calendar.getInstance();
            amEnd.setTime(amEndTime);//上午结束时间
            //处于开始时间之后，和结束时间之前的判断
            return date.after(amBegin) && date.before(amEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }


}
