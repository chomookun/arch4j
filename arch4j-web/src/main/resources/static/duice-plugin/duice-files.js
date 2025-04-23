/*!
 * duice-files - v0.3.3
 * git: https://gitbub.com/chomookun/duice-plugin
 * website: https://duice-plugin.chomookun.org
 * Released under the LGPL(GNU Lesser General Public License version 3) License
 */
this.duice = this.duice || {};
this.duice.plugin = this.duice.plugin || {};
this.duice.plugin.Files = (function (exports, duice) {
    'use strict';

    var __awaiter = (undefined && undefined.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    /**
     * Files Element
     */
    class FilesElement extends duice.CustomElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            this.iconContent = '<svg style="display:block;" xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 50 50" width="1rem" height="1rem"><path d="M 7 2 L 7 48 L 43 48 L 43 14.59375 L 42.71875 14.28125 L 30.71875 2.28125 L 30.40625 2 Z M 9 4 L 29 4 L 29 16 L 41 16 L 41 46 L 9 46 Z M 31 5.4375 L 39.5625 14 L 31 14 Z"/></svg>';
            this.removeContent = '[-]';
            this.addContent = '[+]';
            this.emptyContent = 'No files';
            // attributes
            this.filenameProperty = duice.getElementAttribute(htmlElement, 'filename-property');
            this.sizeProperty = duice.getElementAttribute(htmlElement, 'size-property');
            const files = duice.getElementAttribute(htmlElement, 'files');
            this.files = files ? duice.findVariable(context, files) : null;
            // event listeners
            const onClick = duice.getElementAttribute(htmlElement, 'on-click');
            this.onClick = onClick ? duice.findVariable(context, onClick) : null;
            const onAdd = duice.getElementAttribute(htmlElement, 'on-add');
            this.onAdd = onAdd ? duice.findVariable(context, onAdd) : null;
            const onRemove = duice.getElementAttribute(htmlElement, 'on-remove');
            this.onRemove = onRemove ? duice.findVariable(context, onRemove) : null;
            // custom style
            this.iconContent = duice.getElementAttribute(htmlElement, 'icon-content') || this.iconContent;
            this.removeContent = duice.getElementAttribute(htmlElement, 'remove-content') || this.removeContent;
            this.addContent = duice.getElementAttribute(htmlElement, 'add-content') || this.addContent;
        }
        /**
         * Do render
         * @param object
         */
        doRender(object) {
            this.getHtmlElement().innerHTML = '';
            this.getHtmlElement().appendChild(this.createStyle());
            // items
            let arrayProxy = this.getBindData();
            arrayProxy.forEach((file, index) => {
                let itemElement = this.createItemElement(file, index);
                this.getHtmlElement().appendChild(itemElement);
            });
            // item empty
            if (arrayProxy.length === 0) {
                let emptyElement = document.createElement('div');
                emptyElement.innerHTML = `${this.emptyContent}`;
                emptyElement.classList.add(`${duice.Configuration.getNamespace()}-files__empty`);
                this.getHtmlElement().appendChild(emptyElement);
            }
            // add button
            if (this.files) {
                let addButton = document.createElement('span');
                addButton.innerHTML = this.addContent;
                addButton.classList.add(`${duice.Configuration.getNamespace()}-files__item-add`);
                addButton.addEventListener('click', () => {
                    this.addFile().then();
                });
                this.getHtmlElement().appendChild(addButton);
            }
        }
        createItemElement(item, index) {
            let filename = item[this.filenameProperty];
            let size = parseInt(item[this.sizeProperty]);
            let itemElement = document.createElement('div');
            itemElement.classList.add(`${duice.Configuration.getNamespace()}-files__item`);
            // icon
            let iconElement = document.createElement('span');
            iconElement.innerHTML = `${this.iconContent}`;
            iconElement.classList.add(`${duice.Configuration.getNamespace()}-files__item-icon`);
            itemElement.appendChild(iconElement);
            // filename
            let filenameElement = document.createElement('span');
            filenameElement.innerHTML = `${filename}`;
            filenameElement.classList.add(`${duice.Configuration.getNamespace()}-files__item-filename`);
            itemElement.appendChild(filenameElement);
            // on click listener
            if (this.onClick) {
                filenameElement.style.cursor = 'pointer';
                filenameElement.classList.add(`${duice.Configuration.getNamespace()}-files__item-filename--on-click`);
                filenameElement.addEventListener('click', () => {
                    console.log(this.onClick);
                    duice.callFunction(this.onClick, filenameElement, item);
                });
            }
            // size
            let sizeElement = document.createElement('span');
            sizeElement.innerHTML = `(${this.formatBytes(size)})`;
            sizeElement.classList.add(`${duice.Configuration.getNamespace()}-files__item-size`);
            itemElement.appendChild(sizeElement);
            // remove button
            if (this.files) {
                let removeButton = document.createElement('span');
                removeButton.innerHTML = this.removeContent;
                removeButton.classList.add(`${duice.Configuration.getNamespace()}-files__item-remove`);
                removeButton.dataset.index = index.toString();
                let _this = this;
                removeButton.addEventListener('click', function () {
                    let index = parseInt(this.dataset.index);
                    _this.removeFile(index);
                });
                itemElement.appendChild(removeButton);
            }
            // returns
            return itemElement;
        }
        formatBytes(bytes, scale = 2) {
            if (bytes === 0)
                return '0 Bytes';
            const k = 1024;
            const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
            const i = Math.floor(Math.log(bytes) / Math.log(k));
            const value = parseFloat((bytes / Math.pow(k, i)).toFixed(scale));
            return `${value} ${sizes[i]}`;
        }
        /**
         * Adds file
         */
        addFile() {
            return __awaiter(this, void 0, void 0, function* () {
                let _this = this;
                let input = document.createElement('input');
                input.setAttribute('type', 'file');
                input.setAttribute('multiple', 'true');
                input.addEventListener('change', function () {
                    return __awaiter(this, void 0, void 0, function* () {
                        let files = Array.from(this[`files`]);
                        let items = [];
                        if (_this.onAdd) {
                            let result = yield _this.callOnAddListener(files);
                            if (!result) {
                                return false;
                            }
                            else {
                                items.push(...result);
                            }
                        }
                        else {
                            for (const file of files) {
                                let item = {
                                    [_this.filenameProperty]: file.name,
                                    [_this.sizeProperty]: file.size
                                };
                                items.push(item);
                            }
                        }
                        // set file into item for deletion detection
                        for (let i = 0; i < items.length; i++) {
                            let item = items[i];
                            let file = files[i];
                            globalThis.Object.defineProperty(item, '_file_', { value: file, writable: true });
                            _this.getBindData().push(item);
                            _this.files.push(files[i]);
                        }
                    });
                });
                input.click();
            });
        }
        /**
         * Removes file
         * @param index index
         */
        removeFile(index) {
            return __awaiter(this, void 0, void 0, function* () {
                var _a;
                let arrayProxy = this.getBindData();
                let objectProxy = arrayProxy[index];
                // calls on remove listener
                if (this.onRemove) {
                    let result = yield this.callOnRemoveListener(objectProxy);
                    if (result === false) {
                        return;
                    }
                }
                // if target file is exist, remove file
                let targetFile = (_a = globalThis.Object.getOwnPropertyDescriptor(objectProxy, '_file_')) === null || _a === void 0 ? void 0 : _a.value;
                if (targetFile) {
                    let fileIndex = this.files.findIndex(it => {
                        return it === targetFile;
                    });
                    this.files.splice(fileIndex, 1);
                }
                arrayProxy.splice(index, 1);
            });
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
            ${duice.Configuration.getNamespace()}-files {
                display: block;
            }
            .${duice.Configuration.getNamespace()}-files__item {
                display: flex;
                gap: 0.25rem;
                align-items: center;
            }
            .${duice.Configuration.getNamespace()}-files__item > * {
                display: inline-block;
            }
            .${duice.Configuration.getNamespace()}-files__item-icon {
                height: 1em;
                filter: invert(0.5);
            }
            .${duice.Configuration.getNamespace()}-files__item-filename {
            }
            .${duice.Configuration.getNamespace()}-files__item-filename--on-click:hover {
                text-decoration: underline;
            }
            .${duice.Configuration.getNamespace()}-files__item-size {
            }
            .${duice.Configuration.getNamespace()}-files__item-remove {
                cursor: pointer;
            } 
            .${duice.Configuration.getNamespace()}-files__item-add {
                cursor: pointer;
            }
            .${duice.Configuration.getNamespace()}-files__empty {
            }
        `;
            return style;
        }
        callOnAddListener(files) {
            return __awaiter(this, void 0, void 0, function* () {
                return yield duice.callFunction(this.onAdd, null, files);
            });
        }
        callOnRemoveListener(item) {
            return __awaiter(this, void 0, void 0, function* () {
                return yield duice.callFunction(this.onRemove, null, item);
            });
        }
    }

    /**
     * File Element Factory
     */
    class FilesElementFactory extends duice.CustomElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        doCreateElement(htmlElement, bindData, context) {
            return new FilesElement(htmlElement, bindData, context);
        }
    }
    /**
     * Static block
     */
    (() => {
        // register
        duice.ElementRegistry.register(`${duice.Configuration.getNamespace()}-files`, new FilesElementFactory());
    })();

    exports.FilesElement = FilesElement;
    exports.FilesElementFactory = FilesElementFactory;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, duice);
//# sourceMappingURL=duice-files.js.map
