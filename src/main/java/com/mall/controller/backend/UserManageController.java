package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServiceResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("login.do")
    public ServiceResponse<User> login(String username, String password, HttpServletResponse responsex,HttpSession session){
        ServiceResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()){
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN){
                String s = JsonUtil.obj2String(user);
                RedisPoolUtil.setEx(session.getId(), s, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                CookieUtil.writeLoginToken(responsex,session.getId());
                return response;
            }else {
                return ServiceResponse.createByErrorMessage("不是管理员");
            }
        }
        return response;
    }
}
