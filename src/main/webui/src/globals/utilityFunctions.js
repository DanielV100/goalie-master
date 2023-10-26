export function cloneExistingFieldsInContainer(event, elementContainer, elementToClone) {
    const cloneElement = document.getElementById(elementToClone);
    const clonedElement = cloneElement.cloneNode(true);
    const inputs = clonedElement.querySelectorAll('input');
    inputs.forEach((input) => (input.value = ""));
    document.getElementById(elementContainer).appendChild(clonedElement);
}