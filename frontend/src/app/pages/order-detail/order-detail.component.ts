import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {OrderService} from '../../services/order.service';
import {Order} from '../../models/Order';
import {ActivatedRoute} from '@angular/router';
import {JwtResponse} from '../../response/JwtResponse';
import {UserService} from '../../services/user.service';
import {Role} from '../../enum/Role';

@Component({
    selector: 'app-order-detail',
    templateUrl: './order-detail.component.html',
    styleUrls: ['./order-detail.component.css']
})
export class OrderDetailComponent implements OnInit {

    constructor(private orderService: OrderService,
                private route: ActivatedRoute,
                private userService: UserService) {
    }

    order$: Observable<Order>;

  Role = Role;
  currentUser: JwtResponse;
    ngOnInit() {
      this.userService.currentUser.subscribe(user => {
        this.currentUser = user;
        });
        // this.items$ = this.route.paramMap.pipe(
        //     map(paramMap =>paramMap.get('id')),
        //     switchMap((id:string) => this.orderService.show(id))
        // )
      this.order$ = this.orderService.show(this.route.snapshot.paramMap.get('id'));

      /*this.order$.content.products.forEach(function(prod) {
        prod.productimage = 'data:image/jpeg;base64,' + prod.productimage;
      });*/

      /*this.order$.forEach(prod => {
        prod.productimage = 'data:image/jpeg;base64,' + prod.productimage;
      });*/
    }

}
