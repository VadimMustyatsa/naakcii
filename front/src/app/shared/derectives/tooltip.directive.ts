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
    let tooltipElem = document.createElement('div');
    tooltipElem.className = 'tooltip';
    tooltipElem.innerHTML = this.message;
    document.body.appendChild(tooltipElem);

    let coords = this.elementRef.nativeElement.getBoundingClientRect();

    let left = coords.left + (this.elementRef.nativeElement.offsetWidth - tooltipElem.offsetWidth) / 2;
    if (left < 0) left = 0;

    let top = coords.top - tooltipElem.offsetHeight - 5;
    if (top < 0) {
      top = coords.top + this.elementRef.nativeElement.offsetHeight + 5;
    }

    tooltipElem.style.left = left + 'px';
    tooltipElem.style.top = top + 'px';
    this.showingTooltip = tooltipElem;
  }
  @HostListener('mouseleave') mouseOut() {
    document.body.removeChild(this.showingTooltip);
    this.showingTooltip = null;
  }
}
