package mall.managerweb.controller;

import bean.BaseAttrInfo;
import bean.BaseAttrValue;
import bean.BaseSaleAttr;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.AttrService;

import java.util.List;

@RestController
@CrossOrigin
public class AttrController {
    @Reference
    AttrService attrService;

    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        return attrService.saveAttrInfo(baseAttrInfo);
    }

    @RequestMapping("attrInfoList")
    public List<BaseAttrInfo> attrInfoList(String catalog3Id) {

        return attrService.attrInfoList(catalog3Id);
    }

    @RequestMapping("getAttrValueList")
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        return attrService.getAttrValueList(attrId);
    }

    @RequestMapping("baseSaleAttrList")
    public List<BaseSaleAttr> baseSaleAttrList() {
        return attrService.baseSaleAttrList();
    }
}
