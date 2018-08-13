package com.mall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServiceResponse;
import com.mall.pojo.Shipping;
import com.mall.pojo.User;
import com.mall.service.IShippingService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping/")
public class ShippingController {


    @Autowired
    private IShippingService iShippingService;


    @RequestMapping("add.do")
    public ServiceResponse add(HttpServletRequest request, Shipping shipping){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.add(user.getId(),shipping);
    }


    @RequestMapping("del.do")
    public ServiceResponse del(HttpServletRequest request,Integer shippingId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.del(user.getId(),shippingId);
    }

    @RequestMapping("update.do")
    public ServiceResponse update(HttpServletRequest request,Shipping shipping){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.update(user.getId(),shipping);
    }


    @RequestMapping("select.do")
    public ServiceResponse<Shipping> select(HttpServletRequest request,Integer shippingId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(),shippingId);
    }

    @RequestMapping("list.do")
    public ServiceResponse<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                         HttpServletRequest request){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(),pageNum,pageSize);
    }
}
