import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEjecucionDeRunbook, NewEjecucionDeRunbook } from '../ejecucion-de-runbook.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEjecucionDeRunbook for edit and NewEjecucionDeRunbookFormGroupInput for create.
 */
type EjecucionDeRunbookFormGroupInput = IEjecucionDeRunbook | PartialWithRequiredKeyOf<NewEjecucionDeRunbook>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEjecucionDeRunbook | NewEjecucionDeRunbook> = Omit<T, 'iniciadaEn' | 'finalizadaEn'> & {
  iniciadaEn?: string | null;
  finalizadaEn?: string | null;
};

type EjecucionDeRunbookFormRawValue = FormValueOf<IEjecucionDeRunbook>;

type NewEjecucionDeRunbookFormRawValue = FormValueOf<NewEjecucionDeRunbook>;

type EjecucionDeRunbookFormDefaults = Pick<NewEjecucionDeRunbook, 'id' | 'iniciadaEn' | 'finalizadaEn'>;

type EjecucionDeRunbookFormGroupContent = {
  id: FormControl<EjecucionDeRunbookFormRawValue['id'] | NewEjecucionDeRunbook['id']>;
  iniciadaEn: FormControl<EjecucionDeRunbookFormRawValue['iniciadaEn']>;
  finalizadaEn: FormControl<EjecucionDeRunbookFormRawValue['finalizadaEn']>;
  estado: FormControl<EjecucionDeRunbookFormRawValue['estado']>;
  pasoActual: FormControl<EjecucionDeRunbookFormRawValue['pasoActual']>;
  motivoFalla: FormControl<EjecucionDeRunbookFormRawValue['motivoFalla']>;
  notas: FormControl<EjecucionDeRunbookFormRawValue['notas']>;
  runbook: FormControl<EjecucionDeRunbookFormRawValue['runbook']>;
  incidente: FormControl<EjecucionDeRunbookFormRawValue['incidente']>;
  ejecutor: FormControl<EjecucionDeRunbookFormRawValue['ejecutor']>;
};

export type EjecucionDeRunbookFormGroup = FormGroup<EjecucionDeRunbookFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EjecucionDeRunbookFormService {
  createEjecucionDeRunbookFormGroup(ejecucionDeRunbook?: EjecucionDeRunbookFormGroupInput): EjecucionDeRunbookFormGroup {
    const ejecucionDeRunbookRawValue = this.convertEjecucionDeRunbookToEjecucionDeRunbookRawValue({
      ...this.getFormDefaults(),
      ...(ejecucionDeRunbook ?? { id: null }),
    });

    return new FormGroup<EjecucionDeRunbookFormGroupContent>({
      id: new FormControl(
        { value: ejecucionDeRunbookRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      iniciadaEn: new FormControl(ejecucionDeRunbookRawValue.iniciadaEn, {
        validators: [Validators.required],
      }),
      finalizadaEn: new FormControl(ejecucionDeRunbookRawValue.finalizadaEn),
      estado: new FormControl(ejecucionDeRunbookRawValue.estado, {
        validators: [Validators.required],
      }),
      pasoActual: new FormControl(ejecucionDeRunbookRawValue.pasoActual, {
        validators: [Validators.min(0)],
      }),
      motivoFalla: new FormControl(ejecucionDeRunbookRawValue.motivoFalla, {
        validators: [Validators.maxLength(255)],
      }),
      notas: new FormControl(ejecucionDeRunbookRawValue.notas, {
        validators: [Validators.maxLength(1000)],
      }),
      runbook: new FormControl(ejecucionDeRunbookRawValue.runbook, {
        validators: [Validators.required],
      }),
      incidente: new FormControl(ejecucionDeRunbookRawValue.incidente, {
        validators: [Validators.required],
      }),
      ejecutor: new FormControl(ejecucionDeRunbookRawValue.ejecutor),
    });
  }

  getEjecucionDeRunbook(form: EjecucionDeRunbookFormGroup): IEjecucionDeRunbook | NewEjecucionDeRunbook {
    return this.convertEjecucionDeRunbookRawValueToEjecucionDeRunbook(form.getRawValue());
  }

  resetForm(form: EjecucionDeRunbookFormGroup, ejecucionDeRunbook: EjecucionDeRunbookFormGroupInput): void {
    const ejecucionDeRunbookRawValue = this.convertEjecucionDeRunbookToEjecucionDeRunbookRawValue({
      ...this.getFormDefaults(),
      ...ejecucionDeRunbook,
    });
    form.reset({
      ...ejecucionDeRunbookRawValue,
      id: { value: ejecucionDeRunbookRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EjecucionDeRunbookFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      iniciadaEn: currentTime,
      finalizadaEn: currentTime,
    };
  }

  private convertEjecucionDeRunbookRawValueToEjecucionDeRunbook(
    rawEjecucionDeRunbook: EjecucionDeRunbookFormRawValue | NewEjecucionDeRunbookFormRawValue,
  ): IEjecucionDeRunbook | NewEjecucionDeRunbook {
    return {
      ...rawEjecucionDeRunbook,
      iniciadaEn: dayjs(rawEjecucionDeRunbook.iniciadaEn, DATE_TIME_FORMAT),
      finalizadaEn: dayjs(rawEjecucionDeRunbook.finalizadaEn, DATE_TIME_FORMAT),
    };
  }

  private convertEjecucionDeRunbookToEjecucionDeRunbookRawValue(
    ejecucionDeRunbook: IEjecucionDeRunbook | (Partial<NewEjecucionDeRunbook> & EjecucionDeRunbookFormDefaults),
  ): EjecucionDeRunbookFormRawValue | PartialWithRequiredKeyOf<NewEjecucionDeRunbookFormRawValue> {
    return {
      ...ejecucionDeRunbook,
      iniciadaEn: ejecucionDeRunbook.iniciadaEn ? ejecucionDeRunbook.iniciadaEn.format(DATE_TIME_FORMAT) : undefined,
      finalizadaEn: ejecucionDeRunbook.finalizadaEn ? ejecucionDeRunbook.finalizadaEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
