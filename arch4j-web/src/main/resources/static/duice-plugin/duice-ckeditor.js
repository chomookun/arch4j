/*!
 * duice-ckeditor - v0.3.1
 * git: https://gitbub.com/chomookun/duice-plugin
 * website: https://duice-plugin.chomookun.org
 * Released under the LGPL(GNU Lesser General Public License version 3) License
 */
this.duice = this.duice || {};
this.duice.plugin = this.duice.plugin || {};
this.duice.plugin.Ckeditor = (function (exports, duice) {
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
     * Ckeditor Element
     */
    class CkeditorElement extends duice.ObjectElement {
        /**
         * Constructor
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            this.editor = null; // CKEditor 인스턴스
            this.internalValue = null; // 초기 setValue 저장
            this.getHtmlElement().appendChild(this.createStyle());
            // textarea
            const textarea = document.createElement('textarea');
            // textarea.style.height = this.getHtmlElement().style.height;
            console.error("===========", this.getHtmlElement().style.height);
            htmlElement.appendChild(textarea);
            ClassicEditor
                .create(textarea, {
                extraPlugins: [DefaultUploadPlugin]
            })
                .then((editor) => {
                this.editor = editor;
                // set initial value
                if (this.internalValue !== null) {
                    this.editor.setData(this.internalValue);
                }
                // blur event
                editor.editing.view.document.on('blur', (evt, data) => {
                    if (editor.getData() !== this.internalValue) {
                        this.internalValue = editor.getData();
                        this.notifyPropertyChanged();
                    }
                });
                // double check
                editor.ui.view.editable.element.addEventListener('blur', () => {
                    if (editor.getData() !== this.internalValue) {
                        this.internalValue = editor.getData();
                        this.notifyPropertyChanged();
                    }
                });
            })
                .catch((error) => {
                console.error('Error initializing CKEditor:', error);
            });
        }
        /**
         * Creates style
         */
        createStyle() {
            let style = document.createElement('style');
            style.innerHTML = `
            .ck-editor {
                display: flex;
                flex-direction: column;
                height: 100% !important;
            }
            .ck-editor__main {
                flex: 1;
            }
            .ck-editor__editable_inline:not(.ck-comment__input *) {
                height: 100%;
            }
        `;
            return style;
        }
        /**
         * Notifies property changed
         */
        notifyPropertyChanged() {
            let element = this.getHtmlElement();
            let data = this.getBindData();
            let event = new duice.PropertyChangingEvent(element, data, this.getProperty(), this.getValue(), this.getIndex());
            this.notifyObservers(event);
        }
        /**
         * Sets value
         * @param value property value
         */
        setValue(value) {
            this.internalValue = value;
            if (this.editor) {
                this.editor.setData(value !== null && value !== void 0 ? value : '');
            }
        }
        /**
         * Gets value
         */
        getValue() {
            return this.internalValue;
        }
        /**
         * Sets readonly
         * @param readonly readonly or not
         */
        setReadonly(readonly) {
            if (this.editor) {
                if (readonly === true) {
                    this.editor.enableReadOnlyMode('readonly');
                }
                else {
                    this.editor.disableReadOnlyMode('readonly');
                }
            }
        }
        /**
         * Sets disabled
         * @param disabled disabled or not
         */
        setDisabled(disabled) {
            if (this.editor) {
                if (disabled === true) {
                    this.editor.enableReadOnlyMode('disabled');
                }
                else {
                    this.editor.disableReadOnlyMode('disabled');
                }
            }
        }
    }
    /**
     * Default Upload Plugin
     */
    class DefaultUploadPlugin {
        constructor(editor) {
            editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
                console.log("== loader", loader);
                return new DefaultUploadAdapter(loader);
            };
        }
    }
    /**
     * Default Upload Adapter
     */
    class DefaultUploadAdapter {
        constructor(loader) {
            this.loader = loader;
        }
        upload() {
            return __awaiter(this, void 0, void 0, function* () {
                return this.loader.file
                    .then((file) => new Promise((resolve, reject) => {
                    const reader = new FileReader();
                    reader.onload = () => {
                        resolve({
                            default: reader.result
                        });
                    };
                    reader.onerror = error => {
                        reject(error);
                    };
                    reader.readAsDataURL(file); // 🔥 Base64
                }));
            });
        }
    }

    /**
     * Ckeditor Factory
     */
    class CkeditorElementFactory extends duice.ObjectElementFactory {
        /**
         * Creates element
         * @param htmlElement html element
         * @param bindData bind data
         * @param context context
         */
        createElement(htmlElement, bindData, context) {
            return new CkeditorElement(htmlElement, bindData, context);
        }
    }
    /**
     * Static block
     */
    (() => {
        // register
        duice.ElementRegistry.register(`${duice.Configuration.getNamespace()}-ckeditor`, new CkeditorElementFactory());
    })();

    exports.CkeditorElement = CkeditorElement;
    exports.CkeditorElementFactory = CkeditorElementFactory;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, duice);
//# sourceMappingURL=duice-ckeditor.js.map
