import {getBackendUrl} from '../js/configuration.js';
import {getParameterByName} from "../js/dom_utils.js";

window.addEventListener('load', () => {
    fetchAndDisplayBreed();
    function sendData() {
        const XHR = new XMLHttpRequest();
        const FD = new FormData(form);
        XHR.open("PATCH", getBackendUrl() + '/api/breeds/' + getParameterByName('breed'), true);

        XHR.setRequestHeader("Content-Type", "application/json");
        XHR.onreadystatechange = function () {
            if (XHR.readyState === 4 && XHR.status === 200) {
                var json = JSON.parse(XHR.responseText);
                console.log(json.name + ", " + json.pure);
            }
        };
        var data = JSON.stringify({"name": FD.get("name"), "pure": FD.get("pure") === "true"});
        XHR.send(data);
    }
    const form = document.getElementById("myForm");
    form.addEventListener("submit", (event) => {
        event.preventDefault();
        sendData();
        window.location.replace('../breed_list/breed_list.html');
    });
});

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
    document.getElementById('name').value = breed.name;
    breed.pure?document.getElementById("pure").checked = true:document.getElementById("not_pure").checked = true;

}
