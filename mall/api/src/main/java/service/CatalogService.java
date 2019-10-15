package service;

import bean.BaseCatalog1;
import bean.BaseCatalog2;
import bean.BaseCatalog3;

import java.util.List;

public interface CatalogService {
    public List<BaseCatalog1> getCatalog1();
    public List<BaseCatalog2> getCatalog2(String catalog1Id);
    public List<BaseCatalog3> getCatalog3(String catalog2Id);
}
