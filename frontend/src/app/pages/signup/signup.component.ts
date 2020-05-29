import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Location} from '@angular/common';
import {Client} from '../../models/Client';
import {UserService} from '../../services/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  @ViewChild('googleRef', {static: true}) googleRefElement: ElementRef;


  client: Client;

  constructor( private location: Location,
               private userService: UserService,
               private router: Router) {
    this.client = new Client();

  }

  ngOnInit() {
  }

  onSubmit() {
    this.userService.signUpClient(this.client).subscribe(u => {
      this.router.navigate(['/login']);
    },
        e => {});
  }

}
