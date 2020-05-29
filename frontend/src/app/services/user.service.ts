import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {apiUrl} from '../../environments/environment';
import {BehaviorSubject, Observable, of, Subject} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {JwtResponse} from '../response/JwtResponse';
// @ts-ignore
import {CookieService} from 'ngx-cookie-service';

import {Client} from '../models/Client';
import {Supplier} from '../models/Supplier';


@Injectable({
    providedIn: 'root'
})
export class UserService {

    idUtente: any;
    pwsUtente: any;

    private currentUserSubject: BehaviorSubject<JwtResponse>;
    public currentUser: Observable<JwtResponse>;
    public nameTerms = new Subject<string>();
    public name$ = this.nameTerms.asObservable();
    constructor(private http: HttpClient,
                private cookieService: CookieService) {
        const memo = localStorage.getItem('currentUser');
        this.currentUserSubject = new BehaviorSubject<JwtResponse>(JSON.parse(memo));
        this.currentUser = this.currentUserSubject.asObservable();
        cookieService.set('currentUser', memo);
    }


  get currentUserValue() {
        return this.currentUserSubject.value;
    }


    login(loginForm): Observable<JwtResponse> {
        const url = `${apiUrl}/login`;
        return this.http.post<JwtResponse>(url, loginForm).pipe(
            tap(client => {
                if (client && client.token) {
                    this.cookieService.set('currentUser', JSON.stringify(client));
                    if (loginForm.remembered) {
                        localStorage.setItem('currentUser', JSON.stringify(client));
                    }
                    this.nameTerms.next(client.name);
                    this.currentUserSubject.next(client);
                    return client;
                }
            }),
            catchError(this.handleError('Login Failed', null))
        );
    }

    logout() {
        this.currentUserSubject.next(null);
        localStorage.removeItem('currentUser');
        this.cookieService.delete('currentUser');
    }

    signUpClient(client: Client): Observable<Client> {
        const url = `${apiUrl}/registerClient`;
        return this.http.post<Client>(url, client);
    }

    updateClient(client: Client): Observable<Client> {
        const url = `${apiUrl}/profileClient`;
        return this.http.put<Client>(url, client);    }

    getClient(email: string): Observable<Client> {
        const url = `${apiUrl}/profile/${email}`;
        return this.http.get<Client>(url);
    }


    signUpSupplier(supplier: Supplier): Observable<Supplier> {
      const url = `${apiUrl}/registerSupplier`;
      return this.http.post<Supplier>(url, supplier);
    }

    updateSupplier(supplier: Supplier): Observable<Supplier> {
      const url = `${apiUrl}/profileSupplier`;
      return this.http.put<Supplier>(url, supplier);    }


    getSupplier(email: string): Observable<Supplier> {
      const url = `${apiUrl}/profile/${email}`;
      return this.http.get<Supplier>(url);
    }

    /**
     * Handle Http operation that failed.
     * Let the app continue.
     * @param operation - name of the operation that failed
     * @param result - optional value to return as the observable result
     */
    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            console.log(error); // log to console instead

            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }


  updateCredits(client: Client, discount: number, id: number): Observable<Client> {
      console.log(discount, id);
      const url = `${apiUrl}/updateCredits/${discount}/${id}`;
      return this.http.put<Client>(url, client);

  }


}
