package com.huawei.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.upload.config.UploadProperties;
import com.huawei.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/5/3 23:37
 * @Description:上传微服务逻辑实现层
 */
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FastFileStorageClient storageClient;
    //注入文件上传相关属性
    @Autowired
    private  UploadProperties props;

    //文件上传
    public String upload(MultipartFile file) {
        try {
            //校验文件类型
            String contentType = file.getContentType();
            if (!props.getAllowTypes().contains(contentType)) {
                throw new SelfException(ExceptionEnums.INVALID_FILE_TYPE);
            }

            //校验文件内容
            BufferedImage iamge = ImageIO.read(file.getInputStream());
            if (iamge == null) {
                throw new SelfException(ExceptionEnums.INVALID_FILE_TYPE);
            }
            //上传到DFS
            //截取图片类型
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //返回路径
            return  props.getBaseUrl() + storePath.getFullPath();

        } catch (IOException e) {
            log.error("文件上传失败");
            throw new SelfException(ExceptionEnums.UPLOAD_FILE_ERROR);
        }
    }
}
