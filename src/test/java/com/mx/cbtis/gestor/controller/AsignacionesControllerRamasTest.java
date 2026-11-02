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

import com.mx.cbtis.gestor.modelo.Asignaciones;
import com.mx.cbtis.gestor.repository.AsignacionesRepository;
import com.mx.cbtis.gestor.repository.TareasRepository;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

/**
 * Pruebas de cobertura de ramas (branch coverage) para AsignacionesController.
 */
@WebMvcTest(AsignacionesController.class)
class AsignacionesControllerRamasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AsignacionesRepository asignacionestable;

    @MockBean
    private UsuarioRepository usuariotable;

    @MockBean
    private TareasRepository tareastable;

    @Test
    @DisplayName("Agregar asignación con ID nuevo no existente (200)")
    void testAddAsignacionIdNuevo() throws Exception {
        when(asignacionestable.existsById(50)).thenReturn(false);
        when(usuariotable.existsById(1)).thenReturn(true);
        when(tareastable.existsById(2)).thenReturn(true);
        when(asignacionestable.save(any(Asignaciones.class))).thenReturn(new Asignaciones());

        mockMvc.perform(post("/addAsignacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_asignacion\":50,\"id_usuario\":1,\"id_tarea\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1));
    }

    @Test
    @DisplayName("Agregar asignación sin id_usuario (400, mensaje 'nulo')")
    void testAddAsignacionUsuarioNulo() throws Exception {
        mockMvc.perform(post("/addAsignacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_tarea\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El usuario asociado (id_usuario: nulo) no existe"));
    }

    @Test
    @DisplayName("Agregar asignación sin id_tarea (400, mensaje 'nulo')")
    void testAddAsignacionTareaNula() throws Exception {
        when(usuariotable.existsById(1)).thenReturn(true);

        mockMvc.perform(post("/addAsignacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_usuario\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La tarea asociada (id_tarea: nulo) no existe"));
    }

    @Test
    @DisplayName("Actualizar asignación sin cuerpo (400)")
    void testUpdateAsignacionSinCuerpo() throws Exception {
        mockMvc.perform(put("/updateAsignacion"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Datos incompletos"));
    }

    @Test
    @DisplayName("Actualizar asignación cambiando solo el usuario (200)")
    void testUpdateAsignacionSinTarea() throws Exception {
        when(asignacionestable.existsById(5)).thenReturn(true);
        when(usuariotable.existsById(1)).thenReturn(true);
        when(asignacionestable.save(any(Asignaciones.class))).thenReturn(new Asignaciones());

        mockMvc.perform(put("/updateAsignacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_asignacion\":5,\"id_usuario\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1));
    }

    @Test
    @DisplayName("Eliminar asignación con cuerpo sin ID y sin parámetros (404)")
    void testDeleteAsignacionSinIdentificador() throws Exception {
        mockMvc.perform(delete("/deleteAsignacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultado").value(0));
    }
}

