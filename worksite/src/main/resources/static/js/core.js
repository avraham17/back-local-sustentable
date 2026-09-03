var API_BASE_URL = "http://localhost:8080";

var validMethods = ["GET", "POST", "PUT", "DELETE"];


function callApi(url, method, data, cbSuccess, cbError) {

    console.log("callApi :: " + method + " :: " + url);


    isPresent = validMethods.find(function(item){
        return item === method;
    });

    if(!isPresent) {
        alert("Metodo " + method + "No permitido");
        return;
    }

    var jsonData = "";
    if(method === "POST" || method === "PUT") {
        jsonData = JSON.stringify(data);
    }

   
    var headers = {};
    var token = localStorage.getItem("token");
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    $.ajax({
        url: url,
        type: method,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: jsonData, 
        headers: headers,
        success: function (result) {
            try {
                cbSuccess(result);
            } catch (e) {
                console.log("Error en cbSuccess", e);
            }
        },
        error: function (xhr, status , error) {
            try {
                var responseText = xhr.responseText || "";

                alert("STATUS: " + xhr.status + "\nERROR: " + error + "\nRESPUESTA (primeros 300 chars): " + responseText.substring(0,300) + "\n...\n(últimos 300 chars): " + responseText.substring(responseText.length-300));
                console.log("STATUS:", xhr.status);
                console.log("ERROR:", error);
                console.log("RESPUESTA:", responseText);

                if (xhr.status === 401) {
                    localStorage.removeItem("token");
                    alert("Tu sesión expiró o no es válida. Inicia sesión de nuevo.");
                    window.location.href = "sesion.html";
                    return;
                }

                if (xhr.status === 403) {
                    alert("No tienes permiso para realizar esta acción.");
                }

                
                var errorObj;
                try {
                    errorObj = JSON.parse(xhr.responseText);
                } catch (parseErr) {
                    errorObj = xhr.responseText;
                }

                cbError(errorObj);

            } catch (e) {
                cbErrorBase(xhr.status);
                console.log("Error en cbError", e);
            }
        }
    });
}

function cbErrorBase(error) {
    alert("El llamado al servidor fallo " + error);
}

function removeClassError(target) {
    $(target).removeClass("error");
}

var onChangeInputWithErrorClass = function (e) {
    removeClassError(e.target);
}

window.addEventListener("DOMContentLoaded", function () {
    
    const nombre   = localStorage.getItem("nombreUsuario")   || "";
    const apellido = localStorage.getItem("apellidoUsuario") || "";

    const rol = localStorage.getItem("rol");

    const menuEmpresas = document.getElementById("menuEmpresas");
    if (menuEmpresas) {
    
        menuEmpresas.style.display = (rol === "EMPRESA" || rol === "ADMIN") ? "" : "none";
    }

    const linkVerVacantes = document.getElementById("linkVerVacantes");
    if (linkVerVacantes) {
       
        linkVerVacantes.style.display = (rol === "CANDIDATO" || rol === "ADMIN") ? "" : "none";
    }

    const linkPublicarOferta = document.getElementById("linkPublicarOferta");
    if (linkPublicarOferta) {
        
        linkPublicarOferta.style.display = (rol === "EMPRESA" || rol === "ADMIN") ? "" : "none";
    }

    const menuEstadisticas = document.getElementById("menuEstadisticas");
    if (menuEstadisticas) {
      
        menuEstadisticas.style.display = (rol === "ADMIN") ? "" : "none";
    }

    const inicialNombre   = nombre.charAt(0).toUpperCase();
    const inicialApellido = apellido.charAt(0).toUpperCase();

    const avatar = document.getElementById("avatarInicial");
    if (avatar) {
        avatar.textContent = inicialNombre + inicialApellido;
    }
});