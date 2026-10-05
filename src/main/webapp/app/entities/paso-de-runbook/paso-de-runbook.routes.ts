import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import PasoDeRunbookResolve from './route/paso-de-runbook-routing-resolve.service';

const pasoDeRunbookRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/paso-de-runbook').then(m => m.PasoDeRunbook),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/paso-de-runbook-detail').then(m => m.PasoDeRunbookDetail),
    resolve: {
      pasoDeRunbook: PasoDeRunbookResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/paso-de-runbook-update').then(m => m.PasoDeRunbookUpdate),
    resolve: {
      pasoDeRunbook: PasoDeRunbookResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/paso-de-runbook-update').then(m => m.PasoDeRunbookUpdate),
    resolve: {
      pasoDeRunbook: PasoDeRunbookResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pasoDeRunbookRoute;
