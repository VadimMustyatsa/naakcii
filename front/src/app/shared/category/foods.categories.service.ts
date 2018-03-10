import {Injectable} from '@angular/core';
import {Category} from './foods.category.model';

@Injectable()
export class FoodsCategoriesService {
  private selectedCategory: Category;  //текущая выбранная категория
  private data: Category[] = [
    {id: 0, name: 'Бакалея', img: ''},
    {id: 1, name: 'Детское питание', img: ''},
    {id: 2, name: 'Молочка, яйца', img: ''},
    {id: 3, name: 'Мясо,рыба, колбасы', img: ''},
    {id: 4, name: 'Напитки,вода, чай,кофе', img: ''},
    {id: 5, name: 'Овощи и фрукты', img: ''},
    {id: 6, name: 'Сладости', img: ''},
    {id: 7, name: 'Хлебобулочные изделия', img: ''},
    {id: 8, name: 'Замороженные продукты', img: ''}
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
