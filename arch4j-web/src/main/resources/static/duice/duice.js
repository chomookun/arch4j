/*!
 * duice - v0.3.6
 * git: https://gitbub.com/chomookun/duice
 * website: https://duice.chomookun.com
 * Released under the LGPL(GNU Lesser General Public License version 3) License
 */
var duice = (function (exports) {
    'use strict';

    /**
     * Configuration
     */
    class Configuration {
        /**
         * Sets namespace
         * @param value namespace value
         */
        static setNamespace(value) {
            this.namespace = value;
        }
        /**
         * Gets namespace
         */
        static getNamespace() {
            return this.namespace;
        }
        /**
         * Sets debug enabled
         * @param value
         */
        static setDebugEnabled(value) {
            sessionStorage.setItem(`${this.namespace}.traceEnabled`, JSON.stringify(value));
        }
        /**
         * Checks if debug is enabled
         */
        static isDebugEnabled() {
            const value = sessionStorage.getItem(`${this.namespace}.debugEnabled`);
            return value ? JSON.parse(value) : false;
        }
    }
    Configuration.namespace = 'duice';

    var __awaiter$4 = (window && window.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    /**
     * Checks value is Array
     */
    function isArray(value) {
        return Array.isArray(value);
    }
    /**
     * Checks value is Object
     */
    function isObject(value) {
        return value != null && typeof value === 'object' && !Array.isArray(value);
    }
    /**
     * Checks value is primitive
     */
    function isPrimitive(value) {
        return value !== Object(value);
    }
    /**
     * Checks object is proxy
     * @param object
     */
    function isProxy(object) {
        if (object == null) {
            return false;
        }
        return globalThis.Object.getOwnPropertyDescriptor(object, '_proxy_target_') != null;
    }
    /**
     * Sets proxy target
     * @param proxy
     * @param target
     */
    function setProxyTarget(proxy, target) {
        globalThis.Object.defineProperty(proxy, '_proxy_target_', {
            value: target,
            writable: true
        });
    }
    /**
     * Gets proxy target
     * @param proxy
     */
    function getProxyTarget(proxy) {
        return globalThis.Object.getOwnPropertyDescriptor(proxy, '_proxy_target_').value;
    }
    /**
     * Sets proxy handler
     * @param proxy proxy
     * @param proxyHandler proxy handler
     */
    function setProxyHandler(proxy, proxyHandler) {
        globalThis.Object.defineProperty(proxy, '_proxy_handler_', {
            value: proxyHandler,
            writable: true
        });
    }
    /**
     * Gets proxy handler
     * @param proxy proxy
     */
    function getProxyHandler(proxy) {
        return globalThis.Object.getOwnPropertyDescriptor(proxy, '_proxy_handler_').value;
    }
    /**
     * Gets element query selector
     */
    function getElementQuerySelector() {
        let namespace = Configuration.getNamespace();
        return `*[data-${namespace}-bind]:not([data-${namespace}-id])`;
    }
    /**
     * Marks element as initialized
     * @param container container
     */
    function markInitialized(container) {
        container.querySelectorAll(getElementQuerySelector()).forEach(element => {
            setElementAttribute(element, 'id', '_');
        });
    }
    /**
     * Finds variable in current scope
     * @param context current context
     * @param name variable name
     */
    function findVariable(context, name) {
        // find in context
        try {
            let object = new Function(`return this.${name};`).call(context);
            if (object) {
                return object;
            }
        }
        catch (ignore) { }
        // find in global
        try {
            let object = new Function(`return ${name};`).call(null);
            if (object) {
                return object;
            }
        }
        catch (ignore) { }
        // return default
        return undefined;
    }
    /**
     * Runs code
     * @param code
     * @param htmlElement
     * @param context
     */
    function runCode(code, htmlElement, context) {
        return __awaiter$4(this, void 0, void 0, function* () {
            try {
                let args = [];
                let values = [];
                for (let property in context) {
                    args.push(property);
                    values.push(context[property]);
                }
                return yield Function(...args, code).call(htmlElement, ...values);
            }
            catch (e) {
                console.error(code, e);
                throw e;
            }
        });
    }
    /**
     * Runs if code
     * @param htmlElement html element
     * @param context current context
     */
    function runIfCode(htmlElement, context) {
        return __awaiter$4(this, void 0, void 0, function* () {
            let ifClause = getElementAttribute(htmlElement, 'if');
            if (ifClause) {
                let result = yield runCode(ifClause, htmlElement, context);
                if (!result) {
                    htmlElement.hidden = true;
                }
                else {
                    htmlElement.hidden = false;
                }
                return result;
            }
            return true;
        });
    }
    /**
     * Runs execute code
     * @param htmlElement html element
     * @param context current context
     */
    function runExecuteCode(htmlElement, context) {
        return __awaiter$4(this, void 0, void 0, function* () {
            let script = getElementAttribute(htmlElement, 'execute');
            if (script) {
                return yield runCode(script, htmlElement, context);
            }
            return null;
        });
    }
    /**
     * Checks if element has attribute
     * @param htmlElement html element
     * @param name attribute name
     */
    function hasElementAttribute(htmlElement, name) {
        let namespace = Configuration.getNamespace();
        return htmlElement.hasAttribute(`data-${namespace}-${name}`);
    }
    /**
     * Gets element attribute
     * @param htmlElement html element
     * @param name attribute name
     */
    function getElementAttribute(htmlElement, name) {
        let namespace = Configuration.getNamespace();
        return htmlElement.getAttribute(`data-${namespace}-${name}`);
    }
    /**
     * Sets element attribute
     * @param htmlElement html element
     * @param name attribute name
     * @param value attribute value
     */
    function setElementAttribute(htmlElement, name, value) {
        let namespace = Configuration.getNamespace();
        htmlElement.setAttribute(`data-${namespace}-${name}`, value);
    }
    /**
     * Prints debug message
     */
    function debug(...args) {
        if (Configuration.isDebugEnabled()) {
            console.trace(args);
        }
    }
    /**
     * Asserts condition
     * @param condition condition
     * @param message assertion message
     */
    function assert(condition, message) {
        console.assert(condition, message);
        if (!condition) {
            throw new Error(message || 'Assertion Failed');
        }
    }

    /**
     * Element factory
     */
    class ElementFactory {
    }

    /**
     * Observable
     */
    class Observable {
        constructor() {
            this.observers = [];
            this.notifyEnabled = true;
        }
        /**
         * Adds observer
         * @param observer observer
         */
        addObserver(observer) {
            this.observers.push(observer);
        }
        /**
         * Removes observer
         * @param observer observer
         */
        removeObserver(observer) {
            for (let i = 0, size = this.observers.length; i < size; i++) {
                if (this.observers[i] === observer) {
                    this.observers.splice(i, 1);
                    return;
                }
            }
        }
        /**
         * Suspends notify
         */
        suspendNotify() {
            this.notifyEnabled = false;
        }
        /**
         * Resumes notify
         */
        resumeNotify() {
            this.notifyEnabled = true;
        }
        /**
         * Notifies to observers
         * @param event event (optional)
         */
        notifyObservers(event) {
            if (this.notifyEnabled) {
                this.observers.forEach(observer => {
                    observer.update(this, event);
                });
            }
        }
    }

    /**
     * Element
     */
    class Element extends Observable {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         * @protected
         */
        constructor(htmlElement, bindData, context) {
            super();
            this.htmlElement = htmlElement;
            this.bindData = bindData;
            this.context = context;
            setElementAttribute(this.htmlElement, 'id', this.generateId());
            // bind data
            let proxyHandler = getProxyHandler(bindData);
            assert(proxyHandler, 'ProxyHandler is not found');
            this.addObserver(proxyHandler);
            proxyHandler.addObserver(this);
        }
        /**
         * Generate id
         * @private
         */
        generateId() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        }
        /**
         * Gets html element
         */
        getHtmlElement() {
            return this.htmlElement;
        }
        /**
         * Gets context
         */
        getContext() {
            return this.context;
        }
        /**
         * Gets bind data
         */
        getBindData() {
            return this.bindData;
        }
    }

    var __awaiter$3 = (window && window.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    /**
     * Event Dispatcher
     */
    class EventDispatcher {
        constructor() {
            this.eventListeners = new Map();
        }
        /**
         * Sets parent
         * @param parent parent
         */
        setParent(parent) {
            this.parent = parent;
        }
        /**
         * Gets parent
         */
        getParent() {
            return this.parent;
        }
        /**
         * Adds event listener
         * @param eventType event type
         * @param eventListener event listener
         */
        addEventListener(eventType, eventListener) {
            let listeners = this.eventListeners.get(eventType);
            if (!listeners) {
                listeners = [];
                this.eventListeners.set(eventType, listeners);
            }
            listeners.push(eventListener);
        }
        /**
         * Removes event listener
         * @param eventType event type
         * @param eventListener event listener
         */
        removeEventListener(eventType, eventListener) {
            let listeners = this.eventListeners.get(eventType);
            if (listeners) {
                let index = listeners.indexOf(eventListener);
                if (index >= 0) {
                    listeners.splice(index, 1);
                }
            }
        }
        /**
         * Clears event listeners
         * @param eventType event type
         */
        clearEventListeners(eventType) {
            this.eventListeners.delete(eventType);
        }
        /**
         * Dispatches event listeners
         * @param event event
         */
        dispatchEventListeners(event) {
            return __awaiter$3(this, void 0, void 0, function* () {
                let listeners = this.eventListeners.get(event.constructor);
                let results = [];
                if (listeners) {
                    for (let listener of listeners) {
                        results.push(yield listener.call(this, event));
                    }
                }
                // calls parent
                if (this.parent) {
                    let parentResults = yield this.parent.dispatchEventListeners(event);
                    results = results.concat(parentResults);
                }
                // returns results
                return results;
            });
        }
    }

    var __awaiter$2 = (window && window.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    /**
     * Proxy Handler
     */
    class ProxyHandler extends Observable {
        /**
         * Constructor
         * @protected
         */
        constructor(target) {
            super();
            this.readonlyProperties = new Map();
            this.disabledProperties = new Map();
            this.eventDispatcher = new EventDispatcher();
            this.eventEnabled = true;
            this.target = target;
        }
        /**
         * Sets parent
         * @param parent parent
         */
        setParent(parent) {
            this.parent = parent;
            this.addObserver(parent);
            parent.addObserver(this);
            this.eventDispatcher.setParent(parent.eventDispatcher);
        }
        /**
         * Gets parent
         */
        getParent() {
            return this.parent;
        }
        /**
         * Sets target
         * @param target
         */
        setTarget(target) {
            this.target = target;
        }
        /**
         * Gets target
         */
        getTarget() {
            return this.target;
        }
        /**
         * Sets readonly all
         * @param readonly readonly all
         */
        setReadonlyAll(readonly) {
            this.readonlyAll = readonly;
            this.readonlyProperties.forEach((value, key) => {
                this.readonlyProperties.set(key, readonly);
            });
            this.notifyObservers();
        }
        /**
         * Returns readonly all
         */
        isReadonlyAll() {
            let readonlyAll = false;
            if (this.parent) {
                readonlyAll = (this.parent.isReadonlyAll() === true);
            }
            if (this.readonlyAll === true) {
                readonlyAll = true;
            }
            if (this.readonlyAll === false) {
                readonlyAll = false;
            }
            return readonlyAll;
        }
        /**
         * Sets readonly
         * @param property property
         * @param readonly readonly or not
         */
        setReadonly(property, readonly) {
            this.readonlyProperties.set(property, readonly);
            this.notifyObservers();
        }
        /**
         * Returns whether property is readonly
         * @param property property
         */
        isReadonly(property) {
            let readonly = false;
            readonly = (this.isReadonlyAll() === true);
            if (this.readonlyProperties.has(property)) {
                readonly = (this.readonlyProperties.get(property) === true);
            }
            // returns
            return readonly;
        }
        /**
         * Sets disabled all
         * @param disabledAll
         */
        setDisabledAll(disabledAll) {
            this.disabledAll = disabledAll;
            this.disabledProperties.forEach((value, key) => {
                this.disabledProperties.set(key, disabledAll);
            });
            this.notifyObservers();
        }
        /**
         * Returns disabled all
         */
        isDisabledAll() {
            let disabledAll = false;
            if (this.parent) {
                disabledAll = (this.parent.isDisabledAll() === true);
            }
            if (this.disabledAll === true) {
                disabledAll = true;
            }
            if (this.disabledAll === false) {
                disabledAll = false;
            }
            return disabledAll;
        }
        /**
         * Sets disabled
         * @param property property
         * @param disabled
         */
        setDisabled(property, disabled) {
            this.disabledProperties.set(property, disabled);
            this.notifyObservers();
        }
        /**
         * Returns whether property is disabled
         * @param property
         */
        isDisabled(property) {
            let disabled = false;
            disabled = (this.isDisabledAll() === true);
            // check property is disabled
            if (this.disabledProperties.has(property)) {
                disabled = (this.disabledProperties.get(property) === true);
            }
            // returns
            return disabled;
        }
        /**
         * Adds event listener
         * @param eventType event type
         * @param eventListener event listener
         */
        addEventListener(eventType, eventListener) {
            this.eventDispatcher.addEventListener(eventType, eventListener);
        }
        /**
         * Removes event listener
         * @param eventType event type
         * @param eventListener event listener
         */
        removeEventListener(eventType, eventListener) {
            this.eventDispatcher.removeEventListener(eventType, eventListener);
        }
        /**
         * Clears event listeners
         * @param eventType event type
         */
        clearEventListeners(eventType) {
            this.eventDispatcher.clearEventListeners(eventType);
        }
        /**
         * Suspends event
         */
        suspendEvent() {
            this.eventEnabled = false;
        }
        /**
         * Resumes event listener
         */
        resumeEvent() {
            this.eventEnabled = true;
        }
        /**
         * Calls event listeners
         * @param event
         */
        dispatchEventListeners(event) {
            return __awaiter$2(this, void 0, void 0, function* () {
                if (!this.eventEnabled) {
                    return null;
                }
                return this.eventDispatcher.dispatchEventListeners(event).then(results => {
                    if (results != null && results.length > 0) {
                        return !results.some(result => result === false);
                    }
                    else {
                        return null;
                    }
                });
            });
        }
    }

    /**
     * String Format
     */
    class StringFormat {
        /**
         * Constructor
         * @param pattern pattern
         */
        constructor(pattern) {
            this.pattern = pattern;
        }
        /**
         * Implements format
         * @param value origin value
         */
        format(value) {
            if (!value) {
                return value;
            }
            let encodedValue = '';
            let patternChars = this.pattern.split('');
            let valueChars = value.split('');
            let valueCharsPosition = 0;
            for (let i = 0, size = patternChars.length; i < size; i++) {
                let patternChar = patternChars[i];
                if (patternChar === '#') {
                    encodedValue += valueChars[valueCharsPosition++] || '';
                }
                else {
                    encodedValue += patternChar;
                }
                if (valueCharsPosition >= valueChars.length) {
                    break;
                }
            }
            return encodedValue;
        }
        /**
         * Implements parse
         * @param value formatted value
         */
        parse(value) {
            if (!value) {
                return value;
            }
            let decodedValue = '';
            let patternChars = this.pattern.split('');
            let valueChars = value.split('');
            let valueCharsPosition = 0;
            for (let i = 0, size = patternChars.length; i < size; i++) {
                let patternChar = patternChars[i];
                if (patternChar === '#') {
                    decodedValue += valueChars[valueCharsPosition++] || '';
                }
                else {
                    valueCharsPosition++;
                }
            }
            return decodedValue;
        }
    }

    /**
     * Number Format
     */
    class NumberFormat {
        /**
         * Constructor
         * @param scale scale
         */
        constructor(scale) {
            this.scale = 0;
            this.scale = scale;
        }
        /**
         * Implements format
         * @param number number
         */
        format(number) {
            if (isNaN(Number(number))) {
                return '';
            }
            number = Number(number);
            let string;
            if (this.scale > 0) {
                string = String(number.toFixed(this.scale));
            }
            else {
                string = String(number);
            }
            let reg = /(^[+-]?\d+)(\d{3})/;
            while (reg.test(string)) {
                string = string.replace(reg, '$1' + ',' + '$2');
            }
            return string;
        }
        /**
         * Implements parse
         * @param string string
         */
        parse(string) {
            if (!string) {
                return null;
            }
            if (string.length === 1 && /[+-]/.test(string)) {
                string += '0';
            }
            string = string.replace(/,/gi, '');
            if (isNaN(Number(string))) {
                throw 'NaN';
            }
            let number = Number(string);
            if (this.scale > 0) {
                number = Number(number.toFixed(this.scale));
            }
            return number;
        }
    }

    /**
     * Date Format
     */
    class DateFormat {
        /**
         * Constructor
         * @param pattern pattern
         */
        constructor(pattern) {
            this.patternRex = /yyyy|yy|MM|dd|HH|hh|mm|ss/gi;
            this.pattern = pattern;
        }
        /**
         * Implements format
         * @param string origin value
         */
        format(string) {
            if (!string) {
                return '';
            }
            let date = new Date(string);
            string = this.pattern.replace(this.patternRex, function ($1) {
                switch ($1) {
                    case "yyyy":
                        return date.getFullYear();
                    case "yy":
                        return String(date.getFullYear() % 1000).padStart(2, '0');
                    case "MM":
                        return String(date.getMonth() + 1).padStart(2, '0');
                    case "dd":
                        return String(date.getDate()).padStart(2, '0');
                    case "HH":
                        return String(date.getHours()).padStart(2, '0');
                    case "hh":
                        return String(date.getHours() <= 12 ? date.getHours() : date.getHours() % 12).padStart(2, '0');
                    case "mm":
                        return String(date.getMinutes()).padStart(2, '0');
                    case "ss":
                        return String(date.getSeconds()).padStart(2, '0');
                    default:
                        return $1;
                }
            });
            return string;
        }
        /**
         * Implements parse
         * @param string formatted value
         */
        parse(string) {
            if (!string) {
                return null;
            }
            let date = new Date(0, 0, 0, 0, 0, 0);
            let match;
            while ((match = this.patternRex.exec(this.pattern)) != null) {
                let formatString = match[0];
                let formatIndex = match.index;
                let formatLength = formatString.length;
                let matchValue = string.substr(formatIndex, formatLength);
                matchValue = matchValue.padEnd(formatLength, '0');
                switch (formatString) {
                    case 'yyyy': {
                        let fullYear = parseInt(matchValue);
                        date.setFullYear(fullYear);
                        break;
                    }
                    case 'yy': {
                        let yyValue = parseInt(matchValue);
                        let yearPrefix = Math.floor(new Date().getFullYear() / 100);
                        let fullYear = yearPrefix * 100 + yyValue;
                        date.setFullYear(fullYear);
                        break;
                    }
                    case 'MM': {
                        let monthValue = parseInt(matchValue);
                        date.setMonth(monthValue - 1);
                        break;
                    }
                    case 'dd': {
                        let dateValue = parseInt(matchValue);
                        date.setDate(dateValue);
                        break;
                    }
                    case 'HH': {
                        let hoursValue = parseInt(matchValue);
                        date.setHours(hoursValue);
                        break;
                    }
                    case 'hh': {
                        let hoursValue = parseInt(matchValue);
                        date.setHours(hoursValue > 12 ? (hoursValue + 12) : hoursValue);
                        break;
                    }
                    case 'mm': {
                        let minutesValue = parseInt(matchValue);
                        date.setMinutes(minutesValue);
                        break;
                    }
                    case 'ss': {
                        let secondsValue = parseInt(matchValue);
                        date.setSeconds(secondsValue);
                        break;
                    }
                }
            }
            // timezone offset
            let tzo = new Date().getTimezoneOffset() * -1, dif = tzo >= 0 ? '+' : '-', pad = function (num) {
                return (num < 10 ? '0' : '') + num;
            };
            // return iso string
            return date.getFullYear() +
                '-' + pad(date.getMonth() + 1) +
                '-' + pad(date.getDate()) +
                'T' + pad(date.getHours()) +
                ':' + pad(date.getMinutes()) +
                ':' + pad(date.getSeconds()) +
                dif + pad(Math.floor(Math.abs(tzo) / 60)) +
                ':' + pad(Math.abs(tzo) % 60);
        }
    }

    /**
     * Format Factory
     */
    class FormatFactory {
        /**
         * Gets format instance
         * @param format format
         */
        static getFormat(format) {
            let name;
            let args;
            const match = format.match(/^([a-zA-Z_][a-zA-Z0-9_]*)\((.*)\)$/);
            if (match) {
                name = match[1];
                args = match[2].split(',').map(arg => arg.trim().replace(/^['"]|['"]$/g, ''));
            }
            else {
                throw new Error(`Invalid format: ${format}`);
            }
            switch (name) {
                case 'string':
                    return new StringFormat(args[0]);
                case 'number':
                    return new NumberFormat(parseInt(args[0]));
                case 'date':
                    return new DateFormat(args[0]);
                default:
                    throw new Error(`Unknown format: ${name}`);
            }
        }
    }

    /**
     * Event
     */
    class Event {
        /**
         * Constructor
         * @param element html element
         * @param data data
         */
        constructor(element, data) {
            this.element = element;
            this.data = data;
        }
        /**
         * Gets element
         */
        getElement() {
            return this.element;
        }
        /**
         * Gets data
         */
        getData() {
            return this.data;
        }
    }

    /**
     * Property Changed Event
     */
    class PropertyChangedEvent extends Event {
        /**
         * Constructor
         * @param element element
         * @param data data
         * @param property property
         * @param value value
         * @param index index (optional)
         */
        constructor(element, data, property, value, index) {
            super(element, data);
            this.property = property;
            this.value = value;
            this.index = index;
        }
        /**
         * Gets property name
         */
        getProperty() {
            return this.property;
        }
        /**
         * Gets property value
         */
        getValue() {
            return this.value;
        }
        /**
         * Gets index in array if object is in array
         */
        getIndex() {
            return this.index;
        }
    }

    /**
     * Object Element
     */
    class ObjectElement extends Element {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            // attributes
            this.property = getElementAttribute(htmlElement, 'property');
            let format = getElementAttribute(htmlElement, 'format');
            if (format) {
                this.format = FormatFactory.getFormat(format);
            }
        }
        /**
         * Gets property
         */
        getProperty() {
            return this.property;
        }
        /**
         * Gets format
         */
        getFormat() {
            return this.format;
        }
        /**
         * Overrides render
         */
        render() {
            // context
            let context = Object.assign({}, this.getContext());
            let bind = getElementAttribute(this.getHtmlElement(), 'bind');
            let bindSplit = bind.split('.');
            if (bindSplit.length > 1) {
                context[bindSplit[0]] = findVariable(context, bindSplit[0]);
            }
            else {
                context[bind] = this.getBindData();
            }
            // run if code
            runIfCode(this.htmlElement, context).then(result => {
                // checks result
                if (result === false) {
                    return;
                }
                let objectProxyHandler = getProxyHandler(this.getBindData());
                // set value
                if (this.property) {
                    let value = objectProxyHandler.getValue(this.property);
                    this.setValue(value);
                }
                // sets readonly
                let readonly = objectProxyHandler.isReadonly(this.property);
                this.setReadonly(readonly);
                // sets disabled
                let disabled = objectProxyHandler.isDisabled(this.property);
                this.setDisabled(disabled);
                // run execute code
                runExecuteCode(this.htmlElement, context).then();
            });
        }
        /**
         * Updates
         * @param observable observable
         * @param event event
         */
        update(observable, event) {
            debug('ObjectElement.update', observable, event);
            // ObjectHandler
            if (observable instanceof ObjectProxyHandler) {
                // property changed event
                if (event instanceof PropertyChangedEvent) {
                    this.render();
                    return;
                }
            }
            // default
            this.render();
        }
        /**
         * Sets property value
         * @param value property value
         */
        setValue(value) {
            if (value != null) {
                value = this.getFormat() ? this.getFormat().format(value) : value;
                this.htmlElement.innerText = value;
            }
            else {
                this.htmlElement.innerText = '';
            }
        }
        /**
         * Gets property value
         */
        getValue() {
            let value = this.htmlElement.innerText;
            if (value && value.trim().length > 0) {
                value = this.getFormat() ? this.getFormat().parse(value) : value;
            }
            else {
                value = null;
            }
            return value;
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            // no-op
        }
        /**
         * Sets disabled
         * @param disabled disabled or not
         */
        setDisabled(disabled) {
            // no-op
        }
        /**
         * Gets index
         */
        getIndex() {
            let index = getElementAttribute(this.htmlElement, 'index');
            if (index) {
                return Number(index);
            }
        }
        /**
         * Focus
         */
        focus() {
            // no-ops
            return false;
        }
    }

    /**
     * Property Change Event
     */
    class PropertyChangingEvent extends Event {
        /**
         * Constructor
         * @param element element
         * @param data data
         * @param property property
         * @param value value
         * @param index index (optional)
         */
        constructor(element, data, property, value, index) {
            super(element, data);
            this.property = property;
            this.value = value;
            this.index = index;
        }
        /**
         * Gets property name
         */
        getProperty() {
            return this.property;
        }
        /**
         * Gets property value
         */
        getValue() {
            return this.value;
        }
        /**
         * Gets index in array if object is in array
         */
        getIndex() {
            return this.index;
        }
    }

    /**
     * Item Selecting Event
     */
    class ItemSelectingEvent extends Event {
        /**
         * Constructor
         * @param element element
         * @param data data
         * @param index index
         */
        constructor(element, data, index) {
            super(element, data);
            this.index = index;
        }
        /**
         * Gets index
         */
        getIndex() {
            return this.index;
        }
    }

    /**
     * Item Moving Event
     */
    class ItemMovingEvent extends Event {
        /**
         * Constructor
         * @param element element
         * @param data data
         * @param fromIndex from index
         * @param toIndex to index
         */
        constructor(element, data, fromIndex, toIndex) {
            super(element, data);
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }
        /**
         * Gets from index
         */
        getFromIndex() {
            return this.fromIndex;
        }
        /**
         * Gets to index
         */
        getToIndex() {
            return this.toIndex;
        }
    }

    /**
     * Item Selected Event
     */
    class ItemSelectedEvent extends Event {
        /**
         * Constructor
         * @param element element
         * @param data data
         * @param index index (optional)
         */
        constructor(element, data, index) {
            super(element, data);
            this.index = index;
        }
        /**
         * Gets index
         */
        getIndex() {
            return this.index;
        }
    }

    class ItemMovedEvent extends Event {
        /**
         * Constructor
         * @param element element
         * @param data data
         * @param fromIndex from index
         * @param toIndex to index
         */
        constructor(element, data, fromIndex, toIndex) {
            super(element, data);
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }
        /**
         * Gets from index
         */
        getFromIndex() {
            return this.fromIndex;
        }
        /**
         * Gets to index
         */
        getToIndex() {
            return this.toIndex;
        }
    }

    /**
     * Array Proxy Handler
     */
    class ArrayProxyHandler extends ProxyHandler {
        /**
         * Constructor
         */
        constructor(array) {
            super(array);
        }
        /**
         * Get trap
         * @param target target
         * @param property property
         * @param receiver receiver
         */
        get(target, property, receiver) {
            let _this = this;
            const value = target[property];
            if (typeof value === 'function') {
                // push, unshift
                if (['push', 'unshift'].includes(property)) {
                    return function () {
                        let index;
                        if (property === 'push') {
                            index = receiver['length'];
                        }
                        else if (property === 'unshift') {
                            index = 0;
                        }
                        let rows = [];
                        for (let i in arguments) {
                            rows.push(arguments[i]);
                        }
                        _this.insertItem(target, index, ...rows);
                        return target.length;
                    };
                }
                // splice
                if (['splice'].includes(property)) {
                    return function () {
                        // parse arguments
                        let start = arguments[0];
                        let deleteCount = arguments[1];
                        let deleteRows = [];
                        for (let i = start; i < (start + deleteCount); i++) {
                            deleteRows.push(target[i]);
                        }
                        let insertRows = [];
                        for (let i = 2; i < arguments.length; i++) {
                            insertRows.push(arguments[i]);
                        }
                        // delete rows
                        if (deleteCount > 0) {
                            _this.deleteItem(target, start, deleteCount);
                        }
                        // insert rows
                        if (insertRows.length > 0) {
                            _this.insertItem(target, start, ...insertRows);
                        }
                        // returns deleted rows
                        return deleteRows;
                    };
                }
                // pop, shift
                if (['pop', 'shift'].includes(property)) {
                    return function () {
                        let index;
                        if (property === 'pop') {
                            index = receiver['length'] - 1;
                        }
                        else if (property === 'shift') {
                            index = 0;
                        }
                        let rows = [target[index]];
                        _this.deleteItem(target, index);
                        return rows;
                    };
                }
                // bind
                return value.bind(target);
            }
            // return
            return value;
        }
        /**
         * Set trap
         * @param target target
         * @param property property
         * @param value value
         */
        set(target, property, value) {
            Reflect.set(target, property, value);
            if (property === 'length') {
                this.notifyObservers(null);
            }
            return true;
        }
        /**
         * Updates
         * @param observable observable
         * @param event event
         */
        update(observable, event) {
            debug('ArrayProxyHandler.update', observable, event);
            // observable is array element
            if (observable instanceof ArrayElement) {
                // item selecting event
                if (event instanceof ItemSelectingEvent) {
                    this.dispatchEventListeners(event).then(result => {
                        if (result === false) {
                            return;
                        }
                        this.selectedItemIndex = event.getIndex();
                        // fires item selected event
                        let itemSelectedEvent = new ItemSelectedEvent(event.getElement(), event.getData(), this.selectedItemIndex);
                        this.notifyObservers(itemSelectedEvent);
                        this.dispatchEventListeners(itemSelectedEvent).then();
                    });
                }
                // item moving event
                if (event instanceof ItemMovingEvent) {
                    this.dispatchEventListeners(event).then(result => {
                        if (result === false) {
                            return;
                        }
                        let object = this.getTarget().splice(event.getFromIndex(), 1)[0];
                        this.getTarget().splice(event.getToIndex(), 0, object);
                        // fires item moved event
                        let itemMovedEvent = new ItemMovedEvent(event.getElement(), event.getData(), event.getFromIndex(), event.getToIndex());
                        this.notifyObservers(itemMovedEvent);
                        this.dispatchEventListeners(itemMovedEvent).then();
                    });
                }
            }
            // observable is object proxy handler
            if (observable instanceof ObjectProxyHandler) {
                if (event instanceof PropertyChangedEvent) {
                    this.notifyObservers(event);
                }
            }
        }
        /**
         * Inserts items
         * @param arrayProxy array proxy
         * @param index index
         * @param items items
         */
        insertItem(arrayProxy, index, ...items) {
            let arrayHandler = getProxyHandler(arrayProxy);
            let proxyTarget = getProxyTarget(arrayProxy);
            items.forEach((object, index) => {
                if (typeof object === 'object') {
                    let objectProxy = new ObjectProxy(object);
                    getProxyHandler(objectProxy);
                    items[index] = objectProxy;
                }
            });
            proxyTarget.splice(index, 0, ...items);
            arrayHandler.notifyObservers();
        }
        /**
         * Deletes items from array proxy
         * @param arrayProxy array proxy to delete
         * @param index index to delete
         * @param size size for delete
         */
        deleteItem(arrayProxy, index, size) {
            let arrayHandler = getProxyHandler(arrayProxy);
            let proxyTarget = getProxyTarget(arrayProxy);
            let sliceBegin = index;
            let sliceEnd = (size ? index + size : index + 1);
            proxyTarget.slice(sliceBegin, sliceEnd);
            let spliceStart = index;
            let spliceDeleteCount = (size ? size : 1);
            proxyTarget.splice(spliceStart, spliceDeleteCount);
            arrayHandler.notifyObservers();
        }
        /**
         * Selects item by index
         * @param index index
         */
        selectItem(index) {
            this.selectedItemIndex = index;
            // notify item selected event
            const itemSelectedEvent = new ItemSelectedEvent(null, this.getTarget(), index);
            this.notifyObservers(itemSelectedEvent);
        }
        /**
         * Gets selected item index
         */
        getSelectedItemIndex() {
            return this.selectedItemIndex;
        }
    }

    /**
     * Object Proxy Handler
     */
    class ObjectProxyHandler extends ProxyHandler {
        /**
         * Constructor
         */
        constructor(object) {
            super(object);
        }
        /**
         * Gets target property value
         * @param target target
         * @param property property
         * @param receiver receiver
         */
        get(target, property, receiver) {
            return Reflect.get(target, property, receiver);
        }
        /**
         * Sets target property value
         * @param target
         * @param property
         * @param value
         */
        set(target, property, value) {
            // change value
            Reflect.set(target, property, value);
            // notify
            this.notifyObservers();
            // returns
            return true;
        }
        /**
         * Updates
         * @param observable observable
         * @param event event
         */
        update(observable, event) {
            debug('ObjectProxyHandler.update', observable, event);
            // element
            if (observable instanceof ObjectElement) {
                if (event instanceof PropertyChangingEvent) {
                    this.dispatchEventListeners(event).then(result => {
                        // result is false
                        if (result === false) {
                            // rollback and return
                            observable.update(this, event);
                            return;
                        }
                        // updates property value
                        let property = observable.getProperty();
                        let value = observable.getValue();
                        this.setValue(property, value);
                        // property changed event
                        let propertyChangedEvent = new PropertyChangedEvent(event.getElement(), event.getData(), event.getProperty(), event.getValue(), event.getIndex());
                        this.notifyObservers(propertyChangedEvent);
                        this.dispatchEventListeners(propertyChangedEvent).then();
                    });
                }
            }
        }
        /**
         * Gets specified property value
         * @param property property
         */
        getValue(property) {
            property = property.replace(/\./g, '?.');
            return new Function(`return this.${property};`).call(this.getTarget());
        }
        /**
         * Sets specified property value
         * @param property property
         * @param value value
         */
        setValue(property, value) {
            new Function('value', `this.${property} = value;`).call(this.getTarget(), value);
        }
        /**
         * Sets focus on property element
         * @param property
         */
        focus(property) {
            this.observers.forEach(observer => {
                if (observer instanceof ObjectElement) {
                    if (observer.getProperty() === property) {
                        if (observer.focus()) {
                            return false;
                        }
                    }
                }
            });
        }
        /**
         * Overrides is readonly by property
         * @param property property
         */
        isReadonly(property) {
            let readonly = super.isReadonly(property);
            // parent is ArrayProxyHandler
            if (this.parent && this.parent instanceof ArrayProxyHandler) {
                if (this.parent.isReadonly(property) === true) {
                    readonly = true;
                }
                if (this.parent.isReadonly(property) === false) {
                    readonly = false;
                }
            }
            return readonly;
        }
        /**
         * Overrides is disabled by property
         * @param property property
         */
        isDisabled(property) {
            let disabled = super.isDisabled(property);
            // parent is ArrayProxyHandler
            if (this.parent && this.parent instanceof ArrayProxyHandler) {
                if (this.parent.isDisabled(property) === true) {
                    disabled = true;
                }
                if (this.parent.isDisabled(property) === false) {
                    disabled = false;
                }
            }
            return disabled;
        }
    }

    /**
     * Array Proxy
     */
    class ArrayProxy extends globalThis.Array {
        /**
         * Constructor
         * @param array
         */
        constructor(array) {
            super();
            // is already proxy
            if (array instanceof ArrayProxy) {
                return array;
            }
            // create proxy
            let arrayHandler = new ArrayProxyHandler(array);
            let arrayProxy = new Proxy(array, arrayHandler);
            setProxyTarget(arrayProxy, array);
            setProxyHandler(arrayProxy, arrayHandler);
            // assign
            let initialArray = JSON.parse(JSON.stringify(array));
            ArrayProxy.assign(arrayProxy, initialArray);
            // save
            ArrayProxy.save(arrayProxy);
            // returns
            return arrayProxy;
        }
        /**
         * Assigns array to array proxy
         * @param arrayProxy
         * @param array
         */
        static assign(arrayProxy, array) {
            let arrayProxyHandler = getProxyHandler(arrayProxy);
            try {
                // suspend
                arrayProxyHandler.suspendEvent();
                arrayProxyHandler.suspendNotify();
                // clears elements
                arrayProxy.length = 0;
                // creates elements
                for (let index = 0; index < array.length; index++) {
                    let value = array[index];
                    // source value is object
                    if (isObject(value)) {
                        let objectProxy = new ObjectProxy(value);
                        getProxyHandler(objectProxy).setParent(arrayProxyHandler);
                        arrayProxy[index] = objectProxy;
                        continue;
                    }
                    // default
                    arrayProxy[index] = value;
                }
            }
            finally {
                // resume
                arrayProxyHandler.resumeEvent();
                arrayProxyHandler.resumeNotify();
            }
            // notify observers
            arrayProxyHandler.notifyObservers();
        }
        /**
         * Clears array elements
         * @param arrayProxy
         */
        static clear(arrayProxy) {
            let arrayHandler = getProxyHandler(arrayProxy);
            try {
                // suspend
                arrayHandler.suspendEvent();
                arrayHandler.suspendNotify();
                // clear element
                arrayProxy.length = 0;
            }
            finally {
                // resume
                arrayHandler.resumeEvent();
                arrayHandler.resumeNotify();
            }
            // notify observers
            arrayHandler.notifyObservers();
        }
        /**
         * Save array proxy
         * @param arrayProxy
         */
        static save(arrayProxy) {
            let origin = JSON.stringify(arrayProxy);
            globalThis.Object.defineProperty(arrayProxy, '_origin_', {
                value: origin,
                writable: true
            });
        }
        /**
         * Reset array proxy
         * @param arrayProxy
         */
        static reset(arrayProxy) {
            let origin = JSON.parse(globalThis.Object.getOwnPropertyDescriptor(arrayProxy, '_origin_').value);
            this.assign(arrayProxy, origin);
        }
        /**
         * Checks if all properties are readonly
         * @param arrayProxy array proxy
         * @param readonly readonly
         */
        static setReadonlyAll(arrayProxy, readonly) {
            getProxyHandler(arrayProxy).setReadonlyAll(readonly);
        }
        /**
         * Checks if all properties are readonly
         * @param arrayProxy array proxy
         */
        static isReadonlyAll(arrayProxy) {
            return getProxyHandler(arrayProxy).isReadonlyAll();
        }
        /**
         * Sets readonly
         * @param arrayProxy array proxy
         * @param property property
         * @param readonly readonly
         */
        static setReadonly(arrayProxy, property, readonly) {
            getProxyHandler(arrayProxy).setReadonly(property, readonly);
        }
        /**
         * Checks if property is readonly
         * @param arrayProxy array proxy
         * @param property property
         */
        static isReadonly(arrayProxy, property) {
            return getProxyHandler(arrayProxy).isReadonly(property);
        }
        /**
         * Sets all properties to be disabled
         * @param arrayProxy array proxy
         * @param disable disabled
         */
        static setDisabledAll(arrayProxy, disable) {
            getProxyHandler(arrayProxy).setDisabledAll(disable);
        }
        /**
         * Checks if all properties are disabled
         * @param arrayProxy array proxy
         */
        static isDisabledAll(arrayProxy) {
            return getProxyHandler(arrayProxy).isDisabledAll();
        }
        /**
         * Sets disable
         * @param arrayProxy array proxy
         * @param property property
         * @param disable disable
         */
        static setDisabled(arrayProxy, property, disable) {
            getProxyHandler(arrayProxy).setDisabled(property, disable);
        }
        /**
         * Checks if property is disabled
         * @param arrayProxy array proxy
         * @param property property
         */
        static isDisabled(arrayProxy, property) {
            return getProxyHandler(arrayProxy).isDisabled(property);
        }
        /**
         * Inserts item
         * @param arrayProxy
         * @param index
         */
        static selectItem(arrayProxy, index) {
            return getProxyHandler(arrayProxy).selectItem(index);
        }
        /**
         * Gets selected item index
         * @param arrayProxy array proxy
         */
        static getSelectedItemIndex(arrayProxy) {
            return getProxyHandler(arrayProxy).getSelectedItemIndex();
        }
        /**
         * On item selecting
         * @param arrayProxy array proxy
         * @param eventListener event listener
         */
        static onItemSelecting(arrayProxy, eventListener) {
            let proxyHandler = getProxyHandler(arrayProxy);
            let eventType = ItemSelectingEvent;
            proxyHandler.clearEventListeners(eventType);
            proxyHandler.addEventListener(eventType, eventListener);
        }
        /**
         * On item selected
         * @param arrayProxy array proxy
         * @param eventListener event listener
         */
        static onItemSelected(arrayProxy, eventListener) {
            let proxyHandler = getProxyHandler(arrayProxy);
            let eventType = ItemSelectedEvent;
            proxyHandler.clearEventListeners(eventType);
            proxyHandler.addEventListener(eventType, eventListener);
        }
        /**
         * On item moving
         * @param arrayProxy array proxy
         * @param eventListener event listener
         */
        static onItemMoving(arrayProxy, eventListener) {
            let proxyHandler = getProxyHandler(arrayProxy);
            let eventType = ItemMovingEvent;
            proxyHandler.clearEventListeners(eventType);
            proxyHandler.addEventListener(eventType, eventListener);
        }
        /**
         * On item moved
         * @param arrayProxy array proxy
         * @param eventListener event listener
         */
        static onItemMoved(arrayProxy, eventListener) {
            let proxyHandler = getProxyHandler(arrayProxy);
            let eventType = ItemMovedEvent;
            proxyHandler.clearEventListeners(eventType);
            proxyHandler.addEventListener(eventType, eventListener);
        }
        /**
         * On property changing
         * @param objectProxy object proxy
         * @param eventListener event listener
         */
        static onPropertyChanging(objectProxy, eventListener) {
            let proxyHandler = getProxyHandler(objectProxy);
            let eventType = PropertyChangingEvent;
            proxyHandler.clearEventListeners(eventType);
            proxyHandler.addEventListener(eventType, eventListener);
        }
        /**
         * On property changed
         * @param objectProxy object proxy
         * @param eventListener event listener
         */
        static onPropertyChanged(objectProxy, eventListener) {
            let proxyHandler = getProxyHandler(objectProxy);
            let eventType = PropertyChangedEvent;
            proxyHandler.clearEventListeners(eventType);
            proxyHandler.addEventListener(eventType, eventListener);
        }
    }

    /**
     * Object Proxy
     */
    class ObjectProxy extends globalThis.Object {
        /**
         * Constructor
         * @param object
         */
        constructor(object) {
            super();
            // is already object proxy
            if (object instanceof ObjectProxy) {
                return object;
            }
            // object handler
            let objectProxyHandler = new ObjectProxyHandler(object);
            let objectProxy = new Proxy(object, objectProxyHandler);
            setProxyTarget(objectProxy, object);
            setProxyHandler(objectProxy, objectProxyHandler);
            // assign
            let initialObject = JSON.parse(JSON.stringify(object));
            ObjectProxy.assign(objectProxy, initialObject);
            // save
            ObjectProxy.save(objectProxy);
            // returns
            return objectProxy;
        }
        /**
         * Assign object to object proxy
         * @param objectProxy
         * @param object
         */
        static assign(objectProxy, object) {
            let objectProxyHandler = getProxyHandler(objectProxy);
            try {
                // suspend
                objectProxyHandler.suspendEvent();
                objectProxyHandler.suspendNotify();
                // loop object properties
                for (let name in object) {
                    let source = object[name];
                    let target = objectProxy[name];
                    // source is array
                    if (isArray(source)) {
                        if (isProxy(target)) {
                            ArrayProxy.assign(target, source);
                        }
                        else {
                            objectProxy[name] = new ArrayProxy(source);
                            getProxyHandler(objectProxy[name]).setParent(objectProxyHandler);
                        }
                        continue;
                    }
                    // source is object
                    if (isObject(source)) {
                        if (isProxy(target)) {
                            ObjectProxy.assign(target, source);
                        }
                        else {
                            objectProxy[name] = new ObjectProxy(source);
                            getProxyHandler(objectProxy[name]).setParent(objectProxyHandler);
                        }
                        continue;
                    }
                    // default
                    objectProxy[name] = source;
                }
            }
            finally {
                // resume
                objectProxyHandler.resumeEvent();
                objectProxyHandler.resumeNotify();
            }
            // notify observers
            objectProxyHandler.notifyObservers(null);
        }
        /**
         * Clear object properties
         * @param objectProxy
         */
        static clear(objectProxy) {
            let objectHandler = getProxyHandler(objectProxy);
            try {
                // suspend
                objectHandler.suspendEvent();
                objectHandler.suspendNotify();
                // clear properties
                for (let name in objectProxy) {
                    let value = objectProxy[name];
                    // source value is array
                    if (isArray(value)) {
                        if (isProxy(value)) {
                            ArrayProxy.clear(value);
                        }
                        else {
                            objectProxy[name] = [];
                        }
                        continue;
                    }
                    // source value is object
                    if (isObject(value)) {
                        if (isProxy(value)) {
                            ObjectProxy.clear(value);
                        }
                        else {
                            objectProxy[name] = null;
                        }
                        continue;
                    }
                    // default
                    objectProxy[name] = null;
                }
            }
            finally {
                // resume
                objectHandler.resumeEvent();
                objectHandler.resumeNotify();
            }
            // notify observers
            objectHandler.notifyObservers(null);
        }
        /**
         * Save object properties
         * @param objectProxy
         */
        static save(objectProxy) {
            let origin = JSON.stringify(objectProxy);
            globalThis.Object.defineProperty(objectProxy, '_origin_', {
                value: origin,
                writable: true
            });
        }
        /**
         * Reset object properties
         * @param objectProxy
         */
        static reset(objectProxy) {
            let origin = JSON.parse(globalThis.Object.getOwnPropertyDescriptor(objectProxy, '_origin_').value);
            this.assign(objectProxy, origin);
        }
        /**
         * Set all properties to be readonly
         * @param objectProxy
         * @param readonly
         */
        static setReadonlyAll(objectProxy, readonly) {
            getProxyHandler(objectProxy).setReadonlyAll(readonly);
        }
        /**
         * Get whether all properties are readonly
         * @param objectProxy
         */
        static isReadonlyAll(objectProxy) {
            return getProxyHandler(objectProxy).isReadonlyAll();
        }
        /**
         * Set property to be readonly
         * @param objectProxy
         * @param property
         * @param readonly
         */
        static setReadonly(objectProxy, property, readonly) {
            getProxyHandler(objectProxy).setReadonly(property, readonly);
        }
        /**
         * Get whether property is readonly
         * @param objectProxy
         * @param property
         */
        static isReadonly(objectProxy, property) {
            return getProxyHandler(objectProxy).isReadonly(property);
        }
        /**
         * Set all properties to be disabled
         * @param objectProxy
         * @param disable
         */
        static setDisabledAll(objectProxy, disable) {
            getProxyHandler(objectProxy).setDisabledAll(disable);
        }
        /**
         * Get whether all properties are disabled
         * @param objectProxy
         */
        static isDisabledAll(objectProxy) {
            return getProxyHandler(objectProxy).isDisabledAll();
        }
        /**
         * Set object to be disabled
         * @param objectProxy
         * @param property
         * @param disable
         */
        static setDisabled(objectProxy, property, disable) {
            getProxyHandler(objectProxy).setDisabled(property, disable);
        }
        /**
         * Get whether property is disabled
         * @param objectProxy
         * @param property
         */
        static isDisabled(objectProxy, property) {
            return getProxyHandler(objectProxy).isDisabled(property);
        }
        /**
         * Set property to be focused
         * @param objectProxy
         * @param property
         */
        static focus(objectProxy, property) {
            getProxyHandler(objectProxy).focus(property);
        }
        /**
         * On property changing
         * @param objectProxy object proxy
         * @param eventListener event listener
         */
        static onPropertyChanging(objectProxy, eventListener) {
            let proxyHandler = getProxyHandler(objectProxy);
            proxyHandler.clearEventListeners(PropertyChangingEvent);
            proxyHandler.addEventListener(PropertyChangingEvent, eventListener);
        }
        /**
         * On property changed
         * @param objectProxy object proxy
         * @param eventListener event listener
         */
        static onPropertyChanged(objectProxy, eventListener) {
            let proxyHandler = getProxyHandler(objectProxy);
            proxyHandler.clearEventListeners(PropertyChangedEvent);
            proxyHandler.addEventListener(PropertyChangedEvent, eventListener);
        }
    }

    /**
     * Array Element
     */
    class ArrayElement extends Element {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement.cloneNode(true), bindData, context);
            this.editable = false;
            this.slot = document.createElement('slot');
            this.itemHtmlElements = [];
            // attributes
            this.foreach = getElementAttribute(htmlElement, 'foreach');
            this.recursive = getElementAttribute(htmlElement, 'recursive');
            this.editable = (getElementAttribute(htmlElement, 'editable') === 'true');
            this.selectedItemClass = getElementAttribute(htmlElement, 'selected-item-class');
            // replace with slot for position
            htmlElement.replaceWith(this.slot);
            // mark initialized (not using after clone as templates)
            markInitialized(htmlElement);
        }
        /**
         * Renders
         */
        render() {
            var _a, _b;
            let arrayProxy = this.getBindData();
            // reset row elements
            this.itemHtmlElements.forEach(rowElement => {
                rowElement.parentNode.removeChild(rowElement);
            });
            this.itemHtmlElements.length = 0;
            // foreach
            if (this.foreach) {
                let foreachArgs = this.foreach.split(',');
                let itemName = foreachArgs[0].trim();
                let statusName = (_a = foreachArgs[1]) === null || _a === void 0 ? void 0 : _a.trim();
                // recursive loop
                if (this.recursive) {
                    let recursiveArgs = this.recursive.split(',');
                    let idName = recursiveArgs[0].trim();
                    let parentIdName = (_b = recursiveArgs[1]) === null || _b === void 0 ? void 0 : _b.trim();
                    const _this = this;
                    // visit function
                    let visit = function (array, parentId, depth) {
                        for (let index = 0; index < array.length; index++) {
                            const object = array[index];
                            if (object[parentIdName] === parentId) {
                                // context
                                let context = Object.assign({}, _this.getContext());
                                context[itemName] = object;
                                context[statusName] = new ObjectProxy({
                                    index: index,
                                    count: index + 1,
                                    size: arrayProxy.length,
                                    first: (index === 0),
                                    last: (arrayProxy.length == index + 1),
                                    depth: depth
                                });
                                // create row element
                                _this.createItemHtmlElement(index, context);
                                // visit child elements
                                let id = object[idName];
                                visit(array, id, depth + 1);
                            }
                        }
                    };
                    // start visit
                    visit(arrayProxy, null, 0);
                }
                // default foreach
                else {
                    // normal
                    for (let index = 0; index < arrayProxy.length; index++) {
                        // element data
                        let object = arrayProxy[index];
                        // context
                        let context = Object.assign({}, this.getContext());
                        context[itemName] = object;
                        context[statusName] = new ObjectProxy({
                            index: index,
                            count: index + 1,
                            size: arrayProxy.length,
                            first: (index === 0),
                            last: (arrayProxy.length == index + 1)
                        });
                        // create row element
                        this.createItemHtmlElement(index, context);
                    }
                }
            }
            // not foreach
            else {
                // initialize
                let itemHtmlElement = this.getHtmlElement().cloneNode(true);
                let context = Object.assign({}, this.getContext());
                Initializer.initialize(itemHtmlElement, this.getContext());
                this.itemHtmlElements.push(itemHtmlElement);
                // append to slot
                this.slot.parentNode.insertBefore(itemHtmlElement, this.slot);
                // check if
                runIfCode(itemHtmlElement, context).then(result => {
                    if (result === false) {
                        return;
                    }
                    runExecuteCode(itemHtmlElement, context).then();
                });
            }
        }
        /**
         * Creates item html element
         * @param index index
         * @param context context
         */
        createItemHtmlElement(index, context) {
            // clones row elements
            let itemHtmlElement = this.getHtmlElement().cloneNode(true);
            // adds embedded attribute
            setElementAttribute(itemHtmlElement, 'index', index.toString());
            // editable
            let _this = this;
            if (this.editable) {
                itemHtmlElement.setAttribute('draggable', 'true');
                itemHtmlElement.addEventListener('dragstart', e => {
                    let fromIndex = getElementAttribute(e.currentTarget, 'index');
                    e.dataTransfer.setData("text", fromIndex);
                });
                itemHtmlElement.addEventListener('dragover', e => {
                    e.preventDefault();
                    e.stopPropagation();
                });
                itemHtmlElement.addEventListener('drop', e => {
                    e.preventDefault();
                    e.stopPropagation();
                    // Notifies item move event
                    let element = this.getHtmlElement();
                    let data = getProxyTarget(this.getBindData());
                    let fromIndex = parseInt(e.dataTransfer.getData('text'));
                    let toIndex = parseInt(getElementAttribute(e.currentTarget, 'index'));
                    let itemMoveEvent = new ItemMovingEvent(element, data, fromIndex, toIndex);
                    _this.notifyObservers(itemMoveEvent);
                });
            }
            // initializes row element
            Initializer.initialize(itemHtmlElement, context, index);
            this.itemHtmlElements.push(itemHtmlElement);
            // insert into slot
            this.slot.parentNode.insertBefore(itemHtmlElement, this.slot);
            // trigger item selecting event
            itemHtmlElement.addEventListener('click', e => {
                e.stopPropagation();
                let element = this.getHtmlElement();
                let data = getProxyTarget(this.getBindData());
                let itemSelectingEvent = new ItemSelectingEvent(element, data, index);
                this.notifyObservers(itemSelectingEvent);
            });
            // check if code
            runIfCode(itemHtmlElement, context).then(result => {
                if (result === false) {
                    return;
                }
                runExecuteCode(itemHtmlElement, context).then();
            });
        }
        /**
         * Updates
         * @param observable observable
         * @param event event
         */
        update(observable, event) {
            var _a;
            debug('ArrayElement.update', observable, event);
            // if observable is array proxy handler
            if (observable instanceof ArrayProxyHandler) {
                // item selected event
                if (event instanceof ItemSelectedEvent) {
                    if (this.selectedItemClass) {
                        this.itemHtmlElements.forEach(el => el.classList.remove(this.selectedItemClass));
                        let index = event.getIndex();
                        if (index >= 0) {
                            this.itemHtmlElements.forEach(itemHtmlElement => {
                                if (itemHtmlElement.dataset.duiceIndex === event.getIndex().toString()) {
                                    itemHtmlElement.classList.add(this.selectedItemClass);
                                }
                            });
                        }
                    }
                    // no render
                    return;
                }
                // item moved event
                if (event instanceof ItemMovedEvent) {
                    this.render();
                    return;
                }
                // property change event
                if (event instanceof PropertyChangedEvent) {
                    // if recursive and parent is changed, render array element
                    if (this.recursive) {
                        let parentId = (_a = this.recursive.split(',')[1]) === null || _a === void 0 ? void 0 : _a.trim();
                        if (event.getProperty() === parentId) {
                            this.render();
                            return;
                        }
                    }
                    // default is no-op
                    else {
                        return;
                    }
                }
            }
            // default
            this.render();
        }
    }

    /**
     * Array Element Factory
     */
    class ArrayElementFactory extends ElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            return new ArrayElement(htmlElement, bindData, context);
        }
    }

    /**
     * Custom Element Factory
     */
    class CustomElementFactory extends ElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            return this.doCreateElement(htmlElement, bindData, context);
        }
    }

    /**
     * Object Element Factory
     */
    class ObjectElementFactory extends ElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            return new ObjectElement(htmlElement, bindData, context);
        }
    }

    /**
     * Element Registry
     */
    class ElementRegistry {
        /**
         * Registers element factory
         * @param tagName tag name
         * @param elementFactory element factory
         */
        static register(tagName, elementFactory) {
            if (elementFactory instanceof ArrayElementFactory) {
                this.arrayElementFactories.set(tagName, elementFactory);
            }
            else if (elementFactory instanceof ObjectElementFactory) {
                this.objectElementFactories.set(tagName, elementFactory);
            }
            else if (elementFactory instanceof CustomElementFactory) {
                this.customElementFactories.set(tagName, elementFactory);
            }
            // register custom html element
            if (tagName.includes('-')) {
                globalThis.customElements.define(tagName, class extends HTMLElement {
                });
            }
        }
        /**
         * Gets factory
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        static getFactory(htmlElement, bindData, context) {
            let tagName = htmlElement.tagName.toLowerCase();
            // custom element
            if (this.customElementFactories.has(tagName)) {
                return this.customElementFactories.get(tagName);
            }
            // array element
            if (Array.isArray(bindData)) {
                if (this.arrayElementFactories.has(tagName)) {
                    return this.arrayElementFactories.get(tagName);
                }
                return this.defaultArrayElementFactory;
            }
            // object element
            else {
                if (this.objectElementFactories.has(tagName)) {
                    return this.objectElementFactories.get(tagName);
                }
                return this.defaultObjectElementFactory;
            }
        }
    }
    ElementRegistry.defaultObjectElementFactory = new ObjectElementFactory();
    ElementRegistry.defaultArrayElementFactory = new ArrayElementFactory();
    ElementRegistry.objectElementFactories = new Map();
    ElementRegistry.arrayElementFactories = new Map();
    ElementRegistry.customElementFactories = new Map();

    /**
     * Initializer
     */
    class Initializer {
        /**
         * Initialize the container with the context
         * @param container container
         * @param context context
         * @param index index (optional)
         */
        static initialize(container, context, index) {
            // scan DOM tree
            container.querySelectorAll(getElementQuerySelector()).forEach(element => {
                var _a, _b;
                if (element instanceof HTMLElement) {
                    const htmlElement = element;
                    if (!hasElementAttribute(htmlElement, 'id')) {
                        try {
                            let bindName = getElementAttribute(htmlElement, 'bind');
                            let bindData = findVariable(context, bindName);
                            (_b = (_a = ElementRegistry.getFactory(htmlElement, bindData, context)) === null || _a === void 0 ? void 0 : _a.createElement(htmlElement, bindData, context)) === null || _b === void 0 ? void 0 : _b.render();
                            // index
                            if (index !== undefined) {
                                setElementAttribute(htmlElement, "index", index.toString());
                            }
                        }
                        catch (e) {
                            console.error(e, htmlElement.outerHTML, context);
                            // console.error(e, htmlElement, container, JSON.stringify(context));
                        }
                    }
                }
            });
        }
    }

    /**
     * Custom Element
     */
    class CustomElement extends Element {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context contxt
         * @protected
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
        }
        /**
         * Renders element
         */
        render() {
            runIfCode(this.getHtmlElement(), this.getContext()).then(result => {
                if (result == false) {
                    return;
                }
                this.doRender(this.getBindData());
                Initializer.initialize(this.getHtmlElement(), this.getContext());
                runExecuteCode(this.getHtmlElement(), this.getContext()).then();
            });
        }
        /**
         * Implements update method
         * @param observable observable
         * @param event event
         */
        update(observable, event) {
            debug('ObjectElement.update', observable, event);
            if (observable instanceof ProxyHandler) {
                this.doUpdate(observable.getTarget());
            }
        }
    }

    var __awaiter$1 = (window && window.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    /**
     * Img Element
     */
    class ImgElement extends ObjectElement {
        /**
         * Constructor
         * @param htmlElement img html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            this.editable = false;
            this.clearButtonImg = 'data:image/svg+xml;base64,' + window.btoa('<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path opacity="0.4" d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" fill="#292D32"></path> <path d="M13.0594 12.0001L15.3594 9.70011C15.6494 9.41011 15.6494 8.93011 15.3594 8.64011C15.0694 8.35011 14.5894 8.35011 14.2994 8.64011L11.9994 10.9401L9.69937 8.64011C9.40937 8.35011 8.92937 8.35011 8.63938 8.64011C8.34938 8.93011 8.34938 9.41011 8.63938 9.70011L10.9394 12.0001L8.63938 14.3001C8.34938 14.5901 8.34938 15.0701 8.63938 15.3601C8.78938 15.5101 8.97937 15.5801 9.16937 15.5801C9.35937 15.5801 9.54937 15.5101 9.69937 15.3601L11.9994 13.0601L14.2994 15.3601C14.4494 15.5101 14.6394 15.5801 14.8294 15.5801C15.0194 15.5801 15.2094 15.5101 15.3594 15.3601C15.6494 15.0701 15.6494 14.5901 15.3594 14.3001L13.0594 12.0001Z" fill="#292D32"></path> </g></svg>');
            this.readonly = false;
            this.disabled = false;
            this.originSrc = String(this.getHtmlElement().src);
            // editable
            this.editable = (getElementAttribute(this.getHtmlElement(), 'editable') === 'true');
            if (this.editable) {
                // add click event listener
                this.getHtmlElement().style.cursor = 'pointer';
                this.getHtmlElement().addEventListener('click', event => {
                    this.changeImage();
                });
                // create clear button
                this.clearButton = document.createElement('img');
                this.clearButton.src = this.clearButtonImg;
                this.clearButton.style.cursor = 'pointer';
                this.clearButton.style.width = '16px';
                this.clearButton.style.height = '16px';
                this.clearButton.addEventListener('mouseout', event => {
                    this.hideClearImageButton();
                }, true);
                this.clearButton.addEventListener('click', event => {
                    this.clearImage();
                });
                // mouse over
                this.getHtmlElement().addEventListener('mouseover', event => {
                    this.showClearImageButton();
                }, true);
                // mouse over
                this.getHtmlElement().addEventListener('mouseout', event => {
                    // related target is overlay button
                    if (event.relatedTarget === this.clearButton) {
                        return;
                    }
                    this.hideClearImageButton();
                }, true);
            }
            // size
            let size = getElementAttribute(this.getHtmlElement(), 'size');
            if (size) {
                let sizeArgs = size.split(',');
                this.width = parseInt(sizeArgs[0].trim());
                this.height = parseInt(sizeArgs[1].trim());
            }
        }
        /**
         * Shows clear image button
         */
        showClearImageButton() {
            this.getHtmlElement().parentNode.insertBefore(this.clearButton, this.getHtmlElement().nextSibling);
            this.clearButton.style.position = 'absolute';
            this.clearButton.style.zIndex = '100';
        }
        /**
         * Hides clear image button
         */
        hideClearImageButton() {
            this.getHtmlElement().parentNode.removeChild(this.clearButton);
        }
        /**
         * Clears image
         */
        clearImage() {
            if (this.originSrc) {
                this.getHtmlElement().src = this.originSrc;
            }
            else {
                this.getHtmlElement().src = null;
            }
            // notify observers
            let element = this.getHtmlElement();
            let data = getProxyTarget(this.getBindData());
            let event = new PropertyChangingEvent(element, data, this.getProperty(), this.getValue(), this.getIndex());
            this.notifyObservers(event);
        }
        /**
         * Changes image
         */
        changeImage() {
            let input = document.createElement('input');
            input.setAttribute("type", "file");
            input.setAttribute("accept", "image/gif, image/jpeg, image/png, image/svg+xml");
            let _this = this;
            input.addEventListener('change', function (e) {
                let fileReader = new FileReader();
                if (this.files && this.files[0]) {
                    fileReader.addEventListener("load", (e) => __awaiter$1(this, void 0, void 0, function* () {
                        let image = e.target.result;
                        let value;
                        if (_this.width && _this.height) {
                            value = yield _this.convertImage(image, _this.width, _this.height);
                        }
                        else {
                            value = yield _this.convertImage(image);
                        }
                        _this.setValue(value);
                        // notify observers
                        let element = _this.getHtmlElement();
                        let data = getProxyTarget(_this.getBindData());
                        let event = new PropertyChangingEvent(element, data, _this.getProperty(), _this.getValue(), _this.getIndex());
                        _this.notifyObservers(event);
                    }));
                    fileReader.readAsDataURL(this.files[0]);
                }
                e.preventDefault();
                e.stopPropagation();
            });
            input.click();
        }
        /**
         * Converts image
         * @param dataUrl data url
         * @param width width
         * @param height height
         */
        convertImage(dataUrl, width, height) {
            return new Promise(function (resolve, reject) {
                try {
                    let canvas = document.createElement("canvas");
                    let ctx = canvas.getContext("2d");
                    let image = new Image();
                    image.onload = function () {
                        if (width && height) {
                            canvas.width = width;
                            canvas.height = height;
                            ctx.drawImage(image, 0, 0, width, height);
                        }
                        else {
                            canvas.width = image.naturalWidth;
                            canvas.height = image.naturalHeight;
                            ctx.drawImage(image, 0, 0);
                        }
                        let dataUrl = canvas.toDataURL("image/png");
                        resolve(dataUrl);
                    };
                    image.src = dataUrl;
                }
                catch (e) {
                    reject(e);
                }
            });
        }
        /**
         * Sets value
         * @param value value
         */
        setValue(value) {
            if (value) {
                this.getHtmlElement().src = value;
            }
            else {
                this.getHtmlElement().src = this.originSrc;
            }
        }
        /**
         * Gets value
         */
        getValue() {
            let value = this.getHtmlElement().src;
            if (value === this.originSrc) {
                return null;
            }
            else {
                return value;
            }
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            this.readonly = readonly;
            this.getHtmlElement().style.pointerEvents = (this.readonly || this.disabled ? 'none' : 'unset');
        }
        /**
         * Sets disabled
         * @param disabled disabled or not
         */
        setDisabled(disabled) {
            this.disabled = disabled;
            this.getHtmlElement().style.pointerEvents = (this.disabled || this.readonly ? 'none' : 'unset');
        }
    }

    /**
     * Img Element Factory
     */
    class ImgElementFactory extends ObjectElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            return new ImgElement(htmlElement, bindData, context);
        }
    }
    /**
     * Static block
     */
    (() => {
        // register factory instance
        ElementRegistry.register('img', new ImgElementFactory());
    })();

    /**
     * Input Element
     */
    class InputElement extends ObjectElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            // Adds change event listener
            this.getHtmlElement().addEventListener('change', e => {
                e.stopPropagation();
                let element = this.getHtmlElement();
                let data = getProxyTarget(this.getBindData());
                let propertyChangingEvent = new PropertyChangingEvent(element, data, this.getProperty(), this.getValue(), this.getIndex());
                this.notifyObservers(propertyChangingEvent);
            }, true);
            // turn off autocomplete
            this.getHtmlElement().setAttribute('autocomplete', 'off');
        }
        /**
         * Gets element value
         * @param value element value
         */
        setValue(value) {
            if (value != null) {
                value = this.getFormat() ? this.getFormat().format(value) : value;
            }
            else {
                value = '';
            }
            this.getHtmlElement().value = value;
        }
        /**
         * Gets element value
         */
        getValue() {
            let value = this.getHtmlElement().value;
            if (value) {
                value = this.getFormat() ? this.getFormat().parse(value) : value;
            }
            else {
                value = null;
            }
            return value;
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            this.getHtmlElement().readOnly = readonly;
        }
        /**
         * Sets disabled
         * @param disabled disabled or not
         */
        setDisabled(disabled) {
            if (disabled) {
                this.getHtmlElement().setAttribute('disabled', 'disabled');
            }
            else {
                this.getHtmlElement().removeAttribute('disabled');
            }
        }
        /**
         * Focus element
         */
        focus() {
            this.getHtmlElement().focus();
            return true;
        }
    }

    /**
     * Input Number Element
     */
    class InputNumberElement extends InputElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            // changes type and style
            this.getHtmlElement().setAttribute('type', 'text');
            this.getHtmlElement().style.textAlign = 'right';
            // prevents invalid key press
            this.getHtmlElement().addEventListener('keypress', event => {
                if (/[\d|\.|,]/.test(event.key) === false) {
                    event.preventDefault();
                }
            });
        }
        /**
         * Gets element value
         */
        getValue() {
            let value = super.getValue();
            return Number(value);
        }
    }

    /**
     * Input Checkbox Element
     */
    class InputCheckboxElement extends InputElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            this.trueValue = true;
            this.falseValue = false;
            // true false value
            let trueValue = getElementAttribute(this.getHtmlElement(), 'true-value');
            this.trueValue = trueValue ? trueValue : this.trueValue;
            let falseValue = getElementAttribute(this.getHtmlElement(), 'false-value');
            this.falseValue = falseValue ? falseValue : this.falseValue;
        }
        /**
         * Sets element value
         * @param value element value
         */
        setValue(value) {
            if (value === this.trueValue) {
                this.getHtmlElement().checked = true;
            }
            else {
                this.htmlElement.checked = false;
            }
        }
        /**
         * Gets value
         */
        getValue() {
            if (this.htmlElement.checked) {
                return this.trueValue;
            }
            else {
                return this.falseValue;
            }
        }
        /**
         * Disable click
         * @param event event
         */
        disableClick(event) {
            event.preventDefault();
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            debug('InputCheckboxElement.setReadonly', readonly);
            if (readonly) {
                this.getHtmlElement().addEventListener('click', this.disableClick);
            }
            else {
                this.getHtmlElement().removeEventListener('click', this.disableClick);
            }
        }
    }

    /**
     * Input Datetime Local Element
     */
    class InputDatetimeLocalElement extends InputElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            this.dateFormat = new DateFormat('yyyy-MM-ddTHH:mm');
        }
        /**
         * Sets element value
         * @param value
         */
        setValue(value) {
            if (value) {
                this.getHtmlElement().value = this.dateFormat.format(value);
            }
            else {
                this.getHtmlElement().value = '';
            }
        }
        /**
         * Gets element value
         */
        getValue() {
            let value = this.getHtmlElement().value;
            if (value) {
                return new Date(value).toISOString();
            }
            else {
                return null;
            }
        }
    }

    /**
     * Input Radio Element
     */
    class InputRadioElement extends InputElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
        }
        /**
         * Sets element value
         * @param value element value
         */
        setValue(value) {
            this.getHtmlElement().checked = (this.getHtmlElement().value === value);
        }
        /**
         * Gets element value
         */
        getValue() {
            return this.getHtmlElement().value;
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            if (readonly) {
                this.getHtmlElement().style.pointerEvents = 'none';
            }
            else {
                this.getHtmlElement().style.pointerEvents = '';
            }
        }
    }

    /**
     * Input Range Element
     */
    class InputRangeElement extends InputElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            this.trueValue = true;
            this.falseValue = false;
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            debug('InputRangeElement.setReadonly', readonly);
            this.getHtmlElement().readOnly = readonly;
            if (readonly) {
                this.getHtmlElement().addEventListener('mousedown', this.disableEvent);
                this.getHtmlElement().addEventListener('touchstart', this.disableEvent);
            }
            else {
                this.getHtmlElement().removeEventListener('mousedown', this.disableEvent);
                this.getHtmlElement().removeEventListener('touchstart', this.disableEvent);
            }
        }
        /**
         * Disable event
         * @param event event
         */
        disableEvent(event) {
            event.preventDefault();
            return false;
        }
    }

    /**
     * Input Element Factory
     */
    class InputElementFactory extends ObjectElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            let type = htmlElement.getAttribute('type');
            switch (type) {
                case 'number':
                    return new InputNumberElement(htmlElement, bindData, context);
                case 'checkbox':
                    return new InputCheckboxElement(htmlElement, bindData, context);
                case 'radio':
                    return new InputRadioElement(htmlElement, bindData, context);
                case 'datetime-local':
                    return new InputDatetimeLocalElement(htmlElement, bindData, context);
                case 'range':
                    return new InputRangeElement(htmlElement, bindData, context);
                default:
                    return new InputElement(htmlElement, bindData, context);
            }
        }
    }
    /**
     * Static block
     */
    (() => {
        // register factory instance
        ElementRegistry.register('input', new InputElementFactory());
    })();

    /**
     * Select Element
     */
    class SelectElement extends ObjectElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            this.defaultOptions = [];
            // stores default option
            for (let i = 0; i < this.getHtmlElement().options.length; i++) {
                this.defaultOptions.push(this.getHtmlElement().options[i]);
            }
            // option property
            this.option = getElementAttribute(this.getHtmlElement(), 'option');
            this.optionValueProperty = getElementAttribute(this.getHtmlElement(), 'option-value-property');
            this.optionTextProperty = getElementAttribute(this.getHtmlElement(), 'option-text-property');
            // adds event listener
            this.getHtmlElement().addEventListener('change', e => {
                e.stopPropagation();
                let element = this.getHtmlElement();
                let data = getProxyTarget(this.getBindData());
                let propertyChangingEvent = new PropertyChangingEvent(element, data, this.getProperty(), this.getValue(), this.getIndex());
                this.notifyObservers(propertyChangingEvent);
            }, true);
        }
        /**
         * Overrides render
         */
        render() {
            this.createOptions();
            super.render();
        }
        /**
         * Updates options
         */
        createOptions() {
            let value = this.getHtmlElement().value;
            this.getHtmlElement().innerHTML = '';
            this.defaultOptions.forEach(defaultOption => {
                this.getHtmlElement().appendChild(defaultOption);
            });
            if (this.option) {
                let optionArray = findVariable(this.getContext(), this.option);
                if (optionArray) {
                    optionArray.forEach(it => {
                        let option = document.createElement('option');
                        option.value = it[this.optionValueProperty];
                        option.appendChild(document.createTextNode(it[this.optionTextProperty]));
                        this.getHtmlElement().appendChild(option);
                    });
                }
            }
            this.getHtmlElement().value = value;
        }
        /**
         * Sets element value
         * @param value value
         */
        setValue(value) {
            this.getHtmlElement().value = value;
            // force select option
            if (!value) {
                for (let i = 0; i < this.getHtmlElement().options.length; i++) {
                    let option = this.getHtmlElement().options[i];
                    if (!option.nodeValue) {
                        option.selected = true;
                        break;
                    }
                }
            }
        }
        /**
         * Gets element value
         */
        getValue() {
            let value = this.getHtmlElement().value;
            if (!value || value.trim().length < 1) {
                value = null;
            }
            return value;
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            if (readonly) {
                this.getHtmlElement().style.pointerEvents = 'none';
            }
            else {
                this.getHtmlElement().style.pointerEvents = '';
            }
        }
        /**
         * Sets disabled
         * @param disabled disable or not
         */
        setDisabled(disabled) {
            if (disabled) {
                this.getHtmlElement().setAttribute('disabled', 'disabled');
            }
            else {
                this.getHtmlElement().removeAttribute('disabled');
            }
        }
    }

    /**
     * Select Element Factory
     */
    class SelectElementFactory extends ObjectElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            return new SelectElement(htmlElement, bindData, context);
        }
    }
    /**
     * Static block
     */
    (() => {
        // register factory instance
        ElementRegistry.register('select', new SelectElementFactory());
    })();

    /**
     * Textarea Element
     */
    class TextareaElement extends ObjectElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            // adds change event listener
            this.getHtmlElement().addEventListener('change', e => {
                e.stopPropagation();
                let element = this.getHtmlElement();
                let data = getProxyTarget(this.getBindData());
                let propertyChangingEvent = new PropertyChangingEvent(element, data, this.getProperty(), this.getValue(), this.getIndex());
                this.notifyObservers(propertyChangingEvent);
            }, true);
        }
        /**
         * Sets element value
         * @param value property value
         */
        setValue(value) {
            if (value != null) {
                this.getHtmlElement().value = value;
            }
            else {
                this.getHtmlElement().value = '';
            }
        }
        /**
         * Gets element value
         */
        getValue() {
            let value = this.getHtmlElement().value;
            if (value != null && value.length > 0) {
                return value;
            }
            else {
                return null;
            }
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            if (readonly) {
                this.getHtmlElement().setAttribute('readonly', 'readonly');
            }
            else {
                this.getHtmlElement().removeAttribute('readonly');
            }
        }
        /**
         * Sets disabled
         * @param disabled disabled or not
         */
        setDisabled(disabled) {
            if (disabled) {
                this.getHtmlElement().setAttribute('disabled', 'disabled');
            }
            else {
                this.getHtmlElement().removeAttribute('disabled');
            }
        }
    }

    /**
     * Textarea Element Factory
     */
    class TextareaElementFactory extends ObjectElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            return new TextareaElement(htmlElement, bindData, context);
        }
    }
    /**
     * Static block
     */
    (() => {
        // register
        ElementRegistry.register('textarea', new TextareaElementFactory());
    })();

    var __awaiter = (window && window.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    /**
     * Dialog
     */
    class Dialog {
        /**
         * Constructor
         * @param dialogElement dialog element
         */
        constructor(dialogElement) {
            this.dialogElement = dialogElement;
            let _this = this;
            // dialog fixed style
            this.dialogElement.style.position = 'absolute';
            this.dialogElement.style.left = '0';
            this.dialogElement.style.right = '0';
            this.dialogElement.style.overflowX = 'hidden';
            this.dialogElement.style.boxSizing = 'border-box';
            this.dialogElement.style.maxWidth = '100%';
            // header
            this.header = document.createElement('div');
            this.dialogElement.appendChild(this.header);
            this.header.style.display = 'flex';
            this.header.style.justifyContent = 'end';
            this.header.style.lineHeight = '2rem';
            this.header.style.position = 'absolute';
            this.header.style.left = '0';
            this.header.style.top = '0';
            this.header.style.width = '100%';
            this.header.style.cursor = 'pointer';
            // drag
            this.header.onmousedown = function (event) {
                let pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
                pos3 = event.clientX;
                pos4 = event.clientY;
                window.document.onmouseup = function (event) {
                    window.document.onmousemove = null;
                    window.document.onmouseup = null;
                };
                window.document.onmousemove = function (event) {
                    pos1 = pos3 - event.clientX;
                    pos2 = pos4 - event.clientY;
                    pos3 = event.clientX;
                    pos4 = event.clientY;
                    _this.dialogElement.style.left = (_this.dialogElement.offsetLeft - pos1) + 'px';
                    _this.dialogElement.style.top = (_this.dialogElement.offsetTop - pos2) + 'px';
                };
            };
            // creates close button
            this.closeButton = document.createElement('span');
            this.closeButton.innerHTML = '&#10005;';
            this.closeButton.style.fontFamily = 'monospace';
            this.closeButton.style.fontSize = 'large';
            this.closeButton.style.marginLeft = '0.5rem';
            this.closeButton.style.marginRight = '0.5rem';
            this.closeButton.addEventListener('click', event => {
                _this.hide();
                _this.close();
            });
            this.dialogElement.addEventListener('scroll', () => {
                const scrollTop = this.dialogElement.scrollTop;
                this.header.style.top = `${scrollTop}px`;
            });
            this.header.appendChild(this.closeButton);
            // on resize event
            window.addEventListener('resize', function (event) {
                _this.movePositionToCenter();
            });
        }
        /**
         * Sets opening listener
         * @param listener listener
         */
        onOpening(listener) {
            this.openingListener = listener;
            return this;
        }
        /**
         * Sets opened listener
         * @param listener listener
         */
        onOpened(listener) {
            this.openedListener = listener;
            return this;
        }
        /**
         * Sets closing listener
         * @param listener listener
         */
        onClosing(listener) {
            this.closingListener = listener;
            return this;
        }
        /**
         * Sets closed listener
         * @param listener listener
         */
        onClosed(listener) {
            this.closedListener = listener;
            return this;
        }
        /**
         * Moves position to center
         */
        movePositionToCenter() {
            window.getComputedStyle(this.dialogElement);
            this.dialogElement.style.boxSizing = 'border-box';
            let computedWidth = this.dialogElement.offsetWidth;
            let computedHeight = this.dialogElement.offsetHeight;
            let scrollX = window.scrollX;
            let scrollY = window.scrollY;
            this.dialogElement.style.left = Math.max(0, window.innerWidth / 2 - computedWidth / 2) + scrollX + 'px';
            this.dialogElement.style.top = Math.max(0, window.innerHeight / 3 - computedHeight / 3) + scrollY + 'px';
        }
        /**
         * Gets dialog element
         * @protected
         */
        getDialogElement() {
            return this.dialogElement;
        }
        /**
         * Shows dialog
         * @protected
         */
        show() {
            // saves current scroll position
            let scrollX = window.scrollX;
            let scrollY = window.scrollY;
            // show dialog modal
            window.document.body.appendChild(this.dialogElement);
            this.dialogElement.showModal();
            // restore previous scroll position
            window.scrollTo(scrollX, scrollY);
            // adjusting position
            this.movePositionToCenter();
        }
        /**
         * Hides dialog
         * @protected
         */
        hide() {
            this.dialogElement.close();
        }
        /**
         * Opens dialog
         */
        open() {
            return __awaiter(this, void 0, void 0, function* () {
                // opening listener
                if (this.openingListener) {
                    if (this.openingListener.call(this) == false) {
                        return;
                    }
                }
                // show modal
                this.show();
                // opened listener
                if (this.openedListener) {
                    this.openedListener.call(this);
                }
                // creates promise
                let _this = this;
                this.promise = new Promise(function (resolve, reject) {
                    _this.promiseResolve = resolve;
                    _this.promiseReject = reject;
                });
                return this.promise;
            });
        }
        /**
         * Closes dialog
         * @param args args
         * @protected
         */
        close(...args) {
            // closing listener
            if (this.closingListener) {
                if (this.closingListener.call(this) == false) {
                    return;
                }
            }
            // closed listener
            if (this.closedListener) {
                this.closedListener.call(this);
            }
            // resolve
            this.hide();
            this.promiseResolve(...args);
        }
    }

    /**
     * Alert Dialog
     */
    class AlertDialog extends Dialog {
        /**
         * Constructor
         * @param message message
         */
        constructor(message) {
            super(document.createElement('dialog'));
            this.getDialogElement().style.padding = '1rem';
            this.getDialogElement().style.minWidth = '20rem';
            this.getDialogElement().style.textAlign = 'center';
            // message pre
            this.messagePre = document.createElement('pre');
            this.messagePre.style.whiteSpace = 'pre-wrap';
            this.messagePre.style.marginTop = '1rem';
            this.messagePre.style.marginBottom = '1rem';
            this.messagePre.innerHTML = message;
            this.getDialogElement().appendChild(this.messagePre);
            // confirm button
            this.confirmButton = document.createElement('button');
            this.confirmButton.appendChild(document.createTextNode('OK'));
            this.confirmButton.style.width = '5em';
            this.confirmButton.style.cursor = 'pointer';
            this.confirmButton.addEventListener('click', event => {
                this.confirm();
            });
            this.getDialogElement().appendChild(this.confirmButton);
        }
        /**
         * Overrides open
         */
        open() {
            let promise = super.open();
            this.confirmButton.focus();
            return promise;
        }
        /**
         * Confirm
         */
        confirm() {
            super.close();
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        /**
         * Overrides close
         */
        close() {
            super.close();
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
    }

    /**
     * Confirm Dialog
     */
    class ConfirmDialog extends Dialog {
        /**
         * Constructor
         * @param message message
         */
        constructor(message) {
            super(document.createElement('dialog'));
            this.getDialogElement().style.padding = '1rem';
            this.getDialogElement().style.minWidth = '20rem';
            this.getDialogElement().style.textAlign = 'center';
            // message pre
            this.messagePre = document.createElement('pre');
            this.messagePre.style.whiteSpace = 'pre-wrap';
            this.messagePre.style.marginTop = '1rem';
            this.messagePre.style.marginBottom = '1rem';
            this.messagePre.innerHTML = message;
            this.getDialogElement().appendChild(this.messagePre);
            // cancel button
            this.cancelButton = document.createElement('button');
            this.cancelButton.appendChild(document.createTextNode('Cancel'));
            this.cancelButton.style.width = '5em';
            this.cancelButton.style.cursor = 'pointer';
            this.cancelButton.addEventListener('click', event => {
                this.cancel();
            });
            this.getDialogElement().appendChild(this.cancelButton);
            // divider
            this.getDialogElement().appendChild(document.createTextNode(' '));
            // confirm button
            this.confirmButton = document.createElement('button');
            this.confirmButton.appendChild(document.createTextNode('OK'));
            this.confirmButton.style.width = '5em';
            this.confirmButton.style.cursor = 'pointer';
            this.confirmButton.addEventListener('click', event => {
                this.confirm();
            });
            this.getDialogElement().appendChild(this.confirmButton);
        }
        /**
         * Opens dialog
         */
        open() {
            let promise = super.open();
            this.confirmButton.focus();
            return promise;
        }
        /**
         * Closes dialog
         * @param args args
         */
        close(...args) {
            super.close(false);
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        /**
         * Confirm
         */
        confirm() {
            super.close(true);
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        /**
         * Cancel
         */
        cancel() {
            super.close(false);
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
    }

    /**
     * Prompt Dialog
     */
    class PromptDialog extends Dialog {
        /**
         * Constructor
         * @param message message
         * @param type type
         */
        constructor(message, type) {
            super(document.createElement('dialog'));
            this.getDialogElement().style.padding = '1rem';
            this.getDialogElement().style.minWidth = '20rem';
            this.getDialogElement().style.textAlign = 'center';
            // message pre
            this.messagePre = document.createElement('pre');
            this.messagePre.style.whiteSpace = 'pre-wrap';
            this.messagePre.style.marginTop = '1rem';
            this.messagePre.style.marginBottom = '1rem';
            this.messagePre.innerHTML = message;
            this.getDialogElement().appendChild(this.messagePre);
            // prompt input
            this.promptInput = document.createElement('input');
            this.promptInput.style.textAlign = 'center';
            this.promptInput.style.marginBottom = '1rem';
            this.promptInput.style.width = '100%';
            if (type) {
                this.promptInput.type = type;
            }
            this.getDialogElement().appendChild(this.promptInput);
            // cancel button
            this.cancelButton = document.createElement('button');
            this.cancelButton.appendChild(document.createTextNode('Cancel'));
            this.cancelButton.style.width = '5em';
            this.cancelButton.style.cursor = 'pointer';
            this.cancelButton.addEventListener('click', event => {
                this.cancel();
            });
            this.getDialogElement().appendChild(this.cancelButton);
            // divider
            this.getDialogElement().appendChild(document.createTextNode(' '));
            // confirm button
            this.confirmButton = document.createElement('button');
            this.confirmButton.appendChild(document.createTextNode('OK'));
            this.confirmButton.style.width = '5em';
            this.confirmButton.style.cursor = 'pointer';
            this.confirmButton.addEventListener('click', event => {
                this.confirm(this.promptInput.value);
            });
            this.getDialogElement().appendChild(this.confirmButton);
        }
        /**
         * Overrides open
         */
        open() {
            let promise = super.open();
            this.promptInput.focus();
            return promise;
        }
        /**
         * Overrides close
         * @param args
         */
        close(...args) {
            super.close();
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        /**
         * Confirm
         * @param value
         */
        confirm(value) {
            super.close(value);
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        /**
         * Cancel
         */
        cancel() {
            super.close();
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
    }

    /**
     * Tab Folder
     */
    class TabFolder {
        constructor() {
            this.items = [];
        }
        /**
         * Adds tab item
         * @param item
         */
        addItem(item) {
            item.setTabFolder(this);
            item.setTabIndex(this.items.length);
            this.items.push(item);
        }
        /**
         * set Active tab item
         * @param index index
         */
        setActive(index) {
            for (let i = 0; i < this.items.length; i++) {
                this.items[i].setActive(i === index);
            }
        }
    }

    /**
     * Tab Item
     */
    class TabItem {
        /**
         * Constructor
         * @param button button
         * @param content content
         * @param listener listener
         */
        constructor(button, content, listener) {
            this.button = button;
            this.content = content;
            this.listener = listener;
            // default style
            button.style.cursor = 'pointer';
            // add listener
            let _this = this;
            button.addEventListener('click', () => {
                _this.tabFolder.setActive(_this.tabIndex);
            });
            // set de-active
            this.setActive(false);
        }
        /**
         * Sets tab folder
         * @param tabFolder tab folder
         */
        setTabFolder(tabFolder) {
            this.tabFolder = tabFolder;
        }
        /**
         * Sets tab index
         * @param tabIndex tab index
         */
        setTabIndex(tabIndex) {
            this.tabIndex = tabIndex;
        }
        /**
         * Sets active
         * @param active active or not
         */
        setActive(active) {
            if (active) {
                this.button.style.opacity = 'unset';
                this.content.removeAttribute('hidden');
                this.listener.call(this);
            }
            else {
                this.button.style.opacity = '0.5';
                this.content.setAttribute('hidden', String(true));
            }
        }
    }

    // initializes
    (function () {
        // listen DOMContentLoaded
        if (globalThis.document) {
            document.addEventListener("DOMContentLoaded", event => {
                Initializer.initialize(document.documentElement, {});
            });
        }
        // listen history event and forward to DOMContentLoaded event
        if (globalThis.window) {
            // popstate
            window.addEventListener('popstate', e => {
                document.dispatchEvent(new CustomEvent('DOMContentLoaded'));
            });
            // pageshow
            window.addEventListener('pageshow', (e) => {
                if (e.persisted) {
                    document.dispatchEvent(new CustomEvent('DOMContentLoaded'));
                }
            });
        }
    })();

    exports.AlertDialog = AlertDialog;
    exports.ArrayElement = ArrayElement;
    exports.ArrayElementFactory = ArrayElementFactory;
    exports.ArrayProxy = ArrayProxy;
    exports.Configuration = Configuration;
    exports.ConfirmDialog = ConfirmDialog;
    exports.CustomElement = CustomElement;
    exports.CustomElementFactory = CustomElementFactory;
    exports.Dialog = Dialog;
    exports.ElementRegistry = ElementRegistry;
    exports.ImgElementFactory = ImgElementFactory;
    exports.Initializer = Initializer;
    exports.InputElementFactory = InputElementFactory;
    exports.ItemMovedEvent = ItemMovedEvent;
    exports.ItemMovingEvent = ItemMovingEvent;
    exports.ItemSelectedEvent = ItemSelectedEvent;
    exports.ItemSelectingEvent = ItemSelectingEvent;
    exports.ObjectElement = ObjectElement;
    exports.ObjectElementFactory = ObjectElementFactory;
    exports.ObjectProxy = ObjectProxy;
    exports.PromptDialog = PromptDialog;
    exports.PropertyChangedEvent = PropertyChangedEvent;
    exports.PropertyChangingEvent = PropertyChangingEvent;
    exports.ProxyHandler = ProxyHandler;
    exports.SelectElementFactory = SelectElementFactory;
    exports.TabFolder = TabFolder;
    exports.TabItem = TabItem;
    exports.TextareaElementFactory = TextareaElementFactory;
    exports.assert = assert;
    exports.debug = debug;
    exports.findVariable = findVariable;
    exports.getElementAttribute = getElementAttribute;
    exports.getElementQuerySelector = getElementQuerySelector;
    exports.getProxyHandler = getProxyHandler;
    exports.getProxyTarget = getProxyTarget;
    exports.hasElementAttribute = hasElementAttribute;
    exports.isArray = isArray;
    exports.isObject = isObject;
    exports.isPrimitive = isPrimitive;
    exports.isProxy = isProxy;
    exports.markInitialized = markInitialized;
    exports.runCode = runCode;
    exports.runExecuteCode = runExecuteCode;
    exports.runIfCode = runIfCode;
    exports.setElementAttribute = setElementAttribute;
    exports.setProxyHandler = setProxyHandler;
    exports.setProxyTarget = setProxyTarget;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({});
//# sourceMappingURL=duice.js.map
