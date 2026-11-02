package com.mx.cbtis.gestor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Cubre el método main() de la aplicación arrancándola en modo ligero:
 * sin servidor web y con una base H2 en memoria independiente.
 */
class Ejercicio5MyApiGestorApplicationMainTest {

    @Test
    @DisplayName("El método main arranca la aplicación sin errores")
    void testMainArrancaAplicacion() {
        assertDoesNotThrow(() -> Ejercicio5MyApiGestorApplication.main(new String[] {
                "--spring.main.web-application-type=none",
                "--spring.main.banner-mode=off",
                "--spring.datasource.url=jdbc:h2:mem:maintest;DB_CLOSE_DELAY=-1",
                "--spring.datasource.driver-class-name=org.h2.Driver",
                "--spring.datasource.username=sa",
                "--spring.datasource.password=",
                "--spring.jpa.hibernate.ddl-auto=create-drop"
        }));
    }
}

