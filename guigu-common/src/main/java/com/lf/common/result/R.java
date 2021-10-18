package com.lf.common.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lf
 * @creat 2021-09-09 22:00
 */
@Data
public class R {
    private Integer code;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    private R(){}//私有化构造器

    /**
     * 返回成功的结果
     * @return
     */
    public static R ok(){
        R r = new R();
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(ResponseEnum.SUCCESS.getMessage());
        return r;
    }

    /**
     * 返回失败的结果
     * @return
     */
    public static R error(){
        R r = new R();
        r.setCode(ResponseEnum.ERROR.getCode());
        r.setMessage(ResponseEnum.ERROR.getMessage());
        return r;
    }

    public static R setResult(ResponseEnum responseEnum){
        R r = new R();
        r.setCode(responseEnum.getCode());
        r.setMessage(responseEnum.getMessage());
        return r;
    }


    public R data(String key,Object value){
        this.data.put(key,value);
        return this;
    }
    public R data(Map<String,Object> map){
        this.setData(data);
        return this;
    }


    /**
     * 设置特定的消息
     * @param message
     * @return
     */
    public R message(String message){
        this.setMessage(message);
        return this;
    }

    /**
     * 设置特定的响应码
     * @param code
     * @return
     */
    public R code(Integer code){
        this.setCode(code);
        return this;
    }

}
