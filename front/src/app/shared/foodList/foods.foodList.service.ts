import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { RestDataService } from '../services/rest-data.service';
import { ChainProduct } from '../model/chain-product.model';

@Injectable()
export class FoodsFoodListService {

  chainProductSubject: Subject<any>;
  subCategoryIdList: number[];
  chainIdList: number[];

  constructor( private restDataService: RestDataService ) {
    this.chainProductSubject = new Subject();
    this.subCategoryIdList = [];
    this.chainIdList = [];
  }

  getFoodList( page = 0, size = 25 ) {
    if ( ( this.subCategoryIdList.length === 0 ) || ( this.chainIdList.length === 0 ) ) {
      this.chainProductSubject.next( {} );
      return 0;
    }
    const dataGet = {
      subcategoryIds: this.subCategoryIdList,
      chainIds: this.chainIdList,
    };
    return this.restDataService.getProducts( {...dataGet, page, size} )
      .subscribe( ( productList: any ) => {
        const newProductList = productList.chainProducts.filter( product => {
          if ( product != null ) {
            return true;
          } else {
            console.log( 'returned null' );
          }
        } ).map( product => {
          return new ChainProduct( product );
        } );
        this.chainProductSubject.next( {newProductList, numberOfChainProducts: productList.numberOfChainProducts } );
      } );
  }

  getChainProductSubject(): Subject<any> {
    return this.chainProductSubject;
  }

  changeSubcategory( idSubcategory: number ) {
    this.subCategoryIdList = [];
    this.subCategoryIdList.push( idSubcategory );
    this.getFoodList();
  }

  changeChainList( selectedIds: number[] ) {
    this.chainIdList = selectedIds;
    this.getFoodList();
  }
}




