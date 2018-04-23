import {Injectable} from '@angular/core';
import {FoodList} from './foods.foodList.model';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class FoodsFoodListService {
  private productsLocal = 'assets/json/FoodList.json';
  private productsUrl = 'http://178.124.206.54:8080/api/getProducts';

  constructor(private http: HttpClient) { }

  getFoodList(idCategory, idSubcategory) {  //FoodList[]
    console.log('FoodsFoodListService - getFoodList');
    const dataGet = {idCategory: idCategory, idSubcategory: idSubcategory};

    // return this.http.get<FoodList[]>(this.productsLocal)                      //для отладки - из файла
    return this.http.get<FoodList[]>(this.productsUrl, {params: dataGet})   //для реального запроса с бэка
      .map(productList => {
        return productList.map(product => {
          return {
            id: product['id'],
            name: product['name'],
            allPrice: product['price'],
            discount: product['discount'],
            totalPrice: product['discountPrice'],
            boxWeight: product['quantity'],
            idStrore: product['chainId'],
            img: product['icon'],
            selected: false,
            selectAmount: 1
          };
        });
      });
  }
}
