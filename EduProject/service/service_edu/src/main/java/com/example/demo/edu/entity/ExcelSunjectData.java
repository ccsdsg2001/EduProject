package com.example.demo.edu.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author cc
 * @date 2023年01月21日 23:22
 */
@Data
public class ExcelSunjectData {
    @ExcelProperty(index = 0)
    private String oneSubjectName;
    @ExcelProperty(index = 1)
    private String twoSubjectName;
}
