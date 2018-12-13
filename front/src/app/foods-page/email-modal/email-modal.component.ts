import {Component, OnInit, Output, EventEmitter,OnDestroy} from '@angular/core';
import {NgForm} from '@angular/forms';
import {SubscribeService} from '../../shared/services/subscribe.service';
import {SessionStorageService} from "../../shared/services/session-storage.service";
const ANIMATION_DURATION_TIME = 1000;
const SUCCESS_MESSAGE_TIME = 2000;

@Component({
  selector: 'app-email-modal',
  templateUrl: './email-modal.component.html',
  styleUrls: ['./email-modal.component.scss']
})
export class EmailModalComponent implements OnInit, OnDestroy {
  public messageSwitcher = false;
  public successMessageTimeOut: number;
  public animationDurationTimeOut: number;

  @Output() childEvent = new EventEmitter();
  onClose = () => {
    this.childEvent.emit();
    this.sessionStorageService.setSenderEmailOpened('WAS_OPENED');
    this.animationDurationTimeOut = setTimeout(()=>{this.messageSwitcher = false;}, ANIMATION_DURATION_TIME);
  };

  constructor(private subscribeService:SubscribeService, private sessionStorageService: SessionStorageService) {
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      this.subscribeService.addEmail(form.value['email']).subscribe(rez=>{console.log(rez)},err=>{console.log(err)});
      this.sessionStorageService.setSenderEmailOpened(form.value['email']);
      this.messageSwitcher = true;
      this.successMessageTimeOut = setTimeout(this.onClose, SUCCESS_MESSAGE_TIME);
    }
  }

  ngOnInit() {
  }

  ngOnDestroy(){
    clearTimeout(this.successMessageTimeOut);
    clearTimeout(this.animationDurationTimeOut);
  }

}
