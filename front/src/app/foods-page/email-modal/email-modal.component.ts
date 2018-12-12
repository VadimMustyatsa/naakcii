import {Component, OnInit, Output, EventEmitter,OnDestroy} from '@angular/core';
import {NgForm} from '@angular/forms';
import {SubscribeService} from '../../shared/services/subscribe.service';
import {SessionStorageService} from "../../shared/services/session-storage.service";

@Component({
  selector: 'app-email-modal',
  templateUrl: './email-modal.component.html',
  styleUrls: ['./email-modal.component.scss']
})
export class EmailModalComponent implements OnInit, OnDestroy {
  public email = '';
  public successMessage = '';
  public timeOut: number;


  @Output() childEvent = new EventEmitter();
  onClose = () => {
    this.childEvent.emit();
    this.sessionstorageService.setSenderEmailOpened('email');
  };

  constructor(private subscribeService:SubscribeService, private sessionstorageService: SessionStorageService) {
  }

  onSubmit(form: NgForm, modal) {
    if (form.valid) {
      this.subscribeService.addEmail(form.value['email']).subscribe(rez=>{console.log(rez)},err=>{console.log(err)});
      this.sessionstorageService.setSenderEmailOpened(form.value['email']);
      modal.remove();
      this.successMessage = "Ваша почта успешно сохранена, следите за обновлениями.";
      this.timeOut = setTimeout(this.onClose, 2000);
    }
  }

  ngOnInit() {
  }

  ngOnDestroy(){
    clearTimeout(this.timeOut);
  }

}
