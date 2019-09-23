package mall.managerservice.serviceImpl;

import bean.BaseCatalog1;
import bean.BaseCatalog2;
import bean.BaseCatalog3;
import com.alibaba.dubbo.config.annotation.Service;
import mall.managerservice.mapper.Catalog1Mapper;
import mall.managerservice.mapper.Catalog2Mapper;
import mall.managerservice.mapper.Catalog3Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import service.CatalogService;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    Catalog1Mapper catalog1Mapper;
    @Autowired
    Catalog2Mapper catalog2Mapper;
    @Autowired
    Catalog3Mapper catalog3Mapper;
    @Override
    public List<BaseCatalog1> getCatalog1() {

        return catalog1Mapper.selectAll();
    }
    @Override
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {

        BaseCatalog2 baseCatalog2 =new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);
        return catalog2Mapper.select(baseCatalog2);
    }
    @Override
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
        BaseCatalog3 baseCatalog3 =new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);
        return catalog3Mapper.select(baseCatalog3);
    }
}
