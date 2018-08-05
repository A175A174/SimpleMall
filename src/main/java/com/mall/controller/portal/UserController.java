package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServiceResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登陆
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("login.do")
    public ServiceResponse<User> login(String username, String password, HttpSession session){
        ServiceResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @PostMapping("logout.do")
    public ServiceResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceResponse.createBySuccess("退出登陆成功");
    }

    @PostMapping("register.do")
    public ServiceResponse<String> register(User user){
        return iUserService.register(user);
    }

    @PostMapping("check_valid.do")
    public ServiceResponse<String> checkValid(String str, String type){
        return iUserService.chechValid(str, type);
    }

    //获取用户登陆信息
    @PostMapping("get_user_info.do")
    public ServiceResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return ServiceResponse.createBySuccess(user);
        }
        return ServiceResponse.createByErrorMessage("用户未登录,无法获取用户信息");
    }
    //获取用户密码问题
    @PostMapping("forget_get_question.do")
    public ServiceResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }
    //验证密码问题
    @PostMapping("forget_check_answer.do")
    public ServiceResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.forgetCheckAnswer(username, question, answer);
    }

    //验证密码问题修改密码
    @PostMapping("forget_reset_password.do")
    public ServiceResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }
    //验证原密码修改密码
    @PostMapping("reset_password.do")
    public ServiceResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }
    //更新用户信息
    @PostMapping("update_information.do")
    public ServiceResponse<User> update_information(HttpSession session,User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        ServiceResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    //获取当前用户信息
    @PostMapping("get_information.do")
    public ServiceResponse<User> get_information(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
