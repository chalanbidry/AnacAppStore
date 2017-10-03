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
var userChating;
var video = $("#live").get()[0];
var canvas = $("#canvas");
var ctx = canvas.get()[0].getContext('2d');
var options = {
    "video": true
};



function sendMessage() {
    var $chatComponent = $('#ulChat');
    var $avatar = '#{request.contextPath}/resources/img/placeholders/avatars/avatar9.jpg';
    var $messageLine = $(
            '<li class="chatui-talk-msg chatui-talk-msg-right animation-expandUp themed-background text-light">' +
            ChatText.value +
            '</li>'
            );
    $chatComponent.append($messageLine);
//    var formeMessage = {};
//    formeMessage[0] = userDesti.value;
//    formeMessage[1] = ChatText.value;
//    webSocket.send(JSON.stringify(formeMessage));
//    ChatText.value = "";
}
function sendUserName() {
    websocket.send(userName.value);
}


//username = $('#userField').attr("value");
console.log('will start web socket session');
serverUrl = 'wss://' + serverId + ':8181/AnacAppStore/notificationEndPoint';
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

    var $chatComponent = $('#ulChat');

    var $messageLine;
//    var $newUser = document.getElementById("UserInc");
    var jsonData = JSON.parse(message.data);
//    var valuConnecte = document.getElementById("UserInc").innerHTML;

    if (jsonData.hasOwnProperty("IsChat")) {
        $("#MsgInc").text(parseInt($("#MsgInc").text()) + 1);

       
          
            $messageLine = $(
                    '<li class="chatui-talk-msg animation-expandUp">' +
                    jsonData.Chat +
                    '</li>'
                    );
            $chatComponent.append($messageLine);
        

    }

//
//        var target = document.getElementById("target");
//         alert(message.data);
//      var url = window.webkitURL.createObjectURL(message.data);
//        alert("venu");
//        target.onload = function () {
//            window.webkitURL.revokeObjectURL(url);
//        };
//        target.src = url;
//      
//   


};


$('#btnConnect').click(function () {
    userChating = document.getElementById('spamLoginConnect').innerHTML;

});
$('#btnDeconnect').click(function () {
    userChating = document.getElementById('spamLoginDeconnect').innerHTML;
   
});
$('#MsgInc').click(function () {
    $("#MsgInc").text("0");

});

function initialise() {
    $("#UserInc").text("0");

}

//function f1() {
//   var test=TrayIconDemo.testPass();
////    var test = controller.SocialBean.testPass();
//    alert(test);
//}



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

function checkVersion(messageShow) {
    // Retrieve current version
    var latestVersion = "3.9.5";
    var currentVersion = localStorage.getItem("conciseVersion");
    displayNotification(
            messageShow,
            "https://s3-us-west-2.amazonaws.com/s.cdpn.io/123941/concise-logo.png",
            "PrestAnac",
            '#'
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






// use the chrome specific GetUserMedia function
// pour faire afficher la camera


//navigator.webkitGetUserMedia(options, function (stream) {
//    video.src = webkitURL.createObjectURL(stream);
//}, function (err) {
//    console.log("Unable to get video stream!");
//});

//timer = setInterval(
//        function () {
//            ctx.drawImage(video, 0, 0, 320, 240);
//            var data = canvas.get()[0].toDataURL('image/jpeg', 1.0);
//            newblob = convertToBinary(data);
//            webSocket.send(newblob);
//
//        }, 250);

function convertToBinary(dataURI) {

    // convert base64 to raw binary data held in a string
    // doesn't handle URLEncoded DataURIs
    var byteString = atob(dataURI.split(',')[1]);

    // separate out the mime component
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]

    // write the bytes of the string to an ArrayBuffer
    var ab = new ArrayBuffer(byteString.length);
    var ia = new Uint8Array(ab);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    // write the ArrayBuffer to a blob, and you're done
    var bb = new Blob([ab]);
    return bb;
}


function createObjectURL(file) {
    if (window.webkitURL) {
        return window.webkitURL.createObjectURL(file);
    } else if (window.URL && window.URL.createObjectURL) {
        return window.URL.createObjectURL(file);
    } else {
        return null;
    }
}