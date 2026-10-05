import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { TranslatePipe } from '@ngx-translate/core';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { EjecucionDeRunbookDeleteDialog } from '../delete/ejecucion-de-runbook-delete-dialog';
import { IEjecucionDeRunbook } from '../ejecucion-de-runbook.model';
import { EjecucionDeRunbookService } from '../service/ejecucion-de-runbook.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-ejecucion-de-runbook',
  templateUrl: './ejecucion-de-runbook.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    TranslateDirective,
    TranslatePipe,
    FormatMediumDatetimePipe,
  ],
})
export class EjecucionDeRunbook implements OnInit {
  subscription: Subscription | null = null;
  readonly ejecucionDeRunbooks = signal<IEjecucionDeRunbook[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly ejecucionDeRunbookService = inject(EjecucionDeRunbookService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.ejecucionDeRunbookService.ejecucionDeRunbooksResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.ejecucionDeRunbooks.set(this.fillComponentAttributesFromResponseBody([...this.ejecucionDeRunbookService.ejecucionDeRunbooks()]));
    });
  }

  trackId = (item: IEjecucionDeRunbook): number => this.ejecucionDeRunbookService.getEjecucionDeRunbookIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.ejecucionDeRunbooks().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(ejecucionDeRunbook: IEjecucionDeRunbook): void {
    const modalRef = this.modalService.open(EjecucionDeRunbookDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ejecucionDeRunbook = ejecucionDeRunbook;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected refineData(data: IEjecucionDeRunbook[]): IEjecucionDeRunbook[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IEjecucionDeRunbook[]): IEjecucionDeRunbook[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.ejecucionDeRunbookService.ejecucionDeRunbooksParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
