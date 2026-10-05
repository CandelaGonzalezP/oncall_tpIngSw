import { IRunbook } from 'app/entities/runbook/runbook.model';

export interface IPasoDeRunbook {
  id: number;
  orden?: number | null;
  titulo?: string | null;
  instrucciones?: string | null;
  obligatorio?: boolean | null;
  runbook?: Pick<IRunbook, 'id' | 'nombre'> | null;
}

export type NewPasoDeRunbook = Omit<IPasoDeRunbook, 'id'> & { id: null };
