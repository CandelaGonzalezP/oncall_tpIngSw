import dayjs from 'dayjs/esm';

import { IEjecucionDeRunbook, NewEjecucionDeRunbook } from './ejecucion-de-runbook.model';

export const sampleWithRequiredData: IEjecucionDeRunbook = {
  id: 24151,
  iniciadaEn: dayjs('2023-12-04T17:27'),
  estado: 'COMPLETADA',
};

export const sampleWithPartialData: IEjecucionDeRunbook = {
  id: 15136,
  iniciadaEn: dayjs('2023-12-04T00:50'),
  estado: 'CANCELADA',
};

export const sampleWithFullData: IEjecucionDeRunbook = {
  id: 31834,
  iniciadaEn: dayjs('2023-12-04T20:20'),
  finalizadaEn: dayjs('2023-12-04T18:42'),
  estado: 'EN_CURSO',
  pasoActual: 17528,
  motivoFalla: 'irk corporation ruddy',
  notas: 'strictly freely gah',
};

export const sampleWithNewData: NewEjecucionDeRunbook = {
  iniciadaEn: dayjs('2023-12-04T20:01'),
  estado: 'CANCELADA',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
