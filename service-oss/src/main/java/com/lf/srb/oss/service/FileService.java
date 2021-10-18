package com.lf.srb.oss.service;

import java.io.InputStream;

/**
 * @author lf
 * @creat 2021-09-17 0:22
 */
public interface FileService {
    /**
     * 文件上传至阿里云
     */
    String upload(InputStream inputStream, String module, String fileName);

    void removeFile(String url);
}
