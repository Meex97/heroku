import {Component, OnDestroy, OnInit} from '@angular/core';
// import {prod, products} from '../shared/mockData';
import {ProductService} from '../../services/product.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {JwtResponse} from '../../response/JwtResponse';
import {Role} from '../../enum/Role';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit, OnDestroy {


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
    if (this.route.snapshot.queryParamMap.get('page')) {
      const currentPage = +this.route.snapshot.queryParamMap.get('page');
      const size = +this.route.snapshot.queryParamMap.get('size');
      this.getProds(currentPage, size);
    } else {
      this.getProds();
    }
  }

  getProds(page: number = 1, size: number = 6) {
    if (this.route.snapshot.url.length === 1) {
      this.productService.getAllInPage(+page, +size)
        // tslint:disable-next-line:no-shadowed-variable
        .subscribe(page => {
          this.page = page;

          this.page.content.forEach(function(prod) {
            prod.productimage = 'data:image/jpeg;base64,' + prod.productimage;
          });

          if (this.currentUser && this.currentUser.role === Role.Manager) {
            this.title = 'ToShop\'s Items';
          } else {
            this.title = 'Get Whatever You Want!';
          }
        });
    }  else { //  /category/:id
      const type = this.route.snapshot.url[1].path;
      this.productService.getCategoryInPage(+type, page, size)
        .subscribe(categoryPage => {
          this.title = 'Get Whatever You Want!';
          this.page = categoryPage;

          this.page.content.forEach(function(prod) {
            prod.productimage = 'data:image/jpeg;base64,' + prod.productimage;
          });
        });
    }

  }

}
