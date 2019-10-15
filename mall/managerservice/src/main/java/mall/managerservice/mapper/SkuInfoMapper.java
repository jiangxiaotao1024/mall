package mall.managerservice.mapper;

import bean.SkuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuInfoMapper extends Mapper<SkuInfo> {
    List<SkuInfo> getSkuSaleAttrValueListBySpu(String spuId);
}
