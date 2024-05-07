package com.syztb_idea_gsf.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.JuBao;
import com.syztb_idea_gsf.entity.JuBaoSql;
import com.syztb_idea_gsf.mapper.JuBaoSqlMapper;
import com.syztb_idea_gsf.service.IJuBaoService;
import com.syztb_idea_gsf.service.IJuBaoWhyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JuBaoServiceImpl extends ServiceImpl<JuBaoSqlMapper, JuBaoSql> implements IJuBaoService {

    @Resource
    private IJuBaoWhyService iJuBaoWhyService;
    @Override
    public Result setJuBao(JuBao juBao, String why) {
        JuBaoSql juBaoSql = BeanUtil.copyProperties(juBao, JuBaoSql.class);
        juBaoSql.setWhy(why);

        ArrayList<String> fileName = juBao.getFileName();
        if(fileName.size()!=0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : fileName) {
                stringBuilder.append(s);
                stringBuilder.append("|||");
            }
            juBaoSql.setFileName(stringBuilder.toString());
        }
        save(juBaoSql);
        return Result.ok("success");
    }

    @Override
    public List<JuBao> getByMyName(String myName) {
        List<JuBaoSql> list = this.baseMapper.selectByMap(Map.of("my_name", myName));
        return get(list);
    }

    @Override
    public List<JuBao> getAll() {
        List<JuBaoSql> list = this.baseMapper.selectByMap(Map.of());
        return get(list);
    }

    @Override
    public List<JuBao> getByProjectName(String projectName) {
        List<JuBaoSql> list = this.baseMapper.selectByMap(Map.of("project_name",projectName));
        return get(list);
    }

    public List<JuBao> get(List<JuBaoSql> list) {
        ArrayList<String> fileNameList = new ArrayList<>(); // 文件名
        StringBuilder stringBuilder = new StringBuilder();
        List<JuBao> juBaoList = new ArrayList<>();
        String fileName = null;
        JuBao juBao = new JuBao();
        int index;
        for(int i=0;i<list.size();i++) {
            // JuBaoSql => JuBao
            juBao = BeanUtil.copyProperties(list.get(i), JuBao.class);
            if(list.get(i).getFileName() != null){
                fileName = list.get(i).getFileName();
                stringBuilder.append(fileName);
                index = stringBuilder.indexOf("|||");
                while (index != -1) {
                    fileNameList.add(stringBuilder.substring(0,index));
                    stringBuilder.delete(0,index + 3);
                    index = stringBuilder.indexOf("|||");
                }
            }
            juBao.setFileName(fileNameList);
            juBao.setType(iJuBaoWhyService.getWhy(list.get(i).getWhy())); // 举报原因
            juBaoList.add(i,juBao);
        }
        return juBaoList;
    }

    @Override
    public Result getByName(String name) {
        List<JuBaoSql> list = this.baseMapper.selectByMap(Map.of("name", name));
        JuBao juBao = new JuBao();
        List<JuBao> juBaoList = new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            juBao = BeanUtil.copyProperties(list.get(i), JuBao.class);
            juBao.setType(iJuBaoWhyService.getWhy(list.get(i).getWhy()));
            juBaoList.add(i,juBao);
        }
        return Result.ok("success",juBaoList);
    }

}
