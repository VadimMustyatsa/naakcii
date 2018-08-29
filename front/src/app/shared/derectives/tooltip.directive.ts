import {Directive, HostListener, Input, ElementRef} from '@angular/core';

@Directive({
  selector: '[tooltip]'
})
export class TooltipDirective {
  showingTooltip:any;
  constructor(private elementRef:ElementRef) {
  }
  @Input('tooltip')
  message:string;

  @HostListener('mouseenter') mouseIn() {
    this.elementRef.nativeElement.classList.add("tooltip");
    this.elementRef.nativeElement.style.setProperty('--halfElemWidth', this.elementRef.nativeElement.getBoundingClientRect().width/2 + 'px');
    this.elementRef.nativeElement.style.setProperty('--elemWidth', - this.elementRef.nativeElement.getBoundingClientRect().width + 'px');
  }
  @HostListener('mouseleave') mouseOut() {
    this.elementRef.nativeElement.classList.remove("tooltip");
  }
}
