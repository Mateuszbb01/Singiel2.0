describe('HTTP Example', function () {
    it('GET', function () {

        cy.request({
            method: 'GET',
            url: 'https://httpbin.org/get',
        }).then(function (response) {
            expect(response.body).have.property('url');
        });
    });


    it('POST', function () {

        cy.request({
            method: 'POST',
            url: 'https://httpbin.org/post',
            body: {
                'name': 'Whoami',
                'age': 23
            },
            headers: {
                'content-type': 'application/json'
            }
        }).then(function (response) {
            expect(response.body).have.property('json');
            expect(response.body.json).to.deep.equal({
                "name": "Whoami",
                "age": 23

            });
            cy.wait(2000)

        });
    });

});