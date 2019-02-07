import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UndiscountAddComponent } from './undiscount-add.component';

describe('UndiscountAddComponent', () => {
  let component: UndiscountAddComponent;
  let fixture: ComponentFixture<UndiscountAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UndiscountAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UndiscountAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
