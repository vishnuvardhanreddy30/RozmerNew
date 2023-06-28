import { BreadcrumbButton } from "../../Requires";
import Base from "../../util/Base";

const key = Symbol();

const BreadcrumbManager = {
    separator: '>',
    components: {},

    init: function (id, data, callback) {
        this.components[id] = {};
        this.components[id].buttonsRefs = {};
        this.components[id].callback = callback;
    },

    // redraw
    set: function (data, id) {
        let items = [],
            item, i, ln;
        data = [].concat(data);
        ln = data.length;

        for (i = 0; i < ln; i++) {
            item = Base.create(BreadcrumbButton, {
                id: 'breadcrumb-btn-' + i,
                text: data[i].text || '',
                separator: (i !== ln - 1) ? this.separator : null,
                parentId: id,
                data: data[i]
            });

            items.push(item);
        }

        return items;
    },

    // update node
    update: function () {

    },

    remove: function () {

    },
    refresh: function () {

    },
    onClick: function (data) {
        let cmp = this.components[data.parentId],
            btnRef = cmp.buttonsRefs,
            destroy = false;

        for (let key in btnRef) {
            if (destroy) {
                btnRef[key].$destroy();
            }

            if (key === data.id) {
                destroy = true;
                btnRef[key].separator = null;
            }
        }

        cmp.callback.call(
            cmp.callback, data
        );
    }
}


export { key, BreadcrumbManager };