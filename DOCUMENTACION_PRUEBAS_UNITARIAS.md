# Documentación de Pruebas Unitarias
## Sistema de Arriendo de Bicicletas — Microservicios

---

## ¿Qué es una prueba unitaria?

Una prueba unitaria verifica que **una sola unidad de código** (en este caso, un método de servicio) funcione correctamente de forma **aislada**, sin necesidad de base de datos, servidores ni servicios externos.

Para lograr el aislamiento se usa **Mockito**, que reemplaza las dependencias reales (repositorios, clientes Feign) con objetos simulados llamados **mocks**. Así podemos controlar exactamente qué retorna cada dependencia y verificar que el código reaccione correctamente.

### Anotaciones utilizadas

| Anotación | Significado |
|---|---|
| `@ExtendWith(MockitoExtension.class)` | Activa Mockito para la clase de test |
| `@Mock` | Crea un objeto simulado de esa dependencia |
| `@InjectMocks` | Crea la clase real e inyecta los mocks automáticamente |
| `when(...).thenReturn(...)` | Define qué retorna un mock cuando se llama un método |
| `verify(mock).metodo(...)` | Verifica que ese método fue llamado durante la prueba |
| `assertThrows(...)` | Verifica que se lanzó una excepción |
| `assertEquals(esperado, real)` | Verifica que dos valores son iguales |

---

## 1. BicicletaServiceTest (11 pruebas)

**Ubicación:** `ms-bicicleta/.../Service/BicicletaServiceTest.java`  
**Clase probada:** `BicicletaService`  
**Dependencia mockeada:** `BicicletaRepository`

---

### `getBicycles_retornaListaCompleta`
**Qué hace:** Verifica que el método `getBicycles()` retorna todas las bicicletas del repositorio.  
**Cómo funciona:**
1. Se configura el mock para que `repository.findAll()` retorne una lista con una bicicleta.
2. Se llama a `service.getBicycles()`.
3. Se verifica que el resultado tiene exactamente 1 elemento.

---

### `getBicycle_idExistente_retornaOptional`
**Qué hace:** Verifica que buscar por un ID existente retorna un `Optional` con la bicicleta.  
**Cómo funciona:**
1. El mock retorna `Optional.of(bicicleta)` para el ID `1`.
2. Se verifica que el resultado no está vacío (`isPresent() == true`).

---

### `crearBicicleta_codigoNuevo_guardaYRetorna`
**Qué hace:** Verifica que una bicicleta con código nuevo se guarda correctamente.  
**Cómo funciona:**
1. El mock indica que el código `"B001"` NO existe (`existsByCodigo = false`).
2. El mock retorna la bicicleta al llamar `save()`.
3. Se verifica que el resultado no es nulo y que `repository.save()` fue invocado.

---

### `crearBicicleta_codigoDuplicado_lanzaExcepcion`
**Qué hace:** Verifica que si el código ya existe, se lanza una excepción y **no** se guarda nada.  
**Cómo funciona:**
1. El mock indica que el código `"B001"` SÍ existe (`existsByCodigo = true`).
2. Se espera que `crearBicicleta()` lance `RuntimeException`.
3. Se verifica que `save()` **nunca fue llamado** con `verify(repository, never()).save(any())`.

---

### `actualizarBicicleta_bicicletaExiste_actualizaYGuarda`
**Qué hace:** Verifica que actualizar una bicicleta existente guarda los cambios.  
**Cómo funciona:**
1. `findById(1L)` retorna la bicicleta existente.
2. `findByCodigo()` retorna la misma bicicleta (mismo ID → el código le pertenece, no hay conflicto).
3. Se verifica que `save()` fue llamado con la bicicleta modificada.

---

### `actualizarBicicleta_noExiste_lanzaExcepcion`
**Qué hace:** Verifica que actualizar una bicicleta con ID inexistente lanza excepción.  
**Cómo funciona:**
1. `findById(99L)` retorna `Optional.empty()`.
2. El servicio llama `.orElseThrow()` que lanza `RuntimeException`.
3. Se espera esa excepción con `assertThrows`.

---

### `actualizarBicicleta_codigoUsadoPorOtra_lanzaExcepcion`
**Qué hace:** Verifica que no se puede usar un código que pertenece a otra bicicleta distinta.  
**Cómo funciona:**
1. `findByCodigo()` retorna una bicicleta con ID `2` (diferente al `1` que se está actualizando).
2. El servicio detecta el conflicto y lanza `RuntimeException`.

---

### `ocuparBicicleta_existente_cambiaEstadoOcupada`
**Qué hace:** Verifica que `ocuparBicicleta()` cambia el estado a `"OCUPADA"`.  
**Cómo funciona:**
1. El mock retorna la bicicleta con estado `"DISPONIBLE"`.
2. El servicio cambia el estado a `"OCUPADA"` y llama `save()`.
3. Se verifica que el resultado tiene `estado = "OCUPADA"`.

---

### `ocuparBicicleta_noExiste_lanzaExcepcion`
**Qué hace:** Verifica que ocupar una bicicleta inexistente lanza excepción.  
**Cómo funciona:** `findById(99L)` retorna vacío → `orElseThrow()` lanza excepción.

---

### `liberarBicicleta_existente_cambiaEstadoDisponible`
**Qué hace:** Verifica que `liberarBicicleta()` cambia el estado a `"DISPONIBLE"`.  
**Cómo funciona:** Igual que ocupar, pero verifica `estado = "DISPONIBLE"`.

---

### `deleteBicycle_llamaDeleteById`
**Qué hace:** Verifica que eliminar una bicicleta llama al repositorio con el ID correcto.  
**Cómo funciona:** Llama `service.deleteBicycle(1L)` y verifica con `verify()` que `repository.deleteById(1L)` fue invocado exactamente una vez.

---

## 2. UsuarioServiceTest (9 pruebas)

**Ubicación:** `ms-user/.../Service/UsuarioServiceTest.java`  
**Clase probada:** `UsuarioService`  
**Dependencias mockeadas:** `UsuarioRepository`, `PasswordEncoder`

---

### `getClients_retornaListaCompleta`
**Qué hace:** Verifica que `getClients()` retorna todos los usuarios.

---

### `getClient_idExistente_retornaOptional`
**Qué hace:** Verifica que buscar por ID existente retorna un Optional con el usuario.

---

### `creUsuario_emailNuevo_hashPasswordYGuarda`
**Qué hace:** Verifica que al registrar un usuario nuevo, la contraseña se hashea antes de guardar.  
**Cómo funciona:**
1. El mock indica que el email NO existe.
2. `passwordEncoder.encode("123456")` retorna `"$2a$10$hashed"`.
3. Se verifica que `encode()` fue llamado y que `save()` fue llamado con la contraseña ya hasheada.

---

### `creUsuario_emailDuplicado_lanzaExcepcion`
**Qué hace:** Verifica que registrar un email ya existente lanza excepción sin guardar nada.  
**Cómo funciona:** `existsByEmail` retorna `true` → excepción → `save()` nunca se llama.

---

### `actualizarUsuario_existente_actualizaDatos`
**Qué hace:** Verifica que los datos del usuario se actualizan y se guarda correctamente.

---

### `actualizarUsuario_conNuevaPassword_hashNuevaPassword`
**Qué hace:** Verifica que si se envía una nueva contraseña en la actualización, también se hashea.  
**Cómo funciona:**
1. El usuario actualizado tiene `password = "nueva123"` (no vacío).
2. Se verifica que `passwordEncoder.encode("nueva123")` fue llamado.

---

### `actualizarUsuario_noExiste_lanzaExcepcion`
**Qué hace:** Verifica que actualizar un usuario inexistente lanza excepción.

---

### `obtenerPorEmail_existente_retornaDTOConPassword`
**Qué hace:** Verifica que el endpoint usado por ms-auth retorna el DTO **incluyendo el password hasheado**.  
**Cómo funciona:** Este es el fix crítico que corregimos: el DTO ahora incluye el campo `password` para que Spring Security pueda autenticar. El test verifica que `dto.getPassword()` no es nulo.

---

### `deleteClient_llamaDeleteById`
**Qué hace:** Verifica que eliminar un usuario llama al repositorio con el ID correcto.

---

## 3. ReservaServiceTest (11 pruebas)

**Ubicación:** `ms-reservas/.../Service/ReservaServiceTest.java`  
**Clase probada:** `ReservaService`  
**Dependencias mockeadas:** `ReservaRepository`, `UsuarioFeignClient`, `BicicletaFeignClient`, `NotificacionFeign`

---

### `crearReserva_datosValidos_retornaReservaActiva`
**Qué hace:** Verifica el flujo completo de creación de una reserva válida.  
**Cómo funciona:**
1. El mock de usuario retorna un usuario con ID `1`.
2. El mock de bicicleta retorna una bicicleta en estado `"DISPONIBLE"`.
3. No hay reservas activas del usuario ni de la bicicleta.
4. Se guarda la reserva y se verifica que se envió una notificación.

---

### `crearReserva_bicicletaNoDisponible_lanzaExcepcion`
**Qué hace:** Verifica que no se puede reservar una bicicleta que está `"OCUPADA"`.  
**Cómo funciona:** El mock de bicicleta retorna `estado = "OCUPADA"` → el servicio lanza excepción.

---

### `crearReserva_usuarioYaTieneReservaActiva_lanzaExcepcion`
**Qué hace:** Verifica que un usuario no puede tener dos reservas activas simultáneas.  
**Cómo funciona:** `repository.existsByUsuarioIdAndEstado(1L, "ACTIVA")` retorna `true` → excepción.

---

### `crearReserva_bicicletaYaReservada_lanzaExcepcion`
**Qué hace:** Verifica que no se puede reservar una bicicleta que ya tiene una reserva activa.  
**Cómo funciona:** `repository.existsByBicicletaIdAndEstado(1L, "ACTIVA")` retorna `true` → excepción.

---

### `listarReservas_retornaLista`
**Qué hace:** Verifica que `listarReservas()` retorna todas las reservas del repositorio.

---

### `buscarPorId_existente_retornaReserva`
**Qué hace:** Verifica que buscar por ID existente retorna la reserva correcta.

---

### `buscarPorId_noExiste_lanzaExcepcion`
**Qué hace:** Verifica que buscar un ID inexistente lanza excepción.

---

### `cancelarReserva_cambiaEstadoCancelada`
**Qué hace:** Verifica que cancelar una reserva cambia su estado a `"CANCELADA"`.

---

### `completarReserva_activa_cambiaEstadoUtilizada`
**Qué hace:** Verifica que completar una reserva activa cambia su estado a `"UTILIZADA"`.

---

### `completarReserva_noActiva_lanzaExcepcion`
**Qué hace:** Verifica que no se puede completar una reserva que no está activa (ej. ya cancelada).  
**Cómo funciona:** La reserva tiene `estado = "CANCELADA"` → el servicio verifica que sea `"ACTIVA"` → lanza excepción.

---

### `buscarReservaActiva_encontrada_retornaReserva`
**Qué hace:** Verifica que buscar la reserva activa de un usuario para una bicicleta concreta la retorna correctamente.

---

## 4. RentalServiceTest (10 pruebas)

**Ubicación:** `ms-rental/.../Service/RentalServiceTest.java`  
**Clase probada:** `RentalService`  
**Dependencias mockeadas:** `PrestamoRepository`, `UsuarioFeign`, `ReservaFeign`, `BicicletaFeign`, `PagoFeign`, `NotificacionesFeign`

---

### `iniciarPrestamo_datosValidos_creaPrestamoActivo`
**Qué hace:** Verifica el flujo completo de inicio de préstamo.  
**Cómo funciona:**
1. Se mockean usuario, reserva activa y bicicleta disponible.
2. El repositorio guarda el préstamo con estado `"ACTIVO"`.
3. Se verifica que la bicicleta fue marcada como ocupada (`ocuparBicicleta()`).
4. Se verifica que la reserva fue marcada como utilizada (`completarReserva()`).
5. Se verifica que se envió una notificación.

---

### `iniciarPrestamo_usuarioNoEncontrado_lanzaExcepcion`
**Qué hace:** Verifica que si el Feign de usuario retorna `null`, se lanza excepción.  
**Cómo funciona:** `usuarioFeign.obtenerPorEmail()` retorna `null` → el servicio detecta el null → excepción.

---

### `iniciarPrestamo_sinReservaActiva_lanzaExcepcion`
**Qué hace:** Verifica que no se puede iniciar un préstamo sin una reserva activa previa.  
**Cómo funciona:** `reservaFeign.obtenerReservaActiva()` retorna `null` → excepción.

---

### `iniciarPrestamo_bicicletaNoDisponible_lanzaExcepcion`
**Qué hace:** Verifica que la bicicleta debe estar disponible para iniciar el préstamo.

---

### `finalizarPrestamo_activo_cambiaAFinalizado`
**Qué hace:** Verifica el flujo completo de finalización de préstamo.  
**Cómo funciona:**
1. El préstamo tiene `fechaInicio` hace 45 minutos (para calcular el tiempo de uso).
2. El servicio calcula los minutos, actualiza el estado a `"FINALIZADO"`.
3. Se verifica que la bicicleta fue liberada (`liberarBicicleta()`).
4. Se verifica que se calculó el pago (`pagoFeign.calcularPago()`).
5. Se verifica que se envió notificación.

---

### `finalizarPrestamo_yaFinalizado_lanzaExcepcion`
**Qué hace:** Verifica que un préstamo ya finalizado no se puede finalizar de nuevo.

---

### `finalizarPrestamo_noExiste_lanzaExcepcion`
**Qué hace:** Verifica que finalizar un ID inexistente lanza excepción.

---

### `buscarPorId_existente_retornaPrestamo`
**Qué hace:** Verifica que buscar un préstamo por ID existente lo retorna correctamente.

---

### `buscarPorId_noExiste_lanzaExcepcion`
**Qué hace:** Verifica que buscar un ID inexistente lanza excepción.

---

### `listar_retornaLista`
**Qué hace:** Verifica que `listar()` retorna todos los préstamos.

---

## 5. PagoServiceTest (11 pruebas)

**Ubicación:** `ms-pago/.../Service/PagoServiceTest.java`  
**Clase probada:** `PagoService`  
**Dependencias mockeadas:** `PagoRepository`, `NotificacionFeign`

---

### `calcularPago_menosDe30Min_montoBase1000`
**Qué hace:** Verifica que usar la bicicleta 20 minutos genera un cobro de `$1.000`.  
**Cómo funciona:**
1. `CalculoRequest` lleva `minutosUso = 20`.
2. No existe pago previo para ese préstamo.
3. El servicio calcula `monto = 1000` (tarifa base, sin excedente).
4. Se verifica que se envió notificación de tipo `"PAGO_GENERADO"`.

---

### `calcularPago_exactamente30Min_montoBase1000`
**Qué hace:** Verifica que exactamente 30 minutos no genera cobro adicional (límite del bloque base).

---

### `calcularPago_60Min_monto1500`
**Qué hace:** Verifica que 60 minutos (30 base + 1 bloque extra de 30 min) genera `$1.500`.  
**Lógica del servicio:**
```
monto = 1000
bloquesExtra = (60 - 30) / 30 = 1
monto += 1 * 500 = 1500
```

---

### `calcularPago_pagoYaExiste_lanzaExcepcion`
**Qué hace:** Verifica que no se puede calcular un pago si ya existe uno para ese préstamo.  
**Cómo funciona:** `repository.existsByPrestamoId(1L)` retorna `true` → excepción → `save()` nunca se llama.

---

### `pagar_estadoPendiente_cambiaAPagado`
**Qué hace:** Verifica que un pago pendiente se puede marcar como pagado.  
**Cómo funciona:**
1. El pago tiene `estado = "PENDIENTE"`.
2. El servicio cambia el estado a `"PAGADO"` y asigna `fechaPago`.
3. Se verifica que `fechaPago` no es nulo y que se envió notificación.

---

### `pagar_yaEstabaPagado_lanzaExcepcion`
**Qué hace:** Verifica que un pago ya realizado no se puede pagar de nuevo.

---

### `pagar_noExiste_lanzaExcepcion`
**Qué hace:** Verifica que pagar un ID inexistente lanza excepción.

---

### `buscarPorId_existente_retornaPago`
**Qué hace:** Verifica que buscar un pago por ID existente lo retorna.

---

### `buscarPorId_noExiste_lanzaExcepcion`
**Qué hace:** Verifica que buscar un ID inexistente lanza excepción.

---

### `obtenerPagosUsuario_retornaLista`
**Qué hace:** Verifica que se retornan los pagos de un usuario específico.

---

### `listar_retornaLista`
**Qué hace:** Verifica que `listar()` retorna todos los pagos registrados.

---

## 6. NotificacionServiceTest (2 pruebas)

**Ubicación:** `ms-notificacion/.../Service/NotificacionServiceTest.java`  
**Clase probada:** `NotificacionService`  
**Dependencias mockeadas:** Ninguna (sin dependencias externas)

---

### `enviar_requestValido_noLanzaExcepcion`
**Qué hace:** Verifica que enviar una notificación con datos válidos no lanza ninguna excepción.  
**Cómo funciona:** `assertDoesNotThrow()` ejecuta el método y pasa si no ocurre ningún error. El servicio imprime por consola, lo que no puede fallar salvo errores de sistema.

---

### `enviar_distintosTipos_noLanzaExcepcion`
**Qué hace:** Verifica que todos los tipos de notificación del sistema se procesan sin errores.  
**Cómo funciona:** Se itera sobre los 5 tipos de evento (`RESERVA_CREADA`, `PRESTAMO_INICIADO`, `PRESTAMO_FINALIZADO`, `PAGO_GENERADO`, `PAGO_REALIZADO`) y se verifica que ninguno lanza excepción.

---

## 7. AuthServiceTest (2 pruebas)

**Ubicación:** `ms-auth/.../Service/AuthServiceTest.java`  
**Clase probada:** `AuthService`  
**Dependencias mockeadas:** `AuthenticationManager`, `JwtService`

---

### `login_credencialesValidas_retornaToken`
**Qué hace:** Verifica que un login con credenciales correctas retorna un JWT.  
**Cómo funciona:**
1. `authenticationManager.authenticate()` no lanza excepción (credenciales correctas).
2. `jwtService.generateToken("juan@test.com")` retorna `"jwt.token.mock"`.
3. Se verifica que la respuesta tiene el token esperado.
4. Se verifica que `authenticationManager.authenticate()` fue llamado exactamente una vez.
5. Se verifica que `generateToken()` fue llamado con el email correcto.

---

### `login_credencialesInvalidas_lanzaExcepcion`
**Qué hace:** Verifica que credenciales incorrectas lanzan `BadCredentialsException` sin generar token.  
**Cómo funciona:**
1. `doThrow(new BadCredentialsException(...)).when(authenticationManager).authenticate(any())` configura el mock para lanzar la excepción.
2. Se verifica que `jwtService.generateToken()` **nunca fue llamado** (no se generó token con credenciales inválidas).

---

---

# Cómo hacer fallar las pruebas

A continuación se muestra cómo hacer fallar tests específicos modificando el código de las pruebas. Útil para demostrar que los tests sí detectan errores.

---

## Método 1 — Cambiar el valor esperado en `assertEquals`

Esta es la forma más sencilla. El test fallará con un mensaje claro de "expected X but was Y".

### Ejemplo en `BicicletaServiceTest`

```java
// ORIGINAL (pasa)
assertEquals("OCUPADA", result.getEstado());

// MODIFICADO (falla)
assertEquals("LIBRE", result.getEstado());
// Error: expected: <LIBRE> but was: <OCUPADA>
```

### Ejemplo en `PagoServiceTest`

```java
// ORIGINAL (pasa)
assertEquals(1000.0, result.getMonto());

// MODIFICADO (falla)
assertEquals(500.0, result.getMonto());
// Error: expected: <500.0> but was: <1000.0>
```

---

## Método 2 — Cambiar el estado esperado en una reserva o préstamo

```java
// En ReservaServiceTest — ORIGINAL (pasa)
assertEquals("CANCELADA", result.getEstado());

// MODIFICADO (falla)
assertEquals("ACTIVA", result.getEstado());
// Error: expected: <ACTIVA> but was: <CANCELADA>
```

```java
// En RentalServiceTest — ORIGINAL (pasa)
assertEquals("FINALIZADO", result.getEstado());

// MODIFICADO (falla)
assertEquals("ACTIVO", result.getEstado());
// Error: expected: <ACTIVO> but was: <FINALIZADO>
```

---

## Método 3 — Eliminar el `when()` de un mock

Sin el `when()`, el mock retorna `null` por defecto, lo que causa un `NullPointerException` en el servicio.

```java
// En BicicletaServiceTest — ORIGINAL (pasa)
when(repository.existsByCodigo("B001")).thenReturn(false);
when(repository.save(b)).thenReturn(b);
Bicicleta result = service.crearBicicleta(b);
assertNotNull(result);

// MODIFICADO: eliminar el when del save (falla con NullPointerException)
when(repository.existsByCodigo("B001")).thenReturn(false);
// when(repository.save(b)).thenReturn(b);  ← comentar esta línea
Bicicleta result = service.crearBicicleta(b);
assertNotNull(result);
// Error: NullPointerException o AssertionError: expected not null
```

---

## Método 4 — Cambiar `assertThrows` para que espere la excepción equivocada

```java
// En UsuarioServiceTest — ORIGINAL (pasa)
assertThrows(RuntimeException.class, () -> service.creUsuario(u));

// MODIFICADO (falla porque el servicio lanza RuntimeException, no IllegalArgumentException)
assertThrows(IllegalArgumentException.class, () -> service.creUsuario(u));
// Error: Expected IllegalArgumentException but no exception was thrown
//        (o se lanzó RuntimeException en su lugar)
```

---

## Método 5 — Cambiar el `verify()` para que revise la llamada equivocada

```java
// En BicicletaServiceTest — ORIGINAL (pasa)
verify(repository).deleteById(1L);

// MODIFICADO (falla porque se llamó con 1L, no con 99L)
verify(repository).deleteById(99L);
// Error: Wanted but not invoked: repository.deleteById(99L)
//        However, there were other interactions: repository.deleteById(1L)
```

---

## Método 6 — Cambiar `never()` por una verificación que sí ocurrió

```java
// En UsuarioServiceTest — ORIGINAL (pasa, verifica que save NO fue llamado)
verify(repository, never()).save(any());

// MODIFICADO (falla porque save realmente nunca se llama, pero pedimos que sí lo haya hecho)
verify(repository, times(1)).save(any());
// Error: Wanted but not invoked: repository.save(...)
```

---

## Resumen visual de fallos por servicio

| Servicio | Test a modificar | Cambio sugerido | Error esperado |
|---|---|---|---|
| ms-bicicleta | `ocuparBicicleta_existente_cambiaEstadoOcupada` | `"OCUPADA"` → `"LIBRE"` | `expected: <LIBRE> but was: <OCUPADA>` |
| ms-bicicleta | `crearBicicleta_codigoDuplicado_lanzaExcepcion` | Cambiar `never()` por `times(1)` | `Wanted but not invoked: save(...)` |
| ms-user | `obtenerPorEmail_existente_retornaDTOConPassword` | `dto.getPassword()` por `dto.getNombre()` en el assertEquals | `expected: <Juan Pérez> but was: <$2a$10$hashed>` |
| ms-reservas | `completarReserva_activa_cambiaEstadoUtilizada` | `"UTILIZADA"` → `"COMPLETADA"` | `expected: <COMPLETADA> but was: <UTILIZADA>` |
| ms-rental | `finalizarPrestamo_activo_cambiaAFinalizado` | `"FINALIZADO"` → `"CERRADO"` | `expected: <CERRADO> but was: <FINALIZADO>` |
| ms-pago | `calcularPago_menosDe30Min_montoBase1000` | `1000.0` → `2000.0` | `expected: <2000.0> but was: <1000.0>` |
| ms-auth | `login_credencialesValidas_retornaToken` | `"jwt.token.mock"` → `"token.incorrecto"` | `expected: <token.incorrecto> but was: <jwt.token.mock>` |

---

## Cómo ejecutar los tests en VS Code

### Ejecutar todos los tests de un módulo
```powershell
cd "C:\Users\Duoc\Downloads\Este si ctm\ms-bicicleta\ms-bicicleta"
.\mvnw.cmd test
```

### Ejecutar solo una clase de test
```powershell
.\mvnw.cmd test -Dtest=BicicletaServiceTest
```

### Ejecutar solo un método específico
```powershell
.\mvnw.cmd test -Dtest=BicicletaServiceTest#ocuparBicicleta_existente_cambiaEstadoOcupada
```

### Desde el panel de Testing de VS Code
Con la extensión **Test Runner for Java** instalada, aparece un ícono de tubo de ensayo en la barra lateral. Desde ahí se puede:
- Ejecutar todos los tests con ▶▶
- Ejecutar un test individual con ▶ al lado del método
- Ver resultados con ✅ (pasó) o ❌ (falló) directamente en el editor
