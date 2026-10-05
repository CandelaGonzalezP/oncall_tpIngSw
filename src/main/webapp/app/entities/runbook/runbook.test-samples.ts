import { IRunbook, NewRunbook } from './runbook.model';

export const sampleWithRequiredData: IRunbook = {
  id: 14503,
  nombre: 'eventually righteously',
  activo: true,
};

export const sampleWithPartialData: IRunbook = {
  id: 14281,
  nombre: 'save',
  descripcion: 'phooey boohoo',
  activo: true,
};

export const sampleWithFullData: IRunbook = {
  id: 8192,
  nombre: 'quinoa guzzle',
  descripcion: 'ugh',
  origenAlerta: 'PROMETHEUS',
  severidadMinima: 'SEV2',
  patronFingerprint: 'pish given',
  tiempoMaxMinutos: 1233,
  activo: false,
};

export const sampleWithNewData: NewRunbook = {
  nombre: 'certainly',
  activo: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
