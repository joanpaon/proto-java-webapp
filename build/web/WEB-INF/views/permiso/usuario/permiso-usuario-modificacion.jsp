<%@page import="org.japo.java.entities.Proceso"%>
<%@page import="org.japo.java.entities.PermisoUsuario"%>
<%@page import="org.japo.java.entities.Perfil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Datos Inyectados
    PermisoUsuario permiso = (PermisoUsuario) request.getAttribute("permiso");
    List<Proceso> procesos = (ArrayList<Proceso>) request.getAttribute("procesos");
    List<Perfil> perfiles = (ArrayList<Perfil>) request.getAttribute("perfiles");
%>

<!DOCTYPE html>
<html lang="es">

  <head>
    <!-- These lines go in the first 1024 bytes -->
    <meta charset="utf-8" />
    <meta http-equiv="x-ua-compatible" content="ie=edge" />
    <title>Proteo Web App</title>

    <!-- References -->
    <meta name="author" content="2021 - José A. Pacheco Ondoño - japolabs@gmail.com" />
    <meta name="description" content="Proto Web App" />

    <!-- Configuration -->
    <meta name="keywords" content="" />
    <meta name="robots" content="noindex, nofollow" />

    <!-- Viewport Setup for mobile devices -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <!-- Favicon -->
    <link href="public/img/favicon.ico" rel="icon" type="image/x-icon" />

    <!-- Style Sheet Links -->
    <link rel="stylesheet" href="public/css/permiso/permiso-modificacion.css" /> 
  </head>

  <body>
    <!-- Contenido Web -->
    <div id="container">

    </div>

    <!-- Application Scripts -->
    <script src="public/js/permiso/permiso-modificacion.js"></script>
  </body>
</html>
