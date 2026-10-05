import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PasoDeRunbookDetail } from './paso-de-runbook-detail';

describe('PasoDeRunbook Management Detail Component', () => {
  let comp: PasoDeRunbookDetail;
  let fixture: ComponentFixture<PasoDeRunbookDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./paso-de-runbook-detail').then(m => m.PasoDeRunbookDetail),
              resolve: { pasoDeRunbook: () => of({ id: 15921 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PasoDeRunbookDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load pasoDeRunbook on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PasoDeRunbookDetail);

      // THEN
      expect(instance.pasoDeRunbook()).toEqual(expect.objectContaining({ id: 15921 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vitest.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
