/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var ComboSelect = function (test, choix, valeur, id_a_afficher) {
// Indiquer si l'on teste une égalité ou une différence 'egal' / 'diff'. exemple : creation/ modif/ supp --> egal 'creation' alors affichage OU pomme / poire/ autre --> si différent de 'autre' alors affichage
// par défault indiquer à la place de choix this.value pour prendre la valeur de la liste
    return {
        init: function (test, choix, valeur, id_a_afficher) {
            $(function () {
                if (test === 'egal') {
                    if (choix === valeur) { //Si la sélection dans la liste est égale à la valeur indiquée dans les paramètres
                        //On supprime la classe hidden, on ajoute la classe display, on supprime l'attribut disabled, on cache sans délais afin que l'utilisateur ne voit pas que l'on vient de le passer en display et on fait l'animation d'affichage
                        $('#' + id_a_afficher).prev().removeClass('hidden');
                        $('#' + id_a_afficher).removeClass('hidden').addClass('display').removeAttr('disabled').hide().show(500);

                    } else {
                        // On donne l'attribut disabled afin qu'il ne soit pas enregistré et on fait l'animation pour le cacher
                        $('#' + id_a_afficher).prev().removeClass('display').addClass('hidden');
                        $('#' + id_a_afficher).attr('disabled', 'disabled').hide(500);
                    }
                } else if (test === 'diff') {
                    if (choix !== valeur) { //Si la sélection dans la liste est égale à la valeur indiquée dans les paramètres
                        //On supprime la classe hidden, on ajoute la classe display, on supprime l'attribut disabled, on cache sans délais afin que l'utilisateur ne voit pas que l'on vient de le passer en display et on fait l'animation d'affichage
                        $('#' + id_a_afficher).prev().removeClass('hidden');
                        $('#' + id_a_afficher).removeClass('hidden').addClass('display').removeAttr('disabled').hide().show(500);

                    } else {
                        // On donne l'attribut disabled afin qu'il ne soit pas enregistré et on fait l'animation pour le cacher
                        $('#' + id_a_afficher).prev().removeClass('display').addClass('hidden');
                        $('#' + id_a_afficher).attr('disabled', 'disabled').hide(500);
                    }
                } else {
                    alert("Le test d'égalité n'est pas correct !");
                }
            });
        }
    };

};