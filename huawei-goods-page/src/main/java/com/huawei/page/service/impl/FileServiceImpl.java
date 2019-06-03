package com.huawei.page.service.impl;

import com.huawei.common.utils.ThreadUtils;
import com.huawei.page.service.FileService;
import com.huawei.page.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: huaweishop
 * @description: FileService实现层
 * @author: cuibeijie
 * @create: 2019-05-27 21:36
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private PageService pageService;
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${huawei.thymeleaf.destPath}")
    private String destPath;

    /**
     * 生成商品详情静态html
     *
     * @param id
     */
    public void createHtml(Long id) {
        // 创建上下文，
        Context context = new Context();
        // 把数据加入上下文
        context.setVariables(this.pageService.loadModel(id));

        // 创建输出流，关联到一个临时文件
        File temp = new File(id + ".html");
        // 目标页面文件
        File dest = createPath(id);
        // 备份原页面文件
        File bak = new File(id + "_bak.html");
        try (PrintWriter writer = new PrintWriter(temp, "UTF-8")) {
            // 利用thymeleaf模板引擎生成 静态页面
            templateEngine.process("item", context, writer);

            if (dest.exists()) {
                // 如果目标文件已经存在，先备份
                dest.renameTo(bak);
            }
            // 将新页面覆盖旧页面
            FileCopyUtils.copy(temp, dest);
            // 成功后将备份页面删除
            bak.delete();
        } catch (IOException e) {
            // 失败后，将备份页面恢复
            bak.renameTo(dest);
            log.error("生成商品详情静态页面出错,spuId:{}", id, e);
        } finally {
            // 删除临时页面
            if (temp.exists()) {
                temp.delete();
            }
        }
    }

    private File createPath(Long id) {
        if (id == null) {
            return null;
        }
        File dest = new File(destPath);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        return new File(dest, id + ".html");
    }

    /**
     * 判断某个商品的页面是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(Long id) {
        return this.createPath(id).exists();
    }

    /**
     * 异步创建html页面
     *
     * @param id
     */
    public void syncCreateHtml(Long id) {
        ThreadUtils.execute(() -> {
            try {
                createHtml(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 删除静态页
     * @param spuId
     */
    public void deleteHtml(Long spuId) {
          File dest = createPath(spuId);
          if(dest.exists()){
              dest.delete();
          }
    }
}
