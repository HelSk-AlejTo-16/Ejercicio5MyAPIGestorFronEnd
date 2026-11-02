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

import com.mx.cbtis.gestor.modelo.Proyectos;
import com.mx.cbtis.gestor.repository.ProyectosRepository;

/**
 * Pruebas de cobertura de ramas (branch coverage) para ProyectosController.
 */
@WebMvcTest(ProyectosController.class)
class ProyectosControllerRamasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProyectosRepository proyectostable;

    @Test
    @DisplayName("Agregar proyecto con ID nuevo no existente (200)")
    void testAddProyectoIdNuevo() throws Exception {
        when(proyectostable.existsById(50)).thenReturn(false);
        when(proyectostable.save(any(Proyectos.class))).thenReturn(new Proyectos());

        mockMvc.perform(post("/addProyecto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_proyecto\":50,\"nombre\":\"Proyecto Nuevo\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1));
    }

    @Test
    @DisplayName("Agregar proyecto sin campo nombre (400)")
    void testAddProyectoNombreNulo() throws Exception {
        mockMvc.perform(post("/addProyecto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descripcion\":\"Sin nombre\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Nombre requerido"));
    }

    @Test
    @DisplayName("Actualizar proyecto sin cuerpo (400)")
    void testUpdateProyectoSinCuerpo() throws Exception {
        mockMvc.perform(put("/updateProyecto"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Datos incompletos"));
    }

    @Test
    @DisplayName("Eliminar proyecto con cuerpo sin ID y sin parámetros (404)")
    void testDeleteProyectoSinIdentificador() throws Exception {
        mockMvc.perform(delete("/deleteProyecto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultado").value(0));
    }
}

