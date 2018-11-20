import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';

import {Category} from '../category/foods.category.model';
import {FoodList} from '../foodList/foods.foodList.model';
import {SubCategory} from '../subCategory/foods.subCategory.model';
import {Storag} from '../Storage/foods.storage.model';

const PROTOCOL='http';
const PORT=8080;
const IP_BACK='178.124.206.42';

@Injectable()
export class ApiService {

  private baseUrl:string;
  constructor(private http:HttpClient) {
      this.baseUrl=window.location.hostname === 'localhost' ? `${PROTOCOL}://${IP_BACK}:${PORT}` : `${PROTOCOL}://${window.location.hostname}:${PORT}`;
   }

  public getAllCategory(){
    return this.http.get<Category[]>(this.baseUrl+'/api/category')
      .map(categoryList => {
        return categoryList.map(category => {
          return <Category>{
            id: category['id'],
            name: category['name'],
            icon: category['icon']
          };
        });
      });
  }

  public getApiFoodList(dataGet){
    return this.http.get<FoodList[]>(this.baseUrl+'/api/product', {params: dataGet})
    .map(productList => {
      return productList.map(product => {
        let index = product['picture'].indexOf('%');
        if (index !== -1) {
          let first = product['picture'].substring(0, index);
          let second = product['picture'].substring(index + 1);
          product['picture'] = first + '%25' + second;
        }
        return {
          id: product['id'],
          name: product['name'],
          allPrice: product['price'],
          discount: product['discount'],
          totalPrice: product['discountPrice'],
          boxWeight: '',
          idStrore: product['chainId'],
          img: product['picture'],
          selected: false,
          selectAmount: 1
        };
      });
    });  
  }

  public getSubByCategory(idCategory){
    return this.http.get<SubCategory[]>(this.baseUrl+'/api/subcategory/'+idCategory)
    .map(sabCatList => {
      return sabCatList.map(sabCut => {
        return {
          id: sabCut['id'],
          name: sabCut['name'],
          img: sabCut['img'],
          selected: false
        };
      });
    });
  }

  public getAllFoods(){
    return this.http.get<Storag[]>(this.baseUrl+'/api/chain')
      .map(chainList => {
        return chainList.map(chain => {
          return {
            id: chain['id'],
            name: chain['name'],
            link: chain['link'],
            countGoods: chain['countGoods'],
            percent: chain['percent'],
            imgLogo: chain['imgLogo'],
            imgLogoSmall: chain['imgLogoSmall'],
            selected: false
          };
        });
      });
  }


}
