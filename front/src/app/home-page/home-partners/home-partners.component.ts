import { Component, OnInit } from '@angular/core';
import {BreakPointCheckService} from "../../shared/services/breakpoint-check.service";

@Component({
  selector: 'app-home-partners',
  templateUrl: './home-partners.component.html',
  styleUrls: ['./home-partners.component.scss']
})
export class HomePartnersComponent implements OnInit {

  constructor(public breakPointCheckService: BreakPointCheckService) { }

  ngOnInit() {
  }

  get blockHeight() {
    return screen.availHeight;
  }

}
