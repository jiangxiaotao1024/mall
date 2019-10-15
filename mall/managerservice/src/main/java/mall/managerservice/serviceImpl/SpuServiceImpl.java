package mall.managerservice.serviceImpl;

import bean.ProductImage;
import bean.ProductInfo;
import bean.ProductSaleAttr;
import bean.ProductSaleAttrValue;
import com.alibaba.dubbo.config.annotation.Service;
import mall.managerservice.mapper.ProductImageMapper;
import mall.managerservice.mapper.ProductInfoMapper;
import mall.managerservice.mapper.ProductSaleAttrMapper;
import mall.managerservice.mapper.ProductSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import service.SpuService;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    ProductInfoMapper productInfoMapper;
    @Autowired
    ProductImageMapper productImageMapper;
    @Autowired
    ProductSaleAttrMapper productSaleAttrMapper;
    @Autowired
    ProductSaleAttrValueMapper productSaleAttrValueMapper;

    @Override
    public List<ProductInfo> spuList(String catalog3) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCatalog3Id(catalog3);
        return productInfoMapper.select(productInfo);
    }

    @Override
    public void saveSpuInfo(ProductInfo productInfo) {
        productInfoMapper.insertSelective(productInfo);
        String id=productInfo.getId();
        List<ProductImage> productImageList=productInfo.getSpuImageList();
        for (ProductImage productImage:productImageList){
            productImage.setProductId(id);
            productImageMapper.insertSelective(productImage);
        }
        List<ProductSaleAttr> productSaleAttrList=productInfo.getSpuSaleAttrList();
        for(ProductSaleAttr productSaleAttr:productSaleAttrList){
            productSaleAttr.setProductId(id);
            productSaleAttrMapper.insertSelective(productSaleAttr);
            List<ProductSaleAttrValue> productSaleAttrValueList=productSaleAttr.getSpuSaleAttrValueList();
            for (ProductSaleAttrValue productSaleAttrValue:productSaleAttrValueList){
                productSaleAttrValue.setProductId(id);
                productSaleAttrValueMapper.insertSelective(productSaleAttrValue);
            }
        }
    }

    @Override
    public List<ProductImage> spuImageList(String spuId) {
        ProductImage productImage = new ProductImage();
        productImage.setProductId(spuId);
        return productImageMapper.select(productImage);
    }

    @Override
    public List<ProductSaleAttr> spuSaleAttrList(String spuId) {
        ProductSaleAttr productSaleAttr = new ProductSaleAttr();
        productSaleAttr.setProductId(spuId);
        List<ProductSaleAttr> productSaleAttrList= productSaleAttrMapper.select(productSaleAttr);
        for(ProductSaleAttr productSaleAttr1:productSaleAttrList){
            ProductSaleAttrValue productSaleAttrValue=new ProductSaleAttrValue();
            productSaleAttrValue.setProductId(spuId);
            productSaleAttrValue.setSaleAttrId(productSaleAttr1.getSaleAttrId());
            List<ProductSaleAttrValue> productSaleAttrValueList=productSaleAttrValueMapper.select(productSaleAttrValue);
            productSaleAttr1.setSpuSaleAttrValueList(productSaleAttrValueList);
        }
        return productSaleAttrList;
    }
    public List<ProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId){
        List<ProductSaleAttr> productSaleAttrList=productSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);
        return productSaleAttrList;
    }
}
