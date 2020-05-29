import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CardComponent} from './pages/card/card.component';
import {LoginComponent} from './pages/login/login.component';
import {SignupComponent} from './pages/signup/signup.component';
import {DetailComponent} from './pages/product-detail/detail.component';
import {CartComponent} from './pages/cart/cart.component';
import {AuthGuard} from './_guards/auth.guard';
import {OrderComponent} from './pages/order/order.component';
import {OrderDetailComponent} from './pages/order-detail/order-detail.component';
import {ProductListComponent} from './pages/product-list/product.list.component';
import {UserDetailComponent} from './pages/user-edit/user-detail.component';
import {ProductEditComponent} from './pages/product-edit/product-edit.component';
import {Role} from './enum/Role';
import {SignupSupplierComponent} from './pages/signup-supplier/signup-supplier.component';
import {ProductListCustomerComponent} from './pages/product-list-customer/product-list-customer.component';
import {AdminListComponent} from './pages/admin-list/admin-list.component';
import {UserEditSupplierComponent} from './pages/user-edit-supplier/user-edit-supplier.component';
import {CheckoutComponent} from './pages/checkout/checkout.component';
import {UploadImageComponent} from './pages/upload-image/upload-image.component';
import {NewProductsComponent} from './pages/new-products/new-products.component';
import {SecondHandProductsComponent} from './pages/second-hand-products/second-hand-products.component';
import {InsertProductsSupplierComponent} from './pages/insert-products-supplier/insert-products-supplier.component';
import {InsertProductsCustomerComponent} from './pages/insert-products-customer/insert-products-customer.component';


const routes: Routes = [
  {path: '', redirectTo: '/product', pathMatch: 'full'},
  {path: 'product/:id', component: DetailComponent},
  {path: 'category/:id', component: CardComponent},
  {path: 'product', component: CardComponent},
  {path: 'category', component: CardComponent},
  {path: 'new', component: NewProductsComponent},
  {path: 'second-hand', component: SecondHandProductsComponent},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LoginComponent},
  {path: 'register', component: SignupComponent},
  {path: 'registerSupplier', component: SignupSupplierComponent},
  {path: 'cart', component: CartComponent},
  {path: 'success', component: SignupComponent},
  {path: 'order/:id', component: OrderDetailComponent, canActivate: [AuthGuard]},
  {path: 'order', component: OrderComponent, canActivate: [AuthGuard]},
  {path: 'seller', redirectTo: 'seller/product', pathMatch: 'full'},
  {
    path: 'seller/product',
    component: ProductListComponent,
    canActivate: [AuthGuard],
    data: {roles: [Role.Manager, Role.Employee]}
  },
  {
    path: 'customer/product',
    component: ProductListCustomerComponent,
    canActivate: [AuthGuard],
    data: {roles: [Role.Manager, Role.Customer]}
  },
  {
    path: 'profile',
    component: UserDetailComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'profileSupplier',
    component: UserEditSupplierComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'user/product/:id/edit',
    component: ProductEditComponent,
    canActivate: [AuthGuard],
    data: {roles: [Role.Manager, Role.Employee, Role.Customer]}
  },
  {
    path: 'seller/product/:id/new',
    component: ProductEditComponent,
    canActivate: [AuthGuard],
    data: {roles: [Role.Employee]}
  },
  {
    path: 'admin/list',
    component: AdminListComponent,
    canActivate: [AuthGuard],
    data: {roles: [Role.Manager]}
  },
  {
    path: 'checkout',
    component: CheckoutComponent,
    canActivate: [AuthGuard],
    data: {roles: [Role.Customer]}
  },
  {
    path: 'image',
    component: UploadImageComponent,
    /*    canActivate: [AuthGuard],
        data: {roles: [Role.Customer, Role.Employee]}*/
  },
  {
    path: 'seller/insertProduct',
    component: InsertProductsSupplierComponent,
  },
  {
    path: 'customer/insertProduct',
    component: InsertProductsCustomerComponent,
  },



];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)// {onSameUrlNavigation: 'reload'}
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
