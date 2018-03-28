import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home-steps',
  templateUrl: './home-steps.component.html',
  styleUrls: ['./home-steps.component.css']
})
export class HomeStepsComponent implements OnInit {
  steps = [
    {id: 1, text: 'Выбирайте подходящие торговые сети', img: 'assets\\images\\Steps\\1.png'},
    {id: 2, text: 'Выбирайте и добавляйте акционные товары в список', img: 'assets\\images\\Steps\\2.png'},
    {id: 3, text: 'Смотрите экономию и редактируйте список', img: 'assets\\images\\Steps\\4.png'},
    {id: 4, text: 'Дописывайте в список неакционные товары', img: 'assets\\images\\Steps\\3.png'},
    {id: 5, text: 'Всё! Скачивайте список и отправляйтесь за покупками', img: 'assets\\images\\Steps\\5.png'}
  ]

  constructor() { }

  ngOnInit() {
  }

}
