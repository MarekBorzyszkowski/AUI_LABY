import {getBackendUrl} from '../js/configuration.js';

window.addEventListener("load", () => {
    function sendData() {
        const XHR = new XMLHttpRequest();
        const FD = new FormData(form);
        XHR.open("POST", getBackendUrl() + '/api/breeds/', true);

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