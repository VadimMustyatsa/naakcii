import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListForChainComponent } from './chain-line.component';

describe('ListForChainComponent', () => {
  let component: ListForChainComponent;
  let fixture: ComponentFixture<ListForChainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListForChainComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListForChainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
