/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function startWaitMe(element) {
    if (element === undefined)
        element = '.main-container';
    $(element).waitMe({
        effect: 'timer',
        text: 'Traitement encours...',
        bg: 'rgba(255,255,255,0.7)',
        color: '#000',
        sizeW: '',
        sizeH: ''
    });
}

function stopWaitMe(element) {
    if (element === undefined)
        element = '.main-container';
    $(element).waitMe('hide');
}

 function initialiserParDefaut(options) {
    try {
        if (!options || options.chosen)
            $('.select-chosen').chosen({allow_single_deselect: true, width: "100%"});
    } catch (err) {
    }



    $('.input-datepicker').datepicker({
        language: 'fr',
        autoclose: true,
        todayHighlight: true,
        clearBtn: true
    });



    if (!options || options.modaldraggable)
        try {
            $('.modal.draggable').draggable({
                handle: ".modal-header"
            });
        } catch (ex) {
        }


    if (!options || options.select2)
        try {
            $('.select2').select2();
        } catch (ex) {
        }

    if (!options || options.datatable)
        try {
            $('.data-table').dataTable({
                "language": {
                    "url": "/forsetiPenal/resources/js/French.json"
                }
            });
        } catch (ex) {
        }

    if (!options || options.fileInput)
        try {
            $('.file-input').ace_file_input({
                no_file: 'Aucun fichier ...',
                btn_choose: 'Choisir',
                btn_change: 'Changer',
                droppable: false,
                onchange: null,
                thumbnail: false //| true | large
                        //whitelist:'gif|png|jpg|jpeg'
                        //blacklist:'exe|php'
                        //onchange:''
                        //
            });
        } catch (ex) {
        }
    try {
        var observer = new MutationObserver(function (mutations) {
            // For the sake of...observation...let's output the mutation to console to see how this all works
            mutations.forEach(function (mutation) {
                 
                console.log(mutation.type);

                if ($.trim($('.ui-messages-info-detail').text()) !== '' && !$('.ui-messages-info-detail').hasClass('shown')) {
                    var title = $('.ui-messages-info-summary').text();
                    var message = $('.ui-messages-info-detail').text();
//                    toastr['success'](message, title);

//                    var growlType = $(this).data('growl');
                    $.bootstrapGrowl('<h4><strong>' + title + '</strong></h4> <p>' + message + '</p>', {
                        type: 'success',
                        delay: 3000,
                        allow_dismiss: true,
                        offset: {from: 'top', amount: 20}
                    });

                    $(this).prop('disabled', true);
                    $('.ui-messages-info-detail').addClass('shown');
                }
                if ($.trim($('.ui-messages-error-detail').text()) !== '' && !$('.ui-messages-error-detail').hasClass('shown')) {
                    var title = $('.ui-messages-error-summary').text();
                    var message = $('.ui-messages-error-detail').text();
//                    toastr['error'](message, title);
                    $.bootstrapGrowl('<h4><strong>' + title + '</strong></h4> <p>' + message + '</p>', {
                        type: 'danger',
                        delay: 3000,
                        allow_dismiss: true,
                        offset: {from: 'top', amount: 20}
                    });

                    $(this).prop('disabled', true);
                    $('.ui-messages-error-detail').addClass('shown');
                }
            });
        });

        // Notify me of everything!
        var observerConfig = {
            attributes: true,
            childList: true,
            characterData: true
        };

        // Node, config
        // In this case we'll listen to all changes to body and child nodes
        var targetNode = $('#msgForm')[0]; //   document.body;
        observer.observe(targetNode, observerConfig);
    } catch (ex) {
    }
}
// jQuery(function ($) {
function initialiserChosenSelect() {
    $('.select-chosen').chosen({allow_single_deselect: true, width: "100%"});
}

function disablePastDate(idDate) {
//    $("#datepicker").datepicker({ minDate: 0 });
    alert(idDate);
    $("idDate").datepicker({
        minDate: 0});
}

function disablePastDateBoundDate(idDate, minDate) {
    $(idDate).datepicker("option", minDate, 0);
//    $("#datePicker").datepicker("option", "minDate", 0);
}


