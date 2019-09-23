package mall.managerweb.controller;

import bean.ProductImage;
import bean.ProductInfo;
import bean.ProductSaleAttr;
import com.alibaba.dubbo.config.annotation.Reference;
import mall.managerweb.util.UploadUtil;
import org.csource.common.MyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.SpuService;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class SpuController {
    @Reference
    SpuService spuService;

    @RequestMapping("spuList")
    public List<ProductInfo> spuList(String catalog3Id) {
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping("spuImageList")
    public List<ProductImage> spuImageList(String spuId) {
        return spuService.spuImageList(spuId);
    }

    @RequestMapping("spuSaleAttrList")
    public List<ProductSaleAttr> spuSaleAttrList(String spuId) {

        return spuService.spuSaleAttrList(spuId);
    }

    @RequestMapping("saveSpuInfo")
    public void saveSpuInfo(@RequestBody ProductInfo productInfo) {

        spuService.saveSpuInfo(productInfo);
    }

    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile) {
        String imgUrl = UploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        return imgUrl;
    }
}
