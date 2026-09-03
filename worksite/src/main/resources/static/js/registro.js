var registros = [];

$(function (){

$("#nombre, #Apellido, #correo, #tipoDocumento, #Cedula, #telefono, #fechaNacimiento, #Genero, #experiencia, #contraseña, #confirmar, #cv, #Descripcion, #Estudio, #Cargo, #Ciudad ").on("change", onChangeInputWithErrorClass);
$("#botonRegistrarse").click(onClickButton);


$("#rol").on("change", function () {
    if ($(this).val() === "ADMIN") {
        $("#grupoCodigoAdmin").show();
    } else {
        $("#grupoCodigoAdmin").hide();
        $("#codigoAdmin").val("");
    }
});

});


 var onChangeInputWithErrorClass = function (e) {
  removeClassError(e.target);
}

var onClickButton = function (e) {
  e.preventDefault();
  var isFormValid = true;

  if ($("#nombre").val() === "") {
    $("#nombre").addClass("error");
    isFormValid = false;
  }

  if ($("#correo").val() === "") {
    $("#correo").addClass("error");
    isFormValid = false;
  }
   if ($("#Apellido").val() === "") {
    $("#Apellido").addClass("error");
    isFormValid = false;
  }

  if ($("#tipoDocumento").val() === "") {
    $("#tipoDocumento").addClass("error");
    isFormValid = false;
  }

  if ($("#Cedula").val() === "") {
    $("#Cedula").addClass("error");
    isFormValid = false;
  }

  if ($("#telefono").val() === "") {
    $("#telefono").addClass("error");
    isFormValid = false;
  }

  if ($("#fechaNacimiento").val() === "") {
    $("#fechaNacimiento").addClass("error");
    isFormValid = false;
  }

  if ($("#Genero").val() === "") {
    $("#Genero").addClass("error");
    isFormValid = false;
  }

  if ($("#experiencia").val() === "") {
    $("#experiencia").addClass("error");
    isFormValid = false;
  }

  if ($("#contraseña").val() === "") {
    $("#contraseña").addClass("error");
    isFormValid = false;
  }

  if ($("#confirmar").val() === "") {
    $("#confirmar").addClass("error");
    isFormValid = false;
  }

  if ($("#Ciudad").val() === "") {
    $("#Ciudad").addClass("error");
    isFormValid = false;
  }

  if ($("#Cargo").val() === "") {
    $("#Cargo").addClass("error");
    isFormValid = false;
  }

  if ($("#Estudio").val() === "") {
    $("#Estudio").addClass("error");
    isFormValid = false;
  }

  if ($("#Descripcion").val() === "") {
    $("#Descripcion").addClass("error");
    isFormValid = false;
  }

  if ($("#rol").val() === "") {
    $("#rol").addClass("error");
    isFormValid = false;
  }

  if ($("#rol").val() === "ADMIN" && $("#codigoAdmin").val() === "") {
    $("#codigoAdmin").addClass("error");
    isFormValid = false;
  }

  if (!isFormValid) {
    alert("Formulario incompleto!");
    return;
  }

  var newRegistro = {
    "nombres": $("#nombre").val (),
    "apellidos": $("#Apellido").val (),
    "correoElectronico": $("#correo").val (),
    "tipoIdentificacion": $("#tipoDocumento").val (),
    "cedula": $("#Cedula").val (),
    "numeroTelefonico": $("#telefono").val (),
    "fechaNacimiento": $("#fechaNacimiento").val (),
    "genero": $("#Genero").val (),
    "anosExperiencia": $("#experiencia").val (),
    "contrasenia": $("#contraseña").val (),
    "ciudad": $("#Ciudad").val (),
    "cargo": $("#Cargo").val (),
    "estudio": $("#Estudio").val (),
    "descripcion": $("#Descripcion").val (),
    "rolNombre": $("#rol").val (),
    "codigoAdmin": $("#codigoAdmin").val (),
    
  };


saveData (newRegistro);


}

function saveData (data) {
var base_url = API_BASE_URL + "/ResgistroUsuario";
var method = "POST";
callApi (base_url, method, data, cbSuccess, cbError);

}

function cbSuccess (data)  {   
  console.log(">>> Respuesta completa del registro:", data);
  console.log(">>> data.data:", data.data);
  console.log(">>> rolNombre recibido:", data.data ? data.data.rolNombre : "data.data es undefined");
  console.log(">>> token recibido:", data.data ? data.data.token : "data.data es undefined");

  alert("Registro guardado correctamente");
    $("#formRegistro")[0].reset();

    localStorage.setItem("idUsuario", data.data.id);
    localStorage.setItem("correoUsuario", data.data.correoElectronico);
    localStorage.setItem("rol", data.data.rolNombre);
    localStorage.setItem("token", data.data.token);

    var archivoCv = document.getElementById("cv") ? document.getElementById("cv").files[0] : null;

    if (archivoCv) {
        subirCv(data.data.id, archivoCv, function () {
            irAInicioSegunRol(data.data.rolNombre);
        });
    } else {
        irAInicioSegunRol(data.data.rolNombre);
    }
}

function irAInicioSegunRol(rolNombre) {
    if (rolNombre === "EMPRESA") {
        window.location.href = "empresa.html";
    } else {
        window.location.href = "inicio 2.html";
    }
}

function subirCv(idUsuario, archivo, onDone) {
    var lector = new FileReader();

    lector.onload = function () {
        var cvBase64 = lector.result; 

        callApi(
            API_BASE_URL + "/ResgistroUsuario/" + idUsuario + "/cv",
            "PUT",
            { "cv": cvBase64 },
            function () {
                onDone();
            },
            function (error) {
                alert("Tu cuenta se creó, pero no se pudo subir la hoja de vida. Puedes intentarlo de nuevo desde tu perfil.");
                onDone();
            }
        );
    };

    lector.onerror = function () {
        alert("No se pudo leer el archivo PDF. Puedes subir tu hoja de vida más tarde desde tu perfil.");
        onDone();
    };

    lector.readAsDataURL(archivo);
}

function cbError (data)  {
  alert(JSON.stringify(data));
}