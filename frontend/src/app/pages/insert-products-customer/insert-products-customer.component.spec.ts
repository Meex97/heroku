import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InsertProductsCustomerComponent } from './insert-products-customer.component';

describe('InsertProductsCustomerComponent', () => {
  let component: InsertProductsCustomerComponent;
  let fixture: ComponentFixture<InsertProductsCustomerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InsertProductsCustomerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InsertProductsCustomerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
