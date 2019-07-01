import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { Bisaiapp5SharedModule } from 'app/shared';
import {
  FilmComponent,
  FilmDetailComponent,
  FilmUpdateComponent,
  FilmDeletePopupComponent,
  FilmDeleteDialogComponent,
  filmRoute,
  filmPopupRoute
} from './';

const ENTITY_STATES = [...filmRoute, ...filmPopupRoute];

@NgModule({
  imports: [Bisaiapp5SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [FilmComponent, FilmDetailComponent, FilmUpdateComponent, FilmDeleteDialogComponent, FilmDeletePopupComponent],
  entryComponents: [FilmComponent, FilmUpdateComponent, FilmDeleteDialogComponent, FilmDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Bisaiapp5FilmModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
