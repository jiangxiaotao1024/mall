package mall.managerweb.controller;

import bean.SkuInfo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.SkuService;

@RestController
@CrossOrigin
public class SkuController {
    @Reference
    SkuService skuService;

    @RequestMapping("saveSkuInfo")
    public void saveSkuInfo(@RequestBody SkuInfo skuInfo) {

        String skuDefaultImg = skuInfo.getSkuDefaultImg();
        if (StringUtils.isBlank(skuDefaultImg))
            skuInfo.setSkuDefaultImg(skuInfo.getSkuImageList().get(0).getImgUrl());
        skuService.saveSkuInfo(skuInfo);
    }
}
