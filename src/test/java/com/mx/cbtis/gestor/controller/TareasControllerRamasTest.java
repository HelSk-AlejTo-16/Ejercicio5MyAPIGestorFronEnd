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

import com.mx.cbtis.gestor.modelo.Tareas;
import com.mx.cbtis.gestor.repository.ProyectosRepository;
import com.mx.cbtis.gestor.repository.TareasRepository;

/**
 * Pruebas de cobertura de ramas (branch coverage) para TareasController.
 */
@WebMvcTest(TareasController.class)
class TareasControllerRamasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TareasRepository tareastable;

    @MockBean
    private ProyectosRepository proyectostable;

    @Test
    @DisplayName("Agregar tarea con ID nuevo no existente (200)")
    void testAddTareaIdNuevo() throws Exception {
        when(tareastable.existsById(50)).thenReturn(false);
        when(proyectostable.existsById(1)).thenReturn(true);
        when(tareastable.save(any(Tareas.class))).thenReturn(new Tareas());

        mockMvc.perform(post("/addTareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_tarea\":50,\"nombre\":\"Tarea\",\"id_proyecto\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1));
    }

    @Test
    @DisplayName("Agregar tarea sin id_proyecto (400, mensaje 'nulo')")
    void testAddTareaProyectoNulo() throws Exception {
        mockMvc.perform(post("/addTareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Tarea huérfana\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El proyecto asociado (id_proyecto: nulo) no existe"));
    }

    @Test
    @DisplayName("Actualizar tarea sin cuerpo (400)")
    void testUpdateTareaSinCuerpo() throws Exception {
        mockMvc.perform(put("/updateTareas"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Datos incompletos"));
    }

    @Test
    @DisplayName("Actualizar tarea sin cambiar proyecto (200)")
    void testUpdateTareaSinProyecto() throws Exception {
        when(tareastable.existsById(5)).thenReturn(true);
        when(tareastable.save(any(Tareas.class))).thenReturn(new Tareas());

        mockMvc.perform(put("/updateTareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_tarea\":5,\"estado\":\"Terminada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1));
    }

    @Test
    @DisplayName("Eliminar tarea con cuerpo sin ID y sin parámetros (404)")
    void testDeleteTareaSinIdentificador() throws Exception {
        mockMvc.perform(delete("/deleteTareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultado").value(0));
    }
}

