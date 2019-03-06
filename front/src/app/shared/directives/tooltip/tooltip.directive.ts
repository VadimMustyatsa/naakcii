import {Directive, HostListener, Input, ElementRef} from '@angular/core';

@Directive({
  selector: '[tooltip]'
})
export class TooltipDirective {
  constructor(private elementRef:ElementRef) {
  }
  @Input('tooltip')
  message:string;

  @HostListener('mouseenter') mouseIn() {
    this.elementRef.nativeElement.setAttribute('title', this.message)
  }
  @HostListener('mouseleave') mouseOut() {
    this.elementRef.nativeElement.setAttribute('title', '')
  }
}
