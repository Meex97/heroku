import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {Client} from '../../models/Client';
import {Router} from '@angular/router';
import {Observable, Subject} from 'rxjs';
import {Role} from '../../enum/Role';
import {JwtResponse} from '../../response/JwtResponse';

@Component({
    selector: 'app-user-detail',
    templateUrl: './user-detail.component.html',
    styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {




    constructor(private userService: UserService,
                private router: Router) {
    }

    client = new Client();

    Role = Role;
    currentUser: JwtResponse;

    ngOnInit() {
      this.userService.currentUser.subscribe(client => {
        this.currentUser = client;
      });

      const account = this.userService.currentUserValue.account;

      this.userService.getClient(account).subscribe( u => {
            console.log(u);
            this.client = u;
            this.client.password = '';
            if (u.google) {
              this.client.password = u.email;
            }
        }, e => {

        });
    }

    onSubmit() {
        this.userService.pwsUtente = this.client.password;

        this.userService.updateClient(this.client).subscribe(u => {
            this.userService.nameTerms.next(u.name);
            let url = '/';
            if (this.client.role !== Role.Customer) {
                url = '/seller';
            }
            this.router.navigateByUrl(url);
        }, _ => {});
    }

}
