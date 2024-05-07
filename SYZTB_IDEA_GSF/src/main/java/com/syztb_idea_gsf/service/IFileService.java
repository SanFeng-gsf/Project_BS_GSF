package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.entity.MyFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

public interface IFileService extends IService<MyFile> {
    String upload(MultipartFile file) throws IOException;

    ArrayList<String> getUrl(ArrayList<String> fileName);
}
