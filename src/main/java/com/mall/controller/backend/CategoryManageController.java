package com.mall.controller.backend;

import com.mall.common.ServiceResponse;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @PostMapping("add_category.do")
    public ServiceResponse addCategory(HttpServletRequest request, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        return iCategoryService.addCategory(categoryName,parentId);
    }

    @PostMapping("set_category_name.do")
    public ServiceResponse setCategory(HttpServletRequest request,String categoryName, int categoryId){
        return iCategoryService.updateCategory(categoryName,categoryId);
    }

    //平级查询Category信息，不递归
    @PostMapping("get_category.do")
    public ServiceResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        return iCategoryService.getChildrenParallelCategory(parentId);
    }

    //递归查询Category信息
    @PostMapping("get_deep_category.do")
    public ServiceResponse getCategoryAndDeepChildrenById(HttpServletRequest request, @RequestParam(value = "parentId",defaultValue = "0") int categoryId){
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}
