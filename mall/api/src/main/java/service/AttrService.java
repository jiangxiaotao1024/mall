package service;

import bean.BaseAttrInfo;
import bean.BaseAttrValue;
import bean.BaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrService {
    List<BaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(BaseAttrInfo baseAttrInfo);
    List<BaseAttrValue> getAttrValueList(String attrId);
    List<BaseSaleAttr> baseSaleAttrList();

    List<BaseAttrInfo> gerAttrValueListByValueId(Set<String> valueIdSet);
}
