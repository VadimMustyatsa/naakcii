import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalFinalComponent } from './modal-final.component';

describe('ModalFinalComponent', () => {
  let component: ModalFinalComponent;
  let fixture: ComponentFixture<ModalFinalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalFinalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalFinalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
