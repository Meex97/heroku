import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InsertProductsSupplierComponent } from './insert-products-supplier.component';

describe('InsertProductsSupplierComponent', () => {
  let component: InsertProductsSupplierComponent;
  let fixture: ComponentFixture<InsertProductsSupplierComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InsertProductsSupplierComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InsertProductsSupplierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
