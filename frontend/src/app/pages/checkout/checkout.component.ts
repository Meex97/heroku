import {Component, NgModule, OnInit} from '@angular/core';
import {JwtResponse} from '../../response/JwtResponse';
import {ProductInfo} from '../../models/productInfo';
import {Subscription} from 'rxjs';
import {UserService} from '../../services/user.service';
import {ProductService} from '../../services/product.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Role} from '../../enum/Role';
import {CartService} from '../../services/cart.service';
import {Client} from '../../models/Client';
import {FormsModule, NgForm} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {PaymentMethodsType} from '../../enum/PaymentMethodsType';
import {HttpClient} from '@angular/common/http';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule
  ]
})

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})

export class CheckoutComponent implements OnInit {

  Role = Role;
  currentUser: JwtResponse;
  page: Array<ProductInfo>;

  client = new Client();

  productInOrders = [];

  marked = false;
  theCheckbox = false;

  total: number;
  discount: number;
  amo: string;
  wtf = true;

  paymentMethodType: PaymentMethodsType;

  difference: number;

  public payuform: any = {};
  disablePaymentButton = true;



  constructor(private userService: UserService,
              private productService: ProductService,
              private route: ActivatedRoute,
              private cartService: CartService,
              private router: Router,
              private http: HttpClient) {

    // this.checked = true;
  }

  ngOnInit() {
    this.userService.currentUser.subscribe(client => {
      this.currentUser = client;
    });

    this.userService.getClient(this.currentUser.account).subscribe(u => {
      this.client = u;
    });

    this.total = 0;
    this.cartService.getCart().subscribe(prods => {
      this.total = prods.reduce((prev, cur) => prev + cur.count * cur.productPrice, 0);
      this.productInOrders = prods;
      this.difference = this.total;

      // this.amo = `${this.difference}`;
    });

    this.amo = '4';
    this.confirmPayment();
  }

  checkout() {
    this.disablePaymentButton = false;

    this.cartService.checkout(this.currentUser.account).subscribe(
      _ => {
        this.productInOrders = [];
      },
      error1 => {
        console.log('Checkout Cart Failed');
      });

    if (this.marked) {
      this.userService.updateCredits(this.client, this.discount, this.currentUser.id).subscribe(u => {
      }, _ => {});
    }
  }

  confirmPayment() {
    const paymentPayload = {
      email: this.currentUser.account,
      name: this.currentUser.name,
      phone: this.currentUser.phone,
      productInfo: this.amo,
      amount:  this.amo
    };
    return this.http.post<any>('http://localhost:8080/api/payment/payment-details', paymentPayload).subscribe(
      data => {
        console.log(data);
        this.payuform.txnid = data.txnId;
        this.payuform.surl = data.sUrl;
        this.payuform.furl = data.fUrl;
        this.payuform.key = data.key;
        this.payuform.hash = data.hash;

      }, error1 => {
        console.log(error1);
      });
  }

  toggleVisibility(e) {
    this.theCheckbox = e.target.checked;
    this.marked = e.target.checked;
    this.discount = this.client.credits / 10 ;
    this.total = this.productInOrders.reduce((prev, cur) => prev + cur.count * cur.productPrice, 0);
    if (this.total < this.discount ) {
      this.discount = this.total;
    }

    this.difference = this.total - this.discount;
  }

  ok() {
    this.router.navigate(['/']);
  }
}
