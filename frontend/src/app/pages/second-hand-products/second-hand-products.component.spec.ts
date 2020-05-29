import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SecondHandProductsComponent } from './second-hand-products.component';

describe('SecondHandProductsComponent', () => {
  let component: SecondHandProductsComponent;
  let fixture: ComponentFixture<SecondHandProductsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SecondHandProductsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecondHandProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
