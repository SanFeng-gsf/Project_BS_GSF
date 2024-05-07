package com.syztb_idea_gsf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syztb_idea_gsf.entity.MyFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<MyFile> {
}
