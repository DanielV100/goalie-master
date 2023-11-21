/**
 * Utility functions, which are used in several places of the application.
 * @author daniel
 */

/**** EXPORT FUNCTIONS ****/

/**
 * Method for cloning existing dom node. It's user for materials and descriptions steps e.g.
 * @param event click event
 * @param elementContainer container in which the node should be copied in
 * @param elementToClone element to clone
 */
export function cloneExistingFieldsInContainer(event, elementContainer, elementToClone) {
    event.preventDefault();
    const cloneElement = document.getElementById(elementToClone);
    const clonedElement = cloneElement.cloneNode(true);
    const inputs = clonedElement.querySelectorAll('.input');
    inputs.forEach((input) => (input.value = ''));
    document.getElementById(elementContainer).appendChild(clonedElement);
    bindDeleteEventListenerToAllDeleteButtons(event);

}

/**
 * Binding click listener to dynamic delete buttons.
 * Needed because @click can't be inherited.
 * @param event click event
 */
export function bindDeleteEventListenerToAllDeleteButtons(event = null) {
    const allDeleteButtons = document.querySelectorAll('.deleteButton');
    allDeleteButtons.forEach((deleteButton) => {
        deleteButton.addEventListener('click', deleteDeleteButtonsContainer);
    });
    if (event) {
        if (event.target.id === 'addMaterial') {
            enableAllDeleteButtons('#delete_material');
        } else {
            enableAllDeleteButtons('#delete_description');
        }
    }
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

/**** PRIVATE FUNCTIONS ****/

/**
 * Removing container element from button from dom.
 * @param event click event
 */
function deleteDeleteButtonsContainer(event) {
    event.preventDefault();
    const parentNode = event.target.parentNode;
    parentNode.remove();
    if(event.target.id === 'delete_material') {
        disableLastDeleteButton('#material_delete');
    } else {
        disableLastDeleteButton('#delete_description')
    }
}

/**
 * Method for setting all delete buttons enabled
 * @param selector for delete buttons
 */
function enableAllDeleteButtons(selector) {
    const deleteButtonElements = document.querySelectorAll(selector);
    deleteButtonElements.forEach((deleteButtonElement) => {
        deleteButtonElement.disabled = false;
    });
}
/**
 * Method for setting all delete buttons disabled
 * @param selector for delete buttons
 */
function disableLastDeleteButton(selector) {
    const deleteButtonElements = document.querySelectorAll(selector);
    if(deleteButtonElements.length === 1) {
        deleteButtonElements.forEach((deleteButtonElement) => {
            deleteButtonElement.disabled = true;
        });
    }

}


function getErrorMessageFromHttpError(error) {
    const errorAsString = String(error);

    if (errorAsString.includes('401')) {
        return 'Benutzername oder Passwort falsch.';
    } else if (errorAsString.includes('403')) {
        return 'Zugriff verweigert. Sie haben keine Berechtigung, auf diese Ressource zuzugreifen.';
    } else if (errorAsString.includes('404')) {
        return 'Die angeforderte Ressource wurde nicht gefunden.';
    } else if (errorAsString.includes('500')) {
        return 'Interner Serverfehler. Bitte versuchen Sie es später noch einmal.';
    } else if (errorAsString.includes('502')) {
        return 'Schlechtes Gateway. Probleme bei der Verbindung zum Server.';
    } else if (errorAsString.includes('503')) {
        return 'Dienst nicht verfügbar. Der Server ist derzeit nicht erreichbar.';
    } else if (errorAsString.includes('504')) {
        return 'Gateway-Zeitüberschreitung. Die Anfrage hat zu lange gedauert.';
    } else {
        console.log(error);
        return error;
    }

}

function showErrorDialog() {
    const errorDialog =   document.getElementById('errorDialog');
    if(errorDialog !== null) {
        errorDialog.showModal();
    }
}




