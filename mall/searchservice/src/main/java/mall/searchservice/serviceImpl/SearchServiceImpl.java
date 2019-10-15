package mall.searchservice.serviceImpl;


import bean.SearchParam;
import bean.SearchSkuInfo;
import bean.SkuAttrValue;
import com.alibaba.dubbo.config.annotation.Service;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import service.SearchService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    @Override
    public List<SearchSkuInfo> list(SearchParam searchParam){
        String dslStr = getSearchDsl(searchParam);
        // 用api执行复杂查询
        System.out.println(dslStr);
        List<SearchSkuInfo> SearchSkuInfos = new ArrayList<>();
        Search search = new Search.Builder(dslStr).addIndex("mall").addType("SkuInfo").build();
        SearchResult execute = null;
        try {
            execute = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SearchResult.Hit<SearchSkuInfo, Void>> hits = execute.getHits(SearchSkuInfo.class);
        for (SearchResult.Hit<SearchSkuInfo, Void> hit : hits) {
            SearchSkuInfo source = hit.source;
            Map<String, List<String>> highlight = hit.highlight;
            if(highlight!=null)
            {String skuName = highlight.get("skuName").get(0);
            source.setSkuName(skuName);}
            SearchSkuInfos.add(source);
        }
        return SearchSkuInfos;
    }

    private String getSearchDsl(SearchParam searchParam) {
        String[] skuAttrValueList = searchParam.getValueId();
        String keyword = searchParam.getKeyword();
        String catalog3Id = searchParam.getCatalog3Id();

        // jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        // filter
        if(StringUtils.isNotBlank(catalog3Id)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if(skuAttrValueList!=null){
            for (String SkuAttrValue : skuAttrValueList) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",SkuAttrValue);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        // must
        if(StringUtils.isNotBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        // query
        searchSourceBuilder.query(boolQueryBuilder);

        // highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field("skuName");
        searchSourceBuilder.highlight(highlightBuilder);
        // sort
        searchSourceBuilder.sort("id", SortOrder.DESC);
        // from
        searchSourceBuilder.from(0);
        // size
        searchSourceBuilder.size(20);

        return searchSourceBuilder.toString();
    }
}
