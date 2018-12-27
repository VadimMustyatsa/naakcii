import { Directive, HostListener, Attribute } from '@angular/core';

@Directive({
    selector: '[app-scroll-to-top]'
})

export class ScrollToTopDirective {
    scrollValue: number;

    constructor(@Attribute('app-scroll-to-top') scrollValue: number) {
        this.scrollValue = scrollValue;
    }

    @HostListener('keyup.enter')
    @HostListener('click')  onclick() {
        setTimeout(() => window.scrollTo({ top: this.scrollValue ? this.scrollValue : 0, behavior: 'smooth' }), 100);
    }
}
