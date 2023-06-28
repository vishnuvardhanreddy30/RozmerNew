import { Routes } from "../store/Routes";
import Utils from "./Utils";
import SessionUtil from "./SessionUtil";

const Router = {
    currentToken: null,
    previousToken: null,
    currentParams: null,

    getTokenFromHash: function () {
        let hash = window.location.hash;

        if (Utils.isEmpty(hash)) {
            return '';
        }

        if(hash.indexOf('#home?pid=') !== -1) {
            SessionUtil.set('activatedHash', hash);
        }

        hash = hash.substring(1);

        return hash.split('?')[0];
    },

    manageHistoryChange: function (e) {
        if (Router.ignoreChange) {
            return;
        }
        Router.previousToken = Router.currentToken;
        Router.currentToken = Router.getTokenFromHash();
        Router.currentParams = Utils.getParamsAsObject(window.location.hash);
        Routes.set(Router.getTokenData());
    },

    forward: function () {
        history.forward();
    },

    back: function () {
        history.back();
    },

    updateHistory: function (hash, params, replaceState) {
        let paramStr = '';

        // if (!Utils.isValidUser() && (hash !== 'register' || hash === 'reset')) {
        //     return Utils.reload();
        // }

        if (Utils.isObject(params) && !Utils.isEmptyObject(params)) {
            paramStr = Utils.serializeParams(params);
        }

        if (replaceState) {
            Router.ignoreChange = true;
            Router.back();

            Router.ignoreChange = false;
        }
        history[replaceState ? 'replaceState' : 'pushState']('', '', '#' + hash + paramStr);
    },

    getTokenData: function () {
        return {
            token: Router.currentToken,
            previousToken: Router.previousToken,
            params: Router.currentParams
        }
    },

    /**
     * 
     */
    activateHistory: function () {
        window.onpopstate = Router.manageHistoryChange;

        // fire Route change - on initial load and update router data
        let hashStr = window.location.hash;

        if (!Utils.isEmpty(hashStr)) {
            Router.currentToken = Router.getTokenFromHash();
            Router.currentParams = Utils.getParamsAsObject(window.location.hash);
            Routes.set(Router.getTokenData());
        }
    },

    /**
     * 
     * @param {String} hash 
     * @param {Object} params 
     */
    redirectTo: function (hash, params, replaceState) {
        if (Utils.isEmpty(hash)) {
            return Utils.log('[ Router ] - redirectTo method first param is mandatory');
        }

        Router.previousToken = Router.currentToken;
        Router.currentToken = hash;
        Router.currentParams = params;

        Router.updateHistory(hash, params, replaceState);

        Routes.set(Router.getTokenData());
    },
};


export default Router;