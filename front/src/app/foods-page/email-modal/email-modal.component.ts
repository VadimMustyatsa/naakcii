import {Component, OnInit, Output, EventEmitter, NgForm} from '@angular/core';

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
      modal.remove();
      this.successMessage = "Ваша почта успешно сохранена, следите за обновлениями.";
      setTimeout(this.onClose, 3000);
    }
  }

  constructor() {
  }

  ngOnInit() {
  }

}
