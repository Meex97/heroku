package unito.progetto.esame.controller;


import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unito.progetto.esame.model.*;
import unito.progetto.esame.repository.ProductInfoRepository;
import unito.progetto.esame.service.CategoryService;
import unito.progetto.esame.service.ProductService;
import unito.progetto.esame.service.UserService;
import unito.progetto.esame.service.impl.FileService;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@CrossOrigin
@RestController
public class ProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;


    ProductInfoRepository productInfoRepository;


    private FileService fileService;


    @Autowired
    public void FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/api/files")
    @ResponseStatus(HttpStatus.OK)
    public void handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.storeFile(file);
    }




    /**
     * Show All Categories
     **/

    @Transactional
    @GetMapping("/product2")
    public Page<ProductInfo> findAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "3") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);


        return productService.findAll(request);

    }

    @Transactional
    @GetMapping("/product")
    public Page<ProductInfo> findAll2(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "3") Integer size) {

        PageRequest request2 = PageRequest.of(page - 1, size);
        List<ProductInfo> new_products = this.findBySupplier();
        List<ProductClient> secondHand_products = this.findByUser();
        secondHand_products.forEach(x -> {
            new_products.add(x);

        });
        Collections.sort(new_products);
        int min_prod = size*page;
        if(new_products.size() <= size*page){
            //size = new_products.size() ;
            min_prod = new_products.size();
        }

        Page<ProductInfo> pages = new PageImpl<>(new_products.subList( (page-1)*size, min_prod), request2, new_products.size());

        return pages;

    }

    @Transactional
    @GetMapping("/product/client")
    public Page<ProductClient> findAllAdmin(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "size", defaultValue = "3") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return productService.findAllAdmin(request);
    }

   @Transactional
   @GetMapping(value = "product/Supplier/{idUtente}")
   public List<ProductInfo> findByIdUtente(@PathVariable("idUtente") Long idUtente) {

       Page<ProductInfo> prod =this.findAll(1,50);
       List<ProductInfo> prodSupplier = new ArrayList<>();

       prod.forEach(product ->{
           if(product.getIdUtente().equals(idUtente)){
               prodSupplier.add(product);
           }
       });
       return prodSupplier;
   }

    @Transactional
    @GetMapping("/product/{productId}")
    public ProductInfo showOne(@PathVariable("productId") String productId) {

        ProductInfo productInfo = productService.findOne(productId);

        return productInfo;
    }



    @PostMapping("/seller/producto/new")
    public ResponseEntity create(@Valid @RequestBody ProductInfo product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @Transactional
    @PostMapping("/client/producto/new")
    public ResponseEntity createBaratto(@Valid @RequestBody ProductClient product) {

        return ResponseEntity.ok(productService.save(product));
    }

    @Transactional
    @PutMapping("/product/{id}/edit")
    public ResponseEntity edit(@PathVariable("id") String productId,
                               @Valid @RequestBody ProductInfo product,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }
        if (!productId.equals(product.getProductId())) {
            return ResponseEntity.badRequest().body("Id Not Matched");
        }

        return ResponseEntity.ok(productService.update(product));
    }

    @Transactional
    @DeleteMapping("/seller/product/{id}/delete")
    public ResponseEntity delete(@PathVariable("id") String productId) {
       System.out.println("prodotto da cancellare: " + productId);
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @GetMapping(value = "product/secondhandProductList")
    public List<ProductClient> findByUser() {

        Page<ProductClient> prod = this.findAllAdmin(1,50);
        List<ProductClient> prodAdmin = new ArrayList<>();

        prod.forEach(product ->{
            if(product.getStatus() == 1){
                prodAdmin.add(product);
            }
        });
        return prodAdmin;
    }

    @Transactional
    @GetMapping(value = "product/newProductList")
    public List<ProductInfo> findBySupplier() {

        Page<ProductInfo> prod = this.findAll(1,50);
        List<ProductInfo> prodSupp = new ArrayList<>();

        prod.forEach(product ->{

            if (this.userService.findOneById(product.getIdUtente()).getRole().equals("ROLE_EMPLOYEE")){
                prodSupp.add(product);
            }

        });
        return prodSupp;
    }


    /*
     ADMIN's Methods
     */

    @Transactional
    @GetMapping(value = "product/adminlist")
    public List<ProductClient> findByAdmin() {

        Page<ProductClient> prod = this.findAllAdmin(1,50);
        List<ProductClient> prodAdmin = new ArrayList<>();

        prod.forEach(product ->{
            if(product.getStatus() == 0){
                prodAdmin.add(product);
            }
        });
        return prodAdmin;
    }

    @Transactional
    @PutMapping("/product/decline")
    public ResponseEntity decline(@Valid @RequestBody ProductClient product,
                                  BindingResult bindingResult) {

        product.setStatus(2);
        return ResponseEntity.ok(productService.updateProductAdmin(product));
    }
    @Transactional
    @PutMapping("/product/accept")
    public ResponseEntity accept(@Valid @RequestBody ProductClient product,
                                 BindingResult bindingResult) {

        userService.updateCredits(product.getProductPrice().intValue() * 10 * product.getProductStock(), product.getIdUtente());
        product.setStatus(1);

        return ResponseEntity.ok(productService.updateProductAdmin(product));
    }



    ///////////////////////////// Inserimento Prodotti  di default///////////////////
    @PostMapping("producto/api")
    public void createApi() {

        //////////// Creazione Categorie //////////////////////
        ProductCategory category0 = new ProductCategory();
        category0.setCategoryName("Books");
        category0.setCategoryType(0);
        categoryService.save(category0);

        ProductCategory category1 = new ProductCategory();
        category1.setCategoryName("Food");
        category1.setCategoryType(1);
        categoryService.save(category1);

        ProductCategory category2 = new ProductCategory();
        category2.setCategoryName("Clothes");
        category2.setCategoryType(2);
        categoryService.save(category2);

        ProductCategory category3 = new ProductCategory();
        category3.setCategoryName("Drink");
        category3.setCategoryType(3);
        categoryService.save(category3);

        ProductCategory category4 = new ProductCategory();
        category4.setCategoryName("Hi-Tech");
        category4.setCategoryType(4);
        categoryService.save(category4);

        ProductCategory category5 = new ProductCategory();
        category5.setCategoryName("Home");
        category5.setCategoryType(5);
        categoryService.save(category5);



        //////////////////// Creazione Client GIUSEPPE ///////////////////
        Client client = new Client();
        client.setEmail("giuseppe@gmail.com");
        client.setName("Giuseppe");
        client.setAddress("via turati");
        client.setSurname("Villacapo");
        client.setPassword("12345");
        client.setPhone("39394737347");
        client.setRole("ROLE_CUSTOMER");
        client.setActive(true);
        Client client_service = userService.saveClient(client);

        //////////////////// Creazione Client Laura ///////////////////
        Supplier supplier4 = new Supplier();
        supplier4.setEmail("laura@gmail.com");
        supplier4.setName("Piedi Pazzi");
        supplier4.setAddress("via permalosa");
        supplier4.setSurname("Domenici");
        supplier4.setPassword("12345");
        supplier4.setPhone("35432355235");
        supplier4.setRole("ROLE_EMPLOYEE");
        supplier4.setShopName("Piedi Pazzi");
        supplier4.setVAT("3241113543443");
        supplier4.setActive(true);
        User supplier4_service = userService.save(supplier4);

        //////////////////// Creazione Suppliere Massimo ///////////////////
        Supplier supplier1 = new Supplier();
        supplier1.setEmail("massimo@gmail.com");
        supplier1.setName("Kebab da Massimo");
        supplier1.setAddress("via alessandrello");
        supplier1.setSurname("Domenici");
        supplier1.setPassword("12345");
        supplier1.setPhone("3543545235");
        supplier1.setRole("ROLE_EMPLOYEE");
        supplier1.setShopName("Kebab da Massimo");
        supplier1.setVAT("324111123443");
        supplier1.setActive(true);
        User supplier1_service = userService.save(supplier1);

        //////////////////// Creazione Suppliere Salvo ///////////////////
        Supplier supplier2 = new Supplier();
        supplier2.setEmail("salvo@gmail.com");
        supplier2.setName("Mastro Artigiano Salvo");
        supplier2.setAddress("via piave");
        supplier2.setSurname("Vaticosta");
        supplier2.setPassword("12345");
        supplier2.setPhone("354421235");
        supplier2.setRole("ROLE_EMPLOYEE");
        supplier2.setShopName("Mastro Artigiano Salvo");
        supplier2.setVAT("343534224553");
        supplier2.setActive(true);
        User supplier2_service = userService.save(supplier2);

        //////////////////// Creazione Suppliere Pietro ///////////////////
        Supplier supplier3 = new Supplier();
        supplier3.setEmail("pietro@gmail.com");
        supplier3.setName("Pielettro");
        supplier3.setAddress("via alessandrello");
        supplier3.setSurname("Pieraccioni");
        supplier3.setPassword("12345");
        supplier3.setPhone("354321235");
        supplier3.setRole("ROLE_EMPLOYEE");
        supplier3.setShopName("Pielettro");
        supplier3.setVAT("34234243201432");
        supplier3.setActive(true);
        User supplier3_service = userService.save(supplier3);


        //////////////////// Creazione Suppliere manager ///////////////////
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setName("Admin");
        admin.setSurname("Admin");
        admin.setPassword("12345");
        admin.setPhone("326521235");
        admin.setRole("ROLE_MANAGER");
        admin.setActive(true);
        User admin_service = userService.save(admin);




        // Prodotto 1 Aspirapolvere
        ProductClient prod = new ProductClient();
        prod.setProductId("1");
        prod.setCategoryType(5);
        prod.setIdUtente(client_service.getId());
        prod.setNameUtente("Giuseppe");
        prod.setProductDescription("Un aspirapolvere impolverato, necessita di un altro aspirapolvere per essere pulito.");
        prod.setProductName("Aspirapolvere impolverato");
        prod.setProductPrice(BigDecimal.valueOf(36));
        prod.setProductStatus(0);
        prod.setProductStock(1);
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(46363);
        prod.setProductimage(b.array());
        prod.setType(0);
        prod.setProductIcon("../../../assets/img/aspira.jpg");
        prod.setStatus(1);
        productService.save(prod);

        // Prodotto 2 Cassetta
        ProductClient prod2 = new ProductClient();
        prod2.setProductId("2");
        prod2.setCategoryType(5);
        prod2.setIdUtente(client_service.getId());
        prod2.setNameUtente("Giuseppe");
        prod2.setProductDescription("Cassetta degli attrezzi in ottimo stato per il fai-da-te");
        prod2.setProductName("Cassetta degli attrezzi usata");
        prod2.setProductPrice(BigDecimal.valueOf(25));
        prod2.setProductStatus(0);
        prod2.setProductStock(1);
        ByteBuffer c = ByteBuffer.allocate(4);
        c.putInt(46363);
        prod2.setProductimage(c.array());
        prod2.setType(0);
        prod2.setProductIcon("../../../assets/img/cassetta.jpg");
        prod2.setStatus(1);
        productService.save(prod2);

        // Prodotto 3 kebab
        ProductInfo prod3 = new ProductInfo();
        prod3.setProductId("3");
        prod3.setCategoryType(1);
        prod3.setIdUtente(supplier1_service.getId());
        prod3.setNameUtente("Kebab da Massimo");
        prod3.setProductDescription("Kebab surgelato, comodo e pratico da portare dove vuoi.");
        prod3.setProductName("Kebab surgelato");
        prod3.setProductPrice(BigDecimal.valueOf(4));
        prod3.setProductStatus(0);
        prod3.setProductStock(30);
        ByteBuffer d = ByteBuffer.allocate(4);
        d.putInt(46363);
        prod3.setProductimage(d.array());
        prod3.setType(1);
        prod3.setProductIcon("../../../assets/img/kebab.jpeg");
        productService.save(prod3);


        // Prodotto 4 falafel
        ProductInfo prod4 = new ProductInfo();
        prod4.setProductId("4");
        prod4.setCategoryType(1);
        prod4.setIdUtente(supplier1_service.getId());
        prod4.setNameUtente("Kebab da Massimo");
        prod4.setProductDescription("Confezione da 6 falafel surgelati da gustare quando vuoi e con chi vuoi.");
        prod4.setProductName("Falafel surgelati");
        prod4.setProductPrice(BigDecimal.valueOf(3));
        prod4.setProductStatus(0);
        prod4.setProductStock(20);
        ByteBuffer e = ByteBuffer.allocate(4);
        e.putInt(46363);
        prod4.setProductimage(e.array());
        prod4.setType(1);
        prod4.setProductIcon("../../../assets/img/falafel.jpeg");
        productService.save(prod4);

        // Prodotto 5 piatti
        ProductInfo prod5 = new ProductInfo();
        prod5.setProductId("5");
        prod5.setCategoryType(5);
        prod5.setIdUtente(supplier2_service.getId());
        prod5.setNameUtente("Mastro Artigiano Salvo");
        prod5.setProductDescription("Set di piatti in ceramica dipinti a mano. Edizione limitata firmata dal famoso artista Donini");
        prod5.setProductName("Set di piatti in ceramica");
        prod5.setProductPrice(BigDecimal.valueOf(64));
        prod5.setProductStatus(0);
        prod5.setProductStock(14);
        ByteBuffer f = ByteBuffer.allocate(4);
        f.putInt(46363);
        prod5.setProductimage(f.array());
        prod5.setType(1);
        prod5.setProductIcon("../../../assets/img/piatti.jpg");
        productService.save(prod5);

        // Prodotto 6 urna
        ProductInfo prod6 = new ProductInfo();
        prod6.setProductId("6");
        prod6.setCategoryType(5);
        prod6.setIdUtente(supplier2_service.getId());
        prod6.setNameUtente("Mastro Artigiano Salvo");
        prod6.setProductDescription("Urna per le ceneri dei tuoi cari per tenerli sempre vicino a te. Con un design accattivante del famosissimo artista Donini");
        prod6.setProductName("Urna per le ceneri");
        prod6.setProductPrice(BigDecimal.valueOf(40));
        prod6.setProductStatus(0);
        prod6.setProductStock(18);
        ByteBuffer h = ByteBuffer.allocate(4);
        h.putInt(46363);
        prod6.setProductimage(h.array());
        prod6.setType(1);
        prod6.setProductIcon("../../../assets/img/urna.jpg");
        productService.save(prod6);

        // Prodotto 7 dolby
        ProductInfo prod7 = new ProductInfo();
        prod7.setProductId("7");
        prod7.setCategoryType(4);
        prod7.setIdUtente(supplier3_service.getId());
        prod7.setNameUtente("Pielettro");
        prod7.setProductDescription("Dolby 4.1 con ottime prestazioni per goderti il piacere del cinema direttamente a casa tua.");
        prod7.setProductName("Dolby 4.1");
        prod7.setProductPrice(BigDecimal.valueOf(40));
        prod7.setProductStatus(0);
        prod7.setProductStock(20);
        ByteBuffer x = ByteBuffer.allocate(4);
        x.putInt(46363);
        prod7.setProductimage(x.array());
        prod7.setType(1);
        prod7.setProductIcon("../../../assets/img/stereo.jpg");
        productService.save(prod7);

        // Prodotto 8 calze1
        ProductInfo prod8 = new ProductInfo();
        prod8.setProductId("8");
        prod8.setCategoryType(2);
        prod8.setIdUtente(supplier4_service.getId());
        prod8.setNameUtente("Piedi Pazzi");
        prod8.setProductDescription("Calze a tema, per dimostrare l'interesse nella politica.");
        prod8.setProductName("Calze Trump");
        prod8.setProductPrice(BigDecimal.valueOf(14));
        prod8.setProductStatus(0);
        prod8.setProductStock(30);
        ByteBuffer y = ByteBuffer.allocate(4);
        y.putInt(46363);
        prod8.setProductimage(y.array());
        prod8.setType(1);
        prod8.setProductIcon("../../../assets/img/calze1.jpg");
        productService.save(prod8);

        // Prodotto 9 calze2
        ProductInfo prod9 = new ProductInfo();
        prod9.setProductId("9");
        prod9.setCategoryType(2);
        prod9.setIdUtente(supplier4_service.getId());
        prod9.setNameUtente("Piedi Pazzi");
        prod9.setProductDescription("Le calze in cotone fatte a mano  giuste per rispecchiare il tuo stile. JUST BUY IT.");
        prod9.setProductName("Calze simil nike");
        prod9.setProductPrice(BigDecimal.valueOf(15));
        prod9.setProductStatus(0);
        prod9.setProductStock(35);
        ByteBuffer k = ByteBuffer.allocate(4);
        k.putInt(46363);
        prod9.setProductimage(k.array());
        prod9.setType(1);
        prod9.setProductIcon("../../../assets/img/calze2.jpeg");
        productService.save(prod9);
    }

}
