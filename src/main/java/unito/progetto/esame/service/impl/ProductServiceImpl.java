package unito.progetto.esame.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unito.progetto.esame.enums.ProductStatusEnum;
import unito.progetto.esame.enums.ResultEnum;
import unito.progetto.esame.exception.MyException;
import unito.progetto.esame.model.ProductClient;
import unito.progetto.esame.model.ProductInfo;
import unito.progetto.esame.repository.ProductClientRepository;
import unito.progetto.esame.repository.ProductInfoRepository;
import unito.progetto.esame.service.CategoryService;
import unito.progetto.esame.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Autowired
    ProductClientRepository productClientRepository;

    @Autowired
    CategoryService categoryService;

    ProductInfo productInfo;
    @Transactional
    @Override
    public ProductInfo findOne(String productId) {

        ProductInfo productInfo = productInfoRepository.findByProductId(productId);
        return productInfo;
    }




    @Override
    public Page<ProductInfo> findUpAll(Pageable pageable) {
        return productInfoRepository.findAllByProductStatusOrderByProductIdAsc(ProductStatusEnum.UP.getCode(),pageable);
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAllByOrderByProductId(pageable);
    }

    @Override
    public Page<ProductClient> findAllAdmin(Pageable pageable) {
        return productClientRepository.findAllByOrderByCreateTime(pageable);
    }



    @Override
    public Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable) {
        return productInfoRepository.findAllByCategoryTypeOrderByProductIdAsc(categoryType, pageable);
    }

    @Override
    @Transactional
    public void increaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() + amount;
        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void decreaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() - amount;
        if(update < 0) throw new MyException(ResultEnum.PRODUCT_NOT_ENOUGH );

        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == ProductStatusEnum.DOWN.getCode()) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == ProductStatusEnum.UP.getCode()) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }


        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfo update(ProductInfo productInfo) {

        // if null throw exception
        categoryService.findByCategoryType(productInfo.getCategoryType());
        if(productInfo.getProductStatus() > 1) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }


        return productInfoRepository.save(productInfo);
    }

    @Override
    public ProductClient updateProductAdmin(ProductClient productClient) {

        // if null throw exception
        // categoryService.findByCategoryType(productClient.getCategoryType());
        return productClientRepository.save(productClient);
    }



    //Rimettere

   /*@Override
    public ProductInfo save(ProductInfo productInfo) {
        return update(productInfo);
    }*/

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return create(productInfo);
    }

    @Transactional
    public ProductInfo create(ProductInfo productInfo) {

            //ProductInfo savedSupplier = productInfoRepository.save(productInfo);
            return productInfoRepository.save(productInfo);


    }


    @Override
    public void delete(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
        productInfoRepository.delete(productInfo);

    }


}
