import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IEjecucionDeRunbook } from '../ejecucion-de-runbook.model';
import { EjecucionDeRunbookService } from '../service/ejecucion-de-runbook.service';

const ejecucionDeRunbookResolve = (route: ActivatedRouteSnapshot): Observable<null | IEjecucionDeRunbook> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(EjecucionDeRunbookService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default ejecucionDeRunbookResolve;
