package service;

import bean.BaseAttrInfo;
import bean.SearchParam;
import bean.SearchSkuInfo;

import java.util.List;
import java.util.Set;

public interface SearchService {
    List<SearchSkuInfo> list(SearchParam searchParam);
}
