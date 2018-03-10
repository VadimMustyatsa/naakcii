import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home-steps',
  templateUrl: './home-steps.component.html',
  styleUrls: ['./home-steps.component.css']
})
export class HomeStepsComponent implements OnInit {
  steps = [
    {text: '1. Выбирайте категорию/ подкатегорию', img: ''},
    {text: '2. Добавляйте товар в список', img: ''},
    {text: '3. Редактируйте и качайте список', img: ''},
    {text: '4. Вы готовы к покупкам!', img: ''}
  ]

  constructor() { }

  ngOnInit() {
  }

}
