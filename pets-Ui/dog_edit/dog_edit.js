import {getBackendUrl} from '../js/configuration.js';
import {getParameterByName} from "../js/dom_utils.js";

window.addEventListener('load', () => {
    fetchAndDisplayDog();
    function sendData() {
        const XHR = new XMLHttpRequest();
        const FD = new FormData(form);
        XHR.open("PATCH", getBackendUrl() + '/api/breeds/' + getParameterByName('breed') + '/dogs/' + getParameterByName('dog'), true);

        XHR.setRequestHeader("Content-Type", "application/json");
        XHR.onreadystatechange = function () {
            if (XHR.readyState === 4 && XHR.status === 200) {
                var json = JSON.parse(XHR.responseText);
                console.log(json.name + ", " + json.pure);
            }
        };
        var data = JSON.stringify({"name": FD.get("name"), "age": FD.get("age"), "height": FD.get("height")});
        XHR.send(data);
    }
    const form = document.getElementById("myForm");
    form.addEventListener("submit", (event) => {
        event.preventDefault();
        sendData();
        window.location.replace('../breed/breed.html?breed=' + getParameterByName('breed'));
    });
});

function fetchAndDisplayDog() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            displayDog(JSON.parse(this.responseText))
        }
    };
    xhttp.open("GET", getBackendUrl() + '/api/breeds/' + getParameterByName('breed') + '/dogs/' + getParameterByName('dog'), true);
    xhttp.send();
}

/**
 * Updates the DOM tree in order to display dog.
 *
 * @param {{name: string, age: number, height:number}} dog
 */
function displayDog(dog) {
    document.getElementById('name').value = dog.name;
    document.getElementById('ageId').value = dog.age;
    document.getElementById('heightId').value = dog.height;
}
