import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeLayer1Component } from './home-layer1.component';

describe('HomeLayer1Component', () => {
  let component: HomeLayer1Component;
  let fixture: ComponentFixture<HomeLayer1Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeLayer1Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeLayer1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
