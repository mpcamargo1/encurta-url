import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Page404 } from './page-404';

describe('Page404', () => {
  let component: Page404;
  let fixture: ComponentFixture<Page404>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Page404]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Page404);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
