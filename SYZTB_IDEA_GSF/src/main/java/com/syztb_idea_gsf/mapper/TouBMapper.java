package com.syztb_idea_gsf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syztb_idea_gsf.entity.TouB;
import com.syztb_idea_gsf.entity.ZhaoB;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TouBMapper extends BaseMapper<TouB> {

    TouB selectByNameAndSNameAndProjectName(String name, String suoName,String projectName);

    boolean setSuccessById(Integer id);
}
