import { OrigenAlerta } from 'app/entities/enumerations/origen-alerta.model';
import { Severidad } from 'app/entities/enumerations/severidad.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IRunbook {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  origenAlerta?: keyof typeof OrigenAlerta | null;
  severidadMinima?: keyof typeof Severidad | null;
  patronFingerprint?: string | null;
  tiempoMaxMinutos?: number | null;
  activo?: boolean | null;
  servicio?: Pick<IServicio, 'id' | 'nombre'> | null;
}

export type NewRunbook = Omit<IRunbook, 'id'> & { id: null };
