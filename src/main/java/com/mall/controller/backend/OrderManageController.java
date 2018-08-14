package com.mall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServiceResponse;
import com.mall.service.IOrderService;
import com.mall.service.IUserService;
import com.mall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    public ServiceResponse<PageInfo> orderList(HttpServletRequest request,
                                               @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iOrderService.manageList(pageNum,pageSize);
    }

    @RequestMapping("detail.do")
    public ServiceResponse<OrderVo> orderDetail(HttpServletRequest request, Long orderNo){
        return iOrderService.manageDetail(orderNo);
    }

    @RequestMapping("search.do")
    public ServiceResponse<PageInfo> orderSearch(HttpServletRequest request, Long orderNo,
                                                 @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iOrderService.manageSearch(orderNo,pageNum,pageSize);
    }

    //发货
    @RequestMapping("send_goods.do")
    public ServiceResponse<String> orderSendGoods(HttpServletRequest request, Long orderNo){
        return iOrderService.manageSendGoods(orderNo);
    }
}
