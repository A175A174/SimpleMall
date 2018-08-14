package com.mall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/* 服务器响应对象
 *
 * @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
 * 属性为NULL则不参与JSON序列化
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }
    public String getMsg() {
        return msg;
    }
    public T getData() {
        return data;
    }
    private ServiceResponse(int status){
        this.status = status;
    }
    private ServiceResponse(int status,T data){
        this.status = status;
        this.data = data;
    }
    private ServiceResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }
    private ServiceResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    /*@JsonIgnore
    * 在json序列化时将java bean中的一些属性忽略掉，序列化和反序列化都受影响。
    * 一般标记在属性或者方法上，返回的json数据即不包含该属性。
    * */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }
    public static <T> ServiceResponse<T> createBySuccess(){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServiceResponse<T> createBySuccess(T data){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServiceResponse<T> createBySuccess(String msg,T data){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public static <T> ServiceResponse<T> createBySuccessMessage(String msg){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServiceResponse<T> createByError(){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    public static <T> ServiceResponse<T> createByErrorMessage(String errorMessage){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }
    public static <T> ServiceResponse<T> createByCodeErrorMessage(int errorCode,String errorMessage){
        return new ServiceResponse<T>(errorCode,errorMessage);
    }
}
