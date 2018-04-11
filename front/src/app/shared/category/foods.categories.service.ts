import {Injectable} from '@angular/core';
import {Category} from './foods.category.model';

@Injectable()
export class FoodsCategoriesService {
  private selectedCategory: Category;  //текущая выбранная категория
  private data: Category[] = [
    {id: 0, name: 'Бакалея', img: 'room_service'},
    {id: 1, name: 'Детское питание', img: 'child_care'},
    {id: 2, name: 'Молочные продукты, яйца', img: 'local_drink'},
    {id: 3, name: 'Мясо и колбасные изделия', img: 'local_pizza'},
    {id: 4, name: 'Напитки,кофе, чай,соки', img: 'free_breakfast'},
    {id: 5, name: 'Овощи, фрукты', img: 'spa'},
    {id: 6, name: 'Сладости', img: 'cake'},
    {id: 7, name: 'Хлебобулочные изделия', img: 'filter_drama'},
    {id: 8, name: 'Замороженные продукты', img: 'ac_unit'},
    {id: 9, name: 'Рыба и морепродукты', img: 'kitchen'},
    {id: 10, name: 'Собственное производство', img: 'restaurant_menu'}
  ];
  getAll(): Category[] {
    return this.data;
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
