package service;

import bean.BaseAttrInfo;
import bean.BaseAttrValue;
import bean.BaseSaleAttr;

import java.util.List;

public interface AttrService {
    List<BaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(BaseAttrInfo baseAttrInfo);
    List<BaseAttrValue> getAttrValueList(String attrId);
    List<BaseSaleAttr> baseSaleAttrList();
}
