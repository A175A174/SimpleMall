package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServiceResponse;
import com.mall.pojo.Product;
import com.mall.vo.ProductDetailVo;

public interface IProductService {

    public ServiceResponse saveOrUpdateProduct(Product product);

    public ServiceResponse setSaleStatus(Integer productId, Integer status);

    public ServiceResponse<ProductDetailVo> manageProductDetail(Integer productId);

    public ServiceResponse<PageInfo> getProductList(int pageNum, int pageSize);

    public ServiceResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    public ServiceResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);

    public ServiceResponse<ProductDetailVo> getProductDetail(Integer productId);
}
