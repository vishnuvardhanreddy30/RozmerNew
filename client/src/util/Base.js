// @ts-nocheck
import Alert from "../widget/msg/Alert.svelte";
import Confirm from "../widget/msg/Confirm.svelte";
import ImagePreview from "../widget/container/ImagePreview.svelte";
import TextField from "../widget/fields/TextField.svelte";
import RatingConfirm from "../view/feed/RatingConfirm.svelte";
import { notifications } from "../widget/msg/notification";

const Base = {
    msg: null,

    create: function (Component, props = {}) {
        return function ModalComponent(options) {
            return new Component({
                ...options,
                props: {
                    ...props,
                    ...options.props,
                },
            });
        };
    },


    alert: function (msg, title, callback, options) {
        if (Base.msg) {
            Base.msg.$destroy();
        }

        let prop = {
            message: msg,
            title: title,
            callback: callback
        };


        Base.msg = new Alert({
            target: document.getElementById('app'),

            props: { ...prop, ...options }
        });
    },

    confirm: function (msg, title, callback, confirmText, cancelText) {
        if (Base.msg) {
            Base.msg.$destroy();
        }

        Base.msg = new Confirm({
            target: document.getElementById('app'),

            props: {
                message: msg,
                title: title,
                callback: callback,
                confirmText: confirmText,
                cancelText: cancelText
            }
        });
    },

    imagePreview: function (title, file, url) {
        if (Base.msg) {
            Base.msg.$destroy();
        }

        let prop = {
            title,
            file,
            url
        };

        Base.msg = new ImagePreview({
            target: document.getElementById('app'),

            props: { ...prop }
        });
    },

    hideMsg: function () {
        if (Base.msg) {
            Base.msg.$destroy();
        }

        Base.msg = null;
    },

    /**
     * 
     * @param {String} type 
     *  info
     *  warning
     *  success
     *  danger
     *  default
     * @param {String} message 
     * @param {*} duration 
     */
    toast: function (type = 'info', message = '', duration = 2000) {
        notifications[type](message, duration)
    },

    createReplyField: function (el, props) {
        if (Base.replyField) {
            Base.replyField.$destroy();
        }

        if (!el) {
            return;
        }

        Base.replyField = new TextField({
            target: el,

            props: props
        });
    },

    createRatingPopup: function (props) {
        if (Base.msg) {
            Base.msg.$destroy();
        }

        Base.msg = new RatingConfirm({
            target: document.getElementById('app'),

            props: props
        });
    }
};

export default Base;