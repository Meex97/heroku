import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpEvent, HttpEventType, HttpHeaders, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UploadFileService} from '../../services/UploadFileService';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ProductService} from '../../services/product.service';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {ProductInfo} from '../../models/productInfo';
import {JwtResponse} from '../../response/JwtResponse';
import {Role} from '../../enum/Role';

@Component({
  selector: 'app-upload-image',
  templateUrl: './upload-image.component.html',
  styleUrls: ['./upload-image.component.css']
})


export class UploadImageComponent implements  OnInit {

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
    this.product.type = 1;
    this.productService.create/*ProductSupplier*/(this.product).subscribe(prod => {
      },
      e => {});

    this.onUpload();

  }

  // Gets called when the user selects an image
  public onFileChanged(event) {
    // Select File
    this.selectedFile = event.target.files[0];
  }
  // Gets called when the user clicks on submit to upload the image
  onUpload() {

    // this.userService.logout();

    // FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
    const uploadImageData = new FormData();
    uploadImageData.append('image', this.selectedFile, this.selectedFile.name);

    this.httpClient.post('http://localhost:8080/api/image/upload2', uploadImageData, { observe: 'response' })
      .subscribe((response) => {
          if (response.status === 200) {
            this.message = 'Image uploaded successfully';
          } else {
            this.message = 'Image not uploaded successfully';
          }
        }
      );
/*
    this.model.username = this.userService.idUtente;
    this.model.password = this.userService.pwsUtente;
    this.userService.login(this.model).subscribe(client => {
      console.log(client);
      this.router.navigate(['/seller']);
    });*/
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
