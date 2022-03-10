<%@page import="org.japo.java.entities.Perfil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Datos Inyectados
    List<Perfil> perfiles = (ArrayList<Perfil>) request.getAttribute("perfiles");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <!-- These lines go in the first 1024 bytes -->
        <meta charset="utf-8" />
        <meta http-equiv="x-ua-compatible" content="ie=edge" />
        <title>JAPOLabs Java Framework</title>

        <!-- References -->
        <meta name="author" content="2021 - José A. Pacheco Ondoño - japolabs@gmail.com" />
        <meta name="description" content="JAPOLabs Java Framework" />

        <!-- Configuration -->
        <meta name="keywords" content="" />
        <meta name="robots" content="noindex, nofollow" />

        <!-- Viewport Setup for mobile devices -->
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />

        <!-- Favicon -->
        <link href="public/img/logo.png" rel="icon" type="image/x-icon" />

        <!-- Style Sheet Links -->
        <link rel="stylesheet" href="public/css/usuario/usuario-insercion.css" /> 
        <link rel="stylesheet" href="public/css/partials/header.css" />
        <link rel="stylesheet" href="public/css/partials/footer.css" />
    </head>

    <body>
        <!-- Web Content-->
        <div id="container">
            <%@include file="/WEB-INF/views/partials/header.jspf" %>

            <main>

                <img class="watermark" src="public/img/logo01.png" alt="Logo" />

                <header>
                    <h2>Inserción de Usuarios</h2>
                    <a class="btn btn-listar" href="?cmd=usuario-listado">Listado</a>
                </header>

                <form method="post" 
                      accept-charset="Windows-1252" 
                      enctype="multipart/form-data"
                      action="?cmd=usuario-insercion&op=proceso">
                    <div class="form-content">
                        <div class="field-container">
                            <div class="field-set">
                                <label for="user">Usuario</label>
                                <input id="user" 
                                       type="text" 
                                       name="user" 
                                       pattern="<%= Usuario.REG_USER%>" 
                                       required />
                            </div>

                            <div class="field-set">
                                <label for="pass">Contraseña</label>
                                <input id="pass" 
                                       type="text" 
                                       name="pass" 
                                       pattern="<%= Usuario.REG_PASS%>" 
                                       required />
                            </div>

                            <div class="field-set">
                                <label for="avatar">Avatar</label>
                                <div type="text" class="avatar-name">Predeterminado</div>
                                <input id="avatar" 
                                       type="file" 
                                       name="avatar" 
                                       accept="image/png,image/jpeg" 
                                       style="display: none" />
                                <button type="button" 
                                        class="btn btn-img" 
                                        onclick="document.getElementById('avatar').click()">👀</button>
                            </div>

                            <div class="field-set">
                                <label for="perfil">Perfil</label>
                                <select id="perfil" name="perfil" required>
                                    <option value="<%= Perfil.BASIC%>">Usuario</option>
                                </select>
                            </div>
                        </div>

                        <div class="avatar-frame">
                            <div class="avatar">
                                <img class="actual" src="<%= Usuario.DEF_AVATAR_IMG%>" alt="Avatar"/>
                                <img class="backup" src="<%= Usuario.DEF_AVATAR_IMG%>" alt="Avatar" style="display: none;" />
                            </div>
                        </div>

                    </div>

                    <div class="controles">
                        <button class="btn btn-submit" type="submit">Enviar</button>
                        <button class="btn btn-reset" type="reset">Reiniciar</button>
                    </div>
                </form>

            </main>

            <%@include file="/WEB-INF/views/partials/footer.jspf" %>
        </div>

        <!-- Application Scripts -->
        <script src="public/js/usuario/usuario-insercion.js"></script>
        <script src="public/js/partials/header.js"></script>
        <script src="public/js/partials/footer.js"></script>
    </body>
</html>
