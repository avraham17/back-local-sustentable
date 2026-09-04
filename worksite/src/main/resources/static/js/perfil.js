var usuarioActual = null; 
var fotoBase64Nueva = null; 

    function cbError(error) {
        console.error("Error en la petición:", error);
    
    }


    function loadData(idUsuario){

        callApi(
        API_BASE_URL + "/ResgistroUsuario/" + idUsuario,"GET",null,cargarPerfil,cbError
        );
    }

    function updateData(idUsuario, datos){

        callApi(
        API_BASE_URL + "/ResgistroUsuario/" + idUsuario,"PUT",datos,actualizarPerfil,cbError
        );
    }

    function deleteData(correo){
        callApi(
            API_BASE_URL + "/ResgistroUsuario/correo/" + correo, "DELETE", null, eliminarPerfil, cbError
        );
    }

    function cargarPerfil(response) {
        console.log(response.data);
        usuarioActual = response.data; 

        $("#nombreMostrar").text(response.data.nombres + " " + response.data.apellidos);
        $("#mostrarNombre").text(response.data.nombres + " " + response.data.apellidos);
        $("#mostrarCorreo").text(response.data.correoElectronico);
        $("#mostrarTelefono").text(response.data.numeroTelefonico);
        $("#mostrarCedula").text(response.data.cedula);
        $("#mostrarfechaNacimiento").text(response.data.fechaNacimiento);
        $("#mostrarOcupacion").text(response.data.Cargo);
        $("#cargoMostrar").text(response.data.Cargo);
        $("#mostrarnivelEstudio").text(response.data.estudio);
        $("#mostrarDescripcion").text(response.data.Descripcion);
        $("#mostrarCiudad").text(response.data.Ciudad);

        
        if (response.data.foto) {
            $("#fotoPreview").attr("src", response.data.foto);
        }

        mostrarEnlaceCv(response.data.cv);

    }

    function actualizarPerfil(response){


        $("#nombreMostrar").text(response.data.nombres + " " + response.data.apellidos);
        $("#mostrarNombre").text(response.data.nombres + " " + response.data.apellidos);
        $("#mostrarCorreo").text(response.data.correoElectronico);
        $("#mostrarTelefono").text(response.data.numeroTelefonico);
        $("#mostrarCedula").text(response.data.cedula);
        $("#mostrarfechaNacimiento").text(response.data.fechaNacimiento);
        $("#mostrarOcupacion").text(response.data.Cargo);
        $("#mostrarnivelEstudio").text(response.data.estudio);
        $("#mostrarDescripcion").text(response.data.Descripcion);
        $("#mostrarCiudad").text(response.data.Ciudad);
        $("#cargoMostrar").text(response.data.Cargo);

        if (response.data.foto) {
            $("#fotoPreview").attr("src", response.data.foto);
        }

        mostrarEnlaceCv(response.data.cv);

        fotoBase64Nueva = null;

        var idUsuario = localStorage.getItem("idUsuario");

        loadData(idUsuario);
        alert("datos actualizados correctamente");
    }

    function eliminarPerfil(response){
        alert("Perfil eliminado correctamente");
        localStorage.removeItem("idUsuario");
        window.location.href = "sesion.html";
    }

    $(function () {

            var idUsuario = localStorage.getItem("idUsuario");
            loadData(idUsuario);

            var rol = localStorage.getItem("rol");
        if (rol === "CANDIDATO") {
            $("#seccionPostulaciones").show();
            cargarPostulaciones();
        }

            $("#btneditar").on("click", function () {
            if (usuarioActual) {
                $("#editNombres").val(usuarioActual.nombres || "");
                $("#editApellidos").val(usuarioActual.apellidos || "");
                $("#editCorreo").val(usuarioActual.correoElectronico || "");
                $("#editTelefono").val(usuarioActual.numeroTelefonico || "");
                $("#editNuDocumento").val(usuarioActual.cedula || "");
                $("#editgenero").val(usuarioActual.genero || "");
                $("#editAnios").val(usuarioActual.anosExperiencia || "");
                $("#editFecha").val(usuarioActual.fechaNacimiento || "");
                $("#editCiudad").val(usuarioActual.Ciudad || "");
                $("#editCargo").val(usuarioActual.Cargo || "");
                $("#editEstudio").val(usuarioActual.estudio || "");
                $("#editDescripcion").val(usuarioActual.Descripcion || "");
                $("#editTipoId").val(usuarioActual.tipoIdentificacion || "");
                $("#editContrasenia").val(""); 
            }
            var modal = new bootstrap.Modal(document.getElementById('modalEditar'));
            modal.show();

        });

        $("#btneliminar").on("click", function () {
            // Cerramos el modal de edición antes de abrir el de confirmación,
            // para no apilar el confirm() nativo (o dos modales) uno encima del otro.
            var modalEditar = bootstrap.Modal.getInstance(document.getElementById('modalEditar'));
            if (modalEditar) modalEditar.hide();

            // Pequeño delay para dejar terminar la animación de cierre de Bootstrap
            // antes de mostrar el siguiente modal (evita que se encimen los fondos oscuros).
            setTimeout(function () {
                var modalConfirmar = bootstrap.Modal.getOrCreateInstance(document.getElementById('modalConfirmarEliminar'));
                modalConfirmar.show();
            }, 300);
        });

        $("#confirmarEliminarBtn").on("click", function () {
            var correo = $("#mostrarCorreo").text();

            var modalConfirmar = bootstrap.Modal.getInstance(document.getElementById('modalConfirmarEliminar'));
            if (modalConfirmar) modalConfirmar.hide();

            deleteData(correo);
        });

        // Los navegadores bloquean la navegación directa a una URL "data:" en una
        // pestaña nueva (por seguridad, para evitar phishing). Con la foto no pasa
        // porque ahí solo se usa como src de una <img>, no como navegación de página.
        // Por eso el CV hay que convertirlo a un Blob real antes de abrirlo.
        $("#enlaceCv").on("click", function (e) {
            e.preventDefault();
            if (usuarioActual && usuarioActual.cv) {
                abrirCv(usuarioActual.cv);
            }
        });

            $("#guardarEdicion").on("click", function () {
            var idUsuario = localStorage.getItem("idUsuario");

        var datosActualizados = {
            "nombres": $("#editNombres").val(),
            "apellidos": $("#editApellidos").val(),
            "correoElectronico": $("#editCorreo").val(),
            "numeroTelefonico": $("#editTelefono").val(),
            "cedula": $("#editNuDocumento").val(),
            "genero": $("#editgenero").val(),
            "anosExperiencia": $("#editAnios").val(),
            "fechaNacimiento": $("#editFecha").val(),
            "ciudad": $("#editCiudad").val(),
            "cargo": $("#editCargo").val(),
            "estudio": $("#editEstudio").val(),
            "descripcion": $("#editDescripcion").val(),
            "contrasenia": $("#editContrasenia").val() || usuarioActual.contrasenia,
            "tipoIdentificacion": $("#editTipoId").val(),
            "foto": fotoBase64Nueva || usuarioActual.foto,

        };
        

        updateData(idUsuario, datosActualizados); 

        var modal = bootstrap.Modal.getInstance(document.getElementById('modalEditar'));
        modal.hide();

    });
});

function cargarPostulaciones() {
    var idUsuario = localStorage.getItem("idUsuario");

    callApi(
        API_BASE_URL + "/postulacion/candidato/" + idUsuario, "GET", null,
        mostrarPostulaciones, cbError
    );
}

function mostrarPostulaciones(response) {
    var misPostulaciones = response.data || [];

    var contenedor = $("#listaPostulaciones");
    contenedor.empty();

    if (misPostulaciones.length === 0) {
        $("#sinPostulaciones").show();
        return;
    }

    $("#sinPostulaciones").hide();

    misPostulaciones.forEach(function (p) {
        var claseBadge = "badge-pendiente";
        var estado = (p.estadoPostulacion || "").toLowerCase();

        if (estado === "aceptado" || estado === "aceptada") {
            claseBadge = "badge-aceptado";
        } else if (estado === "rechazado" || estado === "rechazada") {
            claseBadge = "badge-rechazado";
        }

        var item = $(
            '<div class="item postulacion-item">' +
                '<div>' +
                    '<strong>' + p.oferta + '</strong><br>' +
                    '<small>Postulado el: ' + p.fechaPostulacion + '</small>' +
                '</div>' +
                '<span class="badge-estado ' + claseBadge + '">' + p.estadoPostulacion + '</span>' +
            '</div>'
        );

        contenedor.append(item);
    });
}

function previewFoto(e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = ev => {
            const src = ev.target.result;
            document.getElementById('fotoPreview').src = src;
            fotoBase64Nueva = src; 
            guardarFotoAutomatico(src); 
        };
        reader.readAsDataURL(file);
    }
}


function guardarFotoAutomatico(fotoBase64) {
    var idUsuario = localStorage.getItem("idUsuario");

    callApi(
        API_BASE_URL + "/ResgistroUsuario/" + idUsuario + "/foto", "PUT",
        { "foto": fotoBase64 },
        function () {
            fotoBase64Nueva = null;
            loadData(idUsuario); 
        },
        cbError
    );
}

function mostrarEnlaceCv(cv) {
    if (cv) {
        $("#enlaceCv").attr("href", cv).show();
        $("#labelSubirCv").text("Cambiar hoja de vida (PDF)");
    } else {
        $("#enlaceCv").hide();
        $("#labelSubirCv").text("Subir hoja de vida (PDF)");
    }
}

function previewCv(e) {
    const file = e.target.files[0];
    if (!file) return;

    if (file.type !== "application/pdf") {
        alert("Solo se permiten archivos PDF");
        e.target.value = "";
        return;
    }

    const reader = new FileReader();
    reader.onload = ev => {
        guardarCvAutomatico(ev.target.result); 
    };
    reader.readAsDataURL(file);
}

function guardarCvAutomatico(cvBase64) {
    var idUsuario = localStorage.getItem("idUsuario");

    callApi(
        API_BASE_URL + "/ResgistroUsuario/" + idUsuario + "/cv", "PUT",
        { "cv": cvBase64 },
        function () {
            loadData(idUsuario); 
        },
        cbError
    );
}

function abrirCv(cvDataUrl) {
    try {
        var partes = cvDataUrl.split(",");
        var base64 = partes.length > 1 ? partes[1] : partes[0];
        var binario = atob(base64);
        var bytes = new Uint8Array(binario.length);

        for (var i = 0; i < binario.length; i++) {
            bytes[i] = binario.charCodeAt(i);
        }

        var blob = new Blob([bytes], { type: "application/pdf" });
        var url = URL.createObjectURL(blob);
        window.open(url, "_blank");
    } catch (err) {
        console.error("No se pudo abrir el CV:", err);
        alert("No se pudo abrir la hoja de vida.");
    }
}