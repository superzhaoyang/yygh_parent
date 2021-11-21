package com.superzhaoyang.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.superzhaoyang.yygh.common.result.Result;
import com.superzhaoyang.yygh.hosp.service.HospitalSetService;
import com.superzhaoyang.yygh.model.hosp.HospitalSet;
import com.superzhaoyang.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.util.List;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;

    // 1 查询医院设置表的所有信息
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        Result<List<HospitalSet>> ok = Result.ok(list);
        return ok; //jackson进行转换
    }

    // 2 删除医院设置
    @ApiOperation(value = "逻辑删除医院设置信息")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 3 条件查询带分页
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(
            @PathVariable long current,
            @PathVariable long limit,
            @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        String hosName = hospitalSetQueryVo.getHosname();
        String hosCode = hospitalSetQueryVo.getHoscode();
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(hosName)) {
            wrapper.like("hosname", hosName);
        }

        if (!StringUtils.isEmpty(hosCode)) {
            wrapper.like("hoscode", hosCode);
        }

        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);

        //返回结果
        return Result.ok(pageHospitalSet);
    }
    // 4 添加医院设置

    // 5 根据id获取医院设置

    // 6 修改医院设置

    // 7 批量删除医院设置


}
