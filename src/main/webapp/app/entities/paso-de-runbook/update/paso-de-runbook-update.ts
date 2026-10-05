import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IRunbook } from 'app/entities/runbook/runbook.model';
import { RunbookService } from 'app/entities/runbook/service/runbook.service';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IPasoDeRunbook } from '../paso-de-runbook.model';
import { PasoDeRunbookService } from '../service/paso-de-runbook.service';

import { PasoDeRunbookFormGroup, PasoDeRunbookFormService } from './paso-de-runbook-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-paso-de-runbook-update',
  templateUrl: './paso-de-runbook-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PasoDeRunbookUpdate implements OnInit {
  readonly isSaving = signal(false);
  pasoDeRunbook: IPasoDeRunbook | null = null;

  runbooksSharedCollection = signal<IRunbook[]>([]);

  protected pasoDeRunbookService = inject(PasoDeRunbookService);
  protected pasoDeRunbookFormService = inject(PasoDeRunbookFormService);
  protected runbookService = inject(RunbookService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PasoDeRunbookFormGroup = this.pasoDeRunbookFormService.createPasoDeRunbookFormGroup();

  compareRunbook = (o1: IRunbook | null, o2: IRunbook | null): boolean => this.runbookService.compareRunbook(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pasoDeRunbook }) => {
      this.pasoDeRunbook = pasoDeRunbook;
      if (pasoDeRunbook) {
        this.updateForm(pasoDeRunbook);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const pasoDeRunbook = this.pasoDeRunbookFormService.getPasoDeRunbook(this.editForm);
    if (pasoDeRunbook.id === null) {
      this.subscribeToSaveResponse(this.pasoDeRunbookService.create(pasoDeRunbook));
    } else {
      this.subscribeToSaveResponse(this.pasoDeRunbookService.update(pasoDeRunbook));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPasoDeRunbook | null>): void {
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

  protected updateForm(pasoDeRunbook: IPasoDeRunbook): void {
    this.pasoDeRunbook = pasoDeRunbook;
    this.pasoDeRunbookFormService.resetForm(this.editForm, pasoDeRunbook);

    this.runbooksSharedCollection.update(runbooks =>
      this.runbookService.addRunbookToCollectionIfMissing<IRunbook>(runbooks, pasoDeRunbook.runbook),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.runbookService
      .query()
      .pipe(map((res: HttpResponse<IRunbook[]>) => res.body ?? []))
      .pipe(
        map((runbooks: IRunbook[]) => this.runbookService.addRunbookToCollectionIfMissing<IRunbook>(runbooks, this.pasoDeRunbook?.runbook)),
      )
      .subscribe((runbooks: IRunbook[]) => this.runbooksSharedCollection.set(runbooks));
  }
}
