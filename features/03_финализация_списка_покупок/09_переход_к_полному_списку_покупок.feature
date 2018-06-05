#language: ru
Функция: Переход к полному списку покупок
Как покупатель ищущий экономию
Я хочу перейти к сформированному списку покупок
Чтобы просмотреть его и внести необходимые изменения

Сценарий:
Допустим я на странице "Формирование списка покупок – naakcii.by"
  И на панели "Список покупок" отображается следующий текст "Итог: 113,83 руб."
  И на панели "Список покупок" отображаются следующие товары:
    | акционный_товар               |количество |
    | Йогурт Савушкин 2%,...        | 3         |
    | Кефир "Минская Марк...        | 4         |
    | Кефир "Берёзка" 1,5...        | 2         |
    | Булочка Маковая, 10...        | 2         |
    | Сдоба "Мадлен" 75гр           | 6         |
    | Хлеб "Литвинский" 4...        | 3         |
    | Колбаса оригинальна...        | 4         |
    | П/ф фарш "Сельский"...        | 2         |
    | Окорочок "домашний ...        | 3         |
  И на панели "Список покупок" отображается следующая информация для товара "Йогурт Савушкин 2%,...":
    | информация               | торговая сеть | цена на акции | % скидки | итог      | комментарий             |
    | Йогурт Савушкин 2%, 120г | "Виталюр"     | 0,39 руб.     | 20 %     | 1,17 руб. | Проверить срок годности!|
  Если я нажимаю на кнопку "Перейти к списку"
  То должна открыться страница "Список покупок – naakcii.by"
  И в адресной строке браузера должен отобразиться адрес "http://178.124.206.54/finalize-shopping-list/"
    # Должно быть: И в адресной строке браузера должен отобразиться адрес "http://naakcii.by/finalize-shopping-list/
  И должна отобразиться панель "Список покупок"
  И на панели "Список покупок" должна отобразиться следующая информация по сетям:
    |торговая сеть|товарные позиции                     |количество|стоимость без скидки|скидка руб. (%)|стоимость со скидкой|дата окончания акции|
    |Виталюр      |      3                              |    9     |       75,53        |  17,30 (23%)  |        58,23       |        18 мая      |
    |             |Йогурт Савушкин 2%, 120г             |    3     |       1,47         |  0,30  (20%)  |        1,17        |        18 мая      |
    |             |Проверить срок годности!             |          |                    |               |                    |                    |
    |             |Булочка Маковая, 100гр               |    2     |       1,30         |  0,20  (15%)  |        1,10        |        18 мая      |
    |             |Колбаса оригинальная Минская с/к, 1кг|    4     |       72,76        |  16,80 (23%)  |        55,96       |        18 мая      |
    |Белмаркет    |      3                              |    13    |       44,55        |  6,48  (15%)  |        38,07       |        18 мая      |
    |             |Кефир "Минская Марка" 1.5% 900гр.    |    4     |    нет данных      |  –            |        3,96        |        18 мая      |
    |             |Сдоба "Мадлен" 75гр                  |    6     |    нет данных      |  –            |        4,14        |        18 мая      |
    |             |Окорочок "домашний Люкс" к\в 1кг     |    3     |       36,4         |  6,48 (18 %)  |        29,97       |        18 мая      |
    |Соседи       |      3                              |    7     |       20,56        |  3,03 (15 %)  |        17,53       |        18 мая      |
    |             |Кефир "Берёзка" 1,5% 950г            |    2     |       2,58         |  0,40 (16 %)  |        2,18        |        18 мая      |
    |             |Хлеб "Литвинский" 400г               |    3     |       3,00         |  0,63 (21 %)  |        2,37        |        18 мая      |
    |             |П/ф фарш "Сельский" 1кг              |    2     |       14,98        |  2,00 (13 %)  |        12,98       |        18 мая      |
  И в строке "Итого" на панели "Список покупок" должна отобразиться следующая информация:
    |             |товарные позиции                     |количество|стоимость без скидки|скидка руб. (%)|стоимость со скидкой|
    |Итого:       |      9                              |    29    |       140,64       |  26,81(19 %)  |        113,83 руб. |
  И должна появиться кнопка "Вернуться к товарам"
  И должна появиться кнопка "СКАЧАТЬ PDF"