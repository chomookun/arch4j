/*!
 * duice-codemirror - v0.3.0
 * git: https://gitbub.com/chomookun/duice-plugin
 * website: https://duice-plugin.chomookun.org
 * Released under the LGPL(GNU Lesser General Public License version 3) License
 */
this.duice = this.duice || {};
this.duice.plugin = this.duice.plugin || {};
this.duice.plugin.Codemirror = (function (exports, duice) {
    'use strict';

    /**
     * Codemirror Element
     */
    class CodemirrorElement extends duice.ObjectElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            this.mode = 'text/x-markdown';
            this.theme = 'default';
            this.getHtmlElement().style.display = 'block';
            // option
            this.mode = duice.getElementAttribute(htmlElement, 'mode') || this.mode;
            this.theme = duice.getElementAttribute(htmlElement, 'theme') || this.theme;
            // config
            let config = {
                mode: this.mode,
                inputStyle: 'textarea',
                lineNumbers: true,
                theme: this.theme,
                extraKeys: { "Enter": "newlineAndIndentContinueMarkdownList" }
            };
            // textarea
            let textarea = document.createElement('textarea');
            this.getHtmlElement().appendChild(textarea);
            // create code mirror
            this.codeMirror = CodeMirror.fromTextArea(textarea, config);
            this.codeMirror.setSize('100%', '100%');
            // refresh (not working without setTimeout)
            setTimeout(() => {
                this.codeMirror.refresh();
            }, 1);
            // add change event listener
            this.codeMirror.on("blur", () => {
                let element = this.getHtmlElement();
                let data = this.getBindData();
                let event = new duice.PropertyChangingEvent(element, data, this.getProperty(), this.getValue(), this.getIndex());
                this.notifyObservers(event);
            });
        }
        /**
         * Sets value
         * @param value property value
         */
        setValue(value) {
            if (!value) {
                value = '';
            }
            let scrollInfo = this.codeMirror.getScrollInfo();
            this.codeMirror.doc.setValue(value);
            this.codeMirror.scrollTo(scrollInfo.left, scrollInfo.top);
        }
        /**
         * Gets value
         */
        getValue() {
            let value = this.codeMirror.doc.getValue();
            if (!value) {
                return null;
            }
            return value;
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            this.codeMirror.setOption('readOnly', readonly);
        }
    }

    /**
     * Codemirror Factory
     */
    class CodemirrorElementFactory extends duice.ObjectElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            return new CodemirrorElement(htmlElement, bindData, context);
        }
    }
    /**
     * Static block
     */
    (() => {
        // register
        duice.ElementRegistry.register(`${duice.Configuration.getNamespace()}-codemirror`, new CodemirrorElementFactory());
    })();

    exports.CodemirrorElement = CodemirrorElement;
    exports.CodemirrorElementFactory = CodemirrorElementFactory;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, duice);
//# sourceMappingURL=duice-codemirror.js.map
