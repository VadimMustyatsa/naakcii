import {Injectable} from '@angular/core';

import {Category} from './foods.category.model';
import {ApiService} from '../services/api.service';

@Injectable()
export class FoodsCategoriesService {

  private categoryUrl: string;
  private data: Category[] = [];
  private selectedCategory: Category;

  constructor(private apiService: ApiService) {
  }

  getAll() {
    return this.apiService.getAllCategory();
  }

  setCategories(categories: Category[]) {
    this.data = categories;
  }

  getById(id: number): Category {
    return this.data.find(x => x.id === id);
  }

  setSelectCategory(cat: Category) {
    this.selectedCategory = cat;
  }

  getSelectCategory(): Category {
    return this.selectedCategory;
  }
}
