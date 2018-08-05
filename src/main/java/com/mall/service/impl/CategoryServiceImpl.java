package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mall.common.ServiceResponse;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Category;
import com.mall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServiceResponse addCategory(String categoryName,Integer parentId){
        if (parentId == null || StringUtils.isBlank(categoryName)){
            return ServiceResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0 ){
            return ServiceResponse.createBySuccessMessage("添加品类成功");
        }
        return ServiceResponse.createByErrorMessage("添加品类失败");
    }

    public ServiceResponse updateCategory(String categoryName,Integer categoryId){
        if (categoryId == null || StringUtils.isBlank(categoryName)){
            return ServiceResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0 ){
            return ServiceResponse.createBySuccessMessage("更新品类名字成功");
        }
        return ServiceResponse.createByErrorMessage("更新品类名字失败");
    }

    //查询category
    public ServiceResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(parentId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的品类");
        }
        return ServiceResponse.createBySuccess(categoryList);
    }

    //递归查询category
    public ServiceResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null){
            for (Category c:categorySet){
                categoryIdList.add(c.getId());
            }
        }
        return ServiceResponse.createBySuccess(categoryIdList);
    }
    //递归查询category
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category c:categoryList) {
            findChildCategory(categorySet,c.getId());
        }
        return categorySet;
    }
}
