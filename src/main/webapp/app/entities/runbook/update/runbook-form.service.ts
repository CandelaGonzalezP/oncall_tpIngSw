import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRunbook, NewRunbook } from '../runbook.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRunbook for edit and NewRunbookFormGroupInput for create.
 */
type RunbookFormGroupInput = IRunbook | PartialWithRequiredKeyOf<NewRunbook>;

type RunbookFormDefaults = Pick<NewRunbook, 'id' | 'activo'>;

type RunbookFormGroupContent = {
  id: FormControl<IRunbook['id'] | NewRunbook['id']>;
  nombre: FormControl<IRunbook['nombre']>;
  descripcion: FormControl<IRunbook['descripcion']>;
  origenAlerta: FormControl<IRunbook['origenAlerta']>;
  severidadMinima: FormControl<IRunbook['severidadMinima']>;
  patronFingerprint: FormControl<IRunbook['patronFingerprint']>;
  tiempoMaxMinutos: FormControl<IRunbook['tiempoMaxMinutos']>;
  activo: FormControl<IRunbook['activo']>;
  servicio: FormControl<IRunbook['servicio']>;
};

export type RunbookFormGroup = FormGroup<RunbookFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RunbookFormService {
  createRunbookFormGroup(runbook?: RunbookFormGroupInput): RunbookFormGroup {
    const runbookRawValue = {
      ...this.getFormDefaults(),
      ...(runbook ?? { id: null }),
    };

    return new FormGroup<RunbookFormGroupContent>({
      id: new FormControl(
        { value: runbookRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(runbookRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
      descripcion: new FormControl(runbookRawValue.descripcion, {
        validators: [Validators.maxLength(500)],
      }),
      origenAlerta: new FormControl(runbookRawValue.origenAlerta),
      severidadMinima: new FormControl(runbookRawValue.severidadMinima),
      patronFingerprint: new FormControl(runbookRawValue.patronFingerprint, {
        validators: [Validators.maxLength(120)],
      }),
      tiempoMaxMinutos: new FormControl(runbookRawValue.tiempoMaxMinutos, {
        validators: [Validators.min(1), Validators.max(1440)],
      }),
      activo: new FormControl(runbookRawValue.activo, {
        validators: [Validators.required],
      }),
      servicio: new FormControl(runbookRawValue.servicio),
    });
  }

  getRunbook(form: RunbookFormGroup): IRunbook | NewRunbook {
    return form.getRawValue();
  }

  resetForm(form: RunbookFormGroup, runbook: RunbookFormGroupInput): void {
    const runbookRawValue = { ...this.getFormDefaults(), ...runbook };
    form.reset({
      ...runbookRawValue,
      id: { value: runbookRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): RunbookFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
