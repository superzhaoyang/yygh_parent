package com.superzhaoyang.yygh.hosp.service;

import com.superzhaoyang.yygh.model.hosp.Department;
import com.superzhaoyang.yygh.vo.hosp.DepartmentQueryVo;
import com.superzhaoyang.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);

    //分页查询
    Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    //删除科室接口
    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);
}
