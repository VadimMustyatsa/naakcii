import { Component } from '@angular/core';
import {BreakPointCheckService} from "../../shared/services/breakpoint-check.service";

@Component({
  selector: 'app-home-diplom',
  templateUrl: './home-diplom.component.html',
  styleUrls: ['./home-diplom.component.scss']
})
export class HomeDiplomComponent {

  constructor(public breakPointCheckService: BreakPointCheckService) { }

}
