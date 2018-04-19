import { InjectionToken } from '@angular/core';
import {Category} from '../shared/category/foods.category.model';
import {SubCategory} from '../shared/subCategory/foods.subCategory.model';
import {FoodList} from '../shared/foodList/foods.foodList.model';

export enum MODES {
  SELECT_CATEGORY,
  SELECT_SUBCATEGORY,
  SELECT_FOOD_CARD
}

export class SharedState {

  constructor(public mode: MODES,
              public category?: Category,
              public subCatList?: SubCategory[],
              public foodCard?: FoodList,
  ) { }
}

export const SHARED_STATE = new InjectionToken('shared_state');
