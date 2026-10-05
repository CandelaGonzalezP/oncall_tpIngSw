import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IRunbook, NewRunbook } from '../runbook.model';

export type PartialUpdateRunbook = Partial<IRunbook> & Pick<IRunbook, 'id'>;

@Injectable()
export class RunbooksService {
  readonly runbooksParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly runbooksResource = httpResource<IRunbook[]>(() => {
    const params = this.runbooksParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of runbook that have been fetched. It is updated when the runbooksResource emits a new value.
   * In case of error while fetching the runbooks, the signal is set to an empty array.
   */
  readonly runbooks = computed(() => (this.runbooksResource.hasValue() ? this.runbooksResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/runbooks');
}

@Injectable({ providedIn: 'root' })
export class RunbookService extends RunbooksService {
  protected readonly http = inject(HttpClient);

  create(runbook: NewRunbook): Observable<IRunbook> {
    return this.http.post<IRunbook>(this.resourceUrl, runbook);
  }

  update(runbook: IRunbook): Observable<IRunbook> {
    return this.http.put<IRunbook>(`${this.resourceUrl}/${encodeURIComponent(this.getRunbookIdentifier(runbook))}`, runbook);
  }

  partialUpdate(runbook: PartialUpdateRunbook): Observable<IRunbook> {
    return this.http.patch<IRunbook>(`${this.resourceUrl}/${encodeURIComponent(this.getRunbookIdentifier(runbook))}`, runbook);
  }

  find(id: number): Observable<IRunbook> {
    return this.http.get<IRunbook>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IRunbook[]>> {
    const options = createRequestOption(req);
    return this.http.get<IRunbook[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getRunbookIdentifier(runbook: Pick<IRunbook, 'id'>): number {
    return runbook.id;
  }

  compareRunbook(o1: Pick<IRunbook, 'id'> | null, o2: Pick<IRunbook, 'id'> | null): boolean {
    return o1 && o2 ? this.getRunbookIdentifier(o1) === this.getRunbookIdentifier(o2) : o1 === o2;
  }

  addRunbookToCollectionIfMissing<Type extends Pick<IRunbook, 'id'>>(
    runbookCollection: Type[],
    ...runbooksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const runbooks: Type[] = runbooksToCheck.filter(isPresent);
    if (runbooks.length > 0) {
      const runbookCollectionIdentifiers = runbookCollection.map(runbookItem => this.getRunbookIdentifier(runbookItem));
      const runbooksToAdd = runbooks.filter(runbookItem => {
        const runbookIdentifier = this.getRunbookIdentifier(runbookItem);
        if (runbookCollectionIdentifiers.includes(runbookIdentifier)) {
          return false;
        }
        runbookCollectionIdentifiers.push(runbookIdentifier);
        return true;
      });
      return [...runbooksToAdd, ...runbookCollection];
    }
    return runbookCollection;
  }
}
