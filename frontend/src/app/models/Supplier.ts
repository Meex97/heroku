import {Role} from "../enum/Role";

export class Supplier {

  email: string;

  password: string;

  name: string;

  surname: string;

  phone: string;

  address: string;

  active: boolean;

  role: string;

  vat: string;

  shopName: string;

  constructor(){
    this.active = true;
    // this.role = 'ROLE_SUPPLIER';
    this.role = Role.Employee;

  }
}
