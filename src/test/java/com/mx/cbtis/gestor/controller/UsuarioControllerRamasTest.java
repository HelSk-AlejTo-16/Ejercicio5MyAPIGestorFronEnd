package com.mx.cbtis.gestor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

/**
 * Pruebas de cobertura de ramas (branch coverage) para UsuarioController.
 * Cubren cada lado de las condiciones compuestas (&&, ||) que SonarQube mide.
 */
@WebMvcTest(UsuarioController.class)
class UsuarioControllerRamasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuariotable;

    @Test
    @DisplayName("Login sin cuerpo de solicitud (401)")
    void testLoginSinCuerpo() throws Exception {
        mockMvc.perform(post("/getUsuarioLogin"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciales incompletas"));
    }

    @Test
    @DisplayName("Login con correo pero sin contraseña (401)")
    void testLoginSinContrasena() throws Exception {
        mockMvc.perform(post("/getUsuarioLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"correo_electronico\":\"lili@test.com\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciales incompletas"));
    }

    @Test
    @DisplayName("Agregar usuario con ID nuevo no existente (200)")
    void testAddUsuarioIdNuevo() throws Exception {
        when(usuariotable.existsById(50)).thenReturn(false);
        when(usuariotable.save(any(Usuario.class))).thenReturn(new Usuario());

        mockMvc.perform(post("/addUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_usuario\":50,\"correo_electronico\":\"nuevo@test.com\",\"contrasena\":\"abc123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1));
    }

    @Test
    @DisplayName("Agregar usuario sin correo (400)")
    void testAddUsuarioCorreoNulo() throws Exception {
        mockMvc.perform(post("/addUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"contrasena\":\"abc123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Formato de correo inválido"));
    }

    @Test
    @DisplayName("Agregar usuario sin contraseña (400)")
    void testAddUsuarioContrasenaNula() throws Exception {
        mockMvc.perform(post("/addUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"correo_electronico\":\"valido@test.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Contraseña insegura"));
    }

    @Test
    @DisplayName("Agregar usuario con contraseña solo de letras (400)")
    void testAddUsuarioContrasenaSoloLetras() throws Exception {
        mockMvc.perform(post("/addUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"correo_electronico\":\"valido@test.com\",\"contrasena\":\"abcdefg\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Contraseña insegura"));
    }

    @Test
    @DisplayName("Agregar usuario con contraseña solo de números (400)")
    void testAddUsuarioContrasenaSoloNumeros() throws Exception {
        mockMvc.perform(post("/addUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"correo_electronico\":\"valido@test.com\",\"contrasena\":\"1234567\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Contraseña insegura"));
    }

    @Test
    @DisplayName("Actualizar usuario sin cuerpo (400)")
    void testUpdateUsuarioSinCuerpo() throws Exception {
        mockMvc.perform(put("/updateUsuario"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Datos incompletos"));
    }

    @Test
    @DisplayName("Actualizar usuario sin cambiar correo (200)")
    void testUpdateUsuarioSinCorreo() throws Exception {
        when(usuariotable.existsById(2)).thenReturn(true);
        when(usuariotable.save(any(Usuario.class))).thenReturn(new Usuario());

        mockMvc.perform(put("/updateUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_usuario\":2,\"nombre\":\"Solo nombre\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1));
    }

    @Test
    @DisplayName("Eliminar usuario con cuerpo sin ID y sin parámetros (404)")
    void testDeleteUsuarioSinIdentificador() throws Exception {
        mockMvc.perform(delete("/deleteUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultado").value(0));
    }
}

