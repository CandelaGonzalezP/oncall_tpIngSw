import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { IRunbook } from '../runbook.model';
import { RunbookService } from '../service/runbook.service';

import { RunbookFormService } from './runbook-form.service';
import { RunbookUpdate } from './runbook-update';

describe('Runbook Management Update Component', () => {
  let comp: RunbookUpdate;
  let fixture: ComponentFixture<RunbookUpdate>;
  let activatedRoute: ActivatedRoute;
  let runbookFormService: RunbookFormService;
  let runbookService: RunbookService;
  let servicioService: ServicioService;

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

    fixture = TestBed.createComponent(RunbookUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    runbookFormService = TestBed.inject(RunbookFormService);
    runbookService = TestBed.inject(RunbookService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Servicio query and add missing value', () => {
      const runbook: IRunbook = { id: 18487 };
      const servicio: IServicio = { id: 24037 };
      runbook.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicio];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ runbook });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const runbook: IRunbook = { id: 18487 };
      const servicio: IServicio = { id: 24037 };
      runbook.servicio = servicio;

      activatedRoute.data = of({ runbook });
      comp.ngOnInit();

      expect(comp.serviciosSharedCollection()).toContainEqual(servicio);
      expect(comp.runbook).toEqual(runbook);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRunbook>();
      const runbook = { id: 19709 };
      vitest.spyOn(runbookFormService, 'getRunbook').mockReturnValue(runbook);
      vitest.spyOn(runbookService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ runbook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(runbook);
      saveSubject.complete();

      // THEN
      expect(runbookFormService.getRunbook).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(runbookService.update).toHaveBeenCalledWith(expect.objectContaining(runbook));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRunbook>();
      const runbook = { id: 19709 };
      vitest.spyOn(runbookFormService, 'getRunbook').mockReturnValue({ id: null });
      vitest.spyOn(runbookService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ runbook: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(runbook);
      saveSubject.complete();

      // THEN
      expect(runbookFormService.getRunbook).toHaveBeenCalled();
      expect(runbookService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IRunbook>();
      const runbook = { id: 19709 };
      vitest.spyOn(runbookService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ runbook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(runbookService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServicio', () => {
      it('should forward to servicioService', () => {
        const entity = { id: 24037 };
        const entity2 = { id: 644 };
        vitest.spyOn(servicioService, 'compareServicio');
        comp.compareServicio(entity, entity2);
        expect(servicioService.compareServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
