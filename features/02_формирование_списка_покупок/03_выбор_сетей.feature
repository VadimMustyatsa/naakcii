#language: ru
Функция: Выбор торговых сетей
  Как покупатель ищущий экономию
  Я хочу выбрать только интересующие меня торговые сети
  Чтобы добавлять в список покупок только их акционные товары

  Предыстория:
    Допустим я на странице "Формирование списка покупок – naakcii.by"

  Сценарий: Раскрытие фильтра
    Допустим на фильтре "Торговые сети" отображается текст "Выбраны торговые сети: все"
    Если я нажимаю на фильтр "Торговые сети"
    То фильтр "Торговые сети" должен раскрыться
    И в фильтре "Торговые сети" должны отобразиться следующие торговые сети с соответствующими данными и статусами:
      | торговая_сеть | средний_%_скидки  | товаров_на_акции  | статус   |
      | Все           | 30                | 563               | Выбрана  |
      | ProStore      | 30                | 63                | Выбрана  |
      | Белмаркет     | 28                | 54                | Выбрана  |
      | Виталюр       | 22                | 81                | Выбрана  |
      | Евроопт       | 30                | 130               | Выбрана  |
      | Корона        | 50                | 46                | Выбрана  |
      | Рублёвский    | 26                | 94                | Выбрана  |
      | Соседи        | 24                | 95                | Выбрана  |

  Сценарий: Исключение всех торговых сетей из выбора
    Допустим фильтр "Торговые сети" раскрыт
    И в фильтре "Торговые сети" отображаются следующие торговые сети с соответствующими статусами:
      | торговая_сеть | статус   |
      | Все           | Выбрана  |
      | ProStore      | Выбрана  |
      | Белмаркет     | Выбрана  |
      | Виталюр       | Выбрана  |
      | Евроопт       | Выбрана  |
      | Корона        | Выбрана  |
      | Рублёвский    | Выбрана  |
      | Соседи        | Выбрана  |
    Если я нажимаю на пункт "Все" в фильтре "Торговые сети"
    То в фильтре "Торговые сети" должны отобразиться следующие статусы:
      | торговая_сеть | статус      |
      | Все           | Не выбрана  |
      | ProStore      | Не выбрана  |
      | Белмаркет     | Не выбрана  |
      | Виталюр       | Не выбрана  |
      | Евроопт       | Не выбрана  |
      | Корона        | Не выбрана  |
      | Рублёвский    | Не выбрана  |
      | Соседи        | Не выбрана  |
    И на фильтре "Торговые сети" должен отобразиться текст "Выберите торговую сеть"
    И на панели "Список акционных товаров" не должно остаться ни одной карточки
    И на панели "Список акционных товаров" должен появиться следующий текст:
      """
      Не выбрана ни одна торговая сеть.

      Чтобы добавлять акционные товары в список покупок,
      нужно выбрать хотя бы одну из них.

      Рекомендуем выбирать сети поближе к дому с наибольшим количеством товаров
      и наибольшим средним процентом скидки.
      """

  Сценарий: Выбор нескольких торговых сетей из списка
    Допустим фильтр "Торговые сети" раскрыт
    И в фильтре "Торговые сети" отображаются следующие торговые сети с соответствующими статусами:
      | торговая_сеть | статус      |
      | Все           | Не выбрана  |
      | ProStore      | Не выбрана  |
      | Белмаркет     | Не выбрана  |
      | Виталюр       | Не выбрана  |
      | Евроопт       | Не выбрана  |
      | Корона        | Не выбрана  |
      | Рублёвский    | Не выбрана  |
      | Соседи        | Не выбрана  |
    Если я выбираю следующие торговые сети в фильтре "Торговые сети":
      | торговая_сеть |
      | Белмаркет     |
      | Виталюр       |
      | Соседи        |
    То в фильтре "Торговые сети" должны отобразиться следующие статусы:
      | торговая_сеть | статус      |
      | Все           | Не выбрана  |
      | ProStore      | Не выбрана  |
      | Белмаркет     | Выбрана     |
      | Виталюр       | Выбрана     |
      | Евроопт       | Не выбрана  |
      | Корона        | Не выбрана  |
      | Рублёвский    | Не выбрана  |
      | Соседи        | Выбрана     |
    И на панели "Список акционных товаров" должны отобразиться следующие карточки товаров:
      | акционный_товар                                                                         | торговая_сеть |
      | Йогурт "Оптималь" Персик, Чернослив-злаки, Черника-Малина 2% жирность 350гр             | Белмаркет     |
      | Йогурт Савушкин 2%, 120г                                                                | Виталюр       |
      | Йогурт питьевой "Теос" 300г                                                             | Соседи        |
      | Кефир "Берёзка" 1,5% 950г                                                               | Соседи        |
      | Кефир "Минская Марка" 1.5% 900гр.                                                       | Белмаркет     |
      | Коктейль йогуртный "Даниссимо" 260г                                                     | Соседи        |
      | Продукт йогуртный "Нежный" 95г                                                          | Соседи        |
      | Продукт йогуртный Нежный с пюре 0,1%, 100г клубника/яблоко/абрикос                      | Виталюр       |
      | Продукт кисломолочный "Экспонента" 100г                                                 | Соседи        |
      | Масло сливочное "Nadivo" 72.5% 180г                                                     | Белмаркет     |
      | Масло сливочное "Крестьянское" 72,5% 180г                                               | Соседи        |
      | Молоко "Берёзка" 1,5% 950мл                                                             | Соседи        |
