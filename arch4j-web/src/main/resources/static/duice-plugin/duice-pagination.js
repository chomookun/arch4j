/*!
 * duice-pagination - v0.2.5
 * git: https://gitbub.com/chomookun/duice-plugin
 * website: https://duice-plugin.chomookun.org
 * Released under the LGPL(GNU Lesser General Public License version 3) License
 */
this.duice = this.duice || {};
this.duice.plugin = this.duice.plugin || {};
this.duice.plugin.Pagination = (function (exports, duice) {
    'use strict';

    /**
     * Pagination Element
     */
    class PaginationElement extends duice.CustomElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            this.prevContent = '<';
            this.nextContent = '>';
            this.pageNumberSize = 10;
            // attributes
            this.pageProperty = duice.getElementAttribute(htmlElement, 'page-property');
            this.sizeProperty = duice.getElementAttribute(htmlElement, 'size-property');
            this.totalProperty = duice.getElementAttribute(htmlElement, 'total-property');
            this.onclick = new Function(duice.getElementAttribute(htmlElement, 'onclick'));
            // optional
            this.pageNumberSize = Number(duice.getElementAttribute(htmlElement, 'page-number-size') || this.pageNumberSize);
            this.prevContent = duice.getElementAttribute(htmlElement, 'prev-content') || this.prevContent;
            this.nextContent = duice.getElementAttribute(htmlElement, 'next-content') || this.nextContent;
        }
        /**
         * Do render
         * @param object
         */
        doRender(object) {
            // page,size,count
            let page = Number(object[this.pageProperty]);
            let size = Number(object[this.sizeProperty]);
            let total = Number(object[this.totalProperty]);
            // calculate page
            let totalPage = Math.ceil(total / size);
            let startPageIndex = Math.floor(page / this.pageNumberSize) * this.pageNumberSize;
            let endPageIndex = Math.min(startPageIndex + (this.pageNumberSize - 1), totalPage - 1);
            endPageIndex = Math.max(endPageIndex, 0);
            // template
            let pagination = document.createElement('ul');
            pagination.classList.add(`${duice.Configuration.getNamespace()}-pagination`);
            // prev
            let prev = document.createElement('li');
            prev.innerHTML = this.prevContent;
            prev.classList.add(`${duice.Configuration.getNamespace()}-pagination__item-prev`);
            prev.dataset.page = String(Math.max(startPageIndex - this.pageNumberSize, 0));
            prev.addEventListener('click', () => {
                this.onclick.call(prev);
            });
            if (page < this.pageNumberSize) {
                prev.classList.add(`${duice.Configuration.getNamespace()}-pagination__item--disable`);
            }
            pagination.appendChild(prev);
            // pages
            for (let index = startPageIndex; index <= endPageIndex; index++) {
                let item = document.createElement('li');
                item.appendChild(document.createTextNode(String(index + 1)));
                item.dataset.page = String(index);
                item.classList.add(`${duice.Configuration.getNamespace()}-pagination__item-page`);
                if (index === page) {
                    item.classList.add(`${duice.Configuration.getNamespace()}-pagination__item--active`);
                }
                item.addEventListener('click', () => {
                    this.onclick.call(item);
                });
                pagination.appendChild(item);
            }
            // next
            let next = document.createElement('li');
            next.innerHTML = this.nextContent;
            next.classList.add(`${duice.Configuration.getNamespace()}-pagination__item-next`);
            next.dataset.page = String(Math.min(endPageIndex + 1, totalPage));
            next.addEventListener('click', () => {
                this.onclick.call(next);
            });
            if (endPageIndex >= (totalPage - 1)) {
                next.classList.add(`${duice.Configuration.getNamespace()}-pagination__item--disable`);
            }
            pagination.appendChild(next);
            // returns
            this.getHtmlElement().innerHTML = '';
            this.getHtmlElement().appendChild(this.createStyle());
            this.getHtmlElement().appendChild(pagination);
        }
        /**
         * Updates element
         * @param object
         */
        doUpdate(object) {
            this.render();
        }
        /**
         * Creates style
         */
        createStyle() {
            let style = document.createElement('style');
            style.innerHTML = `
            ${duice.Configuration.getNamespace()}-pagination {
                display: inline-block;
            }
            .${duice.Configuration.getNamespace()}-pagination {
                list-style: none;
                display: flex;
                padding-left: 0;
                margin: 0;
            }
            .${duice.Configuration.getNamespace()}-pagination__item-page {
                cursor: pointer;
                padding: 0 0.5em;
            }
            .${duice.Configuration.getNamespace()}-pagination__item-prev {
                cursor: pointer;
                padding: 0 0.5em;
            }
            .${duice.Configuration.getNamespace()}-pagination__item-next {
                cursor: pointer;
                padding: 0 0.5em;
            }
            .${duice.Configuration.getNamespace()}-pagination__item--active {
                font-weight: bold;
                text-decoration: underline;
                pointer-events: none;
            }
            .${duice.Configuration.getNamespace()}-pagination__item--disable {
                pointer-events: none;
            }
        `;
            return style;
        }
    }

    /**
     * Pagination Element Factory
     */
    class PaginationElementFactory extends duice.CustomElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        doCreateElement(htmlElement, bindData, context) {
            return new PaginationElement(htmlElement, bindData, context);
        }
    }
    /**
     * Static block
     */
    (() => {
        // register
        duice.ElementRegistry.register(`${duice.Configuration.getNamespace()}-pagination`, new PaginationElementFactory());
    })();

    exports.PaginationElement = PaginationElement;
    exports.PaginationElementFactory = PaginationElementFactory;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, duice);
//# sourceMappingURL=duice-pagination.js.map
