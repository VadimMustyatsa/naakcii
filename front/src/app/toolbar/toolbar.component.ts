import {Component, EventEmitter, OnInit} from '@angular/core';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent implements OnInit {
  logoText = 'НаАкции.Бел';
  foods = 'Продукты';

  constructor() { }

  ngOnInit() {
  }
}
