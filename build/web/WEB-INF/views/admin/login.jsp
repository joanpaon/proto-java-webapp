<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
        <link rel="stylesheet" href="public/css/admin/login.css" />
        <link rel="stylesheet" href="public/css/partials/header.css" />
        <link rel="stylesheet" href="public/css/partials/footer.css" />
    </head>

    <body>
        <!-- Web Content-->
        <div id="container">
            <%@include file="../partials/header.jspf" %>

            <main>
                <img src="public/img/logo01.png" alt="Logo" />

                <form action="?cmd=login&op=proceso" 
                      method="post" 
                      accept-charset="Windows-1252">

                    <div class="fieldset">
                        <label for="user">Usuario</label>
                        <input
                            id="user"
                            type="text"
                            name="user"
                            required
                            pattern="\w{3,20}"
                            autocomplete="username"
                            />
                    </div>

                    <div class="fieldset">
                        <label for="pass">Contraseña</label>
                        <input
                            id="pass"
                            type="password"
                            name="pass"
                            required
                            pattern="\w{3,20}"
                            autocomplete="current-password"
                            />
                    </div>

                    <div class="buttonset">
                        <button class="btn" type="submit">Aceptar ✅</button> 
                        <button class="btn" type="reset">Reiniciar ❌</button>
                    </div>

                </form>

            </main>

            <%@include file="../partials/footer.jspf" %>
        </div>

        <!-- Application Scripts -->
        <script src="public/js/admin/login.js"></script>
        <script src="public/js/partials/header.js"></script>
        <script src="public/js/partials/footer.js"></script>
    </body>
</html>
