import {
    getParameterByName,
    setTextNode
} from '../js/dom_utils.js';
import {getBackendUrl} from '../js/configuration.js';

window.addEventListener('load', () => {
    fetchAndDisplayDog();
});
/**
 * Fetches single user and modifies the DOM tree in order to display it.
 */
function fetchAndDisplayDog() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            displayBreed(JSON.parse(this.responseText))
        }
    };
    xhttp.open("GET", getBackendUrl() + '/api/breeds/' + getParameterByName('breed')
        + '/dogs/' + getParameterByName('dog'), true);
    xhttp.send();
}

/**
 * Updates the DOM tree in order to display dog.
 *
 * @param {{name: string, age: string, height: string, breed: string}} dog
 */
function displayBreed(dog) {
    setTextNode('dog-name', dog.name);
    setTextNode('age', dog.age);
    setTextNode('height', dog.height);
    setTextNode('breed', dog.breed);
}
