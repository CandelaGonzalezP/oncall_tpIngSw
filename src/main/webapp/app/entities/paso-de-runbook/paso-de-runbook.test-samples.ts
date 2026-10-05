import { IPasoDeRunbook, NewPasoDeRunbook } from './paso-de-runbook.model';

export const sampleWithRequiredData: IPasoDeRunbook = {
  id: 14581,
  orden: 50,
  titulo: 'scruple bleakly towards',
  instrucciones: 'oh colorize handover',
  obligatorio: false,
};

export const sampleWithPartialData: IPasoDeRunbook = {
  id: 25988,
  orden: 42,
  titulo: 'as whistle',
  instrucciones: 'slowly yippee',
  obligatorio: true,
};

export const sampleWithFullData: IPasoDeRunbook = {
  id: 83,
  orden: 50,
  titulo: 'stained save',
  instrucciones: 'sleepily where',
  obligatorio: false,
};

export const sampleWithNewData: NewPasoDeRunbook = {
  orden: 44,
  titulo: 'on providence',
  instrucciones: 'lest boohoo psst',
  obligatorio: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
