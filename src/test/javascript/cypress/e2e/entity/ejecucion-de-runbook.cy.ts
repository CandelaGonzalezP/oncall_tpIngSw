import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('EjecucionDeRunbook e2e test', () => {
  const ejecucionDeRunbookPageUrl = '/ejecucion-de-runbook';
  let username: string;
  let password: string;
  const ejecucionDeRunbookSample = { iniciadaEn: '2023-12-04T17:34:21.794Z', estado: 'CANCELADA' };

  let ejecucionDeRunbook;
  let runbook;
  let incidente;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/runbooks',
      body: {
        nombre: 'distant apparatus',
        descripcion: 'schedule willow lady',
        origenAlerta: 'CLOUDWATCH',
        severidadMinima: 'SEV2',
        patronFingerprint: 'which too behind',
        tiempoMaxMinutos: 526,
        activo: false,
      },
    }).then(({ body }) => {
      runbook = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/incidentes',
      body: {
        titulo: 'cook noon merrily',
        descripcion: 'plus',
        severidad: 'SEV1',
        estado: 'RESUELTO',
        detectadoEn: '2023-12-04T06:55:37.327Z',
        reconocidoEn: '2023-12-04T00:25:47.641Z',
        mitigadoEn: '2023-12-04T14:39:01.524Z',
        resueltoEn: '2023-12-04T05:35:01.805Z',
        usuariosAfectados: 20802,
        cumplioObjetivo: true,
      },
    }).then(({ body }) => {
      incidente = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ejecucion-de-runbooks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ejecucion-de-runbooks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ejecucion-de-runbooks/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/runbooks', {
      statusCode: 200,
      body: [runbook],
    });

    cy.intercept('GET', '/api/incidentes', {
      statusCode: 200,
      body: [incidente],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (ejecucionDeRunbook) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ejecucion-de-runbooks/${ejecucionDeRunbook.id}`,
      }).then(() => {
        ejecucionDeRunbook = undefined;
      });
    }
  });

  afterEach(() => {
    if (runbook) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/runbooks/${runbook.id}`,
      }).then(() => {
        runbook = undefined;
      });
    }
    if (incidente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/incidentes/${incidente.id}`,
      }).then(() => {
        incidente = undefined;
      });
    }
  });

  it('EjecucionDeRunbooks menu should load EjecucionDeRunbooks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ejecucion-de-runbook');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EjecucionDeRunbook').should('exist');
    cy.location('pathname').should('eq', ejecucionDeRunbookPageUrl);
  });

  describe('EjecucionDeRunbook page', () => {
    it('should have translated page title', () => {
      cy.visit(ejecucionDeRunbookPageUrl);
      cy.getEntityHeading('EjecucionDeRunbook').should('not.contain', 'oncallApp.ejecucionDeRunbook.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ejecucionDeRunbookPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EjecucionDeRunbook page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${ejecucionDeRunbookPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('EjecucionDeRunbook');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ejecucionDeRunbookPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ejecucion-de-runbooks',
          body: {
            ...ejecucionDeRunbookSample,
            runbook,
            incidente,
          },
        }).then(({ body }) => {
          ejecucionDeRunbook = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ejecucion-de-runbooks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ejecucionDeRunbook],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ejecucionDeRunbookPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EjecucionDeRunbook page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ejecucionDeRunbook');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ejecucionDeRunbookPageUrl);
      });

      it('edit button click should load edit EjecucionDeRunbook page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EjecucionDeRunbook');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ejecucionDeRunbookPageUrl);
      });

      it('edit button click should load edit EjecucionDeRunbook page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EjecucionDeRunbook');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ejecucionDeRunbookPageUrl);
      });

      it('last delete button click should delete instance of EjecucionDeRunbook', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ejecucionDeRunbook').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ejecucionDeRunbookPageUrl);

        ejecucionDeRunbook = undefined;
      });
    });
  });

  describe('new EjecucionDeRunbook page', () => {
    beforeEach(() => {
      cy.visit(ejecucionDeRunbookPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EjecucionDeRunbook');
    });

    it('should create an instance of EjecucionDeRunbook', () => {
      cy.get(`[data-cy="iniciadaEn"]`).type('2023-12-04T03:41');
      cy.get(`[data-cy="iniciadaEn"]`).blur();
      cy.get(`[data-cy="iniciadaEn"]`).should('have.value', '2023-12-04T03:41');

      cy.get(`[data-cy="finalizadaEn"]`).type('2023-12-04T17:39');
      cy.get(`[data-cy="finalizadaEn"]`).blur();
      cy.get(`[data-cy="finalizadaEn"]`).should('have.value', '2023-12-04T17:39');

      cy.get(`[data-cy="estado"]`).select('EN_CURSO');

      cy.get(`[data-cy="pasoActual"]`).type('29067');
      cy.get(`[data-cy="pasoActual"]`).should('have.value', '29067');

      cy.get(`[data-cy="motivoFalla"]`).type('coin justly');
      cy.get(`[data-cy="motivoFalla"]`).should('have.value', 'coin justly');

      cy.get(`[data-cy="notas"]`).type('well-groomed');
      cy.get(`[data-cy="notas"]`).should('have.value', 'well-groomed');

      cy.get(`[data-cy="runbook"]`).select(1);
      cy.get(`[data-cy="incidente"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        ejecucionDeRunbook = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', ejecucionDeRunbookPageUrl);
    });
  });
});
