import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { EstadoEjecucion } from 'app/entities/enumerations/estado-ejecucion.model';
import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { IRunbook } from 'app/entities/runbook/runbook.model';
import { RunbookService } from 'app/entities/runbook/service/runbook.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';

import { IEjecucionDeRunbook } from '../ejecucion-de-runbook.model';
import { EjecucionDeRunbookService } from '../service/ejecucion-de-runbook.service';

import { EjecucionDeRunbookFormGroup, EjecucionDeRunbookFormService } from './ejecucion-de-runbook-form.service';
import { UserService } from 'app/entities/user/service/user.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-ejecucion-de-runbook-update',
  templateUrl: './ejecucion-de-runbook-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EjecucionDeRunbookUpdate implements OnInit {
  readonly isSaving = signal(false);
  ejecucionDeRunbook: IEjecucionDeRunbook | null = null;
  estadoEjecucionValues = Object.keys(EstadoEjecucion);

  runbooksSharedCollection = signal<IRunbook[]>([]);
  incidentesSharedCollection = signal<IIncidente[]>([]);
  usersSharedCollection = signal<IUser[]>([]);

  protected ejecucionDeRunbookService = inject(EjecucionDeRunbookService);
  protected ejecucionDeRunbookFormService = inject(EjecucionDeRunbookFormService);
  protected runbookService = inject(RunbookService);
  protected incidenteService = inject(IncidenteService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EjecucionDeRunbookFormGroup = this.ejecucionDeRunbookFormService.createEjecucionDeRunbookFormGroup();

  compareRunbook = (o1: IRunbook | null, o2: IRunbook | null): boolean => this.runbookService.compareRunbook(o1, o2);

  compareIncidente = (o1: IIncidente | null, o2: IIncidente | null): boolean => this.incidenteService.compareIncidente(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ejecucionDeRunbook }) => {
      this.ejecucionDeRunbook = ejecucionDeRunbook;
      if (ejecucionDeRunbook) {
        this.updateForm(ejecucionDeRunbook);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const ejecucionDeRunbook = this.ejecucionDeRunbookFormService.getEjecucionDeRunbook(this.editForm);
    if (ejecucionDeRunbook.id === null) {
      this.subscribeToSaveResponse(this.ejecucionDeRunbookService.create(ejecucionDeRunbook));
    } else {
      this.subscribeToSaveResponse(this.ejecucionDeRunbookService.update(ejecucionDeRunbook));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEjecucionDeRunbook | null>): void {
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

  protected updateForm(ejecucionDeRunbook: IEjecucionDeRunbook): void {
    this.ejecucionDeRunbook = ejecucionDeRunbook;
    this.ejecucionDeRunbookFormService.resetForm(this.editForm, ejecucionDeRunbook);

    this.runbooksSharedCollection.update(runbooks =>
      this.runbookService.addRunbookToCollectionIfMissing<IRunbook>(runbooks, ejecucionDeRunbook.runbook),
    );
    this.incidentesSharedCollection.update(incidentes =>
      this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, ejecucionDeRunbook.incidente),
    );
    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, ejecucionDeRunbook.ejecutor));
  }

  protected loadRelationshipsOptions(): void {
    this.runbookService
      .query()
      .pipe(map((res: HttpResponse<IRunbook[]>) => res.body ?? []))
      .pipe(
        map((runbooks: IRunbook[]) =>
          this.runbookService.addRunbookToCollectionIfMissing<IRunbook>(runbooks, this.ejecucionDeRunbook?.runbook),
        ),
      )
      .subscribe((runbooks: IRunbook[]) => this.runbooksSharedCollection.set(runbooks));

    this.incidenteService
      .query()
      .pipe(map((res: HttpResponse<IIncidente[]>) => res.body ?? []))
      .pipe(
        map((incidentes: IIncidente[]) =>
          this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, this.ejecucionDeRunbook?.incidente),
        ),
      )
      .subscribe((incidentes: IIncidente[]) => this.incidentesSharedCollection.set(incidentes));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.ejecucionDeRunbook?.ejecutor)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
