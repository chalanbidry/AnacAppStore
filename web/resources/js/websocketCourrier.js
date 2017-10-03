/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/*
 * établit la connection websocket avec le serveur.
 */
var serverUrl;
//var username;
var webSocket;
var serverId = location.hostname;


//function onMessage(event) {
//    var push = eval("(" + event.data + ")");
//    display(push);
//}
//
function onOpen(event) {
    console.log("yeah open session " + event.data);

}

function onError(event) {
    console.log('Yo we got error!' + event.data);
}
//
//function display(push) {
//    console.log('recu :' + push.expediteur + '->' + push.destinataire + ': ' + push.texte);
//    alert("recu\nde :" + push.expediteur + "\nmessage :" + push.texte);
//}

function sendMessage() {
    var formeMessage = {};
    formeMessage[0] = userDestine.value;
    formeMessage[1] = messageText.value;
    websocket.send(JSON.stringify(formeMessage));
    messageText.value = "";

}
function sendUserName() {
    websocket.send(userName.value);
}


//function webSocketStart() {
//username = $('#userField').attr("value");
console.log('will start web socket session');
serverUrl = 'wss://' + serverId + ':8181/AnacAppStore/notificationCourrierEndPoint';
//    if (webSocket !== undefined && webSocket.readyState !== webSocket.CLOSED) {
//        console.log("WebSocket is already opened.");
//        return;
//    }
//serverUrl = $('#urlField').attr("value");
webSocket = new WebSocket(serverUrl);

webSocket.onerror = function (event) {
    onError(event);
};

webSocket.onopen = function (event) {
    onOpen(event);
};

webSocket.onmessage = function processMessage(message) {
//location.reload()
    var jsonData = JSON.parse(message.data);
    if (jsonData.hasOwnProperty("message")) {
        var evt = document.createEvent("MouseEvents");
        evt.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
        document.getElementById("btnrefreshAllboard").dispatchEvent(evt);
//        $("#msgFormChefDir\\:btnrefreshAllboard").click();
        notif(jsonData.message);

        if (jsonData.hasOwnProperty("notif")) {
            $("#nbNotif").text(parseInt($("#nbNotif").text()) + 1);
            $("#nbNotif2").text($("#nbNotif").text());
        }
        if (jsonData.hasOwnProperty("correctionChat")) {
            $("#nbAlert").text(parseInt($("#nbAlert").text()) + 1);
            $("#nbAlert2").text($("#nbAlert").text());
        }
//            reload();
    } else {

        if (jsonData.hasOwnProperty("IsChat")) {

            $("#nbSocial").text(parseInt($("#nbSocial").text()) + 1);
        }

    }
};
//}
function reload() {
    document.getElementById("alertes").contentWindow.location.reload(true);
    alert("rreload ok");
//    var container = document.getElementById("alertes");
//    var content = container.innerHTML;
//    container.innerHTML = content;
}






function notif(message) {
    // Check permissions
    if ("Notification" in window) {
        var permission = Notification.permission;
        if (permission === "denied") {
            return;
        } else if (permission === "granted") {
            return checkVersion(message);
        }

        Notification.requestPermission().then(function () {
            checkVersion(message);
        });
    }
}

function notifUserConnect(message, loginSocial) {
    // Check permissions
    if ("Notification" in window) {
        var permission = Notification.permission;
        if (permission === "denied") {
            return;
        } else if (permission === "granted") {
            return checkVersionUserConnect(message, loginSocial);
        }

        Notification.requestPermission().then(function () {
            checkVersionUserConnect(message, loginSocial);
        });
    }
}

function checkVersion(messageShow) {
    // Retrieve current version
    var latestVersion = "3.9.5";
    var currentVersion = localStorage.getItem("conciseVersion");
    displayNotification(
            messageShow,
            "https://s3-us-west-2.amazonaws.com/s.cdpn.io/123941/concise-logo.png",
            "PrestAnac"

            );
    localStorage.setItem("conciseVersion", latestVersion);
}

function checkVersionUserConnect(messageShow, loginSocial) {
    // Retrieve current version
    var latestVersion = "3.9.5";
    var currentVersion = localStorage.getItem("conciseVersion");
    displayNotification(
            messageShow,
            "https://www.iconfinder.com/icons/172626/male_user_icon",
            "PrestAnac"

            );
    localStorage.setItem("conciseVersion", latestVersion);
}

function displayNotification(body, icon, title, link, duration) {
    link = link || 0; // Link is optional
    duration = duration || 15000; // Default duration is 5 seconds

    var options = {
        body: body,
        icon: icon
       

    };
    var n = new Notification(title, options);
    if (link) {
        n.onclick = function () {
            window.open(link);
        };
    }

    setTimeout(n.close.bind(n), duration);
}



  