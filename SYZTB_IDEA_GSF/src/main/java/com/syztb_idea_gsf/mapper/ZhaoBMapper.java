package com.syztb_idea_gsf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syztb_idea_gsf.entity.ZhaoB;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ZhaoBMapper extends BaseMapper<ZhaoB> {

    List<ZhaoB> selectByName(String name);

    List<ZhaoB> selectAll();
}
