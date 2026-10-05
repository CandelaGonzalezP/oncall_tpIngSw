import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IPasoDeRunbook, NewPasoDeRunbook } from '../paso-de-runbook.model';

export type PartialUpdatePasoDeRunbook = Partial<IPasoDeRunbook> & Pick<IPasoDeRunbook, 'id'>;

@Injectable()
export class PasoDeRunbooksService {
  readonly pasoDeRunbooksParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly pasoDeRunbooksResource = httpResource<IPasoDeRunbook[]>(() => {
    const params = this.pasoDeRunbooksParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of pasoDeRunbook that have been fetched. It is updated when the pasoDeRunbooksResource emits a new value.
   * In case of error while fetching the pasoDeRunbooks, the signal is set to an empty array.
   */
  readonly pasoDeRunbooks = computed(() => (this.pasoDeRunbooksResource.hasValue() ? this.pasoDeRunbooksResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/paso-de-runbooks');
}

@Injectable({ providedIn: 'root' })
export class PasoDeRunbookService extends PasoDeRunbooksService {
  protected readonly http = inject(HttpClient);

  create(pasoDeRunbook: NewPasoDeRunbook): Observable<IPasoDeRunbook> {
    return this.http.post<IPasoDeRunbook>(this.resourceUrl, pasoDeRunbook);
  }

  update(pasoDeRunbook: IPasoDeRunbook): Observable<IPasoDeRunbook> {
    return this.http.put<IPasoDeRunbook>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPasoDeRunbookIdentifier(pasoDeRunbook))}`,
      pasoDeRunbook,
    );
  }

  partialUpdate(pasoDeRunbook: PartialUpdatePasoDeRunbook): Observable<IPasoDeRunbook> {
    return this.http.patch<IPasoDeRunbook>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPasoDeRunbookIdentifier(pasoDeRunbook))}`,
      pasoDeRunbook,
    );
  }

  find(id: number): Observable<IPasoDeRunbook> {
    return this.http.get<IPasoDeRunbook>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPasoDeRunbook[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPasoDeRunbook[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPasoDeRunbookIdentifier(pasoDeRunbook: Pick<IPasoDeRunbook, 'id'>): number {
    return pasoDeRunbook.id;
  }

  comparePasoDeRunbook(o1: Pick<IPasoDeRunbook, 'id'> | null, o2: Pick<IPasoDeRunbook, 'id'> | null): boolean {
    return o1 && o2 ? this.getPasoDeRunbookIdentifier(o1) === this.getPasoDeRunbookIdentifier(o2) : o1 === o2;
  }

  addPasoDeRunbookToCollectionIfMissing<Type extends Pick<IPasoDeRunbook, 'id'>>(
    pasoDeRunbookCollection: Type[],
    ...pasoDeRunbooksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pasoDeRunbooks: Type[] = pasoDeRunbooksToCheck.filter(isPresent);
    if (pasoDeRunbooks.length > 0) {
      const pasoDeRunbookCollectionIdentifiers = pasoDeRunbookCollection.map(pasoDeRunbookItem =>
        this.getPasoDeRunbookIdentifier(pasoDeRunbookItem),
      );
      const pasoDeRunbooksToAdd = pasoDeRunbooks.filter(pasoDeRunbookItem => {
        const pasoDeRunbookIdentifier = this.getPasoDeRunbookIdentifier(pasoDeRunbookItem);
        if (pasoDeRunbookCollectionIdentifiers.includes(pasoDeRunbookIdentifier)) {
          return false;
        }
        pasoDeRunbookCollectionIdentifiers.push(pasoDeRunbookIdentifier);
        return true;
      });
      return [...pasoDeRunbooksToAdd, ...pasoDeRunbookCollection];
    }
    return pasoDeRunbookCollection;
  }
}
