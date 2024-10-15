package com.example.jiajiawork3.dao;

import com.example.jiajiawork3.domain.excel.ExcelStudent;
import com.example.jiajiawork3.domain.excel.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author chunri
 * @ClassName StudentScoreDao.java
 * @Description TODO
 * @createTime 2025年02月26日 19:14:00
 */
public interface StudentScoreDao extends ElasticsearchRepository<ExcelStudent, String> {
}
