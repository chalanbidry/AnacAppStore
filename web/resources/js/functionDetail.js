/*
 *  Document   : app.js
 *  Author     : pixelcave
 *  Description: Custom scripts and plugin initializations (available to all pages)
 *
 *  Feel free to remove the plugin initilizations from uiInit() if you would like to
 *  use them only in specific pages. Also, if you remove a js plugin you won't use, make
 *  sure to remove its initialization from uiInit().
 */
function showDetail(data) {
    if (data.status === "begin") {
//                         alert("commence");
    }
    if (data.status === "complete") {
//                        alert("complete"); 
    }
    if (data.status === "success") {
        try{
        UiTables.init('#table-dossier-audiencer-enCour', 1);
        UiTables.init('#dynamic-table-event-EnCour', 5);
         }catch (err){}
    }
}

function AfficheDetail() {
    $('#detatilsDossierDialog').modal('show');
    initialiserChosenSelect();
}