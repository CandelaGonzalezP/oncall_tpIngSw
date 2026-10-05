import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IEjecucionDeRunbook, NewEjecucionDeRunbook } from '../ejecucion-de-runbook.model';

export type PartialUpdateEjecucionDeRunbook = Partial<IEjecucionDeRunbook> & Pick<IEjecucionDeRunbook, 'id'>;

type RestOf<T extends IEjecucionDeRunbook | NewEjecucionDeRunbook> = Omit<T, 'iniciadaEn' | 'finalizadaEn'> & {
  iniciadaEn?: string | null;
  finalizadaEn?: string | null;
};

export type RestEjecucionDeRunbook = RestOf<IEjecucionDeRunbook>;

export type NewRestEjecucionDeRunbook = RestOf<NewEjecucionDeRunbook>;

export type PartialUpdateRestEjecucionDeRunbook = RestOf<PartialUpdateEjecucionDeRunbook>;

@Injectable()
export class EjecucionDeRunbooksService {
  readonly ejecucionDeRunbooksParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly ejecucionDeRunbooksResource = httpResource<RestEjecucionDeRunbook[]>(() => {
    const params = this.ejecucionDeRunbooksParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of ejecucionDeRunbook that have been fetched. It is updated when the ejecucionDeRunbooksResource emits a new value.
   * In case of error while fetching the ejecucionDeRunbooks, the signal is set to an empty array.
   */
  readonly ejecucionDeRunbooks = computed(() =>
    (this.ejecucionDeRunbooksResource.hasValue() ? this.ejecucionDeRunbooksResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/ejecucion-de-runbooks');

  protected convertValueFromServer(restEjecucionDeRunbook: RestEjecucionDeRunbook): IEjecucionDeRunbook {
    return {
      ...restEjecucionDeRunbook,
      iniciadaEn: restEjecucionDeRunbook.iniciadaEn ? dayjs(restEjecucionDeRunbook.iniciadaEn) : undefined,
      finalizadaEn: restEjecucionDeRunbook.finalizadaEn ? dayjs(restEjecucionDeRunbook.finalizadaEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class EjecucionDeRunbookService extends EjecucionDeRunbooksService {
  protected readonly http = inject(HttpClient);

  create(ejecucionDeRunbook: NewEjecucionDeRunbook): Observable<IEjecucionDeRunbook> {
    const copy = this.convertValueFromClient(ejecucionDeRunbook);
    return this.http.post<RestEjecucionDeRunbook>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ejecucionDeRunbook: IEjecucionDeRunbook): Observable<IEjecucionDeRunbook> {
    const copy = this.convertValueFromClient(ejecucionDeRunbook);
    return this.http
      .put<RestEjecucionDeRunbook>(
        `${this.resourceUrl}/${encodeURIComponent(this.getEjecucionDeRunbookIdentifier(ejecucionDeRunbook))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ejecucionDeRunbook: PartialUpdateEjecucionDeRunbook): Observable<IEjecucionDeRunbook> {
    const copy = this.convertValueFromClient(ejecucionDeRunbook);
    return this.http
      .patch<RestEjecucionDeRunbook>(
        `${this.resourceUrl}/${encodeURIComponent(this.getEjecucionDeRunbookIdentifier(ejecucionDeRunbook))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IEjecucionDeRunbook> {
    return this.http
      .get<RestEjecucionDeRunbook>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IEjecucionDeRunbook[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEjecucionDeRunbook[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEjecucionDeRunbookIdentifier(ejecucionDeRunbook: Pick<IEjecucionDeRunbook, 'id'>): number {
    return ejecucionDeRunbook.id;
  }

  compareEjecucionDeRunbook(o1: Pick<IEjecucionDeRunbook, 'id'> | null, o2: Pick<IEjecucionDeRunbook, 'id'> | null): boolean {
    return o1 && o2 ? this.getEjecucionDeRunbookIdentifier(o1) === this.getEjecucionDeRunbookIdentifier(o2) : o1 === o2;
  }

  addEjecucionDeRunbookToCollectionIfMissing<Type extends Pick<IEjecucionDeRunbook, 'id'>>(
    ejecucionDeRunbookCollection: Type[],
    ...ejecucionDeRunbooksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ejecucionDeRunbooks: Type[] = ejecucionDeRunbooksToCheck.filter(isPresent);
    if (ejecucionDeRunbooks.length > 0) {
      const ejecucionDeRunbookCollectionIdentifiers = ejecucionDeRunbookCollection.map(ejecucionDeRunbookItem =>
        this.getEjecucionDeRunbookIdentifier(ejecucionDeRunbookItem),
      );
      const ejecucionDeRunbooksToAdd = ejecucionDeRunbooks.filter(ejecucionDeRunbookItem => {
        const ejecucionDeRunbookIdentifier = this.getEjecucionDeRunbookIdentifier(ejecucionDeRunbookItem);
        if (ejecucionDeRunbookCollectionIdentifiers.includes(ejecucionDeRunbookIdentifier)) {
          return false;
        }
        ejecucionDeRunbookCollectionIdentifiers.push(ejecucionDeRunbookIdentifier);
        return true;
      });
      return [...ejecucionDeRunbooksToAdd, ...ejecucionDeRunbookCollection];
    }
    return ejecucionDeRunbookCollection;
  }

  protected convertValueFromClient<T extends IEjecucionDeRunbook | NewEjecucionDeRunbook | PartialUpdateEjecucionDeRunbook>(
    ejecucionDeRunbook: T,
  ): RestOf<T> {
    return {
      ...ejecucionDeRunbook,
      iniciadaEn: ejecucionDeRunbook.iniciadaEn?.toJSON() ?? null,
      finalizadaEn: ejecucionDeRunbook.finalizadaEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestEjecucionDeRunbook): IEjecucionDeRunbook {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestEjecucionDeRunbook[]): IEjecucionDeRunbook[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
