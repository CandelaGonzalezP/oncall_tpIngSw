import dayjs from 'dayjs/esm';

import { EstadoEjecucion } from 'app/entities/enumerations/estado-ejecucion.model';
import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IRunbook } from 'app/entities/runbook/runbook.model';
import { IUser } from 'app/entities/user/user.model';

export interface IEjecucionDeRunbook {
  id: number;
  iniciadaEn?: dayjs.Dayjs | null;
  finalizadaEn?: dayjs.Dayjs | null;
  estado?: keyof typeof EstadoEjecucion | null;
  pasoActual?: number | null;
  motivoFalla?: string | null;
  notas?: string | null;
  runbook?: Pick<IRunbook, 'id' | 'nombre'> | null;
  incidente?: Pick<IIncidente, 'id' | 'titulo'> | null;
  ejecutor?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewEjecucionDeRunbook = Omit<IEjecucionDeRunbook, 'id'> & { id: null };
