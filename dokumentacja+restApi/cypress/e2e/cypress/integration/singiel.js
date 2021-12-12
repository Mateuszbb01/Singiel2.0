describe('Test RestApi Singiel2.0', function () {
    const authorization = 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xOTIuMTY4LjEuNzA6ODA4MVwvYXBpXC9hdXRoXC9sb2dpbiIsImlhdCI6MTYzOTM0MDA4NCwiZXhwIjoxNjM5NTIwMDg0LCJuYmYiOjE2MzkzNDAwODQsImp0aSI6InA2ckNQSWN3UFVJeXNDYjEiLCJzdWIiOjUsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.eLI6gPgpnWuulsYOsSMC2fzNeliWcTwbN2u_Oz0GFUs';

    ////////Test1////////
    it('POST | Logowanie użytkowanika pomyślne', function () {
        cy.request({
            method: 'POST',
            url: 'http://192.168.1.70:8081/api/auth/login',
            headers: {
                'content-type': 'application/json'
            },
            body: {
                'email': 'brt@gmail.com',
                'password': '12345678',
            }
        }).then(function (response) {
            // expect(response).to.have.property('status', true)
            expect(response.body.success).equal(true)

            expect(response.status).to.eq(200)
            expect(response).to.not.be.null
            expect(response.body.success).to.eq(true)
            expect(response.body).to.have.all.keys(
                'token', 'success', 'user'
            )
            expect(response.body.user).to.have.all.keys(
                'id',
                'email',
                'email_verified_at',
                'gcmtoken',
                'created_at',
                'updated_at'
            )
            // expect(response.body.user.length).to.be.eq(1);

            //expect(response.body).to.have.property('success', true)
        });
    });
    ////////Test2////////
    it('POST | Logowanie - błędne dane', function () {
        cy.request({
            method: 'POST',
            url: 'http://192.168.1.70:8081/api/auth/login',
            headers: {
                'content-type': 'application/json'
            },
            failOnStatusCode: false,
            form: true,
            body: {
                'email': 'nataliakowalska@gmail.com',
                'password': '12345678',
            }
        }).then(function (response) {
            // expect(response).to.have.property('status', true)
            expect(response.body.success).equal(false)
            expect(response.body.error).equal('Niepoprawne dane.')
            expect(response.status).to.eq(401)
            expect(response).to.not.be.null
            expect(response.body).to.have.all.keys(
                'error', 'success'
            )

        });
    });
    ////////Test3////////
    it('GET | Wylogowanie', function () {
        const authorization = 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xOTIuMTY4LjEuNzA6ODA4MVwvYXBpXC9hdXRoXC9sb2dpbiIsImlhdCI6MTYzOTM0NDMwNiwiZXhwIjoxNjM5NTI0MzA2LCJuYmYiOjE2MzkzNDQzMDYsImp0aSI6InVWQ1dwWDU5ODRXYUtkbE0iLCJzdWIiOjIsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.0fRcuG9caJM5uqhx-Di8gUZOZrg4G8WMN2r-BE9tJKI';
        cy.request({
            method: 'GET',
            url: 'http://192.168.1.70:8081/api/auth/logout',

            headers: {
                authorization
            },

        }).then(function (response) {
            // expect(response).to.have.property('status', true)
            expect(response.body.success).equal(true)
            // expect(response.body.user.length).to.be.eq(1);

            //expect(response.body).to.have.property('success', true)
        });
    });
    ////////Test4////////
    it('POST | Rejestracja użytkownika', function () {
        cy.request({
            method: 'POST',
            url: 'http://192.168.1.70:8081/api/auth/register',
            headers: {
                'content-type': 'application/json'
            },
            body: {
                'name': 'cypresssingiel',
                'email': 'testcypressa@singiel.com',
                'password': '12345678',
                'password_confirmation': '12345678',
            }
        }).then(function (response) {
            expect(response.status).to.eq(201)
            expect(response).to.not.be.null
            expect(response.body).to.have.all.keys(
                'token', 'success', 'user'
            )
            expect(response.body.user).to.have.all.keys(
                'email',
                'created_at',
                'updated_at',
                'id'
            )

        });
    });
    ////////Test5////////
    it('GET | Wyświetlanie przeciwnej płci', function () {
        cy.request({
            method: 'GET',
            url: 'http://192.168.1.70:8081/api/auth/showUser',

            headers: {
                authorization
            },

        }).then(function (response) {
            expect(response.body.success).equal(true)
            expect(response.body).to.have.all.keys(
                'success', 'users'
            )
        });
    });

    ////////Test6////////
    it('GET | Wyświetlanie sparowanych użytkowników', function () {
        cy.request({
            method: 'GET',
            url: 'http://192.168.1.70:8081/api/auth/showPairedUser',

            headers: {
                authorization
            },

        }).then(function (response) {
            expect(response.body.success).equal(true)
            expect(response.body).to.have.all.keys(
                'success', 'paired'
            )
        });
    });
    ////////Test7////////
    it('GET | Preferencje użytkownika', function () {
        cy.request({
            method: 'GET',
            url: 'http://192.168.1.70:8081/api/auth/preferences/mypreferences',

            headers: {
                authorization
            },

        }).then(function (response) {
            expect(response.body.success).equal(true)
            expect(response.body).to.have.all.keys(
                'success', 'preferences2'
            )
        });
    });
    ////////Test8////////
    it('POST | Wysyłanie wiadomości', function () {
        cy.request({
            method: 'POST',
            url: 'http://192.168.1.70:8081/api/auth/send',

            headers: {
                authorization,
                'content-type': 'application/json'
            },
            body: {
                'message': 'cypresstest',
                'user_to_id': '2',

            }
        }).then(function (response) {
            expect(response.body.success).equal(true)
            expect(response.body).to.have.all.keys(
                'success'
            )
        });
    });
    ////////Test9////////
    it('POST | Wyświetlanie wiadomości', function () {
        cy.request({
            method: 'POST',
            url: 'http://192.168.1.70:8081/api/auth/messages',

            headers: {
                authorization,
                'content-type': 'application/json'
            },
            body: {
                'user_to_id': '2',
                'page': '1',
                'count_notification': '0',

            }
        }).then(function (response) {
            expect(response.status).to.eq(200)
            expect(response.body.success).equal(true)
            expect(response.body).to.have.all.keys(
                'success', 'Message', 'wiadomosc'
            )
            expect(response.body.Message).to.have.all.keys(
                'current_page',
                'data',
                'first_page_url',
                'from',
                'last_page',
                'last_page_url',
                'links',
                'next_page_url',
                'path',
                'per_page',
                'prev_page_url',
                'to',
                'total',
            )
        });
    });
    ////////Test10////////
    it('POST | Dodawanie danych kontakowych', function () {
        cy.request({
            method: 'POST',
            url: 'http://192.168.1.70:8081/api/auth/preferences/mypreferences/update-vcard',

            headers: {
                authorization,
                'content-type': 'application/json'
            },
            body: {
                'phone_no': '666551471',
                'mail': 'essa@singiel.com',
                'address': 'test',
                'company': 'test',
                'website': 'test',
            }
        }).then(function (response) {
            expect(response.status).to.eq(200)
            expect(response.body.success).equal(true)
            expect(response.body).to.have.all.keys(
                'message', 'preferences', 'success'
            )
            expect(response.body.message).equal('Dodano vCard')

            expect(response.body.preferences).to.have.all.keys(
                'id',
                'user_id',
                'name',
                'bornDate',
                'gender',
                'city',
                'interests',
                'photo',
                'created_at',
                'updated_at',
                'phone_no',
                'mail',
                'address',
                'company',
                'website',
                'vcard'
            )
        });
    });
    ////////Test11////////
    it('POST | Wysyłanie vCard', function () {
        cy.request({
            method: 'POST',
            url: 'http://192.168.1.70:8081/api/auth/send-vcard',

            headers: {
                authorization,
                'content-type': 'application/json'
            },
            body: {
                'user_to_id': '2',

            }
        }).then(function (response) {
            expect(response.status).to.eq(200)
            expect(response.body.success).equal(true)
            expect(response.body).to.have.all.keys(
                'success'
            )
        });
    });
    ////////Test12////////
    it('POST | Zapis tokena FCM do bazy', function () {
        cy.request({
            method: 'POST',
            url: 'http://192.168.1.70:8081/api/auth/storegcmtoken',

            headers: {
                authorization,
                'content-type': 'application/json'
            },
            body: {
                'token': 'xZJOOadV-ppp3_962rfI33Mk7aDRUpjjGxZXoUCVlcd4fCGaR1nBBEp_F7OY7BD4Nx3f',

            }
        }).then(function (response) {
            expect(response.status).to.eq(200)
            expect(response.body.success).equal(true)

            expect(response.body).to.have.all.keys(
                'success', 'message', 'token'
            )
            expect(response.body.token[0]).to.have.all.keys(
                'id',
                'email',
                'email_verified_at',
                'gcmtoken',
                'created_at',
                'updated_at',
            )
        });
    });
});
