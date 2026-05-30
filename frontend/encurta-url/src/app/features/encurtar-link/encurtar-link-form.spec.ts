import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbstractControl } from '@angular/forms';
import { EncurtarLinkForm } from './encurtar-link-form';

describe('EncurtarLinkForm', () => {
  let component: EncurtarLinkForm;
  let fixture: ComponentFixture<EncurtarLinkForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EncurtarLinkForm],
    }).compileComponents();

    fixture = TestBed.createComponent(EncurtarLinkForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve ser criado', () => {
    expect(component).toBeTruthy();
  });

  describe('Validações do link', () => {
    let control: AbstractControl;

    beforeEach(() => {
      control = component.encurtaForm.get('longUrl')!;
    });

    it('deve validar o preenchimento do link', () => {
      control.setValue('');
      expect(control.hasError('required')).toBe(true);

      component.isSubmitted.set(true);
      fixture.detectChanges();

      const hintDiv = fixture.nativeElement.querySelector('.validation-error-hint');
      expect(hintDiv).toBeTruthy();

      const mensagemErro = hintDiv.querySelector('p');
      expect(mensagemErro?.textContent?.trim()).toBe('A URL deve ser preenchida!');
    });

    it('deve validar o tamanho do link', () => {
      const testValue = 'a'.repeat(2049);

      control.setValue(testValue);
      expect(control.hasError('maxlength')).toBe(true);

      component.isSubmitted.set(true);
      fixture.detectChanges();

      const hintDiv = fixture.nativeElement.querySelector('.validation-error-hint');
      expect(hintDiv).toBeTruthy();

      const mensagemErro = hintDiv.querySelector('p');
      expect(mensagemErro?.textContent?.trim()).toBe('A URL excede o número de caracteres!');
    });

    describe('Validações do formato do link', () => {
      const invalidLinks = [
        { label: 'ser texto aleatório', value: 'minha-url-invalida' },
        { label: 'estar sem protocolo', value: 'www.google.com' },
        { label: 'ter espaços em branco', value: 'http://site .com' },
        { label: 'ter apenas caracteres especiais', value: 'https://???' },
        { label: 'TLD muito curto', value: 'https://www.google.c' },
      ];

      invalidLinks.forEach((testCase) => {
        it(`o link deve ser inválido por: ${testCase.label}`, () => {
          control.setValue(testCase.value);

          expect(control.hasError('pattern')).toBeTruthy();
        });
      });

      it('deve exibir mensagem de link com formato inválido', () => {
        control.setErrors({
          pattern: true,
        });

        component.isSubmitted.set(true);
        fixture.detectChanges();

        const hintDiv = fixture.nativeElement.querySelector('.validation-error-hint');
        expect(hintDiv).toBeTruthy();

        const mensagemErro = hintDiv.querySelector('p');
        expect(mensagemErro?.textContent?.trim()).toBe('A URL não é válida!');
      });
    });

    it('deve permitir link válido', () => {
      control.setValue('https://www.youtube.com/');
      expect(control.valid).toBe(true);

      component.isSubmitted.set(true);
      fixture.detectChanges();

      const hintDiv = fixture.nativeElement.querySelector('.validation-error-hint');
      expect(hintDiv).toBeFalsy();
    });
  });

  it('deve auto completar link', () => {
    const control = component.encurtaForm.get('longUrl')!;

    control.setValue('www.google.com');

    component.formatUrl();
    expect(control.value).toBe('https://www.google.com');
  });

  it('não deve auto completar link', () => {
    const control = component.encurtaForm.get('longUrl')!;

    control.setValue('https://www.google.com');

    component.formatUrl();
    expect(control.value).toBe('https://www.google.com');
  });
});
