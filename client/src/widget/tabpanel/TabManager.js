

import { TabHeader, TabBody } from "../../Requires";

const key = Symbol();

const tabContext = {
    components: {},
    activeTab: 0, // tab index to activate
    createHeader: function (items, tabId) {
        let headerData = [];
        items = items || [];
        for (let i = 0; i < items.length; i++) {
            headerData.push({
                text: items[i].title || "Untitled",
                parentId: tabId,
                id: tabId + "_tab_" + i,
                active: false,
                view: items[i].view,
                html: items[i].html || ''
            });
        }

        this.components[tabId].header = this.create(TabHeader, {
            items: headerData,
            parentId: tabId
        });
        this.components[tabId].tabs = {};
    },

    createBody: function (tabId) {
        this.components[tabId].body = this.create(TabBody, {
            //parentId: tabId
        });
    },

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

    initialize: function (items, tabId, callback, viewData) {
        this.components[tabId] = {
            callback: callback,
            viewData: viewData
        };
        this.createHeader(items, tabId);
        this.createBody(tabId);

        return this.components[tabId];
    },

    activateTab: function (data) {
        let panel = this.components[data.parentId],
            tab = panel.tabs[data.id] || Object.values(panel.tabs)[0],
            prevActiveTab = panel.prevActiveTab;

        if (!tab) {
            return;
        }

        panel.bodyRef.view = tab.view;
        panel.bodyRef.html = tab.html;

        if (prevActiveTab) {
            prevActiveTab.active = false;
        }

        panel.prevActiveTab = tab;
        tab.active = true;

        if (panel.bodyRef.viewRef) {
            panel.bodyRef.viewRef.callback = panel.callback;
            panel.bodyRef.viewRef.viewData = panel.viewData;
        }
    }
};


export { key, tabContext }