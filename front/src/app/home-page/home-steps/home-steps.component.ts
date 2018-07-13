import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home-steps',
  templateUrl: './home-steps.component.html',
  styleUrls: ['./home-steps.component.css']
})
export class HomeStepsComponent implements OnInit {
  steps = [
    {id: 1, text: 'Выбирайте подходящие торговые сети', img: 'assets\\images\\Steps\\step_1.svg'},
    {id: 2, text: 'Выбирайте и добавляйте акционные товары в список', img: 'assets\\images\\Steps\\step_2.svg'},
    {id: 3, text: 'Смотрите экономию и редактируйте список', img: 'assets\\images\\Steps\\step_3.svg'},
    {id: 4, text: 'Дописывайте в список неакционные товары', img: 'assets\\images\\Steps\\step_4.svg'},
    {id: 5, text: 'Всё! Скачивайте список и отправляйтесь за покупками', img: 'assets\\images\\Steps\\step_5.svg'}
  ];

  arrows = [
    {img: 'assets\\images\\Steps\\arrow.svg'},
    {img: 'assets\\images\\Steps\\arrow.svg'},
    {img: 'assets\\images\\Steps\\arrow.svg'},
    {img: 'assets\\images\\Steps\\arrow.svg'}
  ];

  constructor() { }

  ngOnInit() {
  }

}
