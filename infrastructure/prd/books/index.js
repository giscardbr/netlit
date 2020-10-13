'use strict';

const https = require('https');
const querystring = require('querystring');

const CORS_HEADERS = {
    'access-control-allow-credentials': [{
        key: 'Access-Control-Allow-Credentials',
        value: 'true'
    }],
    'access-control-allow-origin': [{
        key: 'Access-Control-Allow-Origin',
        value: '*'
    }],
    'access-control-allow-methods': [{
        key: 'Access-Control-Allow-Methods',
        value: '*'
    }],
    'access-control-allow-headers': [{
        key: 'Access-Control-Allow-Headers',
        value: '*'
    }],
};

exports.handler = async ({Records: records}, context, callback) => {
    const req = new Request(records);

    // console.log('Request: ', JSON.stringify(req.request));
    console.log('Authorization: ', req.authorization);
    console.log('URI: ', `${req.method} ${req.uri}`);

    if (req.method === 'OPTIONS') {
        return callback(null, new OptionsResponse());
    } else if (req.uri.endsWith('.xhtml')) {
        const {status, data} = await authorize(req.authorization);
        if (status !== 200) return callback(null, new UnauthorizedResponse());
        console.log(JSON.stringify(data));
    }
    console.log('Authorized!');
    req.request.headers = req.request.headers ? Object.assign(req.request.headers, CORS_HEADERS) : CORS_HEADERS;
    return callback(null, req.request);
};

class Request {
    constructor(records) {
        if (records && records.length > 0 && records[0].cf) {
            const {request} = records[0].cf;
            const {headers, uri, method, querystring: params} = request;
            this.request = request;
            this.headers = headers;
            this.params = querystring.parse(params);
            this.uri = uri;
            this.method = method;
        } else {
            this.request = null;
            this.headers = null;
            this.params = null;
            this.uri = null;
            this.method = null;
        }
    }

    get authorization() {
        const {authorization} = this.params;
        if (authorization) {
            console.log('Getting authorization from query params');
            return `Bearer ${authorization}`;
        } else if (this.headers && this.headers.authorization && this.headers.authorization.length > 0) {
            console.log('Getting authorization from header');
            return this.headers.authorization[0].value;
        } else {
            return null;
        }
    }
}

class OptionsResponse {
    constructor() {
        this.headers = CORS_HEADERS;
        this.status = '200';
        this.statusDescription = 'OK';
        console.log('OPTIONS request OK!')
    }
}

class UnauthorizedResponse {
    constructor() {
        this.headers = {
            'content-type': [{key: 'Content-Type', value: 'application/json; charset=UTF-8'}],
            'www-authenticate': [{
                key: 'WWW-Authenticate',
                value: 'Bearer realm="oauth2-resource", error="unauthorized", error_description="Full authentication is required to access this resource"'
            }],
        };
        this.body = JSON.stringify({
            'error': 'unauthorized',
            'error_description': 'Full authentication is required to access this resource'
        });
        this.status = '401';
        this.statusDescription = 'Unauthorized';
        console.log('Unauthorized!')
    }
}

function authorize(authorization) {
    return new Promise(function (resolve, reject) {
        const options = {
            method: 'GET',
            hostname: 'api.netlit.com.br',
            path: '/v1/oauth/user',
            headers: {
                'Authorization': authorization,
            }
        };
        const req = https.request(options, (res) => {
            const chunks = [];
            console.log(`statusCode: ${res.statusCode}`);

            res.on('data', (chunk) => {
                chunks.push(chunk);
            });

            res.on('end', () => {
                const body = Buffer.concat(chunks);
                const json = JSON.parse(body.toString());
                resolve({status: res.statusCode, data: json});
            });
        });

        req.on('error', (error) => {
            reject(error)
        });

        req.end();
    });
}