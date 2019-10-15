package service;

import bean.SkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface SkuService {
    void saveSkuInfo(SkuInfo skuInfo);
    SkuInfo getSkuById(String skuId);

    List<SkuInfo> getAllSku();

    List<SkuInfo> getSkuSaleAttrValueListBySpu(String spuId);

    boolean checkPrice(String productSkuId, BigDecimal price);
}
