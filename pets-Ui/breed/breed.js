import {
    getParameterByName,
    clearElementChildren,
    createLinkCell,
    createButtonCell,
    createTextCell,
    setTextNode
} from '../js/dom_utils.js';
import {getBackendUrl} from '../js/configuration.js';

window.addEventListener('load', () => {
    fetchAndDisplayBreed();
    fetchAndDisplayDogs();
});

/**
 * Fetches all users and modifies the DOM tree in order to display them.
 */
function fetchAndDisplayDogs() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            displayDogs(JSON.parse(this.responseText))
        }
    };
    xhttp.open("GET", getBackendUrl() + '/api/breeds/' + getParameterByName('breed') + '/dogs', true);
    xhttp.send();
}

/**
 * Updates the DOM tree in order to display dogs.
 *
 * @param {{dogs: {id: number, name:string}[]}} dogs
 */
function displayDogs(dogs) {
    let tableBody = document.getElementById('tableBody');
    clearElementChildren(tableBody);
    dogs.dogs.forEach(dog => {
        tableBody.appendChild(createTableRow(dog));
    })
}

/**
 * Creates single table row for entity.
 *
 * @param {{id: number, name: string}} dog
 * @returns {HTMLTableRowElement}
 */
function createTableRow(dog) {
    let tr = document.createElement('tr');
    tr.appendChild(createTextCell(dog.name));
    tr.appendChild(createLinkCell('view', '../dog/dog.html?breed='
        + getParameterByName('breed') + '&dog=' + dog.id));
    tr.appendChild(createLinkCell('edit', '../dog_edit/dog_edit.html?breed='
        + getParameterByName('breed') + '&dog=' + dog.id));
    tr.appendChild(createButtonCell('delete', () => deleteDog(dog.id)));
    return tr;
}

/**
 * Deletes entity from backend and reloads table.
 *
 * @param {number} dog to be deleted
 */
function deleteDog(dog) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 202) {
            fetchAndDisplayDogs();
        }
    };
    xhttp.open("DELETE", getBackendUrl() + '/api/breeds/' + getParameterByName('breed')
        + '/dogs/' + dog, true);
    xhttp.send();
}


/**
 * Fetches single user and modifies the DOM tree in order to display it.
 */
function fetchAndDisplayBreed() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            displayBreed(JSON.parse(this.responseText))
        }
    };
    xhttp.open("GET", getBackendUrl() + '/api/breeds/' + getParameterByName('breed'), true);
    xhttp.send();
}

/**
 * Updates the DOM tree in order to display breed.
 *
 * @param {{name: string, pure: boolean}} breed
 */
function displayBreed(breed) {
    setTextNode('breed-name', breed.name);
    setTextNode('pure', breed.pure?'pure':'not pure');
}
