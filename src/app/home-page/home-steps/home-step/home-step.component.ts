import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-home-step',
  templateUrl: './home-step.component.html',
  styleUrls: ['./home-step.component.css']
})
export class HomeStepComponent implements OnInit {
  @Input() text;

  constructor() { }

  ngOnInit() {
  }

}
