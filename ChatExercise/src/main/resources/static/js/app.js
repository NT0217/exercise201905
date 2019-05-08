var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (message) {
            showMessage(JSON.parse(message["body"]), frame);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/message", {}, JSON.stringify({'body' : $("#body").val()}));
}

function showMessage(message, frame) {
	if (message.username == viewusername) {
	    $("#message").append(
	    		'<div class="outgoing_msg">'+
	    		'<div class="sent_msg">'+
	    		'<div class="message__username">' + message.username + '</div>'+
	    		'<div class="message__text"><p>' + message.body + '</p></div>'+
	    		'<div class="message__timestamp">' + message.createdAt + '</div>'+
	    		'</div>'+
	    		'</div>');
	} else {
	    $("#message").append(
	    		'<div class="incoming_msg">'+
	    		'<div class="incoming_msg_img"> <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>'+
	    		'<div class="received_msg">'+
	    		'<div class="received_withd_msg">'+
	    		'<div class="message__username">' + message.username + '</div>'+
	    		'<div class="message__text"><p>' + message.body + '</p></div>'+
	    		'<div class="message__timestamp">' + message.createdAt + '</div>'+
	    		'</div>'+
	    		'</div>'+
	    		'</div>');
	}
	
	$("#body").val('');
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});