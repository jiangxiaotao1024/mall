package mall.managerservice.mapper;

import bean.ProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProductSaleAttrMapper extends Mapper<ProductSaleAttr> {
    List<ProductSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("productId") String productId,@Param("skuId") String skuId);
}
