import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home-steps',
  templateUrl: './home-steps.component.html',
  styleUrls: ['./home-steps.component.css']
})
export class HomeStepsComponent implements OnInit {
  steps = [
    {text: 'Выбирайте нужный товар'},
    {text: 'Добавляйте в список'},
    {text: 'Формируйте корзину'},
    {text: 'Выбирайте магазин'},
    {text: 'Готово!'},
  ]

  constructor() { }

  ngOnInit() {
  }

}
