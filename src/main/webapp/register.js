var usernameErrorMsg;
var	passwordErrorMsg;

$(document).ready(function() {
    document.querySelector("#inner").addEventListener("submit", function(event) {
        event.preventDefault();

        let username = document.querySelector("#username").value;
        let password = document.querySelector("#password").value;
        let confirmpassword = document.querySelector("#confirmpassword").value;

        // validate returns a boolean, and modifies the global var errorMsg
        let paramsAreValid = validate(username, password, confirmpassword);

		if(paramsAreValid) {
			let params = {
	            username,
	            password,
	            confirmpassword
	        };

	        $.post("api/register", $.param(params), function(response) {
	        	console.log(response);
	            if(response == "Success") {
	                handleSuccess();
	            }
	            else {
	                handleFailure(response);
	            }
	        });
		}
		else {
			displayErrors();
		}

    })
});
function handleSuccess() {
	//subtract "signup" from the url
    let url = window.location.origin;
    window.location.href = url + "/login.jsp";
}

function handleFailure(errorText) {
	usernameErrorMsg = errorText;
    passwordErrorMsg = "";
    displayErrors();
}

function displayErrors() {
	let usernameError = document.querySelector("#usernameError");
	usernameError.innerHTML = usernameErrorMsg;

	let passwordError = document.querySelector("#passwordError");
	passwordError.innerHTML = passwordErrorMsg;
}

function validate(username, password, confirmpassword){
	let errorFound = true;

	usernameErrorMsg = "";
	passwordErrorMsg = "";


	if(username.length < 4) {
		usernameErrorMsg = "Enter a username with at least 4 characters";
	}
	else if(username.length > 20) {
		usernameErrorMsg = "Enter a username with 20 or fewer characters";
	}
	else if(password.length < 8) {
		passwordErrorMsg = "Enter a password with 8 or more characters";
	}
	else if(password.length > 20) {
		passwordErrorMsg = "Enter a password with 20 or fewer characters";
	}
	else if(password != confirmpassword) {
		passwordErrorMsg = "Entered passwords do not match";
	}
	else {
		errorFound = false;
	}

	if(errorFound) {
		displayErrors();
		return false;
	}
	return true;
}
