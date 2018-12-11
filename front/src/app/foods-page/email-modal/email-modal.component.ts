import {Component, OnInit, Output, EventEmitter} from '@angular/core';

@Component({
  selector: 'app-email-modal',
  templateUrl: './email-modal.component.html',
  styleUrls: ['./email-modal.component.scss']
})
export class EmailModalComponent implements OnInit {

  @Output() childEvent = new EventEmitter();
  close() {
    this.childEvent.emit();
  }

  constructor() {
  }

  ngOnInit() {
  }

}
