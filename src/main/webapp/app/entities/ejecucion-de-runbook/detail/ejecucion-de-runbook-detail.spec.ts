import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { EjecucionDeRunbookDetail } from './ejecucion-de-runbook-detail';

describe('EjecucionDeRunbook Management Detail Component', () => {
  let comp: EjecucionDeRunbookDetail;
  let fixture: ComponentFixture<EjecucionDeRunbookDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ejecucion-de-runbook-detail').then(m => m.EjecucionDeRunbookDetail),
              resolve: { ejecucionDeRunbook: () => of({ id: 22255 }) },
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
    fixture = TestBed.createComponent(EjecucionDeRunbookDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load ejecucionDeRunbook on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EjecucionDeRunbookDetail);

      // THEN
      expect(instance.ejecucionDeRunbook()).toEqual(expect.objectContaining({ id: 22255 }));
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
