import axios from "axios";
import SessionUtil from "./SessionUtil";
import Utils from "./Utils";

const Request = {
    defaultHeader: {
        Accept: '*/*',
        'Content-Type': 'application/json'
    },

    getHeaders: function (params) {
        let headers = JSON.parse(JSON.stringify(this.defaultHeader));

        if (params && params.headers) {
            for (let key in params.headers) {
                headers[key] = params.headers[key];
            }

            delete params.headers;
        }

        let userInfo = SessionUtil.get('info', true) || {};

        headers.Authorization = userInfo.userId;

        return headers;
    },

    get: function (url, params, success, failure, scope) {
        let headers = this.getHeaders(params);

        if (!Utils.isValidUser() && !(params && params.ignoreCheck)) {
            Utils.reload();
        }

        if (params) {
            delete params.ignoreCheck;
        }

        axios.get(url, {
            headers: headers,

            // `params` are the URL parameters to be sent with the request
            // Must be a plain object or a URLSearchParams object
            // NOTE: params that are null or undefined are not rendered in the URL.
            params: params,
            timeout: 120000
        })
            .then(function (response) {
                let data = response.data;

                if (scope && success) {
                    success.call(scope, data, response);
                }
            })
            .catch(function (err) {
                Utils.log(err);
                if (scope && failure) {
                    failure.call(scope, err);
                }
            });
    },

    post: function (url, jsonData, success, failure, scope) {
        let headers = this.getHeaders(jsonData),
            params;

        // Apart from login/reset pass/register if any post req. made redirect to login
        if (!Utils.isValidUser() && !(jsonData && jsonData.ignoreCheck)) {
            Utils.reload();
        }

        if (jsonData) {
            delete jsonData.ignoreCheck;
        }

        // no need to send Authorization if its empty
        if (!headers.Authorization) {
            delete headers.Authorization;
        }


        axios.post(url, jsonData, {
            headers: headers,
            params: params,
            timeout: 120000
        })
            .then(function (response) {
                let data = response.data;

                if (scope && success) {
                    success.call(scope, data, response);
                }
            })
            .catch(function (err) {
                if (scope && failure) {
                    failure.call(scope, err);
                }
            });
    },

    put: function (url, jsonData, success, failure, scope) {
        let headers = this.getHeaders(jsonData),
            params;

        if (!Utils.isValidUser()) {
            Utils.reload();
        }

        // no need to send Authorization if its empty
        if (!headers.Authorization) {
            delete headers.Authorization;
        }


        axios.put(url, jsonData, {
            headers: headers,
            params: params,
            timeout: 120000
        })
            .then(function (response) {
                let data = response.data;

                if (scope && success) {
                    success.call(scope, data, response);
                }
            })
            .catch(function (err) {
                if (scope && failure) {
                    failure.call(scope, err);
                }
            });
    },

    delete: function (url, jsonData, success, failure, scope) {
        let headers = this.getHeaders(jsonData),
            params;

        if (!Utils.isValidUser()) {
            Utils.reload();
        }

        // no need to send Authorization if its empty
        if (!headers.Authorization) {
            delete headers.Authorization;
        }


        axios.delete(url, {
            headers: headers,
            params: params,
            timeout: 120000
        })
            .then(function (response) {
                let data = response.data;

                if (scope && success) {
                    success.call(scope, data, response);
                }
            })
            .catch(function (err) {
                if (scope && failure) {
                    failure.call(scope, err);
                }
            });
    },

    uploadFile: function (url, jsonData, success, failure, scope) {
        let headers = this.getHeaders({
            headers: {
                "Content-Type": "multipart/form-data",
            }
        });

        if (!Utils.isValidUser()) {
            Utils.reload();
        }

        axios.post(url, jsonData, {
            headers: headers,
            timeout: 120000
        })
            .then(function (response) {
                let data = response.data;

                if (scope && success) {
                    success.call(scope, data, response);
                }
            })
            .catch(function (err) {
                if (scope && failure) {
                    failure.call(scope, err);
                }
            });
    }
};

export default Request;