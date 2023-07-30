var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class MarkdownEditor extends duice.ObjectElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                this.getHtmlElement().style.display = 'block';
                // config
                let config = {
                    mode: 'markdown',
                    inputStyle: 'textarea',
                    lineNumbers: true,
                    theme: "default",
                    extraKeys: { "Enter": "newlineAndIndentContinueMarkdownList" }
                };
                // textarea
                let textarea = document.createElement('textarea');
                this.getHtmlElement().appendChild(textarea);
                // create code mirror
                this.codeMirror = CodeMirror.fromTextArea(textarea, config);
                this.codeMirror.setSize('100%', '100%');
                // add change event listener
                this.codeMirror.on("blur", () => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                    this.notifyObservers(event);
                });
            }
            createToolbar() {
            }
            setValue(value) {
                if (!value) {
                    value = '';
                }
                this.codeMirror.doc.setValue(value);
            }
            getValue() {
                let value = this.codeMirror.doc.getValue();
                if (!value) {
                    return null;
                }
                return value;
            }
        }
        extension.MarkdownEditor = MarkdownEditor;
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class MarkdownEditorFactory extends duice.ObjectElementFactory {
            createElement(htmlElement, bindData, context) {
                return new extension.MarkdownEditor(htmlElement, bindData, context);
            }
        }
        extension.MarkdownEditorFactory = MarkdownEditorFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-markdown-editor`, new MarkdownEditorFactory());
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class MarkdownViewer extends duice.ObjectElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                // creates child div
                this.div = document.createElement('div');
                this.getHtmlElement().appendChild(this.div);
                // config
                this.config = {
                    headerIds: false,
                    mangle: false,
                    breaks: true,
                    gfm: true
                };
            }
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
        extension.MarkdownViewer = MarkdownViewer;
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class MarkdownViewerFactory extends duice.ObjectElementFactory {
            createElement(htmlElement, bindData, context) {
                return new extension.MarkdownViewer(htmlElement, bindData, context);
            }
        }
        extension.MarkdownViewerFactory = MarkdownViewerFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-markdown-viewer`, new MarkdownViewerFactory());
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class Pagination extends duice.CustomElement {
            constructor(htmlElement, bindData, context) {
                super(htmlElement, bindData, context);
                // attributes
                this.pageProperty = duice.getElementAttribute(htmlElement, 'page-property');
                this.sizeProperty = duice.getElementAttribute(htmlElement, 'size-property');
                this.countProperty = duice.getElementAttribute(htmlElement, 'count-property');
                this.onclick = new Function(duice.getElementAttribute(htmlElement, 'onclick'));
            }
            doRender(object) {
                // optional
                let prevText = duice.getElementAttribute(this.getHtmlElement(), 'prev-text') || '<︎';
                let nextText = duice.getElementAttribute(this.getHtmlElement(), 'next-text') || '>︎';
                // page,size,count
                let page = Number(object[this.pageProperty]);
                let size = Number(object[this.sizeProperty]);
                let count = Number(object[this.countProperty]);
                // calculate page
                let totalPage = Math.ceil(count / size);
                let startPageIndex = Math.floor(page / 10) * 10;
                let endPageIndex = Math.min(startPageIndex + 9, totalPage - 1);
                endPageIndex = Math.max(endPageIndex, 0);
                // template
                let pagination = document.createElement('ul');
                pagination.classList.add(`${duice.getNamespace()}-pagination`);
                // prev
                let prev = document.createElement('li');
                prev.appendChild(document.createTextNode(prevText));
                prev.classList.add(`${duice.getNamespace()}-pagination__item-prev`);
                prev.dataset.page = String(Math.max(startPageIndex - 10, 0));
                prev.addEventListener('click', () => {
                    this.onclick.call(prev);
                });
                if (page < 10) {
                    prev.classList.add(`${duice.getNamespace()}-pagination__item--disable`);
                }
                pagination.appendChild(prev);
                // pages
                for (let index = startPageIndex; index <= endPageIndex; index++) {
                    let item = document.createElement('li');
                    item.appendChild(document.createTextNode(String(index + 1)));
                    item.dataset.page = String(index);
                    item.classList.add(`${duice.getNamespace()}-pagination__item-page`);
                    if (index === page) {
                        item.classList.add(`${duice.getNamespace()}-pagination__item--active`);
                    }
                    item.addEventListener('click', () => {
                        this.onclick.call(item);
                    });
                    pagination.appendChild(item);
                }
                // next
                let next = document.createElement('li');
                next.appendChild(document.createTextNode(nextText));
                next.classList.add(`${duice.getNamespace()}-pagination__item-next`);
                next.dataset.page = String(Math.min(endPageIndex + 1, totalPage));
                next.addEventListener('click', () => {
                    this.onclick.call(next);
                });
                if (endPageIndex >= (totalPage - 1)) {
                    next.classList.add(`${duice.getNamespace()}-pagination__item--disable`);
                }
                pagination.appendChild(next);
                // returns
                this.getHtmlElement().innerHTML = '';
                this.getHtmlElement().appendChild(this.createStyle());
                this.getHtmlElement().appendChild(pagination);
            }
            doUpdate(object) {
                this.render();
            }
            createStyle() {
                let style = document.createElement('style');
                style.innerHTML = `
                .${duice.getNamespace()}-pagination {
                    list-style: none;
                    display: flex;
                    padding-left: 0;
                    margin: 0;
                }
                .${duice.getNamespace()}-pagination__item-page {
                    cursor: pointer;
                    padding: 0 0.5rem;
                }
                .${duice.getNamespace()}-pagination__item-prev {
                    cursor: pointer;
                    padding: 0 0.5rem;
                    font-size: smaller;    
                }
                .${duice.getNamespace()}-pagination__item-next {
                    cursor: pointer;
                    padding: 0 0.5rem;
                    font-size: smaller;
                }
                .${duice.getNamespace()}-pagination__item--active {
                    font-weight: bold;
                    text-decoration: underline;
                    pointer-events: none;
                }
                .${duice.getNamespace()}-pagination__item--disable {
                    pointer-events: none;
                }
            `;
                return style;
            }
        }
        extension.Pagination = Pagination;
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class PaginationFactory extends duice.CustomElementFactory {
            doCreateElement(htmlElement, bindData, context) {
                return new extension.Pagination(htmlElement, bindData, context);
            }
        }
        extension.PaginationFactory = PaginationFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-pagination`, new PaginationFactory());
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class Workflow extends duice.CustomElement {
            constructor(htmlElement, bindData, context) {
                super(htmlElement, bindData, context);
                this.elementItems = new Map();
                this.sourceEndpoints = new Map();
                this.targetEndpoints = new Map();
                this.connections = [];
                // parse attribute
                this.elementProperty = duice.getElementAttribute(this.getHtmlElement(), 'element-property');
                this.elementLoop = duice.getElementAttribute(this.getHtmlElement(), 'element-loop');
                this.elementIdProperty = duice.getElementAttribute(this.getHtmlElement(), 'element-id-property');
                let positionProperty = duice.getElementAttribute(this.getHtmlElement(), 'element-position-property');
                let positionPropertyParts = positionProperty.split(',');
                this.elementPositionXProperty = positionPropertyParts[0];
                this.elementPositionYProperty = positionPropertyParts[1];
                this.linkProperty = duice.getElementAttribute(this.getHtmlElement(), 'link-property');
                this.linkSourceProperty = duice.getElementAttribute(this.getHtmlElement(), 'link-source-property');
                this.linkTargetProperty = duice.getElementAttribute(this.getHtmlElement(), 'link-target-property');
                // mark initialized (not using after clone as templates)
                this.htmlElementTemplate = this.getHtmlElement().innerHTML;
                duice.markInitialized(htmlElement);
                this.getHtmlElement().innerHTML = '';
                this.container = document.createElement('div');
                this.container.classList.add(duice.getNamespace() + '-workflow-container');
                this.container.style.position = 'relative';
                this.container.style.width = '100%';
                this.container.style.height = '100%';
                this.container.style.overflow = 'hidden';
                this.container.classList.add(duice.getNamespace() + '-workflow-container');
                this.container.addEventListener('click', event => {
                    event.preventDefault();
                });
                this.getHtmlElement().appendChild(this.createStyle());
                this.jsPlumbInstance = jsPlumb.getInstance({
                    container: this.container,
                });
                this.getHtmlElement().appendChild(this.container);
                this.jsPlumbInstance.bind('connection', (event, mouseEvent) => {
                    console.debug('== connection:', event, mouseEvent);
                    if (!mouseEvent) {
                        return;
                    }
                    event.connection.addOverlay(["PlainArrow", {
                            location: 1,
                            width: 15,
                            length: 15,
                            id: "arrow",
                        }]);
                    event.connection.addOverlay(['Label', {
                            label: '<span style="cursor:pointer; font-weight:bold;">[X]</span>',
                            location: 0.5,
                            events: {
                                click: (labelOverlay, originalEvent) => {
                                    this.jsPlumbInstance.deleteConnection(event.connection);
                                }
                            }
                        }]);
                    let linkSourceId = duice.getElementAttribute(event.connection.source, 'element-id');
                    let linkTargetId = duice.getElementAttribute(event.connection.target, 'element-id');
                    this.addLinkData(linkSourceId, linkTargetId);
                });
                this.jsPlumbInstance.bind('connectionMoved', event => {
                    console.debug('== connectionMoved:', event);
                    let linkSourceId = duice.getElementAttribute(event.originalSourceEndpoint.element, 'element-id');
                    let linkTargetId = duice.getElementAttribute(event.originalTargetEndpoint.element, 'element-id');
                    this.removeLinkData(linkSourceId, linkTargetId);
                });
                this.jsPlumbInstance.bind('internal.connectionDetached', event => {
                    console.debug('== internal.connectionDetached:', event);
                    let linkSourceId = duice.getElementAttribute(event.connection.source, 'element-id');
                    let linkTargetId = duice.getElementAttribute(event.connection.target, 'element-id');
                    this.removeLinkData(linkSourceId, linkTargetId);
                });
            }
            createStyle() {
                let style = document.createElement('style');
                style.innerHTML = `
                .${duice.getNamespace()}-workflow-container {
                    background-size: 1rem 1rem;
                    background-image: radial-gradient(circle, rgba(128, 128, 128, 0.25) 1px, rgba(0, 0, 0, 0) 1px);
                }
                .${duice.getNamespace()}-workflow-element {
                }
                .${duice.getNamespace()}-workflow-link-disconnect {
                    display: block;
                    cursor: pointer;
                    background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAACXBIWXMAAAsTAAALEwEAmpwYAAAApElEQVR4nGNgoBHgZ2Bg0GJgYDCHYhCbjxiNrAwMDFYMDAyROLAVVA1OzV54NMOwJy5DrIjQDMOW2PwciYQfMjAwLETiL4SKIatBCRNtNEmQhn9QGpmNrAYUsHBgjsWZc6Aa/2PRHAnVg9eARVDNIEOWYZE3o6oX+EgMxAhsCcuKkmhkgCYOUCIhOyHBDAGZjk0jyNkgOZya0cMEOTNpEpuZSAYAwus8FYS1LyQAAAAASUVORK5CYII=');
                    width: 16px;
                    height: 16px;
                }
            `;
                return style;
            }
            doRender(object) {
                var _a;
                console.debug("doRender:", object);
                this.clearContainer();
                this.jsPlumbInstance.setSuspendDrawing(true);
                let elementArray = this.bindData[this.elementProperty];
                let elementLoopArgs = this.elementLoop.split(',');
                let elementItemName = elementLoopArgs[0].trim();
                let elementStatusName = (_a = elementLoopArgs[1]) === null || _a === void 0 ? void 0 : _a.trim();
                for (let index = 0; index < elementArray.length; index++) {
                    let elementObject = elementArray[index];
                    let context = Object.assign({}, this.getContext());
                    context[elementItemName] = elementObject;
                    context[elementStatusName] = new duice.ObjectProxy({
                        index: index,
                        count: index + 1,
                        size: elementArray.length,
                        first: (index === 0),
                        last: (elementArray.length == index + 1)
                    });
                    this.createElementItem(elementObject, context, index);
                }
                let linkArray = this.bindData[this.linkProperty];
                for (let index = 0; index < linkArray.length; index++) {
                    this.createLinkItem(linkArray[index]);
                }
                this.jsPlumbInstance.setSuspendDrawing(false, true);
                this.fitContainerToContent();
            }
            doUpdate(object) {
                console.debug("doUpdate:", object);
                this.doRender(object);
            }
            clearContainer() {
                this.container.innerHTML = '';
                this.elementItems.clear();
                this.sourceEndpoints.clear();
                this.targetEndpoints.clear();
                this.connections.length = 0;
            }
            fitContainerToContent() {
                let maxWidth = 0;
                let maxHeight = 0;
                this.container.querySelectorAll(`.${duice.getNamespace()}-workflow-element`)
                    .forEach(element => {
                    let rect = element.getBoundingClientRect();
                    let width = parseInt(element.style.left) + rect.width;
                    if (width > maxWidth) {
                        maxWidth = width;
                    }
                    let height = parseInt(element.style.top) + rect.height;
                    if (height > maxHeight) {
                        maxHeight = height;
                    }
                });
                maxWidth += 10;
                maxHeight += 10;
                this.container.style.width = maxWidth + 'px';
                this.container.style.height = maxHeight + 'px';
                let rect = this.getHtmlElement().parentElement.getBoundingClientRect();
                if (maxWidth < rect.width) {
                    this.container.style.width = rect.width + 'px';
                }
                if (maxHeight < rect.height) {
                    this.container.style.height = rect.height + 'px';
                }
            }
            createElementItem(elementObject, context, index) {
                console.debug("createElementItem", elementObject, context, index);
                let elementId = elementObject[this.elementIdProperty];
                let elementItem = document.createElement('div');
                elementItem.innerHTML = this.htmlElementTemplate;
                duice.initialize(elementItem, context, index);
                duice.setElementAttribute(elementItem, 'element-id', elementId);
                elementItem.classList.add(duice.getNamespace() + '-workflow-element');
                elementItem.style.position = 'absolute';
                elementItem.style.left = (elementObject[this.elementPositionXProperty] || 10) + 'px';
                elementItem.style.top = (elementObject[this.elementPositionYProperty] || 10) + 'px';
                this.elementItems.set(elementId, elementItem);
                this.container.appendChild(elementItem);
                if (this.isEditable()) {
                    elementItem.style.cursor = 'move';
                    this.jsPlumbInstance.draggable(elementItem, {
                        stop: event => {
                            console.debug(event.finalPos);
                            elementObject[this.elementPositionXProperty] = event.finalPos[0];
                            elementObject[this.elementPositionYProperty] = event.finalPos[1];
                            this.fitContainerToContent();
                        }
                    });
                }
                let sourceEndpoint = this.jsPlumbInstance.addEndpoint(elementItem, {
                    isSource: this.isEditable(),
                    isTarget: false,
                    maxConnections: -1,
                    anchor: ['Bottom', 'Right'],
                    endpoint: ['Rectangle', { width: 15, height: 15 }],
                    connector: 'Straight',
                });
                this.sourceEndpoints.set(elementId, sourceEndpoint);
                let targetEndpoint = this.jsPlumbInstance.addEndpoint(elementItem, {
                    isTarget: this.isEditable(),
                    isSource: false,
                    maxConnections: -1,
                    anchor: ['Top', 'Left'],
                    endpoint: ['Dot', { radius: 8 }],
                });
                this.targetEndpoints.set(elementId, targetEndpoint);
            }
            createLinkItem(linkObject) {
                let linkSourceId = linkObject[this.linkSourceProperty];
                let linkTargetId = linkObject[this.linkTargetProperty];
                let connection = this.jsPlumbInstance.connect({
                    source: this.sourceEndpoints.get(linkSourceId),
                    target: this.targetEndpoints.get(linkTargetId),
                    connector: 'Straight',
                    anchor: "AutoDefault",
                    detachable: this.isEditable(),
                    overlays: [
                        ["PlainArrow", {
                                location: 1,
                                width: 15,
                                length: 15,
                                id: "arrow"
                            }],
                        ['Label', {
                                label: `<span class="${duice.getNamespace() + '-workflow-link-disconnect'}"></span>`,
                                location: 0.5,
                                events: {
                                    click: (labelOverlay, originalEvent) => {
                                        if (this.isEditable()) {
                                            this.jsPlumbInstance.deleteConnection(connection);
                                        }
                                    }
                                }
                            }]
                    ]
                });
                this.connections.push(connection);
            }
            addLinkData(linkSourceId, linkTargetId) {
                console.debug("addLinkData", linkSourceId, linkTargetId);
                let linkArray = this.bindData[this.linkProperty];
                let index = linkArray.findIndex(linkObject => {
                    return linkObject[this.linkSourceProperty] === linkSourceId
                        && linkObject[this.linkTargetProperty] === linkTargetId;
                });
                if (index < 0) {
                    let linkData = {};
                    linkData[this.linkSourceProperty] = linkSourceId;
                    linkData[this.linkTargetProperty] = linkTargetId;
                    linkArray.push(linkData);
                }
            }
            removeLinkData(linkSourceId, linkTargetId) {
                console.debug("removeLinkData", linkSourceId, linkTargetId);
                let linkArray = this.bindData[this.linkProperty];
                let indexToRemove = linkArray.findIndex(linkObject => {
                    return linkObject[this.linkSourceProperty] === linkSourceId
                        && linkObject[this.linkTargetProperty] === linkTargetId;
                });
                console.debug("== indexToRemove:", indexToRemove);
                if (indexToRemove > -1) {
                    linkArray.splice(indexToRemove, 1);
                }
            }
            isEditable() {
                return !duice.ObjectProxy.isReadonlyAll(this.bindData)
                    && !duice.ObjectProxy.isDisableAll(this.bindData);
            }
        }
        extension.Workflow = Workflow;
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class WorkflowFactory extends duice.CustomElementFactory {
            doCreateElement(htmlElement, bindData, context) {
                return new extension.Workflow(htmlElement, bindData, context);
            }
        }
        extension.WorkflowFactory = WorkflowFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-workflow`, new WorkflowFactory());
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
/// <reference path="../node_modules/duice/dist/duice.d.ts" />
//# sourceMappingURL=duice-extension.js.map