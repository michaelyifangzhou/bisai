import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { Bisaiapp5SharedLibsModule, Bisaiapp5SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [Bisaiapp5SharedLibsModule, Bisaiapp5SharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [Bisaiapp5SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Bisaiapp5SharedModule {
  static forRoot() {
    return {
      ngModule: Bisaiapp5SharedModule
    };
  }
}
