package bean;

import javax.persistence.Table;
import java.io.Serializable;
@Table(name = "pms_sku_image")
public class SkuImage implements Serializable {
    String id;
    String skuId;
    String imgName;
    String imgUrl;
    String spuImgId;
    String  isDefault;

    public void setId(String id) {
        this.id = id;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setSpuImgId(String spuImgId) {
        this.spuImgId = spuImgId;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public String getSkuId() {
        return skuId;
    }

    public String getImgName() {
        return imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSpuImgId() {
        return spuImgId;
    }

    public String getIsDefault() {
        return isDefault;
    }
}
