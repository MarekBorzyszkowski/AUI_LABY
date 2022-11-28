import {clearElementChildren, createLinkCell, createButtonCell, createTextCell} from '../js/dom_utils.js';
import {getBackendUrl} from '../js/configuration.js';

window.addEventListener('load', () => {
    fetchAndDisplayBreeds();
    document.getElementById('article').appendChild(createLinkCell('add breed','../breed_add/breed_add.html'));
});

/**
 * Fetches all users and modifies the DOM tree in order to display them.
 */
function fetchAndDisplayBreeds() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            displayBreeds(JSON.parse(this.responseText))
        }
    };
    xhttp.open("GET", getBackendUrl() + '/api/breeds', true);
    xhttp.send();
}

/**
 * Updates the DOM tree in order to display breeds.
 *
 * @param {{breeds: string[]}} breeds
 */
function displayBreeds(breeds) {
    let tableBody = document.getElementById('tableBody');
    clearElementChildren(tableBody);
    breeds.breeds.forEach(breed => {
        tableBody.appendChild(createTableRow(breed));
    })
}

/**
 * Creates single table row for entity.
 *
 * @param {string} breed
 * @returns {HTMLTableRowElement}
 */
function createTableRow(breed) {
    let tr = document.createElement('tr');
    tr.appendChild(createTextCell(breed.name));
    tr.appendChild(createLinkCell('view', '../breed/breed.html?breed=' + breed.id));
    tr.appendChild(createButtonCell('delete', () => deleteUser(breed.id)));
    return tr;
}

/**
 * Deletes entity from backend and reloads table.
 *
 * @param {string } breedId to be deleted
 */
function deleteUser(breedId) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 202) {
            fetchAndDisplayBreeds();
        }
    };
    xhttp.open("DELETE", getBackendUrl() + '/api/breeds/' + breedId, true);
    xhttp.send();
}
