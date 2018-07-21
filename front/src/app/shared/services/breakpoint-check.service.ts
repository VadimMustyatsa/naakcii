import {Injectable} from '@angular/core';
import {BreakpointObserver, Breakpoints, BreakpointState} from '@angular/cdk/layout';
import {Observable} from 'rxjs/Observable';

@Injectable()

export class BreakPointCheckService {
  isMobile: Observable<BreakpointState>;
  isHandset: Observable<BreakpointState>;
  isTablet: Observable<BreakpointState>;
  isNetBook: Observable<BreakpointState>;
  isWeb: Observable<BreakpointState>;
  isPortrait: Observable<BreakpointState>;
  isLandscape: Observable<BreakpointState>;

  constructor(private breakPointObserver: BreakpointObserver) {
    this.isMobile = this.breakPointObserver.observe('(max-width: 320px)');
    this.isHandset = this.breakPointObserver.observe('(max-width: 620px)');
    this.isTablet = this.breakPointObserver.observe('(max-width: 768px)');
    this.isNetBook = this.breakPointObserver.observe('(max-width: 1090px)');
    this.isWeb = this.breakPointObserver.observe('(max-width: 1260px)');
    this.isPortrait = this.breakPointObserver.observe('(orientation: portrait)');
    this.isLandscape = this.breakPointObserver.observe('(orientation: landscape)');
  }
}
