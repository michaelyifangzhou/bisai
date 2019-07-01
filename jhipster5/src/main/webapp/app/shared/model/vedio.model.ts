export interface IVedio {
  id?: number;
  name?: string;
  url?: string;
  isplaying?: boolean;
  length?: number;
}

export class Vedio implements IVedio {
  constructor(public id?: number, public name?: string, public url?: string, public isplaying?: boolean, public length?: number) {
    this.isplaying = this.isplaying || false;
  }
}
