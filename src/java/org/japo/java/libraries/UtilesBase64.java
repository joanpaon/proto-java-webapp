/* 
 * Copyright 2021 José A. Pacheco Ondoño - japolabs@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.japo.java.libraries;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public class UtilesBase64 {

    public static final boolean validarImagenBase64(String img) {
        boolean checkOK;
        try {
            // Elimina Prefijo
            String strImg = img.substring(img.indexOf(',') + 1);

            // Base64 > Binario
            byte[] binImg = Base64.getDecoder().decode(strImg);

            // Binario > Cauce Lectura
            InputStream is = new ByteArrayInputStream(binImg);

            // Cauce Lectura > Tipo MIME
            String mimeType = URLConnection.guessContentTypeFromStream(is);

            // Tipo MIME > Semáforo
            checkOK = false
                    || mimeType.equals("image/jpeg")
                    || mimeType.equals("image/png")
                    || mimeType.equals("image/gif");
        } catch (IOException | NullPointerException e) {
            checkOK = false;
        }

        // Retorno: true | false
        return checkOK;
    }
}
