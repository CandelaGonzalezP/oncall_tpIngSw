import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { OrigenAlerta } from 'app/entities/enumerations/origen-alerta.model';
import { Severidad } from 'app/entities/enumerations/severidad.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IRunbook } from '../runbook.model';
import { RunbookService } from '../service/runbook.service';

import { RunbookFormGroup, RunbookFormService } from './runbook-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-runbook-update',
  templateUrl: './runbook-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class RunbookUpdate implements OnInit {
  readonly isSaving = signal(false);
  runbook: IRunbook | null = null;
  origenAlertaValues = Object.keys(OrigenAlerta);
  severidadValues = Object.keys(Severidad);

  serviciosSharedCollection = signal<IServicio[]>([]);

  protected runbookService = inject(RunbookService);
  protected runbookFormService = inject(RunbookFormService);
  protected servicioService = inject(ServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RunbookFormGroup = this.runbookFormService.createRunbookFormGroup();

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ runbook }) => {
      this.runbook = runbook;
      if (runbook) {
        this.updateForm(runbook);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const runbook = this.runbookFormService.getRunbook(this.editForm);
    if (runbook.id === null) {
      this.subscribeToSaveResponse(this.runbookService.create(runbook));
    } else {
      this.subscribeToSaveResponse(this.runbookService.update(runbook));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IRunbook | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(runbook: IRunbook): void {
    this.runbook = runbook;
    this.runbookFormService.resetForm(this.editForm, runbook);

    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, runbook.servicio),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, this.runbook?.servicio),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));
  }
}
