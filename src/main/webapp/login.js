var errorMsg;

$(document).ready(function() {
    document.querySelector("#inner").addEventListener("submit", function(event) {
        event.preventDefault();

        let username = document.querySelector("#username").value;
        let password = document.querySelector("#password").value;

		// validate returns a boolean, and modifies the global var errorMsg
        let paramsAreValid = validate(username, password);

		if(paramsAreValid) {
            let params = {
                username,
                password
            };

            $.post("api/login", $.param(params), function(response) {
                if(response == "Success") {
                    handleSuccess();
                }
                else {
                    handleFailure(response);
                }
            });
        }
        else {
        	handleFailure(errorMsg);
        }
    })
});
function handleSuccess() {
	//subtract "login" from the url
    let url = window.location.origin;
    window.location.href = url + "/dashboard.jsp";
}

function handleFailure(errorText) {
    error_tag = document.querySelector("#error");
    error_tag.innerHTML = errorText;
}

function validate(username, password) {
	let msg = "";

	if(username.length < 4) {
		msg = "Enter a username with at least 4 characters";
	}
	else if(username.length > 20) {
		msg = "Enter a username with 20 or fewer characters";
	}
	else if(password.length < 8) {
		msg = "Enter a password with 8 or more characters";
	}
	else if(password.length > 20) {
		msg = "Enter a password with 20 or fewer characters";
	}

	if(msg != ""){
		errorMsg = msg;
		return false;
	}

	return true;
}
