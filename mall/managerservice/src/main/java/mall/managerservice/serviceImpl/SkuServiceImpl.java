package mall.managerservice.serviceImpl;

import bean.SkuAttrValue;
import bean.SkuImage;
import bean.SkuInfo;
import bean.SkuSaleAttrValue;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import mall.managerservice.mapper.SkuAttrValueMapper;
import mall.managerservice.mapper.SkuImageMapper;
import mall.managerservice.mapper.SkuInfoMapper;
import mall.managerservice.mapper.SkuSaleAttrValueMapper;
import mall.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import service.SkuService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //skuInfo
        skuInfoMapper.insertSelective(skuInfo);
        String skuId=skuInfo.getId();
        //平台属性
        List<SkuAttrValue> skuAttrValueList=skuInfo.getSkuAttrValueList();
        for(SkuAttrValue skuAttrValue:skuAttrValueList){
            skuAttrValue.setSkuId(skuId);
            skuAttrValueMapper.insertSelective(skuAttrValue);
        }
        //销售属性
        List<SkuSaleAttrValue> skuSaleAttrValueList=skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue:skuSaleAttrValueList){
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);
        }
        //图片信息
        List<SkuImage> skuImageList=skuInfo.getSkuImageList();
        for(SkuImage skuImage:skuImageList){
            skuImage.setSkuId(skuId);
            skuImageMapper.insertSelective(skuImage);
        }
    }

    private SkuInfo getSkuByIdFromDb(String skuId) {
        SkuInfo skuInfo=new SkuInfo();
        skuInfo.setId(skuId);
        SkuInfo skuInfo1=skuInfoMapper.selectOne(skuInfo);
        SkuImage skuImage=new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList=skuImageMapper.select(skuImage);
        skuInfo1.setSkuImageList(skuImageList);
        return skuInfo1;
    }
    public SkuInfo getSkuById(String skuId){
        SkuInfo skuInfo=new SkuInfo();
        Jedis jedis= redisUtil.getJedis();
        String skuKey="sku:"+skuId+":info";
        String skuJson=jedis.get(skuKey);
        //判断有误缓存
        if(StringUtils.isNotBlank(skuJson)){
            skuInfo= JSON.parseObject(skuJson,SkuInfo.class);
        }
        else{
            //设置分布式锁，10秒有效
            String ok = jedis.set("sku:" + skuId + ":lock", "1","nx","px",10);
            if(StringUtils.isNotBlank(ok)&&ok.equals("OK")){
                skuInfo=getSkuByIdFromDb(skuId);
                if(skuInfo!=null){
                    jedis.set("sku:"+skuId+":info", JSON.toJSONString(skuInfo));
                }
                 else{
                    //数据库值不存在设置空缓存60s，防止穿透
                     jedis.setex("sku:"+skuId+":info",60, "");
                }
            }
            else {
                try {
                    //自旋3s
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuById(skuId);
            }
        }
        jedis.close();
        return skuInfo;
    }

    @Override
    public List<SkuInfo> getAllSku() {
        List<SkuInfo> skuInfoList=skuInfoMapper.selectAll();
        for(SkuInfo skuInfo:skuInfoList){
            String skuId=skuInfo.getId();
            SkuAttrValue skuAttrValue=new SkuAttrValue();
            skuAttrValue.setSkuId(skuId);
            List<SkuAttrValue> skuAttrValueList=skuAttrValueMapper.select(skuAttrValue);
            skuInfo.setSkuAttrValueList(skuAttrValueList);
        }
        return skuInfoList;
    }

    @Override
    public List<SkuInfo> getSkuSaleAttrValueListBySpu(String spuId) {
        List<SkuInfo> skuInfoList=new ArrayList<>();
        skuInfoList=skuInfoMapper.getSkuSaleAttrValueListBySpu(spuId);
        return skuInfoList;
    }

    @Override
    public boolean checkPrice(String productSkuId, BigDecimal price) {
        boolean b=false;
        SkuInfo skuInfo=new SkuInfo();
        skuInfo.setId(productSkuId);
        SkuInfo skuInfo1=skuInfoMapper.selectOne(skuInfo);
        if(skuInfo1.getPrice().compareTo(price)==0){
            b=true;
        }
        return b;
    }

}
