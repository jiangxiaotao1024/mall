package mall.managerweb.controller;

import bean.BaseCatalog1;
import bean.BaseCatalog2;
import bean.BaseCatalog3;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CatalogService;

import java.util.List;

@RestController
@CrossOrigin
public class CatalogController {
    @Reference
    CatalogService catalogService;

    @RequestMapping("getCatalog1")
    public List<BaseCatalog1> getCatalog1() {
        return catalogService.getCatalog1();
    }

    @RequestMapping("getCatalog2")
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        return catalogService.getCatalog2(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {

        return catalogService.getCatalog3(catalog2Id);
    }
}
