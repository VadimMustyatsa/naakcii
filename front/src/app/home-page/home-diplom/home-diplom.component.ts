import { Component, OnInit } from '@angular/core';
import {BreakPointCheckService} from "../../shared/services/breakpoint-check.service";

@Component({
  selector: 'app-home-diplom',
  templateUrl: './home-diplom.component.html',
  styleUrls: ['./home-diplom.component.scss']
})
export class HomeDiplomComponent implements OnInit {

  constructor(public breakPointCheckService: BreakPointCheckService) { }

  ngOnInit() {
  }

  get blockHeight() {
    return screen.availHeight;
  }

}
