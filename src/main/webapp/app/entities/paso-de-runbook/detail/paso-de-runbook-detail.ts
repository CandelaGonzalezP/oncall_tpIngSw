import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IPasoDeRunbook } from '../paso-de-runbook.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-paso-de-runbook-detail',
  templateUrl: './paso-de-runbook-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, TranslatePipe, RouterLink],
})
export class PasoDeRunbookDetail {
  readonly pasoDeRunbook = input<IPasoDeRunbook | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
