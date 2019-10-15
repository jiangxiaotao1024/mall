package bean;

import com.sun.xml.internal.ws.developer.Serialization;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.List;

public class SearchSkuInfo implements Serializable {
    @Id
    private long id;
    private String skuName;
    private String skuDesc;
    private String catalog3Id;
    private BigDecimal price;
    private String skuDefaultImg;
    private double hotScore;
    private String productId;
    private List<SkuAttrValue> skuAttrValueList;

    public void setId(long id) {
        this.id = id;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public void setSkuDesc(String skuDesc) {
        this.skuDesc = skuDesc;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setSkuDefaultImg(String skuDefaultImg) {
        this.skuDefaultImg = skuDefaultImg;
    }

    public void setHotScore(double hotScore) {
        this.hotScore = hotScore;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSkuAttrValueList(List<SkuAttrValue> skuAttrValueList) {
        this.skuAttrValueList = skuAttrValueList;
    }

    public Long getId() {
        return id;
    }

    public String getSkuName() {
        return skuName;
    }

    public String getSkuDesc() {
        return skuDesc;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSkuDefaultImg() {
        return skuDefaultImg;
    }

    public double getHotScore() {
        return hotScore;
    }

    public String getProductId() {
        return productId;
    }

    public List<SkuAttrValue> getSkuAttrValueList() {
        return skuAttrValueList;
    }
}
