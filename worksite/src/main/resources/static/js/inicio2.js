window.addEventListener("DOMContentLoaded", function () {

    const nombre   = localStorage.getItem("nombreUsuario")   || "";
    const apellido = localStorage.getItem("apellidoUsuario") || "";

    const inicialNombre   = nombre.charAt(0).toUpperCase();
    const inicialApellido = apellido.charAt(0).toUpperCase();

    const avatar = document.getElementById("avatarInicial");
    if (avatar) {
        avatar.textContent = inicialNombre + inicialApellido;
    }

    inicializarBusqueda();
    inicializarCategorias();
    cargarEmpleosDestacados();
    inicializarBotonPublicarEmpleo();
});


function puedeVerVacantes() {
    const rol = localStorage.getItem("rol");
    return rol === "CANDIDATO" || rol === "ADMIN";
}

function irAVacantesOAvisar(params) {
    if (!puedeVerVacantes()) {
        alert("Esta sección es para candidatos. Como empresa, puedes publicar y gestionar tus propias ofertas.");
        window.location.href = "crear-oferta.html";
        return;
    }

    var limpios = {};
    Object.keys(params).forEach(function (key) {
        if (params[key]) limpios[key] = params[key];
    });

    var query = new URLSearchParams(limpios).toString();
    window.location.href = "vacantes.html" + (query ? "?" + query : "");
}

function inicializarBusqueda() {
    var inputCargo  = document.getElementById("buscarCargo");
    var inputCiudad = document.getElementById("buscarCiudad");
    var btnBuscar   = document.getElementById("btnBuscarInicio");

    if (!inputCargo || !inputCiudad || !btnBuscar) return;

    function ejecutarBusqueda() {
        irAVacantesOAvisar({
            busqueda: inputCargo.value.trim(),
            ciudad: inputCiudad.value.trim()
        });
    }

    btnBuscar.addEventListener("click", ejecutarBusqueda);

    [inputCargo, inputCiudad].forEach(function (input) {
        input.addEventListener("keypress", function (e) {
            if (e.which === 13) ejecutarBusqueda();
        });
    });
}

function inicializarCategorias() {
    document.querySelectorAll(".category").forEach(function (el) {
        el.style.cursor = "pointer";
        el.addEventListener("click", function () {
            var sector = el.getAttribute("data-sector") || "";
            irAVacantesOAvisar({ sector: sector });
        });
    });
}

function cargarEmpleosDestacados() {
    var contenedor = document.getElementById("jobsDestacados");
    if (!contenedor) return;

    
    callApi(API_BASE_URL + "/oferta", "GET", null, function (response) {
        var ofertas = (response.data || [])
            .filter(function (o) { return o.estado === "activa"; })
            .slice(0, 3);

        if (ofertas.length === 0) {
            contenedor.innerHTML = '<div class="estado-info">Aún no hay ofertas activas publicadas.</div>';
            return;
        }

        contenedor.innerHTML = "";
        ofertas.forEach(function (o) {
            contenedor.appendChild(crearTarjetaDestacada(o));
        });
    }, function () {
        contenedor.innerHTML = '<div class="estado-info">No se pudieron cargar las ofertas destacadas.</div>';
    });
}

function crearTarjetaDestacada(o) {
    var salario = o.salario
        ? "$" + parseFloat(o.salario).toLocaleString("es-CO")
        : "Salario a convenir";

    var div = document.createElement("div");
    div.className = "job-card";

    var h3 = document.createElement("h3");
    h3.textContent = o.titulo || "";

    var pEmpresa = document.createElement("p");
    pEmpresa.textContent = "Empresa: " + (o.empresa || "No especificada");

    var pUbicacion = document.createElement("p");
    pUbicacion.textContent = "Ubicación: " + (o.ubicacion || "No especificada");

    var pSalario = document.createElement("p");
    pSalario.textContent = "Salario: " + salario;

    var btn = document.createElement("button");
    btn.type = "button";
    btn.textContent = "Aplicar";
    btn.addEventListener("click", function () {
        aplicarAOferta(o.id);
    });

    div.appendChild(h3);
    div.appendChild(pEmpresa);
    div.appendChild(pUbicacion);
    div.appendChild(pSalario);
    div.appendChild(btn);

    return div;
}

function aplicarAOferta(idOferta) {
    var rol = localStorage.getItem("rol");
    var idUsuario = localStorage.getItem("idUsuario");

    if (!idUsuario) {
        alert("Debes iniciar sesión para postularte");
        window.location.href = "sesion.html";
        return;
    }

    if (rol !== "CANDIDATO" && rol !== "ADMIN") {
        alert("Solo las cuentas de candidato pueden postularse a ofertas.");
        return;
    }

    window.location.href = "aplicar-oferta.html?id=" + idOferta;
}

function inicializarBotonPublicarEmpleo() {
    var btn = document.getElementById("btnPublicarEmpleo");
    if (!btn) return;

    btn.addEventListener("click", function () {
        var rol = localStorage.getItem("rol");

        if (rol === "EMPRESA" || rol === "ADMIN") {
            window.location.href = "crear-oferta.html";
        } else {
          
            alert("Esta opción es para cuentas de empresa. Regístrate como empresa para publicar tus propias ofertas.");
            window.location.href = "registro.html";
        }
    });
}