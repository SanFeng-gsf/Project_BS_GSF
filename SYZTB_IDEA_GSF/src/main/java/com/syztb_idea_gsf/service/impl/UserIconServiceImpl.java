package com.syztb_idea_gsf.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syztb_idea_gsf.entity.IconFile;
import com.syztb_idea_gsf.mapper.IconFileMapper;
import com.syztb_idea_gsf.service.IUserIconService;
import com.syztb_idea_gsf.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class UserIconServiceImpl extends ServiceImpl<IconFileMapper, IconFile> implements IUserIconService {
    @Override
    public String saveIcon(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        long size = file.getSize();
        IconFile iconFile = new IconFile();
        iconFile.setName(filename);
        iconFile.setSize(size);

        String url;
        InputStream inputStream = file.getInputStream();
        String md5 = SecureUtil.md5(inputStream);
        IconFile iconFile1 = query().eq("md5", md5).one();
        if (iconFile1 != null) {
            url = iconFile1.getUrl();
        } else {
            File newFile = new File(SystemConstants.fileUserIconPath);
            if(!newFile.exists()) {
                // 判断文件是否存在
                // 不存在就创建
                newFile.mkdir();
            }
            String newFilePath = newFile.getAbsolutePath() + "/";
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            String finalFileName = uuid + ".jpg";
            File targetFile = new File(newFilePath + finalFileName);
            file.transferTo(targetFile);
            url = "/iconFile/" + finalFileName;
            iconFile.setUrl(url);
            iconFile.setMd5(md5);
            this.baseMapper.insert(iconFile);
        }
        return url;
    }

}
