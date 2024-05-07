package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.DTO;
import com.syztb_idea_gsf.entity.TouB;

public interface ITouBService extends IService<TouB> {
    Result selectByName(String name);

    Result selectDetail(DTO dto);

    Result insert(TouB touB);

    Result delete(DTO dto);

    Result selectByN(DTO dto);

    Result setSuccessById(Integer id);
}
