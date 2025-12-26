import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncurtarLinkButton } from './encurtar-link-button';

describe('EncurtarLinkButton', () => {
  let component: EncurtarLinkButton;
  let fixture: ComponentFixture<EncurtarLinkButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EncurtarLinkButton]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EncurtarLinkButton);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
