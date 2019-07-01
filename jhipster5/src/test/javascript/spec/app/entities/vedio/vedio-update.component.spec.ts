/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { Bisaiapp5TestModule } from '../../../test.module';
import { VedioUpdateComponent } from 'app/entities/vedio/vedio-update.component';
import { VedioService } from 'app/entities/vedio/vedio.service';
import { Vedio } from 'app/shared/model/vedio.model';

describe('Component Tests', () => {
  describe('Vedio Management Update Component', () => {
    let comp: VedioUpdateComponent;
    let fixture: ComponentFixture<VedioUpdateComponent>;
    let service: VedioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Bisaiapp5TestModule],
        declarations: [VedioUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VedioUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VedioUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VedioService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Vedio(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Vedio();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
