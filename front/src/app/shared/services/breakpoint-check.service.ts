import { Injectable } from '@angular/core';
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { Observable } from 'rxjs/Observable';

@Injectable()

export class BreakPointCheckService {
    isHandset: Observable<BreakpointState>;
    isTablet: Observable<BreakpointState>;
    isWeb: Observable<BreakpointState>;
    isPortrait: Observable<BreakpointState>;
    isLandscape: Observable<BreakpointState>;

    constructor(private breakPointObserver: BreakpointObserver) {
        this.isHandset = this.breakPointObserver.observe([Breakpoints.HandsetLandscape,
        Breakpoints.HandsetPortrait]);
        this.isTablet = this.breakPointObserver.observe(Breakpoints.Tablet);
        this.isWeb = this.breakPointObserver.observe([Breakpoints.WebLandscape, Breakpoints.WebPortrait]);
        this.isPortrait = this.breakPointObserver.observe('(orientation: portrait)');
        this.isLandscape = this.breakPointObserver.observe('(orientation: landscape)');
    }
}
