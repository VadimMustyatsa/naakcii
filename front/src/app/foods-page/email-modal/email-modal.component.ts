import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {NgForm} from '@angular/forms';

import {SubscribeService} from '../../shared/services/subscribe.service';
import { from } from 'rxjs/observable/from';

@Component({
  selector: 'app-email-modal',
  templateUrl: './email-modal.component.html',
  styleUrls: ['./email-modal.component.scss']
})
export class EmailModalComponent implements OnInit {
  public email = '';
  public successMessage = '';

  @Output() childEvent = new EventEmitter();
  onClose = () => {
    this.childEvent.emit();
  };

  onSubmit(form: NgForm, modal) {
    if (form.valid) {
      // console.log(form.value['email']);
      this.subscribeService.addEmail(form.value['email']).subscribe(rez=>{console.log(rez)},err=>{console.log(err)});
      modal.remove();
      this.successMessage = "Ваша почта успешно сохранена, следите за обновлениями.";
      setTimeout(this.onClose, 3000);
    }
  }

  constructor(private subscribeService:SubscribeService) {
  }

  ngOnInit() {
  }

}
