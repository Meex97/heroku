import {ProductInOrder} from "./ProductInOrder";

export class ProductClient {
  productId: string;
  productName: string;
  productPrice: number;
  productStock: number;
  productDescription: string;
  productIcon: string;
  productStatus: number; // 0: onsale 1: offsale
  categoryType: number;
  createTime: string;
  updateTime: string;
  idUtente: number;
  status: number;
  productimage: any;
  oldimg: any;



  constructor(productInOrder?: ProductInOrder) {
    if (productInOrder) {
      this.productId = productInOrder.productId;
      this.productName = productInOrder.productName;
      this.productPrice = productInOrder.productPrice;
      this.productStock = productInOrder.productStock;
      this.productDescription = productInOrder.productDescription;
      this.productIcon = productInOrder.productIcon;
      this.categoryType = productInOrder.categoryType;
      this.productStatus = 0;
      this.status = 0;
    } else {
      this.productId = '';
      this.productName = '';
      this.productPrice = 0;
      this.productStock = 0;
      this.productDescription = '';
      this.productIcon = '';
      this.categoryType = 0;
      this.productStatus = 0;
      this.status = 0;
    }
  }

}

