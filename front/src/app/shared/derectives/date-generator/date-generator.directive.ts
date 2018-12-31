import {Directive, ElementRef, OnInit, Renderer2} from '@angular/core';
import {DatePipe} from "@angular/common";

@Directive({
  selector: '[appDateGenerator]',
  providers : [DatePipe]
})
export class DateGeneratorDirective implements OnInit{

  public date = Number(new Date()) + 30 * 24 * 60 * 60 * 1000;

  constructor(private elementRef:ElementRef, private datePipe: DatePipe, private renderer: Renderer2) {
  }

  public formatDate(){
    const string = this.datePipe.transform(this.date, 'd MMMM');
    const text = this.renderer.createText(string);
    this.renderer.appendChild(this.elementRef.nativeElement, text);
  }

ngOnInit(){
  this.formatDate();
}
}
