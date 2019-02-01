import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UndiscountHeaderComponent } from './undiscount-header.component';

describe('UndiscountHeaderComponent', () => {
  let component: UndiscountHeaderComponent;
  let fixture: ComponentFixture<UndiscountHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UndiscountHeaderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UndiscountHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
