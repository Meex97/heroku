import {Role} from "../enum/Role";

export class Client {

     email: string;

     password: string;

     name: string;

     surname: string;

     phone: string;

     address: string;

     active: boolean;

     role: string;

     credits: number;

     google: boolean;

    constructor() {
        this.active = true;
        this.google = false;
        this.role = Role.Customer;
        this.credits = 0;
    }

    createClient(email: string, name: string, surname: string, password: string ) {
      this.email = email;
      this.name = name;
      this.surname = surname;
      this.password = password;
      this.address = ' ';
      this.phone = ' ';
      this.google = true;
    }

}
