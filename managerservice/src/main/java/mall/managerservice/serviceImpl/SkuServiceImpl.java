package mall.managerservice.serviceImpl;

import bean.SkuAttrValue;
import bean.SkuImage;
import bean.SkuInfo;
import com.alibaba.dubbo.config.annotation.Service;
import mall.managerservice.mapper.SkuAttrValueMapper;
import mall.managerservice.mapper.SkuImageMapper;
import mall.managerservice.mapper.SkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import service.SkuService;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        skuInfoMapper.insertSelective(skuInfo);
        String skuId=skuInfo.getId();
        List<SkuAttrValue> skuAttrValueList=skuInfo.getSkuAttrValueList();
        for(SkuAttrValue skuAttrValue:skuAttrValueList){
            skuAttrValue.setSkuId(skuId);
            skuAttrValueMapper.insertSelective(skuAttrValue);
        }
        List<SkuImage> skuImageList=skuInfo.getSkuImageList();
        for(SkuImage skuImage:skuImageList){
            skuImage.setSkuId(skuId);
            skuImageMapper.insertSelective(skuImage);
        }
    }
}
