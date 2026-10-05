import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IEjecucionDeRunbook } from '../ejecucion-de-runbook.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ejecucion-de-runbook.test-samples';

import { EjecucionDeRunbookService, RestEjecucionDeRunbook } from './ejecucion-de-runbook.service';

const requireRestSample: RestEjecucionDeRunbook = {
  ...sampleWithRequiredData,
  iniciadaEn: sampleWithRequiredData.iniciadaEn?.toJSON(),
  finalizadaEn: sampleWithRequiredData.finalizadaEn?.toJSON(),
};

describe('EjecucionDeRunbook Service', () => {
  let service: EjecucionDeRunbookService;
  let httpMock: HttpTestingController;
  let expectedResult: IEjecucionDeRunbook | IEjecucionDeRunbook[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EjecucionDeRunbookService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a EjecucionDeRunbook', () => {
      const ejecucionDeRunbook = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ejecucionDeRunbook).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EjecucionDeRunbook', () => {
      const ejecucionDeRunbook = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ejecucionDeRunbook).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EjecucionDeRunbook', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EjecucionDeRunbook', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EjecucionDeRunbook', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addEjecucionDeRunbookToCollectionIfMissing', () => {
      it('should add a EjecucionDeRunbook to an empty array', () => {
        const ejecucionDeRunbook: IEjecucionDeRunbook = sampleWithRequiredData;
        expectedResult = service.addEjecucionDeRunbookToCollectionIfMissing([], ejecucionDeRunbook);
        expect(expectedResult).toEqual([ejecucionDeRunbook]);
      });

      it('should not add a EjecucionDeRunbook to an array that contains it', () => {
        const ejecucionDeRunbook: IEjecucionDeRunbook = sampleWithRequiredData;
        const ejecucionDeRunbookCollection: IEjecucionDeRunbook[] = [
          {
            ...ejecucionDeRunbook,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEjecucionDeRunbookToCollectionIfMissing(ejecucionDeRunbookCollection, ejecucionDeRunbook);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EjecucionDeRunbook to an array that doesn't contain it", () => {
        const ejecucionDeRunbook: IEjecucionDeRunbook = sampleWithRequiredData;
        const ejecucionDeRunbookCollection: IEjecucionDeRunbook[] = [sampleWithPartialData];
        expectedResult = service.addEjecucionDeRunbookToCollectionIfMissing(ejecucionDeRunbookCollection, ejecucionDeRunbook);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ejecucionDeRunbook);
      });

      it('should add only unique EjecucionDeRunbook to an array', () => {
        const ejecucionDeRunbookArray: IEjecucionDeRunbook[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ejecucionDeRunbookCollection: IEjecucionDeRunbook[] = [sampleWithRequiredData];
        expectedResult = service.addEjecucionDeRunbookToCollectionIfMissing(ejecucionDeRunbookCollection, ...ejecucionDeRunbookArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ejecucionDeRunbook: IEjecucionDeRunbook = sampleWithRequiredData;
        const ejecucionDeRunbook2: IEjecucionDeRunbook = sampleWithPartialData;
        expectedResult = service.addEjecucionDeRunbookToCollectionIfMissing([], ejecucionDeRunbook, ejecucionDeRunbook2);
        expect(expectedResult).toEqual([ejecucionDeRunbook, ejecucionDeRunbook2]);
      });

      it('should accept null and undefined values', () => {
        const ejecucionDeRunbook: IEjecucionDeRunbook = sampleWithRequiredData;
        expectedResult = service.addEjecucionDeRunbookToCollectionIfMissing([], null, ejecucionDeRunbook, undefined);
        expect(expectedResult).toEqual([ejecucionDeRunbook]);
      });

      it('should return initial array if no EjecucionDeRunbook is added', () => {
        const ejecucionDeRunbookCollection: IEjecucionDeRunbook[] = [sampleWithRequiredData];
        expectedResult = service.addEjecucionDeRunbookToCollectionIfMissing(ejecucionDeRunbookCollection, undefined, null);
        expect(expectedResult).toEqual(ejecucionDeRunbookCollection);
      });
    });

    describe('compareEjecucionDeRunbook', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEjecucionDeRunbook(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22255 };
        const entity2 = null;

        const compareResult1 = service.compareEjecucionDeRunbook(entity1, entity2);
        const compareResult2 = service.compareEjecucionDeRunbook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22255 };
        const entity2 = { id: 10851 };

        const compareResult1 = service.compareEjecucionDeRunbook(entity1, entity2);
        const compareResult2 = service.compareEjecucionDeRunbook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22255 };
        const entity2 = { id: 22255 };

        const compareResult1 = service.compareEjecucionDeRunbook(entity1, entity2);
        const compareResult2 = service.compareEjecucionDeRunbook(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
