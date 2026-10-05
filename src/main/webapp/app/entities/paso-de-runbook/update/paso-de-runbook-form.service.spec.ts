import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../paso-de-runbook.test-samples';

import { PasoDeRunbookFormService } from './paso-de-runbook-form.service';

describe('PasoDeRunbook Form Service', () => {
  let service: PasoDeRunbookFormService;

  beforeEach(() => {
    service = TestBed.inject(PasoDeRunbookFormService);
  });

  describe('Service methods', () => {
    describe('createPasoDeRunbookFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPasoDeRunbookFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            orden: expect.any(Object),
            titulo: expect.any(Object),
            instrucciones: expect.any(Object),
            obligatorio: expect.any(Object),
            runbook: expect.any(Object),
          }),
        );
      });

      it('passing IPasoDeRunbook should create a new form with FormGroup', () => {
        const formGroup = service.createPasoDeRunbookFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            orden: expect.any(Object),
            titulo: expect.any(Object),
            instrucciones: expect.any(Object),
            obligatorio: expect.any(Object),
            runbook: expect.any(Object),
          }),
        );
      });
    });

    describe('getPasoDeRunbook', () => {
      it('should return NewPasoDeRunbook for default PasoDeRunbook initial value', () => {
        const formGroup = service.createPasoDeRunbookFormGroup(sampleWithNewData);

        const pasoDeRunbook = service.getPasoDeRunbook(formGroup);

        expect(pasoDeRunbook).toMatchObject(sampleWithNewData);
      });

      it('should return NewPasoDeRunbook for empty PasoDeRunbook initial value', () => {
        const formGroup = service.createPasoDeRunbookFormGroup();

        const pasoDeRunbook = service.getPasoDeRunbook(formGroup);

        expect(pasoDeRunbook).toMatchObject({});
      });

      it('should return IPasoDeRunbook', () => {
        const formGroup = service.createPasoDeRunbookFormGroup(sampleWithRequiredData);

        const pasoDeRunbook = service.getPasoDeRunbook(formGroup);

        expect(pasoDeRunbook).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPasoDeRunbook should not enable id FormControl', () => {
        const formGroup = service.createPasoDeRunbookFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPasoDeRunbook should disable id FormControl', () => {
        const formGroup = service.createPasoDeRunbookFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
