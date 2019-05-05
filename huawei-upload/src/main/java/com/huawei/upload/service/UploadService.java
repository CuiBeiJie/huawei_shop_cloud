package com.huawei.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: cuibeijie
 * @Date: 2019/5/3 23:33
 * @Description:
 */
public interface UploadService {
    //上传文件
    String upload(MultipartFile file);
}
