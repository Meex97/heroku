import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {ProductService} from '../../services/product.service';
import {ActivatedRoute} from '@angular/router';
import {JwtResponse} from '../../response/JwtResponse';
import {ProductClient} from '../../models/ProductClient';
import {Subscription} from 'rxjs';
import {Role} from '../../enum/Role';

@Component({
  selector: 'app-new-products',
  templateUrl: './new-products.component.html',
  styleUrls: ['./new-products.component.css']
})
export class NewProductsComponent  implements OnInit, OnDestroy {


  title: string;
  page: any;
  private paramSub: Subscription;
  private querySub: Subscription;
  currentUserSubscription: Subscription;
  currentUser: JwtResponse;
  root = '/';
  Role = Role;

  constructor(private productService: ProductService,
              private route: ActivatedRoute,
              private userService: UserService) {

  }


  ngOnInit() {
    this.currentUserSubscription = this.userService.currentUser.subscribe(client => {
      this.currentUser = client;

      if (!client || client.role === Role.Customer ) {
        this.root = '/';
      } else {
        this.root = '/seller';
      }
    });


    this.querySub = this.route.queryParams.subscribe(() => {
      this.update();
    });
    this.paramSub = this.route.params.subscribe(() => {
      this.update();
    });

  }

  ngOnDestroy(): void {
    this.querySub.unsubscribe();
    this.paramSub.unsubscribe();
  }

  update() {
    this.getProds();
  }

  getProds() {
    this.productService.getAllInPageNew()
      // tslint:disable-next-line:no-shadowed-variable
      .subscribe(page => {
        this.page = page;

        this.page.forEach(prod => {
          prod.productimage = 'data:image/jpeg;base64,' + prod.productimage;
        });
        this.title = 'New products';
      });

  }

}
