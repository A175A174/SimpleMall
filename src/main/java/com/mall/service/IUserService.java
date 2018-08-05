package com.mall.service;

import com.mall.common.ServiceResponse;
import com.mall.pojo.User;

public interface IUserService {
    public ServiceResponse<User> login(String username, String password);

    public ServiceResponse<String> register(User user);

    public ServiceResponse<String> chechValid(String str,String type);

    public ServiceResponse<String> selectQuestion(String username);

    public ServiceResponse<String> forgetCheckAnswer(String username, String question, String answer);

    public ServiceResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    public ServiceResponse<String> resetPassword(String passwordOld, String passwordNew,User user);

    public ServiceResponse<User> updateInformation(User user);

    public ServiceResponse<User> getInformation(Integer userId);

    public ServiceResponse<User> checkAdminRole(User user);
}
