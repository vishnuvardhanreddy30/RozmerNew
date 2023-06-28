const Boot = {
    browserPrefixes: {
        ie: 'MSIE ',
        edge: 'Edge/',
        firefox: 'Firefox/',
        chrome: 'Chrome/',
        safari: 'Version/',
        opera: 'OPR/',
        dolfin: 'Dolfin/',
        webosbrowser: 'wOSBrowser/',
        chromeMobile: 'CrMo/',
        chromeiOS: 'CriOS/',
        silk: 'Silk/'
    },

    browserNames: {
        ie: 'IE',
        firefox: 'Firefox',
        safari: 'Safari',
        chrome: 'Chrome',
        opera: 'Opera',
        dolfin: 'Dolfin',
        edge: 'Edge',
        webosbrowser: 'webOSBrowser',
        chromeMobile: 'ChromeMobile',
        chromeiOS: 'ChromeiOS',
        silk: 'Silk',
        other: 'Other'
    },
    osNames: {
        ios: 'iOS',
        android: 'Android',
        windowsPhone: 'WindowsPhone',
        webos: 'webOS',
        blackberry: 'BlackBerry',
        rimTablet: 'RIMTablet',
        mac: 'MacOS',
        win: 'Windows',
        tizen: 'Tizen',
        linux: 'Linux',
        bada: 'Bada',
        chromeOS: 'ChromeOS',
        other: 'Other'
    },
    osPrefixes: {
        tizen: '(Tizen )',
        ios: 'i(?:Pad|Phone|Pod)(?:.*)CPU(?: iPhone)? OS ',
        android: '(Android |HTC_|Silk/)',
        // Some HTC devices ship with an OSX userAgent by default,
        // so we need to add a direct check for HTC_
        windowsPhone: 'Windows Phone ',
        blackberry: '(?:BlackBerry|BB)(?:.*)Version/',
        rimTablet: 'RIM Tablet OS ',
        webos: '(?:webOS|hpwOS)/',
        bada: 'Bada/',
        chromeOS: 'CrOS '
    },
    getBrowserName: function() {
        let userAgent = navigator.userAgent,
            browserMatch = userAgent.match(new RegExp('((?:' +
                Object.values(this.browserPrefixes).join(')|(?:') + '))([\\w\\._]+)')),
            browserName = 'Other';

        if (browserMatch) {
            browserName = this.browserNames[Boot.getKey(this.browserPrefixes, browserMatch[1])];
        }

        return browserName
    },

    getKey: function(object, value) {
        for (var property in object) {
            if (object.hasOwnProperty(property) && object[property] === value) {
                return property;
            }
        }
        return null;
    },

    getOSName: function() {
        let userAgent = navigator.userAgent;

        var me = this,
            names = me.osNames,
            prefixes = me.osPrefixes,
            name,
            version = '',
            is = me.is,
            i, prefix, match, item, match1;
        //browserScope = browserScope || Ext.browser;
        for (i in prefixes) {
            if (prefixes.hasOwnProperty(i)) {
                prefix = prefixes[i];
                match = userAgent.match(new RegExp('(?:' + prefix + ')([^\\s;]+)'));
                if (match) {
                    name = names[i];
                    match1 = match[1];
                    break;
                }
            }
        }
        if (!name) {
            name = names[(userAgent.toLowerCase().match(/mac|win|linux/) || [
                'other'
            ])[0]];
        }

        return name;
    },

    getDeviceType: function() {
        let ua = navigator.userAgent;

        if (/(tablet|ipad|playbook|silk)|(android(?!.*mobi))/i.test(ua)) {
            return "Tablet";
        }

        if (
            /Mobile|iP(hone|od)|Android|BlackBerry|IEMobile|Kindle|Silk-Accelerated|(hpw|web)OS|Opera M(obi|ini)/.test(
                ua
            )
        ) {
            return "Mobile";
        }
        return "Desktop";
    },

    // channelJson: function() {
    //     return {
    //         device: this.getDeviceType(),
    //         os: this.getOSName(),
    //         browser: this.getBrowserName()
    //     }
    // },

    isDesktop: function() {
        return this.getDeviceType() === 'Desktop'
    },

    isTablet: function() {
        return this.getDeviceType() === 'Tablet'
    },

    isMobile: function() {
        return this.getDeviceType() === 'Mobile'
    },

    isBigScreen: function() {
        return this.getDeviceType() !== 'Mobile'
    }
}

export default Boot;
