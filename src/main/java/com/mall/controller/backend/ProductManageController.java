package com.mall.controller.backend;

import com.google.common.collect.Maps;
import com.mall.common.ServiceResponse;
import com.mall.pojo.Product;
import com.mall.service.IFileService;
import com.mall.service.IProductService;
import com.mall.service.IUserService;
import com.mall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    //添加或更新商品
    @PostMapping("save.do")
    public ServiceResponse productSave(HttpServletRequest request, Product product){
        return iProductService.saveOrUpdateProduct(product);
    }

    //产品上下架
    @PostMapping("set_sale_status.do")
    public ServiceResponse setSaleStatus(HttpServletRequest request, Integer productId, Integer status){
        return iProductService.setSaleStatus(productId,status);
    }

    //获取产品详情
    @PostMapping("detail.do")
    public ServiceResponse getDetail(HttpServletRequest request, Integer productId){
        return iProductService.manageProductDetail(productId);
    }

    //获取产品详情
    @PostMapping("list.do")
    public ServiceResponse getList(HttpServletRequest request, @RequestParam(value = "pagrNum",defaultValue = "1") Integer pagrNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        return iProductService.getProductList(pagrNum,pageSize);
    }

    //搜索产品
    @PostMapping("search.do")
    public ServiceResponse productSearch(HttpServletRequest request,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iProductService.searchProduct(productName,productId,pageNum,pageSize);
    }

    @PostMapping("upload.do")
    public ServiceResponse upload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServiceResponse.createBySuccess(fileMap);
    }

    @PostMapping("richtext_img_upload.do")
    public Map richtextImgUpload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
//        富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;
    }
}
