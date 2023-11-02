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