package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.common.ServiceResponse;
import com.mall.dao.ShippingMapper;
import com.mall.pojo.Shipping;
import com.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShippingServiceImpl implements IShippingService {


    @Autowired
    private ShippingMapper shippingMapper;

    public ServiceResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServiceResponse.createBySuccess("新建地址成功",result);
        }
        return ServiceResponse.createByErrorMessage("新建地址失败");
    }

    public ServiceResponse<String> del(Integer userId,Integer shippingId){
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(resultCount > 0){
            return ServiceResponse.createBySuccess("删除地址成功");
        }
        return ServiceResponse.createByErrorMessage("删除地址失败");
    }


    public ServiceResponse update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServiceResponse.createBySuccess("更新地址成功");
        }
        return ServiceResponse.createByErrorMessage("更新地址失败");
    }

    public ServiceResponse<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServiceResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServiceResponse.createBySuccess("查询地址成功",shipping);
    }

    public ServiceResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServiceResponse.createBySuccess(pageInfo);
    }
}
