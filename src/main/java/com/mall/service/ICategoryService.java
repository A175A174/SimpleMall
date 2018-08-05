package com.mall.service;

import com.mall.common.ServiceResponse;
import com.mall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    public ServiceResponse addCategory(String categoryName, Integer parentId);

    public ServiceResponse updateCategory(String categoryName,Integer parentId);

    public ServiceResponse<List<Category>> getChildrenParallelCategory(Integer parentId);

    public ServiceResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
