package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.entity.IconFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUserIconService extends IService<IconFile> {
    String saveIcon(MultipartFile file) throws IOException;

}
