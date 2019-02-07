export interface Product {
  // TODO - актуализировать в соответствие с получаемыми данными
  actionType?: string; // goodPrice, onePlusOne, regularDiscount
  icon?: string; // 'className', '1 + 1', '15%'
  tooltip?: string; // 'Хорошая цена', '1 + 1', 'Скидка'
  selectedQuantity?: number; // разрыв шага по выбору товаров
  chainId: number;
  discount: number;
  discountPrice: number;
  endDate: string;
  id: number;
  measure: string;
  name: string;
  picture: string;
  price: number;
  startDate: string;
  subcategoryId: number;
  type: string;
}
