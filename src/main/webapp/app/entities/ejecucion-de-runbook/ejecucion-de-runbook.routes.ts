import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import EjecucionDeRunbookResolve from './route/ejecucion-de-runbook-routing-resolve.service';

const ejecucionDeRunbookRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ejecucion-de-runbook').then(m => m.EjecucionDeRunbook),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ejecucion-de-runbook-detail').then(m => m.EjecucionDeRunbookDetail),
    resolve: {
      ejecucionDeRunbook: EjecucionDeRunbookResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ejecucion-de-runbook-update').then(m => m.EjecucionDeRunbookUpdate),
    resolve: {
      ejecucionDeRunbook: EjecucionDeRunbookResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ejecucion-de-runbook-update').then(m => m.EjecucionDeRunbookUpdate),
    resolve: {
      ejecucionDeRunbook: EjecucionDeRunbookResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ejecucionDeRunbookRoute;
