/*!
 * duice-marked - v0.3.0
 * git: https://gitbub.com/chomookun/duice-plugin
 * website: https://duice-plugin.chomookun.org
 * Released under the LGPL(GNU Lesser General Public License version 3) License
 */
this.duice = this.duice || {};
this.duice.plugin = this.duice.plugin || {};
this.duice.plugin.Marked = (function (exports, duice) {
    'use strict';

    /**
     * Marked Element
     */
    class MarkedElement extends duice.ObjectElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            // creates child div
            this.div = document.createElement('div');
            this.getHtmlElement().appendChild(this.div);
            // customizes responsive table
            const renderer = new marked.Renderer();
            renderer.table = (header, body) => {
                return `<table style="display:inline-block;overflow-x:scroll;max-width:100%;">\n<thead>${header}</thead>\n<tbody>${body}</tbody>\n</table>`;
            };
            // config
            this.config = {
                headerIds: false,
                mangle: false,
                breaks: true,
                gfm: true,
                renderer: renderer
            };
        }
        /**
         * Sets value
         * @param value value
         */
        setValue(value) {
            value = value ? value : '';
            value = marked.parse(value, this.config);
            this.div.innerHTML = value;
            this.div.querySelectorAll('[class^=language-]').forEach(function (pre) {
                pre.classList.add('line-numbers');
            });
            // highlight
            Prism.highlightAll();
        }
    }

    /**
     * Marked Element Factory
     */
    class MarkedElementFactory extends duice.ObjectElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            return new MarkedElement(htmlElement, bindData, context);
        }
    }
    /**
     * Static block
     */
    (() => {
        // register
        duice.ElementRegistry.register(`${duice.Configuration.getNamespace()}-marked`, new MarkedElementFactory());
    })();

    exports.MarkedElement = MarkedElement;
    exports.MarkedElementFactory = MarkedElementFactory;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, duice);
//# sourceMappingURL=duice-marked.js.map
