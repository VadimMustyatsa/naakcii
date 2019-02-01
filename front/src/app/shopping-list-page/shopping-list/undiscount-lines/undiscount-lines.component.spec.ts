import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UndiscountLineComponent } from './undiscount-lines.component';

describe('UndiscountLinesComponent', () => {
  let component: UndiscountLineComponent;
  let fixture: ComponentFixture<UndiscountLineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UndiscountLineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UndiscountLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
