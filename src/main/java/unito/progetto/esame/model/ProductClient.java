package unito.progetto.esame.model;


import javax.persistence.Entity;

@Entity
public class ProductClient extends ProductInfo {

    private int status;

    public ProductClient() {
    }

    public ProductClient(ProductInfo productInfo){
        this.setStatus(0);
        this.setProductName(productInfo.getProductName());
        // this.setProductId(getProductId());
        this.setProductStatus(getProductStatus());
        this.setProductStock(getProductStock());
        this.setProductDescription(getProductDescription());
        this.setProductIcon(getProductIcon());
        //this.setProductimage(getProductimage());
        this.setProductPrice(getProductPrice());
        this.setIdUtente(productInfo.getIdUtente());
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
