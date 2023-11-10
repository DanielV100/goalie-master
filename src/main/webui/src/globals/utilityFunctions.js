export function cloneExistingFieldsInContainer(event, elementContainer, elementToClone) {
    const cloneElement = document.getElementById(elementToClone);
    const clonedElement = cloneElement.cloneNode(true);
    const inputs = clonedElement.querySelectorAll('.input');
    inputs.forEach((input) => (input.value = ''));
    document.getElementById(elementContainer).appendChild(clonedElement);
    bindDeleteEventListenerToAllDeleteButtons(event);
}

export function bindDeleteEventListenerToAllDeleteButtons(event) {
    const allDeleteButtons =   document.querySelectorAll('.deleteButton');
    allDeleteButtons.forEach((deleteButton) => {
        deleteButton.addEventListener('click', deleteDeleteButtonsContainer);
    });
}

function deleteDeleteButtonsContainer(event) {
    const parentNode = event.target.parentNode;
    parentNode.innerHTML = '';
}

export function getJwtTokenFromSessionStorage() {
    return sessionStorage.getItem('jwttoken');
}

/**
 * Setting loading circle into submit button.
 * Optical indicator, that http-request is performed.
 */
export function setLoadingCircleInSubmitButton() {
    const submitButton = document.querySelector('.submit');
    submitButton.textContent = '';
    submitButton.classList.add('loading');
}

/**
 * Method for unsetting the loading circle and writing standard text into submit buttons.
 * @param buttonText
 */
export function unsetLoadingCircleInSubmitButton(buttonText) {
    const submitButton = document.querySelector('.submit');
    submitButton.textContent = buttonText;
    submitButton.classList.remove('loading');
}

/**
 * Central method for all kinds of error handling, which shall be seen by the user.
 * Function calls error dialog, sets the submit button text back to standard and return error message.
 * @param error
 * @param submitButtonText
 * @returns {string}
 */
export function errorHandling(error, submitButtonText) {
    showErrorDialog();
    unsetLoadingCircleInSubmitButton(submitButtonText);
    return getErrorMessageFromHttpError(error);
}
function getErrorMessageFromHttpError(error) {
    const errorAsString = String(error);
    if (errorAsString.includes('401')) {
        return 'Benutzername oder Passwort falsch.';
    } else {
        return 'Es ist ein unbekannter Fehler aufgetreten.';
    }
}

function showErrorDialog() {
    const errorDialog =   document.getElementById('errorDialog');
    if(errorDialog !== null) {
        errorDialog.showModal();
    }
}

export function getExercisesFromSessionStorage(category) {
    let exercises = [];
    if(category === 'warmUp') {
        exercises = JSON.parse(sessionStorage.getItem('exercisesWarmUp'));
    } else if(category === 'main') {
        exercises = JSON.parse(sessionStorage.getItem('exercisesMain'));
    } else if(category === 'end'){
        exercises = JSON.parse(sessionStorage.getItem('exercisesEnd'));
    }
    return exercises;
}


