import {getBackendUrl} from '../js/configuration.js';
import {getParameterByName} from '../js/dom_utils.js';

window.addEventListener("load", () => {
    function sendData() {
        const XHR = new XMLHttpRequest();
        const FD = new FormData(form);
        XHR.open("POST", getBackendUrl() + '/api/breeds/' + getParameterByName('breed') + '/dogs/', true);

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