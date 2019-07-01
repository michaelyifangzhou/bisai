export interface IFilm {
  id?: number;
  name?: string;
  url?: string;
  length?: number;
  playing?: boolean;
  curtime?: number;
}

export class Film implements IFilm {
  constructor(
    public id?: number,
    public name?: string,
    public url?: string,
    public length?: number,
    public playing?: boolean,
    public curtime?: number
  ) {
    this.playing = this.playing || false;
  }
}
