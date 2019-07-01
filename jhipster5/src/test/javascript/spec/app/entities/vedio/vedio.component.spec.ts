/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Bisaiapp5TestModule } from '../../../test.module';
import { VedioComponent } from 'app/entities/vedio/vedio.component';
import { VedioService } from 'app/entities/vedio/vedio.service';
import { Vedio } from 'app/shared/model/vedio.model';

describe('Component Tests', () => {
  describe('Vedio Management Component', () => {
    let comp: VedioComponent;
    let fixture: ComponentFixture<VedioComponent>;
    let service: VedioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Bisaiapp5TestModule],
        declarations: [VedioComponent],
        providers: []
      })
        .overrideTemplate(VedioComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VedioComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VedioService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Vedio(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.vedios[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
