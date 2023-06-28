
import Router from "./Router";
import Base from "./Base";
import SessionUtil from "./SessionUtil";
import { LoadMask } from "../store/Loader";

const Utils = {
    log: function (msg) {
        if(location.hostname === 'localhost') {
            console.log(msg);
        }
    },

    reload: function () {
        // location.hash = "";
        // location.search = "";
        location.href =  location.origin + '/';
        // location.reload();
    },

    mask: function (show) {
        LoadMask.set(!!show);
    },

    alert: function (message, title, callback, options) {
        Base.alert(message, title, callback, options);
    },

    confirm: function (message, title, callback, confirmText, cancelText) {
        Base.confirm(message, title, callback, confirmText, cancelText);
    },

    showImagePreview: function (file, title, url) {
        Base.imagePreview(title, file, url);
    },

    hideAlert: function () {
        Base.hideMsg();
    },

    redirectTo: function (hash, params, replaceState) {
        Router.redirectTo(hash, params, replaceState)
    },

    redirectBack: function () {
        Router.back();
    },

    redirectNext: function () {
        Router.forward();
    },

    isValidUser: function () {
        return SessionUtil.get('info') !== null;
    },

    serializeParams: function (obj) {
        let str = '?' + Object.keys(obj).reduce(function (a, k) {
            a.push(k + '=' + encodeURIComponent(obj[k]));
            return a;
        }, []).join('&');
        return str;
    },

    getParamsAsObject: function (query) {
        query = query.substring(query.indexOf('?') + 1);

        var re = /([^&=]+)=?([^&]*)/g;
        var decodeRE = /\+/g;

        var decode = function (str) {
            return decodeURIComponent(str.replace(decodeRE, " "));
        };

        var params = {}, e;

        while (e = re.exec(query)) {
            var k = decode(e[1]), v = decode(e[2]);
            if (k.substring(k.length - 2) === '[]') {
                k = k.substring(0, k.length - 2);
                (params[k] || (params[k] = [])).push(v);
            }
            else params[k] = v;
        }

        var assign = function (obj, keyPath, value) {
            var lastKeyIndex = keyPath.length - 1;
            for (var i = 0; i < lastKeyIndex; ++i) {
                var key = keyPath[i];
                if (!(key in obj))
                    obj[key] = {}
                obj = obj[key];
            }
            obj[keyPath[lastKeyIndex]] = value;
        }

        for (var prop in params) {
            var structure = prop.split('[');
            if (structure.length > 1) {
                var levels = [];
                structure.forEach(function (item, i) {
                    var key = item.replace(/[?[\]\\ ]/g, '');
                    levels.push(key);
                });
                assign(params, levels, params[prop]);
                delete (params[prop]);
            }
        }
        return params;
    },

    getHash: function () {
        return location.hash.replace('#', '').split('?')[0];
    },

    isObject: (toString.call(null) === '[object Object]')
        ? function (value) {
            // check ownerDocument here as well to exclude DOM nodes
            return value != null && toString.call(value) === '[object Object]' &&
                value.ownerDocument === undefined;
        }
        : function (value) {
            return toString.call(value) === '[object Object]';
        },

    isEmptyObject: function (object) {
        var key;

        for (key in object) {
            if (object.hasOwnProperty(key)) {
                return false;
            }
        }

        return true;
    },

    /**
     * Returns true if the passed value is empty, false otherwise. The value is deemed to be
     * empty if it is either:
     *
     * - `null`
     * - `undefined`
     * - a zero-length array
     * - a zero-length string (Unless the `allowEmptyString` parameter is set to `true`)
     *
     * @param {Object} value The value to test.
     * @param {Boolean} [allowEmptyString=false] `true` to allow empty strings.
     * @return {Boolean} 
     */
    isEmpty: function (value, allowEmptyString) {
        return (value == null) || (!allowEmptyString ? value === '' : false) ||
            (Utils.isArray(value) && value.length === 0);
    },

    /**
     * Returns `true` if the passed value is a JavaScript Array, `false` otherwise.
     *
     * @param {Object} target The target to test.
     * @return {Boolean} 
     * @method
     */
    isArray: ('isArray' in Array)
        ? Array.isArray
        : function (value) {
            return toString.call(value) === '[object Array]';
        },

    /**
     * Returns `true `if the passed value is a string.
     * @param {Object} value The value to test.
     * @return {Boolean}
     */
    isString: function (value) {
        return typeof value === 'string';
    },

    /**
     * Returns `true` if the passed value is a JavaScript Date object, `false` otherwise.
     * @param {Object} obj The object to test.
     * @return {Boolean} 
     */
    isDate: function (obj) {
        return toString.call(obj) === '[object Date]';
    },

    //TODO - remove this once API start reading it from body
    encodeForUrl: function (obj) {
        let str = '?';

        for (let key in obj) {

            str += key + '=' + obj[key] + '&'
        }

        return str;
    },

    getImageSize: function (size, asObject) {
        let units = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];


        let l = 0,
            n = parseInt(size, 10) || 0;

        while (n >= 1024 && ++l) {
            n = n / 1024;
        }


        size = n.toFixed(n < 10 && l > 0 ? 1 : 0);
        let unit = units[l];

        if (asObject) {
            return { size, unit }
        }

        return (size + ' ' + unit);
    },

    calculateAvailableSpace: function (el, adjust) {
        if (!el) {
            return '0px';
        }

        adjust = adjust || 0;


        return ((innerHeight - el.getBoundingClientRect().top) - adjust) + 'px';
    },

    isValidEmail: function (email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(String(email));
    },

    removeAt: function (array, index, count) {
        var len = array.length;

        if (index >= 0 && index < len) {
            count = count || 1;
            count = Math.min(count, len - index);
            this.eraseNative(array, index, count);
        }

        return array;
    },

    eraseNative: function (array, index, removeCount) {
        array.splice(index, removeCount);

        return array;
    },
};


export default Utils;