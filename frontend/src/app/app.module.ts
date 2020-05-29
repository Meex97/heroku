import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {NavigationComponent} from './parts/navigation/navigation.component';
import {CardComponent} from './pages/card/card.component';
import {PaginationComponent} from './parts/pagination/pagination.component';
import {AppRoutingModule} from './app-routing.module';
import {LoginComponent} from './pages/login/login.component';
import {SignupComponent} from './pages/signup/signup.component';
import {DetailComponent} from './pages/product-detail/detail.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {CartComponent} from './pages/cart/cart.component';
import {CookieService} from 'ngx-cookie-service';
import {ErrorInterceptor} from './_interceptors/error-interceptor.service';
import {JwtInterceptor} from './_interceptors/jwt-interceptor.service';
import {OrderComponent} from './pages/order/order.component';
import {OrderDetailComponent} from './pages/order-detail/order-detail.component';
import {ProductListComponent} from './pages/product-list/product.list.component';
import {UserDetailComponent} from './pages/user-edit/user-detail.component';
import {ProductEditComponent} from './pages/product-edit/product-edit.component';
import { SignupSupplierComponent } from './pages/signup-supplier/signup-supplier.component';
import { ProductListCustomerComponent } from './pages/product-list-customer/product-list-customer.component';
import { AdminListComponent } from './pages/admin-list/admin-list.component';
import { UserEditSupplierComponent } from './pages/user-edit-supplier/user-edit-supplier.component';
import { CheckoutComponent } from './pages/checkout/checkout.component';
import {RouterModule} from '@angular/router';
import {FileUploadModule} from 'ng2-file-upload';
import { UploadImageComponent } from './pages/upload-image/upload-image.component';
import { ToolbarComponent } from './parts/toolbar/toolbar.component';

import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout';
import {MatIconModule} from '@angular/material/icon';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatCardModule, MatCheckboxModule, MatFormFieldModule, MatInputModule, MatListModule, MatMenuModule} from '@angular/material';
import { NewProductsComponent } from './pages/new-products/new-products.component';
import { SecondHandProductsComponent } from './pages/second-hand-products/second-hand-products.component';
import { InsertProductsSupplierComponent } from './pages/insert-products-supplier/insert-products-supplier.component';
import { InsertProductsCustomerComponent } from './pages/insert-products-customer/insert-products-customer.component';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    CardComponent,
    PaginationComponent,
    LoginComponent,
    SignupComponent,
    DetailComponent,
    CartComponent,
    OrderComponent,
    OrderDetailComponent,
    ProductListComponent,
    UserDetailComponent,
    ProductEditComponent,
    SignupSupplierComponent,
    ProductListCustomerComponent,
    AdminListComponent,
    UserEditSupplierComponent,
    CheckoutComponent,
    UploadImageComponent,
    ToolbarComponent,
    NewProductsComponent,
    SecondHandProductsComponent,
    InsertProductsSupplierComponent,
    InsertProductsCustomerComponent,



  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    FileUploadModule,

    MatIconModule,
    MatButtonModule,
    MatSidenavModule,
    MatToolbarModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    MatListModule,
    MatMenuModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatCheckboxModule,

  ],
  providers: [CookieService,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
