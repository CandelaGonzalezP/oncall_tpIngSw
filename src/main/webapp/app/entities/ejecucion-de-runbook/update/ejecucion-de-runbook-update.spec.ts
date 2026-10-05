import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { IRunbook } from 'app/entities/runbook/runbook.model';
import { RunbookService } from 'app/entities/runbook/service/runbook.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IEjecucionDeRunbook } from '../ejecucion-de-runbook.model';
import { EjecucionDeRunbookService } from '../service/ejecucion-de-runbook.service';

import { EjecucionDeRunbookFormService } from './ejecucion-de-runbook-form.service';
import { EjecucionDeRunbookUpdate } from './ejecucion-de-runbook-update';

describe('EjecucionDeRunbook Management Update Component', () => {
  let comp: EjecucionDeRunbookUpdate;
  let fixture: ComponentFixture<EjecucionDeRunbookUpdate>;
  let activatedRoute: ActivatedRoute;
  let ejecucionDeRunbookFormService: EjecucionDeRunbookFormService;
  let ejecucionDeRunbookService: EjecucionDeRunbookService;
  let runbookService: RunbookService;
  let incidenteService: IncidenteService;
  let userService: UserService;

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

    fixture = TestBed.createComponent(EjecucionDeRunbookUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ejecucionDeRunbookFormService = TestBed.inject(EjecucionDeRunbookFormService);
    ejecucionDeRunbookService = TestBed.inject(EjecucionDeRunbookService);
    runbookService = TestBed.inject(RunbookService);
    incidenteService = TestBed.inject(IncidenteService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Runbook query and add missing value', () => {
      const ejecucionDeRunbook: IEjecucionDeRunbook = { id: 10851 };
      const runbook: IRunbook = { id: 19709 };
      ejecucionDeRunbook.runbook = runbook;

      const runbookCollection: IRunbook[] = [{ id: 19709 }];
      vitest.spyOn(runbookService, 'query').mockReturnValue(of(new HttpResponse({ body: runbookCollection })));
      const additionalRunbooks = [runbook];
      const expectedCollection: IRunbook[] = [...additionalRunbooks, ...runbookCollection];
      vitest.spyOn(runbookService, 'addRunbookToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ejecucionDeRunbook });
      comp.ngOnInit();

      expect(runbookService.query).toHaveBeenCalled();
      expect(runbookService.addRunbookToCollectionIfMissing).toHaveBeenCalledWith(
        runbookCollection,
        ...additionalRunbooks.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.runbooksSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Incidente query and add missing value', () => {
      const ejecucionDeRunbook: IEjecucionDeRunbook = { id: 10851 };
      const incidente: IIncidente = { id: 31968 };
      ejecucionDeRunbook.incidente = incidente;

      const incidenteCollection: IIncidente[] = [{ id: 31968 }];
      vitest.spyOn(incidenteService, 'query').mockReturnValue(of(new HttpResponse({ body: incidenteCollection })));
      const additionalIncidentes = [incidente];
      const expectedCollection: IIncidente[] = [...additionalIncidentes, ...incidenteCollection];
      vitest.spyOn(incidenteService, 'addIncidenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ejecucionDeRunbook });
      comp.ngOnInit();

      expect(incidenteService.query).toHaveBeenCalled();
      expect(incidenteService.addIncidenteToCollectionIfMissing).toHaveBeenCalledWith(
        incidenteCollection,
        ...additionalIncidentes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.incidentesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call User query and add missing value', () => {
      const ejecucionDeRunbook: IEjecucionDeRunbook = { id: 10851 };
      const ejecutor: IUser = { id: 3944 };
      ejecucionDeRunbook.ejecutor = ejecutor;

      const userCollection: IUser[] = [{ id: 3944 }];
      vitest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [ejecutor];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vitest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ejecucionDeRunbook });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const ejecucionDeRunbook: IEjecucionDeRunbook = { id: 10851 };
      const runbook: IRunbook = { id: 19709 };
      ejecucionDeRunbook.runbook = runbook;
      const incidente: IIncidente = { id: 31968 };
      ejecucionDeRunbook.incidente = incidente;
      const ejecutor: IUser = { id: 3944 };
      ejecucionDeRunbook.ejecutor = ejecutor;

      activatedRoute.data = of({ ejecucionDeRunbook });
      comp.ngOnInit();

      expect(comp.runbooksSharedCollection()).toContainEqual(runbook);
      expect(comp.incidentesSharedCollection()).toContainEqual(incidente);
      expect(comp.usersSharedCollection()).toContainEqual(ejecutor);
      expect(comp.ejecucionDeRunbook).toEqual(ejecucionDeRunbook);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEjecucionDeRunbook>();
      const ejecucionDeRunbook = { id: 22255 };
      vitest.spyOn(ejecucionDeRunbookFormService, 'getEjecucionDeRunbook').mockReturnValue(ejecucionDeRunbook);
      vitest.spyOn(ejecucionDeRunbookService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ejecucionDeRunbook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(ejecucionDeRunbook);
      saveSubject.complete();

      // THEN
      expect(ejecucionDeRunbookFormService.getEjecucionDeRunbook).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ejecucionDeRunbookService.update).toHaveBeenCalledWith(expect.objectContaining(ejecucionDeRunbook));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEjecucionDeRunbook>();
      const ejecucionDeRunbook = { id: 22255 };
      vitest.spyOn(ejecucionDeRunbookFormService, 'getEjecucionDeRunbook').mockReturnValue({ id: null });
      vitest.spyOn(ejecucionDeRunbookService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ejecucionDeRunbook: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(ejecucionDeRunbook);
      saveSubject.complete();

      // THEN
      expect(ejecucionDeRunbookFormService.getEjecucionDeRunbook).toHaveBeenCalled();
      expect(ejecucionDeRunbookService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEjecucionDeRunbook>();
      const ejecucionDeRunbook = { id: 22255 };
      vitest.spyOn(ejecucionDeRunbookService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ejecucionDeRunbook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ejecucionDeRunbookService.update).toHaveBeenCalled();
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

    describe('compareIncidente', () => {
      it('should forward to incidenteService', () => {
        const entity = { id: 31968 };
        const entity2 = { id: 10195 };
        vitest.spyOn(incidenteService, 'compareIncidente');
        comp.compareIncidente(entity, entity2);
        expect(incidenteService.compareIncidente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vitest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
