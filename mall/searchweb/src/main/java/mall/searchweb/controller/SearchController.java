package mall.searchweb.controller;

import bean.*;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import service.AttrService;
import service.SearchService;
import service.SkuService;

import javax.sql.rowset.serial.SerialArray;
import java.util.*;

@Controller
public class SearchController {
    @Reference
    SearchService searchService;
    @Reference
    AttrService attrService;
    @RequestMapping("list.html")
    public String list(SearchParam searchParam, ModelMap modelMap){
        List<SearchSkuInfo> searchSkuInfoList=searchService.list(searchParam);
        modelMap.put("skuLsInfoList",searchSkuInfoList);
        Set<String> valueIdSet=new HashSet<>();
        for (SearchSkuInfo searchSkuInfo:searchSkuInfoList){
            List<SkuAttrValue> skuAttrValueList=searchSkuInfo.getSkuAttrValueList();
            for (SkuAttrValue skuAttrValue:skuAttrValueList){
                String valueId=skuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }
        List<BaseAttrInfo> baseAttrInfoList=attrService.gerAttrValueListByValueId(valueIdSet);
        modelMap.put("attrList",baseAttrInfoList);
        String[] delValueIds=searchParam.getValueId();
        if(delValueIds!=null){
            Iterator<BaseAttrInfo> iterator=baseAttrInfoList.iterator();
            List<SearchCrumb> searchCrumbList=new ArrayList<>();
            while (iterator.hasNext()){
                BaseAttrInfo baseAttrInfo=iterator.next();
                List<BaseAttrValue> baseAttrValueList=baseAttrInfo.getAttrValueList();
                for(BaseAttrValue baseAttrValue:baseAttrValueList){
                    String valueName=baseAttrValue.getValueName();
                    String valueId=baseAttrValue.getId();
                    for(String delValueId:delValueIds){
                        if(delValueId.equals(valueId)){
                            iterator.remove();
                            SearchCrumb searchCrumb=new SearchCrumb();
                            searchCrumb.setValueId(delValueId);
                            searchCrumb.setValueName(valueName);
                            searchCrumb.setUrlParam(getUrlParam1(searchParam,delValueId));
                            searchCrumbList.add(searchCrumb);
                        }
                    }
                }
            }
            modelMap.put("attrValueSelectedList",searchCrumbList);
        }
//        if(delValueIds!=null){
//            List<SearchCrumb> searchCrumbList=new ArrayList<>();
//            for(String delValueId:delValueIds){
//                SearchCrumb searchCrumb=new SearchCrumb();
//                searchCrumb.setValueId(delValueId);
//                searchCrumb.setValueName(delValueId);
//                searchCrumb.setUrlParam(getUrlParam1(searchParam,delValueId));
//                searchCrumbList.add(searchCrumb);
//            }
//            modelMap.put("attrValueSelectedList",searchCrumbList);
//        }
        String urlParam=getUrlParam(searchParam);
        modelMap.put("urlParam",urlParam);
        String keyword = searchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            modelMap.put("keyword", keyword);
        }
       return "list";
    }

    private String getUrlParam(SearchParam searchParam) {
        String keyword=searchParam.getKeyword();
        String catalog3Id = searchParam.getCatalog3Id();
        String[] valueId = searchParam.getValueId();
        String urlParam="";
        if(StringUtils.isNotBlank(keyword)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam=urlParam+"&";
            }
            urlParam=urlParam+"keyword="+keyword;
        }
        if(StringUtils.isNotBlank(catalog3Id)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam=urlParam+"&";
            }
            urlParam+="catalog3Id="+catalog3Id;
        }
        if(valueId!=null){
            for (String value1:valueId){
                    if(StringUtils.isNotBlank(urlParam)){
                        urlParam+="&";
                    }
                    urlParam+="valueId="+value1;
            }
        }
        return urlParam;
    }
    private String getUrlParam1(SearchParam searchParam,String  delValueId) {
        String keyword=searchParam.getKeyword();
        String catalog3Id = searchParam.getCatalog3Id();
        String[] valueId = searchParam.getValueId();
        String urlParam="";
        if(StringUtils.isNotBlank(keyword)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam=urlParam+"&";
            }
            urlParam=urlParam+"keyword="+keyword;
        }
        if(StringUtils.isNotBlank(catalog3Id)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam=urlParam+"&";
            }
            urlParam+="catalog3Id="+catalog3Id;
        }
        if(valueId!=null){
            for (String value1:valueId){
                if(!value1.equals(delValueId)){
                    if(StringUtils.isNotBlank(urlParam)){
                        urlParam+="&";
                    }
                    urlParam+="valueId="+value1;
                }
            }
        }
        return urlParam;
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
