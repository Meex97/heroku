package unito.progetto.esame.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import unito.progetto.esame.model.ProductCategory;
import unito.progetto.esame.model.ProductInfo;
import unito.progetto.esame.service.CategoryService;
import unito.progetto.esame.service.ProductService;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductController productController;

    /**
     * Show products in category
     *
     * @param categoryType
     * @param page
     * @param size
     * @return
     */
    @Transactional
    @GetMapping("/category/{type}")
    public Page<ProductInfo> showOne(@PathVariable("type") Integer categoryType,
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "3") Integer size) {

        ProductCategory cat = categoryService.findByCategoryType(categoryType);
        PageRequest request = PageRequest.of(page-1 , size);
        List<ProductInfo> category = this.productController.findAll2(page , size).toList();
        List<ProductInfo> tmp = new ArrayList<>();
        category.forEach(x -> {
            if(x.getCategoryType() == categoryType){
                tmp.add(x);
            }
        });

        if(tmp.size() <= size){
            size = tmp.size() ;
        }
        Page<ProductInfo> pages = new PageImpl<>(tmp.subList(page-1, size), request, tmp.size());
        //Page<ProductInfo> productInCategory =  productService.findAllInCategory(categoryType, request);
        //CategoryPage tmp = new CategoryPage("", productInCategory);
        //tmp.setCategory(cat.getCategoryName());
        return pages;
    }
}
