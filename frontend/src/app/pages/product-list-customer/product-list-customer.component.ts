import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {ProductService} from '../../services/product.service';
import {ActivatedRoute} from '@angular/router';
import {JwtResponse} from '../../response/JwtResponse';
import {Subscription} from 'rxjs';
import {ProductInfo} from '../../models/productInfo';
import {Role} from '../../enum/Role';
import {CategoryType} from '../../enum/CategoryType';
import {ProductStatus} from '../../enum/ProductStatus';
import {ProductClient} from '../../models/ProductClient';

@Component({
  selector: 'app-product-list-customer',
  templateUrl: './product-list-customer.component.html',
  styleUrls: ['./product-list-customer.component.css']
})
export class ProductListCustomerComponent implements OnInit, OnDestroy {


  productId: number;

  constructor(private userService: UserService,
              private productService: ProductService,
              private route: ActivatedRoute) {
  }

  Role = Role;
  currentUser: JwtResponse;
  page: any;
  CategoryType = CategoryType;
  ProductStatus = ProductStatus;
  private querySub: Subscription;

  ngOnInit() {
    this.userService.currentUser.subscribe(client => {
      this.currentUser = client;
    });
    this.productId = this.currentUser.id;
    // console.log(this.productId);

    this.querySub = this.route.queryParams.subscribe(() => {
      this.update();
    });
  }

  ngOnDestroy(): void {
    this.querySub.unsubscribe();
  }

  update() {
      this.getProdsCustomer();
  }


  getProdsCustomer() {
    this.productService.getAllInPageSupplier(this.productId)
      .subscribe(page => {
        this.page = page;

        this.page.forEach(prod => {
          prod.productimage = 'data:image/jpeg;base64,' + prod.productimage;
        });
      });
  }



  remove(productClient: ProductClient) {

    this.page = this.page.filter(e => e.productId !== productClient.productId);

    this.productService.delelte(productClient).subscribe(_ => {
      },
      err => {
      });
  }


}
