package com.superzhaoyang.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.superzhaoyang.yygh.cmn.mapper.DictMapper;
import com.superzhaoyang.yygh.model.cmn.Dict;
import com.superzhaoyang.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;


public class DictListener extends AnalysisEventListener<DictEeVo> {
    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    //一行一行读取
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
