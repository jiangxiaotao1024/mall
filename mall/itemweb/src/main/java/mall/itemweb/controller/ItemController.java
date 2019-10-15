package mall.itemweb.controller;

import bean.ProductSaleAttr;
import bean.SkuInfo;
import bean.SkuSaleAttrValue;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import service.SkuService;
import service.SpuService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {
    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;
    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap modelMap){
        SkuInfo skuInfo=skuService.getSkuById(skuId);
        //sku对象
        modelMap.put("skuInfo",skuInfo);
        List<ProductSaleAttr> productSaleAttrList=spuService.spuSaleAttrListCheckBySku(skuInfo.getSpuId(),skuId);
        //属性列表
        modelMap.put("spuSaleAttrListCheckBySku",productSaleAttrList);
        //同一spu,sku哈希表
        Map<String,String> skuSaleAttrHash=new HashMap<>();
        List<SkuInfo> skuInfoList=skuService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        for(SkuInfo skuInfo1:skuInfoList){
            String k="";
            String v=skuInfo1.getId();
            List<SkuSaleAttrValue> skuSaleAttrValueList=skuInfo1.getSkuSaleAttrValueList();
            for(SkuSaleAttrValue skuSaleAttrValue:skuSaleAttrValueList){
                k+=skuSaleAttrValue.getSaleAttrValueId()+"|";
            }
            skuSaleAttrHash.put(k,v);
        }
        modelMap.put("skuSaleAttrHashJsonStr", JSON.toJSONString(skuSaleAttrHash));
        return "item";
    }
}
