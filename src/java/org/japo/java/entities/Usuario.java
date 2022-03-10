/* 
 * Copyright 2022 JAPO Labs - japolabs@gmail.com.
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
package org.japo.java.entities;

import java.io.Serializable;
import org.japo.java.libraries.UtilesValidacion;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class Usuario implements Serializable {

    // Valores por Defecto
    public static final int DEF_ID = 0;
    public static final String DEF_USER = "desconocido";
    public static final String DEF_PASS = "123456";
    public static final int DEF_AVATAR = Avatar.DEF_ID;
    public static final String DEF_AVATAR_IMG = Avatar.DEF_IMAGEN;
    public static final int DEF_PERFIL = Perfil.DEF_ID;
    public static final String DEF_PERFIL_INFO = Perfil.DEF_INFO;

    // Expresiones regulares
    public static final String REG_USER = "[\\w]{3,30}";
    public static final String REG_PASS = "[\\w]{3,30}";

    // Campos
    private int id;
    private String user;
    private String pass;
    private int avatar;
    private String avatarImg;
    private int perfil;
    private String perfilInfo;

    public Usuario() {
        id = DEF_ID;
        user = DEF_USER;
        pass = DEF_PASS;
        avatar = DEF_AVATAR;
        avatarImg = DEF_AVATAR_IMG;
        perfil = DEF_PERFIL;
        perfilInfo = DEF_PERFIL_INFO;
    }

    public Usuario(int id, String user, String pass,
            int avatar, String avatarImg,
            int perfil, String perfilInfo) {
        this.id = id;
        this.user = user;
        this.pass = pass;
        this.avatar = avatar;
        this.avatarImg = avatarImg;
        this.perfil = perfil;
        this.perfilInfo = perfilInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getPerfilInfo() {
        return perfilInfo;
    }

    public void setPerfilInfo(String perfilInfo) {
        this.perfilInfo = perfilInfo;
    }

    // ---
    //
    public final boolean validarId() {
        return validarId(id);
    }

    public final boolean validarUser() {
        return validarUser(user);
    }

    public final boolean validarPass() {
        return validarPass(pass);
    }

    public final boolean validarAvatar() {
        return validarAvatar(avatar);
    }

    public final boolean validarAvatarImg() {
        return validarAvatarImg(avatarImg);
    }

    public final boolean validarPerfil() {
        return validarPerfil(perfil);
    }

    public final boolean validarPerfilInfo() {
        return validarPerfilInfo(perfilInfo);
    }

    // ---
    //
    public static final boolean validarId(int id) {
        return id >= 0;
    }

    public static final boolean validarUser(String user) {
        return UtilesValidacion.validarDato(user, REG_USER);
    }

    public static final boolean validarPass(String pass) {
        return UtilesValidacion.validarDato(pass, REG_PASS);
    }

    public static final boolean validarAvatar(int avatar) {
        return Avatar.validarId(avatar);
    }

    public static final boolean validarAvatarImg(String avatarImg) {
        return Avatar.validarImagen(avatarImg);
    }

    public static final boolean validarPerfil(int perfil) {
        return Perfil.validarId(perfil);
    }

    public static final boolean validarPerfilInfo(String perfilInfo) {
        return Perfil.validarInfo(perfilInfo);
    }
}
