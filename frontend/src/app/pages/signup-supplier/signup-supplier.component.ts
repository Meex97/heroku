import { Component, OnInit } from '@angular/core';
import {Location} from '@angular/common';
import {Supplier} from '../../models/Supplier';
import {UserService} from '../../services/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-signup-supplier',
  templateUrl: './signup-supplier.component.html',
  styleUrls: ['./signup-supplier.component.css']
})
export class SignupSupplierComponent implements OnInit {

  supplier: Supplier;

  constructor( private location: Location,
               private userService: UserService,
               private router: Router) {
    this.supplier = new Supplier();

  }

  ngOnInit() {
  }

  onSubmit() {
    this.userService.signUpSupplier(this.supplier).subscribe(u => {
        this.router.navigate(['/login']);
      },
      e => {});
  }
}




