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

import com.mx.cbtis.gestor.modelo.Comentarios;
import com.mx.cbtis.gestor.repository.ComentariosRepository;
import com.mx.cbtis.gestor.repository.TareasRepository;

/**
 * Pruebas de cobertura de ramas (branch coverage) para ComentariosController.
 */
@WebMvcTest(ComentariosController.class)
class ComentariosControllerRamasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComentariosRepository comentariostable;

    @MockBean
    private TareasRepository tareastable;

    @Test
    @DisplayName("Agregar comentario con ID nuevo no existente (200)")
    void testAddComentarioIdNuevo() throws Exception {
        when(comentariostable.existsById(50)).thenReturn(false);
        when(tareastable.existsById(1)).thenReturn(true);
        when(comentariostable.save(any(Comentarios.class))).thenReturn(new Comentarios());

        mockMvc.perform(post("/addComentario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_comentario\":50,\"id_tarea\":1,\"texto_comentario\":\"Hola\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1));
    }

    @Test
    @DisplayName("Agregar comentario sin id_tarea (400, mensaje 'nulo')")
    void testAddComentarioTareaNula() throws Exception {
        mockMvc.perform(post("/addComentario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"texto_comentario\":\"Sin tarea\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La tarea asociada (id_tarea: nulo) no existe"));
    }

    @Test
    @DisplayName("Actualizar comentario sin cuerpo (400)")
    void testUpdateComentarioSinCuerpo() throws Exception {
        mockMvc.perform(put("/updateComentario"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Datos incompletos"));
    }

    @Test
    @DisplayName("Actualizar comentario sin cambiar tarea (200)")
    void testUpdateComentarioSinTarea() throws Exception {
        when(comentariostable.existsById(5)).thenReturn(true);
        when(comentariostable.save(any(Comentarios.class))).thenReturn(new Comentarios());

        mockMvc.perform(put("/updateComentario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id_comentario\":5,\"texto_comentario\":\"Editado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1));
    }

    @Test
    @DisplayName("Eliminar comentario con cuerpo sin ID y sin parámetros (404)")
    void testDeleteComentarioSinIdentificador() throws Exception {
        mockMvc.perform(delete("/deleteComentario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultado").value(0));
    }
}

