import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IRunbook } from '../runbook.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../runbook.test-samples';

import { RunbookService } from './runbook.service';

const requireRestSample: IRunbook = {
  ...sampleWithRequiredData,
};

describe('Runbook Service', () => {
  let service: RunbookService;
  let httpMock: HttpTestingController;
  let expectedResult: IRunbook | IRunbook[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RunbookService);
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

    it('should create a Runbook', () => {
      const runbook = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(runbook).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Runbook', () => {
      const runbook = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(runbook).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Runbook', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Runbook', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Runbook', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addRunbookToCollectionIfMissing', () => {
      it('should add a Runbook to an empty array', () => {
        const runbook: IRunbook = sampleWithRequiredData;
        expectedResult = service.addRunbookToCollectionIfMissing([], runbook);
        expect(expectedResult).toEqual([runbook]);
      });

      it('should not add a Runbook to an array that contains it', () => {
        const runbook: IRunbook = sampleWithRequiredData;
        const runbookCollection: IRunbook[] = [
          {
            ...runbook,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRunbookToCollectionIfMissing(runbookCollection, runbook);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Runbook to an array that doesn't contain it", () => {
        const runbook: IRunbook = sampleWithRequiredData;
        const runbookCollection: IRunbook[] = [sampleWithPartialData];
        expectedResult = service.addRunbookToCollectionIfMissing(runbookCollection, runbook);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(runbook);
      });

      it('should add only unique Runbook to an array', () => {
        const runbookArray: IRunbook[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const runbookCollection: IRunbook[] = [sampleWithRequiredData];
        expectedResult = service.addRunbookToCollectionIfMissing(runbookCollection, ...runbookArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const runbook: IRunbook = sampleWithRequiredData;
        const runbook2: IRunbook = sampleWithPartialData;
        expectedResult = service.addRunbookToCollectionIfMissing([], runbook, runbook2);
        expect(expectedResult).toEqual([runbook, runbook2]);
      });

      it('should accept null and undefined values', () => {
        const runbook: IRunbook = sampleWithRequiredData;
        expectedResult = service.addRunbookToCollectionIfMissing([], null, runbook, undefined);
        expect(expectedResult).toEqual([runbook]);
      });

      it('should return initial array if no Runbook is added', () => {
        const runbookCollection: IRunbook[] = [sampleWithRequiredData];
        expectedResult = service.addRunbookToCollectionIfMissing(runbookCollection, undefined, null);
        expect(expectedResult).toEqual(runbookCollection);
      });
    });

    describe('compareRunbook', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRunbook(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19709 };
        const entity2 = null;

        const compareResult1 = service.compareRunbook(entity1, entity2);
        const compareResult2 = service.compareRunbook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19709 };
        const entity2 = { id: 18487 };

        const compareResult1 = service.compareRunbook(entity1, entity2);
        const compareResult2 = service.compareRunbook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 19709 };
        const entity2 = { id: 19709 };

        const compareResult1 = service.compareRunbook(entity1, entity2);
        const compareResult2 = service.compareRunbook(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
