import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeStepComponent } from './home-step.component';

describe('HomeStepComponent', () => {
  let component: HomeStepComponent;
  let fixture: ComponentFixture<HomeStepComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeStepComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeStepComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
