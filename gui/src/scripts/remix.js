// ReMix.js

let URL = "http://aarons-macbook.local:9000"

$.post_JSON = function(url, data, callback, error) {
    $.ajax({
        type: 'POST',
        url: url,
        data: data,
        success: callback,
        contentType: "application/json",
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        }
    }).fail(error)
}

$.get_JSON = function(url, callback, error) {
    $.ajax({
        type: 'GET',
        url: url,
        success: callback,
        contentType: "application/json",
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        }
    }).fail(error)
}

function makeRequest() {
    let retfun = function(data) {
        console.log(data)
        $("#json_viewer").text(JSON.stringify(data, null, 2))
    }
    let error = function(r) {
        console.log(r.responseText)
        let reason = JSON.parse(r.responseText).reason
        $("#error-message-box").text(reason)
        $("#errorbox").modal('show')
        $("#json_viewer").text(r.responseText)
    }
    if ($("#json_editor").val().replace(/ /g,'') === "") {
        console.log("Making GET request to " + URL + $("#url_editor").val())
        $.get_JSON(URL + $("#url_editor").val(), retfun, error)
    } else {
        console.log("Making POST request to " + URL + $("#url_editor").val())
        let data = $("#json_editor").val()
        console.log(data)
        $.post_JSON(URL + $("#url_editor").val(), data, retfun, error);
    }
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
