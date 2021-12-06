package com.superzhaoyang.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.superzhaoyang.yygh.hosp.repository.DepartmentRepository;
import com.superzhaoyang.yygh.hosp.service.DepartmentService;
import com.superzhaoyang.yygh.model.hosp.Department;
import com.superzhaoyang.yygh.vo.hosp.DepartmentQueryVo;
import com.superzhaoyang.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // 上传科室接口
    @Override
    public void save(Map<String, Object> paramMap) {
        // paramMap 转换成department对象
        String paramMapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramMapString, Department.class);

        Department departmentExist = departmentRepository.
                getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());

        // 根据医院编号和科室编号查询
        if (departmentExist != null) {
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }

    }

    @Override
    public Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Department> example = Example.of(department, matcher);
        Page<Department> pages = departmentRepository.findAll(example, pageable);
        return pages;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        // 根据医院编号和科室编号查询
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        // 创建list集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();
        // 根据医院编号，查询医院所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        // 所有科室列表信息 departmentList
        List<Department> departmentList = departmentRepository.findAll(example);
        Map<String, List<Department>> departmentMap = departmentList.stream().
                collect(Collectors.groupingBy(Department::getBigcode));

        // 遍历map集合 departmentMap
        for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            // 大科室编号
            String bigCode = entry.getKey();
            //
            List<Department> departmentLists = entry.getValue();

            // 封装大科室
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(departmentLists.get(0).getBigname());
            // 封装小科室

            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : departmentLists) {
                DepartmentVo departmentVo2 = new DepartmentVo();
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
                // 封装到list集合
                children.add(departmentVo2);
            }

            //把小科室list集合封装到大科室children里面
            departmentVo.setChildren(children);
            result.add(departmentVo);
        }

        return result;

    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }

        return null;
    }


}
