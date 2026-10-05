import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IRunbook } from 'app/entities/runbook/runbook.model';
import { RunbookService } from 'app/entities/runbook/service/runbook.service';
import { IPasoDeRunbook } from '../paso-de-runbook.model';
import { PasoDeRunbookService } from '../service/paso-de-runbook.service';

import { PasoDeRunbookFormService } from './paso-de-runbook-form.service';
import { PasoDeRunbookUpdate } from './paso-de-runbook-update';

describe('PasoDeRunbook Management Update Component', () => {
  let comp: PasoDeRunbookUpdate;
  let fixture: ComponentFixture<PasoDeRunbookUpdate>;
  let activatedRoute: ActivatedRoute;
  let pasoDeRunbookFormService: PasoDeRunbookFormService;
  let pasoDeRunbookService: PasoDeRunbookService;
  let runbookService: RunbookService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PasoDeRunbookUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pasoDeRunbookFormService = TestBed.inject(PasoDeRunbookFormService);
    pasoDeRunbookService = TestBed.inject(PasoDeRunbookService);
    runbookService = TestBed.inject(RunbookService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Runbook query and add missing value', () => {
      const pasoDeRunbook: IPasoDeRunbook = { id: 2779 };
      const runbook: IRunbook = { id: 19709 };
      pasoDeRunbook.runbook = runbook;

      const runbookCollection: IRunbook[] = [{ id: 19709 }];
      vitest.spyOn(runbookService, 'query').mockReturnValue(of(new HttpResponse({ body: runbookCollection })));
      const additionalRunbooks = [runbook];
      const expectedCollection: IRunbook[] = [...additionalRunbooks, ...runbookCollection];
      vitest.spyOn(runbookService, 'addRunbookToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pasoDeRunbook });
      comp.ngOnInit();

      expect(runbookService.query).toHaveBeenCalled();
      expect(runbookService.addRunbookToCollectionIfMissing).toHaveBeenCalledWith(
        runbookCollection,
        ...additionalRunbooks.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.runbooksSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const pasoDeRunbook: IPasoDeRunbook = { id: 2779 };
      const runbook: IRunbook = { id: 19709 };
      pasoDeRunbook.runbook = runbook;

      activatedRoute.data = of({ pasoDeRunbook });
      comp.ngOnInit();

      expect(comp.runbooksSharedCollection()).toContainEqual(runbook);
      expect(comp.pasoDeRunbook).toEqual(pasoDeRunbook);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPasoDeRunbook>();
      const pasoDeRunbook = { id: 15921 };
      vitest.spyOn(pasoDeRunbookFormService, 'getPasoDeRunbook').mockReturnValue(pasoDeRunbook);
      vitest.spyOn(pasoDeRunbookService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pasoDeRunbook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(pasoDeRunbook);
      saveSubject.complete();

      // THEN
      expect(pasoDeRunbookFormService.getPasoDeRunbook).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pasoDeRunbookService.update).toHaveBeenCalledWith(expect.objectContaining(pasoDeRunbook));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPasoDeRunbook>();
      const pasoDeRunbook = { id: 15921 };
      vitest.spyOn(pasoDeRunbookFormService, 'getPasoDeRunbook').mockReturnValue({ id: null });
      vitest.spyOn(pasoDeRunbookService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pasoDeRunbook: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(pasoDeRunbook);
      saveSubject.complete();

      // THEN
      expect(pasoDeRunbookFormService.getPasoDeRunbook).toHaveBeenCalled();
      expect(pasoDeRunbookService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPasoDeRunbook>();
      const pasoDeRunbook = { id: 15921 };
      vitest.spyOn(pasoDeRunbookService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pasoDeRunbook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pasoDeRunbookService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRunbook', () => {
      it('should forward to runbookService', () => {
        const entity = { id: 19709 };
        const entity2 = { id: 18487 };
        vitest.spyOn(runbookService, 'compareRunbook');
        comp.compareRunbook(entity, entity2);
        expect(runbookService.compareRunbook).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
