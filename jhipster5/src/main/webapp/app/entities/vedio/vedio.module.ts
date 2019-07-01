import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { Bisaiapp5SharedModule } from 'app/shared';
import {
  VedioComponent,
  VedioDetailComponent,
  VedioUpdateComponent,
  VedioDeletePopupComponent,
  VedioDeleteDialogComponent,
  vedioRoute,
  vedioPopupRoute
} from './';

const ENTITY_STATES = [...vedioRoute, ...vedioPopupRoute];

@NgModule({
  imports: [Bisaiapp5SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [VedioComponent, VedioDetailComponent, VedioUpdateComponent, VedioDeleteDialogComponent, VedioDeletePopupComponent],
  entryComponents: [VedioComponent, VedioUpdateComponent, VedioDeleteDialogComponent, VedioDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Bisaiapp5VedioModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
