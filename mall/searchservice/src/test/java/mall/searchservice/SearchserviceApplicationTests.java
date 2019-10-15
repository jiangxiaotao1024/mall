package mall.searchservice;

import bean.SearchSkuInfo;
import bean.SkuAttrValue;
import bean.SkuInfo;
import com.alibaba.dubbo.config.annotation.Reference;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.SkuService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchserviceApplicationTests {
    @Reference
    SkuService skuService;
    @Autowired
    JestClient jestClient;
    @Test
    public void contextLoads() throws InvocationTargetException, IllegalAccessException, IOException {
        put();
    }

    private void put() throws InvocationTargetException, IllegalAccessException, IOException {
        List<SkuInfo> skuInfoList=new ArrayList<>();
        skuInfoList=skuService.getAllSku();
        List<SearchSkuInfo> searchSkuInfoList=new ArrayList<>();
        for(SkuInfo skuInfo:skuInfoList){
            SearchSkuInfo searchSkuInfo=new SearchSkuInfo();
            BeanUtils.copyProperties(skuInfo,searchSkuInfo);
            searchSkuInfo.setId(Long.parseLong(skuInfo.getId()));
            searchSkuInfoList.add(searchSkuInfo);
        }
        for(SearchSkuInfo searchSkuInfo:searchSkuInfoList){
            Index put=new Index.Builder(searchSkuInfo).index("mall").type("SkuInfo").id(searchSkuInfo.getId()+"").build();
            jestClient.execute(put);
        }
    }

}
