package org.japo.java.entities;

import java.io.Serializable;
import org.japo.java.libraries.UtilesBase64;
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
    public static final int DEF_AVATAR = 0;
    public static final String DEF_AVATAR_IMG = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAANQAAADICAMAAACnFD/KAAADAFBMVEUAAABUVFSEhIQcHBy8vLzk5OQ7OzuioqJzc3MzMzOZmZlmZmbOzs4MDAwsLCxDQ0P09PSsrKzc3NxqamrS0tJcXFyMjIxLS0t7e3skJCTExMSzs7P9/f3s7OwTExMfHx8gICAhISEiIiIjIyMkJCQlJSUmJiYnJycoKCgpKSkqKiorKyssLCwtLS0uLi4vLy8wMDAxMTEyMjIzMzM0NDQ1NTU2NjY3Nzc4ODg5OTk6Ojo7Ozs8PDw9PT0+Pj4/Pz9AQEBBQUFCQkJDQ0NERERFRUVGRkZHR0dISEhJSUlKSkpLS0tMTExNTU1OTk5PT09QUFBRUVFSUlJTU1NUVFRVVVVWVlZXV1dYWFhZWVlaWlpbW1tcXFxdXV1eXl5fX19gYGBhYWFiYmJjY2NkZGRlZWVmZmZnZ2doaGhpaWlqampra2tsbGxtbW1ubm5vb29wcHBxcXFycnJzc3N0dHR1dXV2dnZ3d3d4eHh5eXl6enp7e3t8fHx9fX1+fn5/f3+AgICBgYGCgoKDg4OEhISFhYWGhoaHh4eIiIiJiYmKioqLi4uMjIyNjY2Ojo6Pj4+QkJCRkZGSkpKTk5OUlJSVlZWWlpaXl5eYmJiZmZmampqbm5ucnJydnZ2enp6fn5+goKChoaGioqKjo6OkpKSlpaWmpqanp6eoqKipqamqqqqrq6usrKytra2urq6vr6+wsLCxsbGysrKzs7O0tLS1tbW2tra3t7e4uLi5ubm6urq7u7u8vLy9vb2+vr6/v7/AwMDBwcHCwsLDw8PExMTFxcXGxsbHx8fIyMjJycnKysrLy8vMzMzNzc3Ozs7Pz8/Q0NDR0dHS0tLT09PU1NTV1dXW1tbX19fY2NjZ2dna2trb29vc3Nzd3d3e3t7f39/g4ODh4eHi4uLj4+Pk5OTl5eXm5ubn5+fo6Ojp6enq6urr6+vs7Ozt7e3u7u7v7+/w8PDx8fHy8vLz8/P09PT19fX29vb39/f4+Pj5+fn6+vr7+/v8/Pz9/f3+/v7///+SALbsAAABAHRSTlP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8AU/cHJQAAAAlwSFlzAAALEwAACxMBAJqcGAAAC8lJREFUeNrtneuSq6wShqNfCkRSUBYRNbn/61zbRI6ewlGdqs2/qTWzksduXpqmaW//DhiADGoQkP/zbln/97oqKECLQWnL/yZUjenHMAStDDKaDrXlH4MqC7LBY5ANA+3/DFTdjUTIZYy/19V/AapCjkSSC/VXh2Ljl5xNH2L74fxnND6E6spQvY30+fa0wKzqSy5G2Ves7QCyyQZSXhWqRoMFRAtW8rquYW0O+PmRVy1FJtcArglFTSTUsXKOMxucdcjEwteDKrU8EAIY3weabDZyAW0ugq4G1Q0aiVYORJKrotpaA7sUlPG8ae+MtMAa6HWgSm2m0Uq+A9ajEyZ1wRRQeFBfCXtZSWHxQhuruQRUoZi6MgTpO3plrKG+AJSWCFyHDzgGjOK/+a8+HUrOcgJ6WEcNrKj4yVDy+RLK68gBe5TIVpFQcoKToo4fsJQTK5IqDqoV84m0dZLBpTMP8DQolphpHIrqLCienklTkZOgcjApKkJPgUIJNcKcVyKnNlQnQLXiiXZ14lGiWLEIhqoHseamZhrXq9hpFQwll/+yTk/FhBO0B0OJVZdUsM4wxP8eGrEHQjVDFpFQQybgD4USHwoyMdViWg3lgVBSJXqYi0pq64FQKK/zaQcMW6xuEYZCPB+TUsDDoKanSDDMaCkRLgWZ6hZuKFBnHXIJPghq2u1mNlQtkhYheZgQqBBDwXE0TfN+v5sGQqfHUU3PrjsEqh+8NhzN/fWYxlOM6afX6/7eh6OhUnELXnhLN6KHopiNie++zQWn9FLAAnwL9D6nHQe8bwCZaK9NLI4C/c8fakpMEPZ7XjSPX0hfrMf7h1QcAAVcF174cBzP++4C7O9//lDE1fteT2eq967/FdmhppXXYZFqnJlGqmbP/1B2KJHr+619dx+o157+kexQ08MDKb3v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";
    public static final int DEF_PERFIL = Perfil.BASIC;
    public static final String DEF_PERFIL_INFO = Perfil.BASIC_INFO;

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

    public final boolean validarId() {
        return getId() > 0;
    }

    public final boolean validarUser() {
        return UtilesValidacion.validarDato(getUser(), REG_USER);
    }

    public final boolean validarPass() {
        return UtilesValidacion.validarDato(getPass(), REG_PASS);
    }

    public final boolean validarAvatar() {
        return avatar >= 0;
    }

    public final boolean validarAvatarImg() {
        return UtilesBase64.validarImagenBase64(avatarImg);
    }

    public final boolean validarPerfil() {
        return perfil > 0;
    }

    public final boolean validarPerfilInfo() {
        return Perfil.validarInfo(perfilInfo);
    }

    public static final boolean validarUser(String user) {
        return UtilesValidacion.validarDato(user, REG_USER);
    }

    public static final boolean validarPass(String pass) {
        return UtilesValidacion.validarDato(pass, REG_PASS);
    }
}
