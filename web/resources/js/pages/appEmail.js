/*
 *  Document   : appEmail.js
 *  Author     : pixelcave
 *  Description: Custom javascript code used in Email Center page
 */

var AppEmail = function() {

    return {
        init: function() {
            // Choose one of the highlight classes for the message list rows: 'active', 'success', 'warning', 'danger'
            var rowHighlightClass = 'warning';

            /* Add/Remove row highlighting on checkbox click */
            $('tbody input:checkbox').click(function() {
                var checkedStatus   = $(this).prop('checked');
                var tableRow        = $(this).closest('tr');

                if (checkedStatus) {
                    tableRow.addClass(rowHighlightClass);
                } else {
                    tableRow.removeClass(rowHighlightClass);
                }
            });

            /* Show/Hide Message view - Just for preview */
            var inboxList = $('#message-list');
            var inboxView = $('#message-view');
            var inboxComposite=$('#page-content-sidebar');
            var inboxPageComposite=$('#message-composite');

            inboxList.find('h4 > a').on('click', function(){
                inboxList
                    .removeClass('animation-fadeInQuick2Inv')
                    .addClass('display-none');

                inboxView
                    .removeClass('display-none')
                    .addClass('animation-fadeInQuick2');
            });

            inboxView.find('#message-view-back').on('click', function(){
                inboxView
                    .removeClass('animation-fadeInQuick2')
                    .addClass('display-none');

                inboxList
                    .removeClass('display-none')
                    .addClass('animation-fadeInQuick2Inv');
            });
            
            inboxPageComposite.find('#message-view-back-From-Composite').on('click', function(){
                inboxPageComposite
                    .removeClass('animation-fadeInQuick2')
                    .addClass('display-none');

                inboxList
                    .removeClass('display-none')
                    .addClass('animation-fadeInQuick2Inv');
            });
            
             inboxComposite.find('#btnInboxComposite').on('click', function(){
                inboxView
                    .removeClass('animation-fadeInQuick2')
                    .addClass('display-none');
            
               inboxList
                    .removeClass('animation-fadeInQuick2Inv')
                    .addClass('display-none');

                inboxPageComposite
                    .removeClass('display-none')
                    .addClass('animation-fadeInQuick2Inv');
            });
            
            $('#BR').click(function () {
                    $('#message-view-back-From-Composite').click();
                    $('#message-view-back').click();
                });
        }
    };
}();