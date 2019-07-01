import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'vedio',
        loadChildren: './vedio/vedio.module#Bisaiapp5VedioModule'
      },
      {
        path: 'film',
        loadChildren: './film/film.module#Bisaiapp5FilmModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Bisaiapp5EntityModule {}
