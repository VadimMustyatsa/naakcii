import {Injectable} from '@angular/core';
import {FoodList} from './foods.foodList.model';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class FoodsFoodListService {
  private productsLocal = 'assets/json/FoodList.json';
  private productsUrl = 'http://178.124.206.42:8080/api/product';

  constructor(private http: HttpClient) {
  }

  getFoodList(selectedSubCatListID, first, last) {  //FoodList[]
    let listID = '';
    selectedSubCatListID.map(id => {
      listID += String(id.id) + ',';
    });
    const dataGet = {SubcategoryList: listID, first: first, last: last};

    //return this.http.get<FoodList[]>(this.productsLocal)                      //для отладки - из файла
    return this.http.get<FoodList[]>(this.productsUrl, {params: dataGet})   //для реального запроса с бэка
      .map(productList => {
        return productList.map(product => {
          return {
            id: product['id'],
            name: product['name'],
            allPrice: product['price'],
            discount: product['discount'],
            totalPrice: product['discountPrice'],
            boxWeight: "",
            idStrore: product['chainId'],
            img: product['picture'],
            selected: false,
            selectAmount: 1
          };
        });
      });
  }
}


