import Utils from "./Utils";
const SessionUtil = {
    get: function(key, parse) {
        let val = localStorage.getItem(key);

        if (parse) {
            return JSON.parse(val);
        }

        return val;
    },

    set: function(key, value) {
        if (Utils.isObject(value)) {
            value = JSON.stringify(value);
        }

        localStorage.setItem(key, value);
    },

    remove: function(key) {
        localStorage.removeItem(key);
    },

    removeAll: function() {
        localStorage.clear();
    }
};

export default SessionUtil;