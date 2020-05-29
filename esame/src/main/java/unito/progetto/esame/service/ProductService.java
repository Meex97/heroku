package unito.progetto.esame.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unito.progetto.esame.model.ProductClient;
import unito.progetto.esame.model.ProductInfo;


public interface ProductService {

    ProductInfo findOne(String productId);

    // All selling products
    Page<ProductInfo> findUpAll(Pageable pageable);
    // All products
    Page<ProductInfo> findAll(Pageable pageable);

    Page<ProductClient> findAllAdmin(Pageable pageable);

    // All products in a category
    Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable);

    // increase stock
    void increaseStock(String productId, int amount);

    //decrease stock
    void decreaseStock(String productId, int amount);

    ProductInfo offSale(String productId);

    ProductInfo onSale(String productId);

    ProductInfo update(ProductInfo productInfo);

    ProductClient updateProductAdmin(ProductClient productClient);

    ProductInfo save(ProductInfo productInfo);

    // AGGIUNTO
   // ProductInfo saveProductSupplier(ProductInfo productInfo);

    void delete(String productId);


}
