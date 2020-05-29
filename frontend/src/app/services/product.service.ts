import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {ProductInfo} from '../models/productInfo';
import {apiUrl} from '../../environments/environment';
import {ProductClient} from '../models/ProductClient';

@Injectable({
    providedIn: 'root'
})
export class ProductService {

    private productUrl = `${apiUrl}/product`;
    private categoryUrl = `${apiUrl}/category`;

    private productUrlSupplier = `${apiUrl}/product/Supplier`;
    constructor(private http: HttpClient) {
    }



    getAllInPage(page: number, size: number): Observable<any> {
        const url = `${this.productUrl}?page=${page}&size=${size}`;
        return this.http.get(url)
            .pipe(
                // tap(_ => console.log(_)),
            );
    }

    getAllInPageSupplier(idUtente: number): Observable<any> {
      const url = `${this.productUrlSupplier}/${idUtente}`;
      return this.http.get(url)
        .pipe(
          // tap(_ => console.log(_)),
        );
    }

    getAllInPageAdmin(): Observable<any> {
      const url = `${this.productUrl}/adminlist`;
      return this.http.get(url)
        .pipe(
          // tap(_ => console.log(_)),
        );
    }

    getCategoryInPage(categoryType: number, page: number, size: number): Observable<any> {
        const url = `${this.categoryUrl}/${categoryType}?page=${page}&size=${size}`;
        return this.http.get(url).pipe(
            // tap(data => console.log(data))
        );
    }

  getTypeInPage(Type: number, page: number, size: number): Observable<any> {
    const url = `${this.categoryUrl}/${Type}?page=${page}&size=${size}`;
    return this.http.get(url).pipe(
      // tap(data => console.log(data))
    );
  }

    getDetail(id: string): Observable<ProductInfo> {
        const url = `${this.productUrl}/${id}`;
        return this.http.get<ProductInfo>(url).pipe(
            catchError(_ => {
                console.log('Get Detail Failed');
                return of(new ProductInfo());
            })
        );
    }

    update(productInfo: ProductInfo): Observable<ProductInfo> {
        const url = `${apiUrl}/product/${productInfo.productId}/edit`;
        console.log('sparing');
        console.log(url);
        return this.http.put<ProductInfo>(url, productInfo);
    }

    create(productInfo: ProductInfo): Observable<ProductInfo> {


        const url = `${apiUrl}/seller/producto/new`;
        return this.http.post<ProductInfo>(url, productInfo);

    }

  createProductCustomer(productInfo: ProductInfo): Observable<ProductInfo> {

    const url = `${apiUrl}/client/producto/new`;
    return this.http.post<ProductInfo>(url, productInfo);

  }


    delelte(productInfo: any): Observable<any> {
        const url = `${apiUrl}/seller/product/${productInfo.productId}/delete`;
        console.log(productInfo.productId);
        // return this.http.delete(url);
        return this.http.delete(url);
    }


    /**
     * Handle Http operation that failed.
     * Let the app continue.
     * @param operation - name of the operation that failed
     * @param result - optional value to return as the observable result
     */

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            console.error(error); // log to console instead

            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }


    /*
      Methods for Admin
     */

  declineProd(productClient: ProductClient): Observable<ProductClient> {
    const url = `${apiUrl}/product/decline`;
    return this.http.put<ProductClient>(url, productClient);
  }


  acceptProd(productClient: ProductClient) {
    const url = `${apiUrl}/product/accept`;
    return this.http.put<ProductClient>(url, productClient);
  }

  getAllInPageNew(): Observable<any> {
    const url = `${this.productUrl}/newProductList`;
    return this.http.get(url)
      .pipe(
        // tap(_ => console.log(_)),
      );
  }

  getAllInPageSecondHand(): Observable<any> {
    const url = `${this.productUrl}/secondhandProductList`;
    return this.http.get(url)
      .pipe(
        // tap(_ => console.log(_)),
      );
  }
}
