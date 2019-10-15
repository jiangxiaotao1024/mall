package bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "pms_product_info")
public class ProductInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
    String productName;
    String catalog3Id;
    String description;
    @Transient
    List<ProductSaleAttr> spuSaleAttrList;
    @Transient
    List<ProductImage> spuImageList;

    public void setSpuImageList(List<ProductImage> spuImageList) {
        this.spuImageList = spuImageList;
    }

    public List<ProductImage> getSpuImageList() {
        return spuImageList;
    }

    public void setSpuSaleAttrList(List<ProductSaleAttr> spuSaleAttrList) {
        this.spuSaleAttrList = spuSaleAttrList;
    }

    public List<ProductSaleAttr> getSpuSaleAttrList() {
        return spuSaleAttrList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public String getDescription() {
        return description;
    }
}
