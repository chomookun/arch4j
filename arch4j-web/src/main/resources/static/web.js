/**
 * alert
 * @param message
 * @param option_
 * @returns {*}
 */
const _alert = function(message, option_) {
    message = `<img class="icon font-size--x-large" src="/static/image/icon-dialog-alert.svg" alt="alert"/><br/>${message}<br/>`;
    return new duice.AlertDialog(message)
        .onOpening(option_?.onOpening)
        .onOpened(option_?.onOpened)
        .onClosing(option_?.onClosing)
        .onClosed(option_?.onClosed)
        .open();
}

/**
 * confirm
 * @param message
 * @param option_
 * @returns {*}
 */
const _confirm = function(message, option_) {
    message = `<img class="icon font-size--x-large" src="/static/image/icon-dialog-confirm.svg" alt="confirm"/><br/>${message}<br/>`;
    return new duice.ConfirmDialog(message)
        .onOpening(option_?.onOpening)
        .onOpened(option_?.onOpened)
        .onClosing(option_?.onClosing)
        .onClosed(option_?.onClosed)
        .open();
}

/**
 * prompt
 * @param message
 * @param type
 * @param option_
 * @returns {*}
 */
const _prompt = function(message, type, option_) {
    message = `<img class="icon font-size--x-large" src="/static/image/icon-dialog-prompt.svg" alt="prompt"/><br/>${message}<br/>`;
    return new duice.PromptDialog(message, type)
        .onOpening(option_?.onOpening)
        .onOpened(option_?.onOpened)
        .onClosing(option_?.onClosing)
        .onClosed(option_?.onClosed)
        .open();
}

/**
 * Gets cookie value
 * @param name
 */
const _getCookie = function(name) {
    let value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

/**
 * Sets cookie value
 * @param name
 * @param value
 * @param day
 */
const _setCookie = function(name, value, day) {
    let date = new Date();
    date.setTime(date.getTime() + day * 60 * 60 * 24 * 1000);
    document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
}

/**
 * Deletes cookie
 * @param name
 */
const _deleteCookie = function(name) {
    _setCookie(name, '', -1);
}

/**
 * start progress
 * @private
 */
const _startProgress = function() {
    if(window['NProgress'] && window.self === window.top) {
        NProgress.start();
    }
}

/**
 * stop progress
 * @private
 */
const _stopProgress = function() {
    if(window['NProgress'] && window.self === window.top) {
        NProgress.done();
    }
}

/**
 * _fetch
 * @param url
 * @param option
 */
const _fetch = function(url, option) {
    if(!option){
        option = {};
    }
    if(!option.headers){
        option.headers = {};
    }
    // csrf token
    ['XSRF-TOKEN', 'CSRF-TOKEN'].forEach(tokenName => {
        let tokenValue = _getCookie(tokenName);
        if(tokenValue){
            option.headers[`X-${tokenName}`] = tokenValue;
        }
    });
    option.headers['Cache-Control'] = 'no-cache, no-store, must-revalidate';
    option.headers['Pragma'] = 'no-cache';
    option.headers['Expires'] = '0';
    // request interceptors
    _fetch.interceptors.request.forEach(interceptor => {
        interceptor.call(null, option);
    });
    _startProgress();
    return globalThis.fetch(url, option)
        .then(async function(response) {
            console.debug(response);
            // calls interceptor
            _fetch.interceptors.response.forEach(interceptor => {
                interceptor.call(response);
            });
            // checks response
            if (response.ok) {
                return response;
            } else {
                const clonedResponse = response.clone();
                const contentType = response.headers.get('content-type');
                let errorMessage;
                if (contentType && contentType.includes('application/json')) {
                    let responseJson = await clonedResponse.json();
                    // redirect
                    if (responseJson.redirect) {
                        console.log(`redirect to ${responseJson.redirectUri}`);
                        location.href = responseJson.redirectUri;
                        return;
                    }
                    errorMessage = responseJson.message;
                } else {
                    errorMessage = await clonedResponse.text();
                }
                // alert error message
                if (!option['_suppressAlert']) {
                    await _alert(errorMessage).then();
                }
                return response;
            }
        })
        .catch(async (error)=> {
            // alert error message
            if (!option['_suppressAlert']) {
                await _alert(error.message).then();
            }
            console.error(error);
            // calls error interceptor
            _fetch.interceptors.error.forEach(interceptor => {
                interceptor.call(error);
            });
            throw error;
        })
        .finally(() => {
            _stopProgress();
        });
}

/**
 * _fetch interceptors
 * @type {{request: *, response: *, error: *}}
 */
_fetch.interceptors = {
    request: [],
    response: [],
    error: []
};

/**
 * Parsed total count from Content-Range header
 * @Param {Response} response
 */
const _parseTotalCount = function(response){
    let totalCount = -1;
    let contentRange = response.headers.get("Content-Range");
    try {
        totalCount = contentRange.split(' ')[1].split('/')[1];
        totalCount = parseInt(totalCount);
        if(isNaN(totalCount)){
            return -1;
        }
        return totalCount;
    }catch(e){
        console.error(e);
        return -1;
    }
}

/**
 * _isDarkMode
 * @returns {boolean}
 * @private
 */
const _isDarkMode = function() {
    // checks cookie
    if(_getCookie('dark-mode') === 'true') {
        return true;
    }
    if(_getCookie('dark-mode') === 'false') {
        return false;
    }
    // checks media query
    if (window.matchMedia) {
        if(window.matchMedia('(prefers-color-scheme: dark)')?.matches){
            return true;
        }
    }
    // returns false
    return false;
}

/**
 * setDarkMode
 * @param enable
 */
const _setDarkMode = function(enable) {
    if(enable){
        document.documentElement.classList.add('dark-mode');
        _setCookie('dark-mode', 'true', 356);
    }else{
        document.documentElement.classList.remove('dark-mode');
        _setCookie('dark-mode', 'false', 356);
    }
}

/**
 * toggle dark mode
 */
const _toggleDarkMode = function() {
    if(_isDarkMode()) {
        _setCookie('dark-mode', 'false', 356);
    }else{
        _setCookie('dark-mode', 'true', 356);
    }
    window.location.reload();
}

// set color scheme
_setDarkMode(_isDarkMode());

/**
 * return random color code
 * @private
 */
const _getRandomColor = function() {
    if (_isDarkMode()) {
        return randomColor({
            luminosity: 'dark',
            format: 'rgb'
        });
    } else {
        return randomColor({
            luminosity: 'bright',
            format: 'rgb'
        });
    }
}

/**
 * Formats bytes size
 * @returns {string}
 * @private
 */
const _formatBytes = function(bytes, scale = 2) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    const value = parseFloat((bytes / Math.pow(k, i)).toFixed(scale));
    return `${value} ${sizes[i]}`;
}

/**
 * Opens link
 * @param linkUrl
 * @param linkTarget
 */
const _openLink = function(linkUrl, linkTarget) {
    if(linkUrl) {
        if(linkTarget) {
            let result = window.open(linkUrl, linkTarget);
            // ios is security block
            if (!result) {
                window.location.href = linkUrl;
            }
        }else{
            window.location.href = linkUrl;
        }
    }
}

/**
 * checks is empty
 * @param value
 * @returns {boolean}
 * @private
 */
const _isEmpty = function(value) {
    return !(value && value.trim().length > 0);
}

/**
 * Checks generic ID (alphabet + number + -,_)
 * @param value
 */
const _isIdFormat = function(value) {
    if(value){
        let pattern = /^[a-zA-Z0-9\-\_\.]{1,}$/;
        return pattern.test(value);
    }
    return false;
}

/**
 * Checks generic password (At least 1 alphabet, 1 number, 1 special char)
 * @param value
 */
const _isPasswordFormat = function(value) {
    if(value){
        let pattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;
        return pattern.test(value);
    }
    return false;
}

/**
 * Checks valid email address pattern
 * @param value
 */
const _isEmailFormat = function(value) {
    if(value){
        let pattern = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        return pattern.test(value);
    }
    return false;
}

/**
 * Checks if value is URL address format
 * @param value
 */
const _isUrlFormat = function(value) {
    if(value){
        let pattern = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
        return pattern.test(value);
    }
    return false;
}

/**
 * change language
 * @param language
 * @private
 */
const _changeLanguage = function(language) {
    if(language) {
        // reload with language parameter
        let url = new URL(document.location.href);
        url.searchParams.delete('_language');
        url.searchParams.append('_language', language);
        document.location.href = url.href;
    }
}

/**
 * webSocketClient
 */
class WebSocketClient {
    constructor(url) {
        this.stomp = Stomp.client(url);
        // this.stomp = Stomp.over(new SockJS(url));
        this.subscriptions = [];
        this.stomp.reconnect = true;
    }

    /**
     * subscribe
     * @param subscription
     * @returns {*}
     */
    subscribe(subscription) {
        this.subscriptions.push(subscription);
        if (this.stomp.connected) {
            subscription.subscribe = this.stomp.subscribe(subscription.destination, subscription.listener);
        }
        return subscription;
    }

    /**
     * unsubscribe
     * @param subscription
     */
    unsubscribe(subscription) {
        if (subscription?.subscribe) {
            subscription.subscribe.unsubscribe();
        }
    }

    /**
     * connect
     */
    connect() {
        const _this = this;
        this.stomp.connect({}, function() {
            _this.subscriptions.forEach(subscription => {
                subscription.subscribe = _this.stomp.subscribe(subscription.destination, subscription.listener);
            });
        }, function(error) {
            console.error(error);
            setTimeout(() => _this.connect(), 5000);
        });
    }

    /**
     * send message
     * @param message
     */
    send(message) {
        this.stomp.send(message.destination, message.headers, message.body);
    }

    /**
     * disconnect
     */
    disconnect() {
        this.stomp.disconnect(() => console.log('websocket disconnected.'));
    }

}

/**
 * load from url search params
 */
const _loadUrlSearchParams = function(object, _properties) {
    const url = new URL(location.href);
    const properties = _properties || Object.keys(object);
    properties.forEach(property => {
        const value = url.searchParams.get(property);
        if (value != null) {
            if (Array.isArray(object[property])) {
                url.searchParams.getAll(property).forEach(v => object[property].push(v));
            } else {
                switch (typeof object[property]) {
                    case 'boolean':
                        object[property] = value === 'true';
                        break;
                    case 'number':
                        object[property] = Number(value);
                        break;
                    default:
                        object[property] = value;
                        break;
                }
            }
        }
    });
}

/**
 * push to url search params
 */
const _pushUrlSearchParams = function(object, _properties) {
    const url = new URL(location.href);
    const properties = _properties || Object.keys(object);
    properties.forEach(property => {
        const value = object[property];
        if (value != null) {
            if (Array.isArray(value)) {
                url.searchParams.delete(property);
                value.forEach(v => url.searchParams.append(property, v));
            } else {
                url.searchParams.set(property, value);
            }
        }
    });
    history.pushState({ time: new Date().getTime() }, null, url);
};

/**
 * deletes url search params
 * @param _properties
 * @private
 */
const _deleteUrlSearchParams = function(_properties) {
    const url = new URL(location.href);
    const properties = _properties || [];
    properties.forEach(property => {
        url.searchParams.delete(property);
    });
    history.pushState({ time: new Date().getTime() }, null, url);
};

/**
 * call function
 * @param fn
 * @param context
 * @param args
 * @returns {Promise<never>|*|Promise<Awaited<unknown>>}
 * @private
 */
const _callFunction = function(fn, context, ...args) {
    try {
        const result = fn.call(context, ...args);
        return (result && typeof result.then === 'function')
            ? result
            : Promise.resolve(result);
    } catch (e) {
        return Promise.reject(e);
    }
}

/**
 * initialize
 * @type {(function(*): void)|*}
 * @private
 */
const _initialize = (function() {
    const initializedMap = new WeakMap();
    return function(callback) {
        function handler(event) {
            if (initializedMap.has(callback)) return;
            initializedMap.set(callback, true);
            callback(event);
        }
        document.addEventListener('DOMContentLoaded', handler);
        window.addEventListener('popstate', handler);
        window.addEventListener('pageshow', handler);
    };
})();

/**
 * Checks if the browser is mobile
 * @returns {boolean}
 * @private
 */
const _isMobileBrowser = function() {
    return /Mobi|Android|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}
