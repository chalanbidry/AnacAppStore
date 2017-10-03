/**
 * 
 * @param {type} xhr
 * @param {type} status
 * @param {type} args
 * @param {type} dialog
 * @returns {undefined}
 */
function handleLoginRequest(xhr, status, args, dialog) {
    //console.info($('#editForm'));
    //var jqDialog = jQuery('#'+dialog.id);
    var element = $(dialog);
    if (args.validationFailed) {
        //element.jq.effect('shake', {times: 3}, 100);
        element.effect('shake', 50);
    } else {
        element.modal('hide');
    }
}

function handleLoginRequestOnJsf(xhr, dialog) {
    //console.log(xhr); //while ,data.status can be 1)"begin" 2)"complete" 3)"success"
    
    if(xhr.status === 'complete')
    {
        updateGritterOnJsf(xhr, dialog);
    }
        
    
    /*if (args.validationFailed) {
        //element.jq.effect('shake', {times: 3}, 100);
        element.effect('shake', 50);
    } else {
        element.modal('hide');
    }*/
}

function handleLoginTest(xhr, status, args) {
    //console.info($('#editForm'));
    //var jqDialog = jQuery('#'+dialog.id);
    if (args.validationFailed) {
        //element.jq.effect('shake', {times: 3}, 100);
        return false;
    } else {
        return true;
    }
}

/**
 * 
 * @param {type} element
 * @param {type} my
 * @param {type} at
 * @param {type} of
 * @returns {undefined}
 * exemple: onShow="adjustPositionning(
 $('[id$=&#34;#{cc.attrs.id}addiolog&#34;]'),
 'left bottom', 'right bottom',
 $('[id$=&#34;#{cc.attrs.id}overlaybutton&#34;]')
 )"
 */
function popoverShow(element, my, at, of) {
    element.position({
        my: my,
        at: at,
        of: of,
        collision: "fit fit"
    });
}

function popoverHide(element) {
    element.removeAttr("style" );
}

function handleChangePasswd(xhr, status, args, dialog) {
    //console.info($('#editForm'));
    //var jqDialog = jQuery('#'+dialog.id);
    if (args.validationFailed) {
        dialog.jq.effect('shake', {times: 3}, 100);
    } else {
        //dialog.hide();
    }
}
function exportChart() {
    var chart = $('#columnchart').highcharts();
    chart.exportChart({
        type: 'application/pdf',
        filename: 'my-pdf'
    });
}
//$(document).ready(function (){
//    $("iframe").$(".jrPage").css("backgroundColor", "rgb(255, 255, 255)!important");
//    $("iframe").$(".jrPage").css("border", "1px solid #cacaca!important");
//});

function refreshForm(id)
{
    PrimeFaces.ajax.AjaxRequest({
        formId: id,
        source: id,
        process: '@none',
        update: id,
        oncomplete: function() {
            
        }
    });
}


function updateGritterOnJsf(xhr, dialog)
{

    var element = $(dialog);
    doc = $(xhr['responseXML']).find('update#msgForm\\:msg');
    doc = $(doc.text());
    doc.find('div[class^="ui-messages-"]').each(function () {

        severity = $(this).attr('class').split(" ")[0].split("-")[2];
        $(this).find('li').each(function () {
            summary = $(this).find('span.ui-messages-' + severity + '-summary').text();
            detail = $(this).find('span.ui-messages-' + severity + '-detail').text();

            if (severity === 'info')
            {
                Notify(summary, detail, 'top-right', '5000', 'success', 'fa-check', true);
                element.modal('hide');
            }
            if (severity === 'warn')
            {
                Notify(summary, detail, 'top-right', '5000', 'warning', 'fa-warning', true);
            }
            if (severity === 'error')
            {
                Notify(summary, detail, 'top-right', '5000', 'darkorange', 'fa-times-circle', true);
                element.effect('shake', 50);
            }
            if (severity === 'fatal')
            {
                Notify(summary, detail, 'top-right', '5000', 'danger', 'fa-ban', true);
                element.effect('shake', 50);
            }
        });
    });
}