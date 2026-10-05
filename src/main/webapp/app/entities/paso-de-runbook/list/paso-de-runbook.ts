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
import { TranslateDirective } from 'app/shared/language';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { PasoDeRunbookDeleteDialog } from '../delete/paso-de-runbook-delete-dialog';
import { IPasoDeRunbook } from '../paso-de-runbook.model';
import { PasoDeRunbookService } from '../service/paso-de-runbook.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-paso-de-runbook',
  templateUrl: './paso-de-runbook.html',
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
  ],
})
export class PasoDeRunbook implements OnInit {
  subscription: Subscription | null = null;
  readonly pasoDeRunbooks = signal<IPasoDeRunbook[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly pasoDeRunbookService = inject(PasoDeRunbookService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.pasoDeRunbookService.pasoDeRunbooksResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.pasoDeRunbooks.set(this.fillComponentAttributesFromResponseBody([...this.pasoDeRunbookService.pasoDeRunbooks()]));
    });
  }

  trackId = (item: IPasoDeRunbook): number => this.pasoDeRunbookService.getPasoDeRunbookIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.pasoDeRunbooks().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(pasoDeRunbook: IPasoDeRunbook): void {
    const modalRef = this.modalService.open(PasoDeRunbookDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pasoDeRunbook = pasoDeRunbook;
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

  protected refineData(data: IPasoDeRunbook[]): IPasoDeRunbook[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IPasoDeRunbook[]): IPasoDeRunbook[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.pasoDeRunbookService.pasoDeRunbooksParams.set(queryObject);
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
