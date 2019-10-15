package mall.managerservice.serviceImpl;

import bean.BaseAttrInfo;
import bean.BaseAttrValue;
import bean.BaseSaleAttr;
import com.alibaba.dubbo.config.annotation.Service;
import mall.managerservice.mapper.AttrInfoMapper;
import mall.managerservice.mapper.AttrValueMapper;
import mall.managerservice.mapper.BaseSaleMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import service.AttrService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {
    @Autowired
    AttrInfoMapper attrInfoMapper;
    @Autowired
    AttrValueMapper attrValueMapper;
    @Autowired
    BaseSaleMapper baseSaleMapper;

    @Override
    public String saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        String id= baseAttrInfo.getId();
        if(id==null){
            attrInfoMapper.insertSelective(baseAttrInfo);
            List<BaseAttrValue> baseAttrValueList = baseAttrInfo.getAttrValueList();
            for (BaseAttrValue baseAttrValue : baseAttrValueList) {
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                attrValueMapper.insertSelective(baseAttrValue);
            }}
        else{
            Example example=new Example(BaseAttrInfo.class);
            example.createCriteria().andEqualTo("id",id);
            attrInfoMapper.updateByExampleSelective(baseAttrInfo,example);
            BaseAttrValue baseAttrValueDel =new BaseAttrValue();
            baseAttrValueDel.setAttrId(id);
            attrValueMapper.delete(baseAttrValueDel);
            List<BaseAttrValue> baseAttrValueList = baseAttrInfo.getAttrValueList();
            for(BaseAttrValue baseAttrValue : baseAttrValueList){
                baseAttrValue.setAttrId(id);
                attrValueMapper.insertSelective(baseAttrValue);
            }
        }
        return "success";
    }
    @Override
    public List<BaseAttrInfo> attrInfoList(String catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> baseAttrInfoList=attrInfoMapper.select(baseAttrInfo);
        for(BaseAttrInfo baseAttrInfo1:baseAttrInfoList){
            BaseAttrValue baseAttrValue=new BaseAttrValue();
            baseAttrValue.setAttrId(baseAttrInfo1.getId());
            List<BaseAttrValue> baseAttrValueList=attrValueMapper.select(baseAttrValue);
            baseAttrInfo1.setAttrValueList(baseAttrValueList);
        }
        return  baseAttrInfoList;
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        BaseAttrValue baseAttrValue =new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        return attrValueMapper.select(baseAttrValue);
    }

    @Override
    public List<BaseSaleAttr> baseSaleAttrList() {

        return baseSaleMapper.selectAll();
    }

    @Override
    public List<BaseAttrInfo> gerAttrValueListByValueId(Set<String> valueIdSet) {
        String valueIdStr= StringUtils.join(valueIdSet,",");
        List<BaseAttrInfo> baseAttrInfoList=attrInfoMapper.selectAttrValueListByValueId(valueIdStr);
        return baseAttrInfoList;
    }
}
