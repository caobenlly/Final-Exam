function login() {
    // Get username & password
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var token = "";
    let newReply = {
        token : token
    };
    console.log("username",username);
    console.log("password",password);

    // validate
    if (!username) {
        showNameErrorMessage("Please input username!");
        return;
    }

    if (!password) {
        showNameErrorMessage("Please input password!");
        return;
    }


    // validate username 6 -> 30 characters
    if (username.length < 3 || username.length > 50 || password.length < 6 || password.length > 50) {
        // show error message
        showNameErrorMessage("Login fail!");
        return;
    }

    // Call API
    $.ajax({
        url: 'http://localhost:8080/api/v1/user/login?username='+  username + '&password=' + password,
        type: 'POST',
        contentType: "application/json",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType: 'json', // datatype return
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader("Authorization", "Bearer " + btoa(username + ":" + password));
        // },
        success: function (data, textStatus, xhr) {
            console.log("data: " , data)
            // save data to storage
            // https://www.w3schools.com/html/html5_webstorage.asp
            localStorage.setItem("token", data.token);
            localStorage.setItem("role", data.role);
            // redirect to home page
            // https://www.w3schools.com/howto/howto_js_redirect_webpage.asp
            location.href = "program.html"
        },
        error(jqXHR, textStatus, errorThrown) {
            confirm(jqXHR.responseJSON.error)
            if (jqXHR.status == 401) {
                showNameErrorMessage("Login fail!");
            } else {
                console.log(jqXHR.responseJSON.error);
            }
        }
    });
}

function showNameErrorMessage(message) {
    document.getElementById("nameErrorMessage").style.display = "block";
    document.getElementById("nameErrorMessage").innerHTML = message;
}

function hideNameErrorMessage() {
    document.getElementById("nameErrorMessage").style.display = "none";
}

///////////////////////////
$('document').ready(function(){
    $('input[type="text"], input[type="email"], textarea').focus(function(){
        var background = $(this).attr('id');
        $('#' + background + '-form').addClass('formgroup-active');
        $('#' + background + '-form').removeClass('formgroup-error');
    });
    $('input[type="text"], input[type="email"], textarea').blur(function(){
        var background = $(this).attr('id');
        $('#' + background + '-form').removeClass('formgroup-active');
    });

    function errorfield(field){
        $(field).addClass('formgroup-error');
        console.log(field);
    }

    // $("#waterform").submit(function() {
    //     var stopsubmit = false;
    //
    //     if($('#name').val() == "") {
    //         errorfield('#name-form');
    //         stopsubmit=true;
    //     }
    //     if($('#email').val() == "") {
    //         errorfield('#email-form');
    //         stopsubmit=true;
    //     }
    //     if(stopsubmit) return false;
    // });

});