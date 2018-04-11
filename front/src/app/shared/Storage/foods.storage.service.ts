import {Injectable} from '@angular/core';
import {Storag} from './foods.storage.model';

@Injectable()
export class FoodsStorageService {
  private data: Storag[] = [
    {id: 0, name: 'Алми', location: 'street 20', countGoods: 20, persent: 20,
      imgLogo: 'assets/images/Storage/Full/Almy.png',
      imgLogoSmall: 'assets/images/Storage/small/Almy.png'},
    {id: 1, name: 'Белмаркет', location: 'street 30', countGoods: 25, persent: 25,
      imgLogo: 'assets/images/Storage/Full/Belmarket.png',
      imgLogoSmall: 'assets/images/Storage/small/Belmarket.png'},
    {id: 2, name: 'Евроопт', location: 'street 40', countGoods: 30, persent: 30,
      imgLogo: 'assets/images/Storage/Full/Evroopt.png',
      imgLogoSmall: 'assets/images/Storage/small/Evroopt.png'},
    {id: 3, name: 'Гиппо', location: 'street 50', countGoods: 35, persent: 17,
      imgLogo: 'assets/images/Storage/Full/Gippo.png',
      imgLogoSmall: 'assets/images/Storage/small/Gippo.png'},
    {id: 4, name: 'Грин', location: 'street 60', countGoods: 40, persent: 27,
      imgLogo: 'assets/images/Storage/Full/Green.png',
      imgLogoSmall: 'assets/images/Storage/small/Green.png'},
    {id: 5, name: 'Корона', location: 'street 70', countGoods: 45, persent: 15,
      imgLogo: 'assets/images/Storage/Full/Korona.png',
      imgLogoSmall: 'assets/images/Storage/small/Korona.png'},
    {id: 6, name: 'MartInn', location: 'street 80', countGoods: 27, persent: 12,
      imgLogo: 'assets/images/Storage/Full/Martin.png',
      imgLogoSmall: 'assets/images/Storage/small/Martin.png'},
    {id: 7, name: 'Prostore', location: 'street 90', countGoods: 37, persent: 19,
      imgLogo: 'assets/images/Storage/Full/Prostore.png',
      imgLogoSmall: 'assets/images/Storage/small/Prostore.png'},
    {id: 8, name: 'Рублевский', location: 'street 85', countGoods: 47, persent: 23,
      imgLogo: 'assets/images/Storage/Full/Rublev.png',
      imgLogoSmall: 'assets/images/Storage/small/Rublev.png'},
    {id: 9, name: 'Соседи', location: 'street 75', countGoods: 32, persent: 26,
      imgLogo: 'assets/images/Storage/Full/Sosedy.png',
      imgLogoSmall: 'assets/images/Storage/small/Sosedy.png'},
    {id: 10, name: 'Виталюр', location: 'street 65', countGoods: 28, persent: 14,
      imgLogo: 'assets/images/Storage/Full/Vitalur.png',
      imgLogoSmall: 'assets/images/Storage/small/Vitalur.png'}
  ];
  getAll(): Storag[] {
    return this.data;
  }
  getById(id: number): Storag {
    return this.data.find(x => x.id === id);
  }
}
