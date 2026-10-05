import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPasoDeRunbook, NewPasoDeRunbook } from '../paso-de-runbook.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPasoDeRunbook for edit and NewPasoDeRunbookFormGroupInput for create.
 */
type PasoDeRunbookFormGroupInput = IPasoDeRunbook | PartialWithRequiredKeyOf<NewPasoDeRunbook>;

type PasoDeRunbookFormDefaults = Pick<NewPasoDeRunbook, 'id' | 'obligatorio'>;

type PasoDeRunbookFormGroupContent = {
  id: FormControl<IPasoDeRunbook['id'] | NewPasoDeRunbook['id']>;
  orden: FormControl<IPasoDeRunbook['orden']>;
  titulo: FormControl<IPasoDeRunbook['titulo']>;
  instrucciones: FormControl<IPasoDeRunbook['instrucciones']>;
  obligatorio: FormControl<IPasoDeRunbook['obligatorio']>;
  runbook: FormControl<IPasoDeRunbook['runbook']>;
};

export type PasoDeRunbookFormGroup = FormGroup<PasoDeRunbookFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PasoDeRunbookFormService {
  createPasoDeRunbookFormGroup(pasoDeRunbook?: PasoDeRunbookFormGroupInput): PasoDeRunbookFormGroup {
    const pasoDeRunbookRawValue = {
      ...this.getFormDefaults(),
      ...(pasoDeRunbook ?? { id: null }),
    };

    return new FormGroup<PasoDeRunbookFormGroupContent>({
      id: new FormControl(
        { value: pasoDeRunbookRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      orden: new FormControl(pasoDeRunbookRawValue.orden, {
        validators: [Validators.required, Validators.min(1), Validators.max(50)],
      }),
      titulo: new FormControl(pasoDeRunbookRawValue.titulo, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      instrucciones: new FormControl(pasoDeRunbookRawValue.instrucciones, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      obligatorio: new FormControl(pasoDeRunbookRawValue.obligatorio, {
        validators: [Validators.required],
      }),
      runbook: new FormControl(pasoDeRunbookRawValue.runbook, {
        validators: [Validators.required],
      }),
    });
  }

  getPasoDeRunbook(form: PasoDeRunbookFormGroup): IPasoDeRunbook | NewPasoDeRunbook {
    return form.getRawValue();
  }

  resetForm(form: PasoDeRunbookFormGroup, pasoDeRunbook: PasoDeRunbookFormGroupInput): void {
    const pasoDeRunbookRawValue = { ...this.getFormDefaults(), ...pasoDeRunbook };
    form.reset({
      ...pasoDeRunbookRawValue,
      id: { value: pasoDeRunbookRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PasoDeRunbookFormDefaults {
    return {
      id: null,
      obligatorio: false,
    };
  }
}
