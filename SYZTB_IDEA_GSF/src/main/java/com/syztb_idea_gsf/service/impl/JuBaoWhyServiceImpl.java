package com.syztb_idea_gsf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.entity.JuBaoWhy;
import com.syztb_idea_gsf.mapper.JuBaoWhyMapper;
import com.syztb_idea_gsf.service.IJuBaoWhyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JuBaoWhyServiceImpl extends ServiceImpl<JuBaoWhyMapper, JuBaoWhy> implements IJuBaoWhyService {
    @Override
    public void setJuBaoWhy(String why) {
        JuBaoWhy juBaoWhy = new JuBaoWhy();
        juBaoWhy.setWhy(why);
        juBaoWhy.setZj(true); // 作假
        juBaoWhy.setBs(true); // 不实
        juBaoWhy.setWf(true); // 违反公平公正
        juBaoWhy.setOther(true);
        save(juBaoWhy);
    }

    @Override
    public void setJuBaoWhy(ArrayList<String> type, String why) {
        JuBaoWhy juBaoWhy = new JuBaoWhy();
        juBaoWhy.setWhy(why);
        for (String s : type) {
            switch (s) {
                case "公司相关信息作假" -> juBaoWhy.setZj(true);
                case "公司招标信息不实" -> juBaoWhy.setBs(true);
                case "公司招标违反公平公正原则" -> juBaoWhy.setWf(true);
                default -> juBaoWhy.setOther(true);
            }
        }
        save(juBaoWhy);
    }

    @Override
    public ArrayList<String> getWhy(String why) {
        List<JuBaoWhy> list = this.baseMapper.selectByMap(Map.of("why", why));
        JuBaoWhy juBaoWhy = list.get(0);
        ArrayList<String> arrayList = new ArrayList<>();
        if(juBaoWhy.isZj()){
            arrayList.add("公司相关信息作假");
        }
        if(juBaoWhy.isBs()){
            arrayList.add("公司招标信息不实");
        }
        if(juBaoWhy.isWf()){
            arrayList.add("公司招标违反公平公正原则");
        }
        return arrayList;
    }
}
