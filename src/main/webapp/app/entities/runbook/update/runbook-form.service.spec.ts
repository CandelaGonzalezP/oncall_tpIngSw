import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../runbook.test-samples';

import { RunbookFormService } from './runbook-form.service';

describe('Runbook Form Service', () => {
  let service: RunbookFormService;

  beforeEach(() => {
    service = TestBed.inject(RunbookFormService);
  });

  describe('Service methods', () => {
    describe('createRunbookFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRunbookFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            origenAlerta: expect.any(Object),
            severidadMinima: expect.any(Object),
            patronFingerprint: expect.any(Object),
            tiempoMaxMinutos: expect.any(Object),
            activo: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });

      it('passing IRunbook should create a new form with FormGroup', () => {
        const formGroup = service.createRunbookFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            origenAlerta: expect.any(Object),
            severidadMinima: expect.any(Object),
            patronFingerprint: expect.any(Object),
            tiempoMaxMinutos: expect.any(Object),
            activo: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });
    });

    describe('getRunbook', () => {
      it('should return NewRunbook for default Runbook initial value', () => {
        const formGroup = service.createRunbookFormGroup(sampleWithNewData);

        const runbook = service.getRunbook(formGroup);

        expect(runbook).toMatchObject(sampleWithNewData);
      });

      it('should return NewRunbook for empty Runbook initial value', () => {
        const formGroup = service.createRunbookFormGroup();

        const runbook = service.getRunbook(formGroup);

        expect(runbook).toMatchObject({});
      });

      it('should return IRunbook', () => {
        const formGroup = service.createRunbookFormGroup(sampleWithRequiredData);

        const runbook = service.getRunbook(formGroup);

        expect(runbook).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRunbook should not enable id FormControl', () => {
        const formGroup = service.createRunbookFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRunbook should disable id FormControl', () => {
        const formGroup = service.createRunbookFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
