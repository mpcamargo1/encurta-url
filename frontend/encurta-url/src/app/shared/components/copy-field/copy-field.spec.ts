import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CopyField } from './copy-field';

describe('CopyField', () => {
  let component: CopyField;
  let fixture: ComponentFixture<CopyField>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CopyField],
    }).compileComponents();

    fixture = TestBed.createComponent(CopyField);
    // Se não tiver um texto inicializado o teste de criação irá falhar.
    fixture.componentRef.setInput('textToCopy', 'https://encurtaurl.net/1lT0Z1MeWW');
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('deve ser criado', () => {
    expect(component).toBeTruthy();
  });

  it('deve exibir como link', () => {
    fixture.componentRef.setInput('textToCopy', 'https://encurtaurl.net/1lT0Z1MeWW');
    fixture.componentRef.setInput('isLink', true);
    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('input')).toBeFalsy();

    const link = fixture.nativeElement.querySelector('a');
    expect(link.textContent.trim()).toBe('https://encurtaurl.net/1lT0Z1MeWW');
  });

  it('não deve exibir como link', () => {
    fixture.componentRef.setInput('textToCopy', 'teste');
    fixture.componentRef.setInput('isLink', false);
    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('a')).toBeFalsy();

    const text = fixture.nativeElement.querySelector('input');
    expect(text.value.trim()).toBe('teste');
  });

  it('deve copiar o texto ao clicar no botão', async () => {
    Object.defineProperty(navigator, 'clipboard', {
      value: {
        writeText: vi.fn(),
      },
      configurable: true,
    });

    const clipboardSpy = vi.spyOn(navigator.clipboard, 'writeText').mockResolvedValue(undefined);

    fixture.componentRef.setInput('textToCopy', 'testeCopiar');
    fixture.detectChanges();

    vi.useFakeTimers();

    const botao = fixture.nativeElement.querySelector('button');
    botao.click();

    await vi.waitFor(() => expect(component.isCopied()).toBe(true));
    fixture.detectChanges();

    expect(clipboardSpy).toHaveBeenCalledWith('testeCopiar');
    expect(fixture.nativeElement.querySelector('.copy-badge')).toBeTruthy();

    vi.advanceTimersByTime(2000);
    fixture.detectChanges();

    expect(component.isCopied()).toBe(false);
    expect(fixture.nativeElement.querySelector('.copy-badge')).toBeFalsy();
    vi.useRealTimers();
  });
});
