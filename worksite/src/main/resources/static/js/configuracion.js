function updateData(idUsuario, datosContrasenia) {

    callApi(
            API_BASE_URL + "/ResgistroUsuario/" + idUsuario + "/contrasenia",
            "PUT",
            datosContrasenia,
            actualizarContrasenia,
            function (error) {
                // El backend valida la contraseña actual (passwordEncoder.matches)
                // y devuelve un mensaje claro si no coincide.
                var mensaje = (error && error.message) ? error.message : "No se pudo actualizar la contraseña";
                alert(mensaje);
            }
            );
    }

    function actualizarContrasenia(response) {
         $("#passActual").val(""); 
            $("#passNueva").val(""); 
            $("#passConfirmar").val(""); 

            alert("Contraseña actualizada correctamente");
        
    }

    $(function () {
        $("#btnActualizarPass").on("click", function () {
            var idUsuario = localStorage.getItem("idUsuario");

            if ($("#passActual").val() === "" || $("#passNueva").val() === "" || $("#passConfirmar").val() === "") {
                alert("Por favor, complete todos los campos");
                return;
            }
            if ($("#passNueva").val().length < 4) {
                alert("La nueva contraseña debe tener al menos 4 caracteres");
                return;
            }
            if ($("#passNueva").val() !== $("#passConfirmar").val()) {
                alert("Las contraseñas no coinciden");
                return;
            }

            
            var datosContrasenia = {
                "contraseniaActual": $("#passActual").val(),
                "contraseniaNueva": $("#passNueva").val()
            };

            updateData(idUsuario, datosContrasenia);
        });
    });


function cbError(error) {
        console.error("Error en la petición:", error);
        alert("Error General");
    }









    function showTab(tab, el) {
        document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
        document.querySelectorAll('.sidebar-item').forEach(i => i.classList.remove('active'));
        document.getElementById('tab-' + tab).classList.add('active');
        el.classList.add('active');
        return false;
    }

    window.addEventListener("DOMContentLoaded", function () {
        const btnSeguridad = document.querySelector('[onclick*="seguridad"]');
        if (btnSeguridad) showTab('seguridad', btnSeguridad);
    });