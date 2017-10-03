/*
 *  Document   : uiTables.js
 *  Author     : pixelcave
 *  Description: Custom javascript code used in Tables page
 */

var UiTables = function () {

    return {
        init: function (dataTableID, columnNumber) {
            /* Initialize Bootstrap Datatables Integration */
            App.datatables();
//            alert("id"+dataTableID);
//            console.log(dataTableID);
//            alert("columnNumber"+columnNumber);
//            console.log(columnNumber);
            /* Initialize Datatables */
            $(dataTableID).dataTable({
                columnDefs: [{orderable: false, targets: [columnNumber]}],
                pageLength: 5,
                lengthMenu: [[5, 10, 20], [5, 10, 20]],
                "language": {
                    "lengthMenu": "Afficher _MENU_ enregistrements par page",
                    "zeroRecords": "Aucun enregistrement trouvé",
                    "info": "Affichage page _PAGE_ de _PAGES_",
                    "infoEmpty": "",
                    "infoFiltered": "(filtered from _MAX_ total records)", 
                    "search": "Rechercher:",
                    "processing":"En cours ...",
                    "paginate": {
                        "next": "Suivant",
                        "previous":   "Précédent"
                    }
                }
            });

            /* Add placeholder attribute to the search input */
            $('.dataTables_filter input').attr('placeholder', '');

            /* Select/Deselect all checkboxes in tables */
            $('thead input:checkbox').click(function () {
                var checkedStatus = $(this).prop('checked');
                var table = $(this).closest('table');

                $('tbody input:checkbox', table).each(function () {
                    $(this).prop('checked', checkedStatus);
                });
            });

            /* Table Styles Switcher */
            var genTable = $('#general-table');
            var styleBorders = $('#style-borders');

            $('#style-default').on('click', function () {
                styleBorders.find('.btn').removeClass('active');
                $(this).addClass('active');

                genTable.removeClass('table-bordered').removeClass('table-borderless');
            });

            $('#style-bordered').on('click', function () {
                styleBorders.find('.btn').removeClass('active');
                $(this).addClass('active');

                genTable.removeClass('table-borderless').addClass('table-bordered');
            });

            $('#style-borderless').on('click', function () {
                styleBorders.find('.btn').removeClass('active');
                $(this).addClass('active');

                genTable.removeClass('table-bordered').addClass('table-borderless');
            });

            $('#style-striped').on('click', function () {
                $(this).toggleClass('active');

                if ($(this).hasClass('active')) {
                    genTable.addClass('table-striped');
                } else {
                    genTable.removeClass('table-striped');
                }
            });

            $('#style-condensed').on('click', function () {
                $(this).toggleClass('active');

                if ($(this).hasClass('active')) {
                    genTable.addClass('table-condensed');
                } else {
                    genTable.removeClass('table-condensed');
                }
            });

            $('#style-hover').on('click', function () {
                $(this).toggleClass('active');

                if ($(this).hasClass('active')) {
                    genTable.addClass('table-hover');
                } else {
                    genTable.removeClass('table-hover');
                }
            });
        }
    };
}();

