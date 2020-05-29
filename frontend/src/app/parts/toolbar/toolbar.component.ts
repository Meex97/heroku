import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {JwtResponse} from '../../response/JwtResponse';
import {Client} from '../../models/Client';
import {UserService} from '../../services/user.service';
import {Router} from '@angular/router';
import {Role} from '../../enum/Role';
@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit, OnDestroy{



  currentUserSubscription: Subscription;
  name$;
  name: string;
  currentUser: JwtResponse;
  root = '/';
  Role = Role;

  customer: Client;

  constructor(private userService: UserService,
              private router: Router,
  ) {

  }


  ngOnInit() {
    this.name$ = this.userService.name$.subscribe(aName => this.name = aName);
    this.currentUserSubscription = this.userService.currentUser.subscribe(client => {
      this.currentUser = client;

      if (!client || client.role === Role.Customer || client.role === Role.Manager) {
        this.root = '/';
      } else {
        this.root = '/seller';
      }

      if (client && client.role === Role.Customer) {
        this.userService.getClient(this.currentUser.account).subscribe(c => {
          this.customer = c;

        });
      }
    });
  }

  ngOnDestroy(): void {
    this.currentUserSubscription.unsubscribe();
    // this.name$.unsubscribe();
  }

  logout() {
    this.userService.logout();
    // this.router.navigate(['/login'], {queryParams: {logout: 'true'}} );
  }


}
