import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {Router} from '@angular/router';
import {Client} from '../../models/Client';
import {JwtResponse} from '../../response/JwtResponse';
import {Supplier} from '../../models/Supplier';
import {Role} from '../../enum/Role';


@Component({
  selector: 'app-user-edit-supplier',
  templateUrl: './user-edit-supplier.component.html',
  styleUrls: ['./user-edit-supplier.component.css']
})
export class UserEditSupplierComponent implements OnInit {



  constructor(private userService: UserService,
              private router: Router) {
  }

  supplier = new Supplier();

  Role = Role;
  currentUser: JwtResponse;

  ngOnInit() {
    this.userService.currentUser.subscribe(supplier => {
      this.currentUser = supplier;
    });

    const account = this.userService.currentUserValue.account;

    this.userService.getSupplier(account).subscribe( u => {
      console.log(u);
      this.supplier = u;
      this.supplier.password = '';
    }, e => {

    });
  }



  onSubmit() {
    this.userService.pwsUtente = this.supplier.password;

    this.userService.updateSupplier(this.supplier).subscribe(u => {
      this.userService.nameTerms.next(u.name);
      let url = '/';
      if (this.supplier.role !== Role.Customer) {
        url = '/seller';
      }
      this.router.navigateByUrl(url);
    }, _ => {});
  }
}
