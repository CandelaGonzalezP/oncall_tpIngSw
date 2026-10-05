import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import RunbookResolve from './route/runbook-routing-resolve.service';

const runbookRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/runbook').then(m => m.Runbook),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/runbook-detail').then(m => m.RunbookDetail),
    resolve: {
      runbook: RunbookResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/runbook-update').then(m => m.RunbookUpdate),
    resolve: {
      runbook: RunbookResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/runbook-update').then(m => m.RunbookUpdate),
    resolve: {
      runbook: RunbookResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default runbookRoute;
