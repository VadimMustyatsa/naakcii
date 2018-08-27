import { Component } from '@angular/core';
import {BreakPointCheckService} from "../../shared/services/breakpoint-check.service";

@Component({
  selector: 'app-home-partners',
  templateUrl: './home-partners.component.html',
  styleUrls: ['./home-partners.component.scss']
})
export class HomePartnersComponent {

  constructor(public breakPointCheckService: BreakPointCheckService) { }

  get blockHeight() {
    return screen.availHeight - 150;
  }

}
