package com.lf.srb.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.lf.srb.oss.service.FileService;
import com.lf.srb.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author lf
 * @creat 2021-09-17 0:23
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(InputStream inputStream, String module, String fileName) {
        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET);

        //判断BUCKET_NAME是否存在
        if (!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)){
            ossClient.createBucket(OssProperties.BUCKET_NAME);
            ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }
        //上传文件流。
        //文件目录结构 "avatar/2021/09/17/uuid.jpg"
        //构建日期的路径
        String timeFolder = new DateTime().toString("/yyyy/MM/dd/");
        fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));

        String key = module + timeFolder + fileName;
        ossClient.putObject(OssProperties.BUCKET_NAME,key,inputStream);

        //文件url地址
        return "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/" + key;
    }

    @Override
    public void removeFile(String url) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET);
        //
        //https://srb-file-210916.oss-cn-beijing.aliyuncs.com/
        // test1/2021/09/17/034bcb68-f2b4-44c4-b06b-fda6c65c528c.jpg
        // 填写文件完整路径。文件完整路径中不能包含Bucket名称。
        String host = "https://"+ OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/";
        String objectName = url.substring(host.length());

        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(OssProperties.BUCKET_NAME, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
