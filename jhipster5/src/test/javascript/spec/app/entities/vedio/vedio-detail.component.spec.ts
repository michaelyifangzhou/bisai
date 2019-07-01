/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Bisaiapp5TestModule } from '../../../test.module';
import { VedioDetailComponent } from 'app/entities/vedio/vedio-detail.component';
import { Vedio } from 'app/shared/model/vedio.model';

describe('Component Tests', () => {
  describe('Vedio Management Detail Component', () => {
    let comp: VedioDetailComponent;
    let fixture: ComponentFixture<VedioDetailComponent>;
    const route = ({ data: of({ vedio: new Vedio(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Bisaiapp5TestModule],
        declarations: [VedioDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(VedioDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VedioDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vedio).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
