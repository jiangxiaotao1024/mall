package service;

import bean.ProductImage;
import bean.ProductInfo;
import bean.ProductSaleAttr;

import java.util.List;

public interface SpuService {
    List<ProductInfo> spuList(String catalog3);

    void saveSpuInfo(ProductInfo productInfo);

    List<ProductImage> spuImageList(String spuId);

    List<ProductSaleAttr> spuSaleAttrList(String spuId);
}
