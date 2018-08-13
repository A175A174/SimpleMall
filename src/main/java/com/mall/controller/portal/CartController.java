package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServiceResponse;
import com.mall.pojo.User;
import com.mall.service.ICartService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import com.mall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;



    @RequestMapping("list.do")
    public ServiceResponse<CartVo> list(HttpServletRequest request){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("add.do")
    public ServiceResponse<CartVo> add(HttpServletRequest request, Integer count, Integer productId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(),productId,count);
    }



    @RequestMapping("update.do")
    public ServiceResponse<CartVo> update(HttpServletRequest request, Integer count, Integer productId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(),productId,count);
    }

    @RequestMapping("delete_product.do")
    public ServiceResponse<CartVo> deleteProduct(HttpServletRequest request, String productIds){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }


    @RequestMapping("select_all.do")
    public ServiceResponse<CartVo> selectAll(HttpServletRequest request){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select_all.do")
    public ServiceResponse<CartVo> unSelectAll(HttpServletRequest request){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }



    @RequestMapping("select.do")
    public ServiceResponse<CartVo> select(HttpServletRequest request,Integer productId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select.do")
    
    public ServiceResponse<CartVo> unSelect(HttpServletRequest request,Integer productId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createByCodeErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }



    @RequestMapping("get_cart_product_count.do")
    
    public ServiceResponse<Integer> getCartProductCount(HttpServletRequest request){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if(user ==null){
            return ServiceResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }
    //全选
    //全反选

    //单独选
    //单独反选

    //查询当前用户的购物车里面的产品数量,如果一个产品有10个,那么数量就是10.
}
