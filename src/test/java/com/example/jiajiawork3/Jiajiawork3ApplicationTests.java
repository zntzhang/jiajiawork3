package com.example.jiajiawork3;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.jiajiawork3.dao.AutoAnswerDao;
import com.example.jiajiawork3.domain.AutoAnswer;
import com.example.jiajiawork3.rest.DingingController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class Jiajiawork3ApplicationTests {


    @Autowired
    AutoAnswerDao answerDao;
    @Resource
    DingingController dingingController;

    @Test
    public void add() {
        AutoAnswer autoAnswer = new AutoAnswer();
        autoAnswer.setQuestion("放个屁");
        autoAnswer.setAnswer("噗噗噗噗噗,嘿嘿嘿，羞死了");
        answerDao.insert(autoAnswer);
    }

    @Test
    public void update() {
        AutoAnswer autoAnswer = new AutoAnswer();
        autoAnswer.setId(26L);
        autoAnswer.setAnswer("放个屁");
        autoAnswer.setQuestion("噗噗噗噗噗,嘿嘿嘿，羞死了");
        answerDao.updateById(autoAnswer);
    }

    @Test
    public void delete() {
        answerDao.deleteById(24L);
    }

    @Test
    public void selectById() {
        AutoAnswer autoAnswer = answerDao.selectById(1L);
        System.out.println(autoAnswer);
    }

    @Test
    public void selectByWrapper() {
        // 条件封装
        QueryWrapper<AutoAnswer> wrapper = new QueryWrapper<>();
        wrapper.eq("id", 1L);
        List<AutoAnswer> autoAnswers = answerDao.selectList(wrapper);
        System.out.println(autoAnswers);
    }

    @Test
    public void selectByWrapper2() {

        System.out.println(dingingController.queryByQuestion("谁最帅","11"));
    }


    @Test
    public void page() {
        Page<AutoAnswer> page = new Page<>(1, 2);
        IPage<AutoAnswer> autoAnswerIPage = answerDao.selectPage(page, null);
        System.out.println(autoAnswerIPage);
    }


}
