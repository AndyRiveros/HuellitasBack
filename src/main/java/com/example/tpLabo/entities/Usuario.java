package com.example.tpLabo.entities;

import com.example.tpLabo.Enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreUsuario;
    private String clave;
    @Enumerated(EnumType.STRING)
    private Rol rol;



    // Método para encriptar la clave
    public String encriptarClave(String clave) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(clave.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}