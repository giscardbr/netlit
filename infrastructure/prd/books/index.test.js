'use strict';

const headers = {
    'host': [{'key': 'Host', 'value': 'd1ylab63jtfdx9.cloudfront.net'}],
    'user-agent': [{'key': 'User-Agent', 'value': 'PostmanRuntime/7.6.0'}],
    'cache-control': [{'key': 'cache-control', 'value': 'no-cache'}],
    'postman-token': [{'key': 'Postman-Token', 'value': '6e181543-60a4-4dd1-be5d-33645e489a09'}],
    'authorization': [{'key': 'Authorization', 'value': 'Bearer c8441bec-68bd-4bea-bdf3-13c80da86880'}],
    'accept': [{'key': 'Accept', 'value': '*/*'}],
    'accept-encoding': [{'key': 'accept-encoding', 'value': 'gzip, deflate'}]
};

function test() {
    return require('./index').handler({
        Records: [{
            cf: {
                request: {
                    uri: '/A_velhinha_e_o_porco_v5/assets/19/page.xhtml?authorization=f53d4fee-b150-4c9a-b758-858c484c949c',
                    headers
                }
            }
        }]
    }, null, callback);
}

function callback(a, b) {
    return {}
}

test()
    .then(value => console.log(value))
    .catch(reason => console.log(reason));