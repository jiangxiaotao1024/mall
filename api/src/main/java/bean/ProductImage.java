package bean;

import javax.persistence.Table;
import java.io.Serializable;
@Table(name = "product_image")
public class ProductImage implements Serializable {
    String id;
    String productId;
    String imgName;
    String imgUrl;

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getImgName() {
        return imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}

