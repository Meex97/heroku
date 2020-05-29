import {ProductInfo} from "./productInfo";

export class ProductInOrder {
    productId: string;
    productName: string;
    productPrice: number;
    productStock: number;
    productDescription: string;
    productIcon: string;
    categoryType: number;
    count: number;
    idUtente: number;
    nameUtente: string;
    productimage: any;

    constructor(productInfo: ProductInfo, quantity = 1) {
        this.productId = productInfo.productId;
        this.productName = productInfo.productName;
        this.productPrice = productInfo.productPrice;
        this.productStock = productInfo.productStock;
        this.productDescription = productInfo.productDescription;
        this.productIcon = productInfo.productIcon;
        this.categoryType = productInfo.categoryType;
        this.count = quantity;
        this.idUtente = productInfo.idUtente;
        this.nameUtente = productInfo.nameUtente;
        this.productimage = productInfo.productimage;
    }
}
