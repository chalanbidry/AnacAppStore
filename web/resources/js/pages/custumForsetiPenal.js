/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function initDataTable(datatableId) {
/* Initialize Datatables */
            $(datatableId).dataTable({
                columnDefs: [ { orderable: false, targets: [ 4 ] } ],
                pageLength: 10,
                lengthMenu: [[5, 10, 20], [5, 10, 20]]
            });
}


