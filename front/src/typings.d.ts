/* SystemJS module definition */
declare var module: NodeModule;
declare module 'pdfmake/build/pdfmake.js';
declare module 'pdfmake/build/vfs_fonts.js';
interface NodeModule {
  id: string;
}
