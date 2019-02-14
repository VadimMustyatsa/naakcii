import {Injectable} from '@angular/core';

import {RestDataService} from '../services/rest-data.service';
import {ChainProduct} from '../model/chain-product.model';
import {FoodsCategoriesService} from '../category/foods.categories.service';

@Injectable()
export class FoodsFoodListService {

  constructor(private restDataService: RestDataService,
    private categoryService: FoodsCategoriesService) {
  }

  getFoodList(selectedSubCatListID, first, last) {
    let listID = '';
    selectedSubCatListID.map(id => {
      listID += String(id.id) + ',';
    });
    console.log(this.categoryService.getSelectCategory().id);
    const dataGet = {SubcategoryList: listID, first: first, last: last};
    return this.restDataService.getProducts(dataGet)
      .map(productList => {
        return productList.map(product => {
          // product['picture'] = product['picture'].replace('%', '%25');
          // {...product, picture: product['picture'], boxWeight: '', selectAmount: ''};
          return new ChainProduct(product, this.categoryService.getSelectCategory().id);
          // {id: product['id'],
          //   name: product['name'],
          //   allPrice: product['price'],
          //   discount: product['discount'],
          //   totalPrice: product['discountPrice'],
          //   boxWeight: '',
          //   idStrore: product['chainId'],
          //   img: product['picture'],
          //   selected: false,
          //   selectAmount: 1
          // };
        });
      });
  }
}




