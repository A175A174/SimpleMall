package com.mall.service.impl;

import com.mall.common.Const;
import com.mall.common.ServiceResponse;
import com.mall.common.TokenCache;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    public ServiceResponse<User> login(String username,String password){
        ServiceResponse<String> response = chechValid(username, Const.USERNAME);
        if (response.isSuccess()){
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }
        //密码MD5
        User user = userMapper.selectLogin(username,MD5Util.MD5EncodeUtf8(password));
        if (user == null){
            return  ServiceResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登陆成功",user);
    }

    public ServiceResponse<String> register(User user){
        ServiceResponse<String> response = chechValid(user.getUsername(), Const.USERNAME);
        if (!response.isSuccess()){
            return response;
        }
        response = chechValid(user.getEmail(), Const.EMAIL);
        if (!response.isSuccess()){
            return response;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);//设置角色为用户
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);//写入数据库
        if (resultCount == 0){
            return ServiceResponse.createByErrorMessage("注册失败");
        }
        return ServiceResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 检测用户信息是否存在
     * @param str
     * @param type
     * @return
     */
    public ServiceResponse<String> chechValid(String str,String type){
        if (StringUtils.isNoneBlank(type)){//type不为空和NULL
            int resultCount;
            if(Const.USERNAME.equals(type)){
                resultCount = userMapper.checkUseuname(str);
                if (resultCount > 0){
                    return ServiceResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)){
                resultCount = userMapper.checkEmail(str);
                if (resultCount > 0){
                    return ServiceResponse.createByErrorMessage("Email已存在");
                }
            }
        }else {
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        return ServiceResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 查询用户密码问题
     * @param username
     * @return
     */
    public ServiceResponse<String> selectQuestion(String username){
        ServiceResponse<String> response = chechValid(username, Const.USERNAME);
        if (response.isSuccess()){
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNoneBlank(question)){
            return ServiceResponse.createBySuccessMessage(question);
        }
        return ServiceResponse.createByErrorMessage("找回密码问题为空");
    }

    //验证密码问题
    public ServiceResponse<String> forgetCheckAnswer(String username, String question, String answer){
        int restulCount = userMapper.checkAnswer(username, question, answer);
        if (restulCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServiceResponse.createBySuccessMessage(forgetToken);
        }
        return ServiceResponse.createByErrorMessage("问题答案错误");
    }
    //验证密码问题后修改密码
    public ServiceResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return ServiceResponse.createByErrorMessage("token需要传递");
        }
        ServiceResponse<String> response = chechValid(username, Const.USERNAME);
        if (response.isSuccess()){
            return ServiceResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){
            return ServiceResponse.createByErrorMessage("token无效或过期");
        }
        if (StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (rowCount > 0){
                return ServiceResponse.createBySuccessMessage("修改密码成功");
            }
        }else {
            return ServiceResponse.createByErrorMessage("token错误，请重新获取");
        }
        return ServiceResponse.createByErrorMessage("修改密码失败");
    }
    //验证原密码修改密码
    public ServiceResponse<String> resetPassword(String passwordOld, String passwordNew,User user){
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if (resultCount == 0){
            return ServiceResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount > 0){
            return ServiceResponse.createBySuccessMessage("密码更新成功");
        }
        return ServiceResponse.createByErrorMessage("密码更新失败");
    }
    //更新用户信息
    public ServiceResponse<User> updateInformation(User user){
        //user不能被更新，Email需要校验
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount > 0){
            return ServiceResponse.createByErrorMessage("Email已被使用，请更换其他Email");
        }
        User updaeUser = new User();
        updaeUser.setId(user.getId());
        updaeUser.setEmail(user.getEmail());
        updaeUser.setPhone(user.getPhone());
        updaeUser.setQuestion(user.getQuestion());
        updaeUser.setAnswer(user.getAnswer());

        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount > 0){
            return ServiceResponse.createBySuccess("更新个人信息成功",updaeUser);
        }
        return ServiceResponse.createByErrorMessage("更新个人信息失败");
    }
    //更新用户信息
    public ServiceResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServiceResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess(user);
    }
    //检测是否为管理员
    public ServiceResponse<User> checkAdminRole(User user){
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServiceResponse.createBySuccess();
        }
        return ServiceResponse.createByError();
    }
}
