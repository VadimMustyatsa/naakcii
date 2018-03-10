import {Injectable} from '@angular/core';
import {SubCategory} from './foods.subCategory.model';

@Injectable()
export class FoodsSubCategoriesService {
  private dataCategoryList: SubCategory[][] = [
    [ //Бакалея
      {id: 0, name: 'Мука', img: '', selected: false},
      {id: 1, name: 'Макароны', img: '', selected: false},
      {id: 2, name: 'Крупы', img: '', selected: false},
      {id: 3, name: 'Бобовые', img: '', selected: false},
      {id: 4, name: 'Сухие завтраки', img: '', selected: false},
      {id: 5, name: 'Специи', img: '', selected: false},
      {id: 6, name: 'Масло/Уксус', img: '', selected: false},
      {id: 7, name: 'Соусы', img: '', selected: false},
      {id: 8, name: 'Быстрое приготовление', img: '', selected: false},
      {id: 9, name: 'Снеки', img: '', selected: false},
      {id: 9, name: 'Сухофрукты', img: '', selected: false},
      {id: 9, name: 'Консервы', img: '', selected: false}
    ],
    [ //Детское питание
      {id: 0, name: 'Сухие смеси', img: '', selected: false},
      {id: 1, name: 'Каши', img: '', selected: false},
      {id: 2, name: 'Соки', img: '', selected: false},
      {id: 3, name: 'Пюре', img: '', selected: false},
      {id: 4, name: 'Консервы', img: '', selected: false}
    ],
    [ //Молочные изделия, яйца
      {id: 0, name: 'Молоко', img: '', selected: false},
      {id: 1, name: 'Сливки', img: '', selected: false},
      {id: 2, name: 'Масло', img: '', selected: false},
      {id: 3, name: 'Кисломолочные', img: '', selected: false},
      {id: 4, name: 'Сметана', img: '', selected: false},
      {id: 5, name: 'Творожные продукты', img: '', selected: false},
      {id: 6, name: 'Сыр', img: '', selected: false},
      {id: 7, name: 'Йогурт', img: '', selected: false},
      {id: 8, name: 'Мороженое', img: '', selected: false},
      {id: 9, name: 'Яйцо', img: '', selected: false}
    ],
    [ //Мясо, рыба, колбасы
      {id: 0, name: 'Птица', img: '', selected: false},
      {id: 1, name: 'Свинина', img: '', selected: false},
      {id: 2, name: 'Говядина', img: '', selected: false},
      {id: 3, name: 'Рыба', img: '', selected: false},
      {id: 4, name: 'Морепродукты', img: '', selected: false},
      {id: 5, name: 'Колбасы', img: '', selected: false}
    ],
    [ //Напитки,вода, чай,кофе
      {id: 0, name: 'Вода', img: '', selected: false},
      {id: 1, name: 'Квас', img: '', selected: false},
      {id: 2, name: 'Соки', img: '', selected: false},
      {id: 3, name: 'Сиропы, топпинги', img: '', selected: false},
      {id: 4, name: 'Чай', img: '', selected: false},
      {id: 5, name: 'Кофе', img: '', selected: false},
      {id: 5, name: 'Напитки', img: '', selected: false},
      {id: 5, name: 'Какао', img: '', selected: false},
      {id: 5, name: 'Цикорий', img: '', selected: false}
    ],
    [ //Овощи и фрукты
      {id: 0, name: 'Овощи', img: '', selected: false},
      {id: 1, name: 'Фрукты', img: '', selected: false},
      {id: 2, name: 'Грибы', img: '', selected: false},
      {id: 3, name: 'Ягоды', img: '', selected: false}
    ],
    [ //Сладости
      {id: 0, name: 'Шоколад', img: '', selected: false},
      {id: 1, name: 'Конфеты', img: '', selected: false},
      {id: 2, name: 'Батончики', img: '', selected: false},
      {id: 3, name: 'Леденцы', img: '', selected: false},
      {id: 4, name: 'Зефир', img: '', selected: false},
      {id: 5, name: 'Мармелад', img: '', selected: false},
      {id: 5, name: 'Халва', img: '', selected: false},
      {id: 5, name: 'Вафли', img: '', selected: false},
      {id: 5, name: 'Мучные изделия', img: '', selected: false}
    ],
    [ //Хлебобулочные изделия
      {id: 0, name: 'Хлеб', img: '', selected: false},
      {id: 1, name: 'Батон', img: '', selected: false},
      {id: 2, name: 'Лаваш', img: '', selected: false},
      {id: 3, name: 'Сухари', img: '', selected: false},
      {id: 4, name: 'Сушки', img: '', selected: false}
    ],
    [ //Замороженные продукты
      {id: 0, name: 'Грибы', img: '', selected: false},
      {id: 1, name: 'Овощи', img: '', selected: false},
      {id: 2, name: 'Фрукты', img: '', selected: false},
      {id: 3, name: 'Ягоды', img: '', selected: false},
      {id: 4, name: 'Полуфабрикаты', img: '', selected: false}
    ]
  ];

  getByCategory(idCategoty): SubCategory[] {
    return this.dataCategoryList[idCategoty];
  }
}
