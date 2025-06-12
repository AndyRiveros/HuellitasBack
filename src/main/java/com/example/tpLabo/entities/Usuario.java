package com.example.tpLabo.entities;

import com.example.tpLabo.Enums.Rol;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombreUsuario;

    private String clave;

    @Column(unique = true)
    private String mail;

    private String nombre;
    private String apellido;
    private String direccion;

    @Column(unique = true)
    private Long DNI;

    @Column(unique = true)
    private String telefono;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private String imagenPerfil;

    private String tokenRecuperacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Mascota> mascotas = new ArrayList<>();

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String encriptarClave(String clave) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(clave.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}