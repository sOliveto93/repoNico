/**
 * Created by jaguilar on 28/04/15.
 */

(function($){
    $.fn.jonyPrinter=function(options){

        var setting=$.extend({
            cssLink:null,
            onAfterPrint:null
        }, options);

        var detectBrowser={
            isOpera:(!!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0),
            isFirefox: (typeof InstallTrigger!== 'undefined'),
            isSafari: (Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0),
            isChrome: (!!window.chrome && !((!!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0))),
            isIE: (false || !!document.documentMode)
        }

        var initialize=function(context){

            if(context.length>1){
                context.each(function(){
                    $(context).jonyPrinter();
                });
            }

            var ventana = window.open();
            ventana.focus();
            ventana.document.open();
            ventana.document.write('<html>');

            if(setting.cssLink!=null){
                ventana.document.write('<head> <link href="'+ setting.cssLink +'" type="text/css" rel="stylesheet"></link> </head>')
            }
            ventana.document.write('<body>');
            ventana.document.write($(context).html());
            ventana.document.write('<'+'/body'+'><'+'/html'+'>');
            ventana.document.close();
            ventana.print();
            ventana.close();


            if (setting.onAfterPrint != null)
                setting.onAfterPrint(this);

            return context;

        }

        return initialize(this);
    }
})(jQuery);