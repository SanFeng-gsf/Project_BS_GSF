package com.syztb_idea_gsf.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.entity.MyFile;
import com.syztb_idea_gsf.mapper.FileMapper;
import com.syztb_idea_gsf.service.IFileService;
import com.syztb_idea_gsf.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, MyFile> implements IFileService {

    @Override
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); // 文件名
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1); // 文件后缀
        long size = file.getSize() / 1024; // 文件大小 (kb)
        MyFile myFile = new MyFile();
        myFile.setName(originalFilename);
        myFile.setType(type);
        myFile.setSize(size);

        String url;

        // 通过md5判断文件是否已经存在，防止在服务器存储相同文件
        InputStream inputStream = file.getInputStream();
        String md5 = SecureUtil.md5(inputStream);
        MyFile myFile1 = query().eq("md5", md5).one();
        if(myFile1 != null){
            // 存在一样的文件
            url = myFile1.getUrl();
        } else {
            // 不存在 保存文件
            File newFile = new File(SystemConstants.fileFolderPath);
            if(!newFile.exists()) {
                // 判断文件是否存在
                // 不存在就创建
                newFile.mkdir();
            }
            String newFilePath = newFile.getAbsoluteFile() + "/";
            // 随机生成文件名
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            String finalFileName = uuid + "." + type;
            File targetFile = new File(newFilePath + finalFileName);
            file.transferTo(targetFile);
            url = "/file/" + finalFileName;
            myFile.setUrl(url);
            myFile.setMd5(md5);
            this.baseMapper.insert(myFile);
        }
        return url;
    }

    @Override
    public ArrayList<String> getUrl(ArrayList<String> fileName) {
        MyFile file;
        ArrayList<String> arrayList = new ArrayList<>();
        for (String s : fileName) {
            file = query().eq("name", s).one();
            arrayList.add(file.getUrl());
        }
        return arrayList;
    }
}
