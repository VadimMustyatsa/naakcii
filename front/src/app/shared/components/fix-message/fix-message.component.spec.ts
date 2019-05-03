import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FixMessageComponent } from './fix-message.component';

describe('FixMessageComponent', () => {
  let component: FixMessageComponent;
  let fixture: ComponentFixture<FixMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FixMessageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FixMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
