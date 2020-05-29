import { Component, OnInit } from '@angular/core';
import {ProductInfo} from '../../models/productInfo';
import {JwtResponse} from '../../response/JwtResponse';
import {ProductService} from '../../services/product.service';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-insert-products-supplier',
  templateUrl: './insert-products-supplier.component.html',
  styleUrls: ['./insert-products-supplier.component.css']
})
export class InsertProductsSupplierComponent implements OnInit {

  selectedFile: File;
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;

  product: ProductInfo;
  private currentUser: JwtResponse;
  model: any = {
    username: '',
    password: '',
    googleIdToken: null,
    remembered: false
  };

  fileData: File = null;

  constructor(private productService: ProductService,
              private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.product = new ProductInfo();
  }

  productId: string;

  ngOnInit() {

    this.userService.currentUser.subscribe(supplier => {
      this.currentUser = supplier;
    });
    this.product.idUtente = this.currentUser.id;
    this.product.nameUtente = this.currentUser.name;
  }

  onSubmit() {
    this.product.productStatus = 0;
    this.add();
  }


  add() {
    this.product.productimage = ' ';
    this.product.type = 1;
    this.productService.create(this.product).subscribe(prod => {
        this.onUpload();
     },
      e => {});

  }

  // Gets called when the user selects an image
  public onFileChanged(event) {
    // Select File
    this.selectedFile = event.target.files[0];
    this.fileData = event.target.files[0] as File;
    this.preview();
  }

  preview() {
    // Show preview
    var mimeType = this.fileData.type;
    if (mimeType.match(/image\/*/) == null) {
      return;
    }

    var reader = new FileReader();
    reader.readAsDataURL(this.fileData);
    reader.onload = (_event) => {
      this.product.productimage = reader.result;
    }
  }

  // Gets called when the user clicks on submit to upload the image
  onUpload() {
    this.userService.logout();

    // FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
    const uploadImageData = new FormData();
    uploadImageData.append('image', this.selectedFile, this.product.productId);

    this.httpClient.post('http://localhost:8080/api/image/upload', uploadImageData, { observe: 'response' })
      .subscribe((response) => {
          if (response.status === 200) {
            this.message = 'Image uploaded successfully';
          } else {
            this.message = 'Image not uploaded successfully';
          }
        }
      );

    this.model.username = this.userService.idUtente;
    this.model.password = this.userService.pwsUtente;
    this.userService.login(this.model).subscribe(client => {
      console.log(client);
      this.router.navigate(['/seller']);
    });
  }
  // Gets called when the user clicks on retieve image button to get the image from back end
  getImage() {
    // Make a call to Sprinf Boot to get the Image Bytes.
    this.httpClient.get('http://localhost:8080/api/image/get/' + this.imageName)
      .subscribe(
        res => {
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
        }
      );
  }

}
