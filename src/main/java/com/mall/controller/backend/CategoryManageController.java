package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServiceResponse;
import com.mall.pojo.User;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @PostMapping("add_category.do")
    public ServiceResponse addCategory(HttpServletRequest request, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if (user == null){
            return ServiceResponse.createByErrorMessage("用户未登陆");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return ServiceResponse.createByErrorMessage("需要管理员权限");
        }
    }

    @PostMapping("set_category_name.do")
    public ServiceResponse setCategory(HttpServletRequest request,String categoryName, int categoryId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if (user == null){
            return ServiceResponse.createByErrorMessage("用户未登陆");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.updateCategory(categoryName,categoryId);
        }else {
            return ServiceResponse.createByErrorMessage("需要管理员权限");
        }
    }

    //平级查询Category信息，不递归
    @PostMapping("get_category.do")
    public ServiceResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if (user == null){
            return ServiceResponse.createByErrorMessage("用户未登陆");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.getChildrenParallelCategory(parentId);
        }else {
            return ServiceResponse.createByErrorMessage("需要管理员权限");
        }
    }

    //递归查询Category信息
    @PostMapping("get_deep_category.do")
    public ServiceResponse getCategoryAndDeepChildrenById(HttpServletRequest request, @RequestParam(value = "parentId",defaultValue = "0") int categoryId){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(CookieUtil.readLoginToken(request)), User.class);
        if (user == null){
            return ServiceResponse.createByErrorMessage("用户未登陆");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else {
            return ServiceResponse.createByErrorMessage("需要管理员权限");
        }
    }
}
