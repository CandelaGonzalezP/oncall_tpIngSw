import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ejecucion-de-runbook.test-samples';

import { EjecucionDeRunbookFormService } from './ejecucion-de-runbook-form.service';

describe('EjecucionDeRunbook Form Service', () => {
  let service: EjecucionDeRunbookFormService;

  beforeEach(() => {
    service = TestBed.inject(EjecucionDeRunbookFormService);
  });

  describe('Service methods', () => {
    describe('createEjecucionDeRunbookFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEjecucionDeRunbookFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            iniciadaEn: expect.any(Object),
            finalizadaEn: expect.any(Object),
            estado: expect.any(Object),
            pasoActual: expect.any(Object),
            motivoFalla: expect.any(Object),
            notas: expect.any(Object),
            runbook: expect.any(Object),
            incidente: expect.any(Object),
            ejecutor: expect.any(Object),
          }),
        );
      });

      it('passing IEjecucionDeRunbook should create a new form with FormGroup', () => {
        const formGroup = service.createEjecucionDeRunbookFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            iniciadaEn: expect.any(Object),
            finalizadaEn: expect.any(Object),
            estado: expect.any(Object),
            pasoActual: expect.any(Object),
            motivoFalla: expect.any(Object),
            notas: expect.any(Object),
            runbook: expect.any(Object),
            incidente: expect.any(Object),
            ejecutor: expect.any(Object),
          }),
        );
      });
    });

    describe('getEjecucionDeRunbook', () => {
      it('should return NewEjecucionDeRunbook for default EjecucionDeRunbook initial value', () => {
        const formGroup = service.createEjecucionDeRunbookFormGroup(sampleWithNewData);

        const ejecucionDeRunbook = service.getEjecucionDeRunbook(formGroup);

        expect(ejecucionDeRunbook).toMatchObject(sampleWithNewData);
      });

      it('should return NewEjecucionDeRunbook for empty EjecucionDeRunbook initial value', () => {
        const formGroup = service.createEjecucionDeRunbookFormGroup();

        const ejecucionDeRunbook = service.getEjecucionDeRunbook(formGroup);

        expect(ejecucionDeRunbook).toMatchObject({});
      });

      it('should return IEjecucionDeRunbook', () => {
        const formGroup = service.createEjecucionDeRunbookFormGroup(sampleWithRequiredData);

        const ejecucionDeRunbook = service.getEjecucionDeRunbook(formGroup);

        expect(ejecucionDeRunbook).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEjecucionDeRunbook should not enable id FormControl', () => {
        const formGroup = service.createEjecucionDeRunbookFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEjecucionDeRunbook should disable id FormControl', () => {
        const formGroup = service.createEjecucionDeRunbookFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
