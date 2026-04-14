

# Documento de Arquitectura del Sistema de Gestión de Órdenes y Entregas

## 1. Introducción  
Este documento describe la arquitectura inicial del sistema de gestión de órdenes y entregas, incluyendo requisitos funcionales, requisitos de calidad y restricciones que deben ser consideradas en el diseño del software.

**Equipo:**

- Moises Uriel Medina Villa - 2459709
- Santiago Avalo Monsalve - 2359442
- Daniel Gómez Cano - 2359396
- Edward Stivens Pinto - 2359431

**Fecha:** 01/03/2026

---

## 2. Requisitos Funcionales  
Los requisitos funcionales se presentan en forma de **historias de usuario**, especificando los **criterios de aceptación**.

### **Historias de Usuario**
| **ID**    | **Historia de Usuario**         | **Criterios de Aceptación**         | INVEST                                      |
| --------- | ------------------------------- | ----------------------------------- | ------------------------------------------- |
| **US-01** | Como organizador de eventos quiero publicar un evento con su información y número de boletas disponibles para vender entradas a los asistentes. | - El organizador puede crear un evento.<br> - Debe poder definir fecha, lugar y cupo máximo. <br> - El evento queda visible para los usuarios. <br> - No se pueden vender más boletas que el cupo definido. | I: Es independiente a las demas historias. <br> N: Negociable <br> V: Alta <br> E: 5 pts <br> S: Pequeña <br> T: Comprobable |
| **US-02** | Como organizador quiero definir diferentes tipos de boletas (general, VIP, estudiante) para ofrecer distintas opciones de compra a los usuarios. | - El organizador puede crear múltiples tipos de boleta. <br> - Cada tipo tiene precio y cantidad. <br> - El sistema controla el inventario por tipo de boleta. | I: Es independiente a las demas historias. <br> N: Negociable <br> V: Alta <br> E: 3 pts <br> S: Pequeña <br> T: Comprobable|
| **US-03** | Como usuario quiero buscar eventos disponibles para comprar entradas al evento de mi interés. | - El sistema muestra eventos disponibles. <br> - Los usuarios pueden ver información del evento. | I: Es independiente a las demas historias. <br> N: Negociable <br> V: Alta <br> E: 3 pts <br> S: Pequeña <br> T: Comprobable |
| **US-04-A** | Como comprador, quiero seleccionar tipo(s) de boleta y agregarlas a un carrito para revisar antes de pagar. | - Selección persistente en sesión; muestra subtotal y impuestos si aplica. <br> - Reserva temporal de cupo (p. ej. 10 min) para evitar sobreventa durante el checkout.<br> - Manejo de códigos de descuento (aplicar/validar). | I: Es independiente a las demas historias.<br> N: Negociable, nos pueden pedir metodos de pago especificos <br> V: Media <br> E: 3-5 pts <br> S: Pequeña <br> T: Comprobable|
| **US-04-B** | Como comprador, quiero pagar mediante una pasarela y recibir confirmación del estado del pago. |- Integración con pasarela en modo sandbox; flujo de pago seguro. <br> - Estados de orden: pendiente, confirmado, fallido; callbacks manejados idempotentemente. <br> - No duplicar cobros; reintentos controlados. | I: Es independiente a las demas historias. <br> N: Negociable, nos pueden pedir metodos de pago especificos <br> V: Alta <br> E: 8 pts <br> S: Pequeña <br> T: Comprobable|
| **US-05** | Como usuario quiero recibir una boleta digital con código QR después del pago confirmado para poder ingresar al evento. | - Se genera una boleta digital única  con un QR integrado. <br> - La boleta se envía por correo o notificación. <br> - Solo se genera cuando el pago está confirmado. | I: Parcialmente independiente <br> N: Negociable, presetación de la boleta <br> V: Alto <br> E: 5 pts <br> S: Pequeña <br> T: Comprobable|
| **US-06** | Como personal de logística quiero escanear el QR de una boleta para verificar si es válida y permitir el ingreso al evento. | - El sistema verifica si la boleta existe. <br> - El sistema verifica si ya fue usada. <br> - Si es válida, se marca como utilizada. <br> - Si ya fue usada, se rechaza el acceso. | I: Dependiente <br> N: Negociable <br> V: Alta <br> E: 8 pts  <br> S: Pequeña <br> T: Parcialmente comprobable |
| **US-07** | Como organizador quiero modificar precios, cupos o códigos promocionales para gestionar la venta de boletas según la estrategia del evento. | - El organizador puede actualizar precios. <br> - Puede activar o desactivar códigos promocionales. <br> - El sistema registra quién realizó el cambio y cuándo. | I: Dependiente <br> N: Negociable <br> V: Alta <br> E: 5 pts <br> S: Pequeña <br> T: Parcialmente comprobable |
| **US-08** | Como organizador quiero cancelar un evento publicado para notificar a los asistentes y gestionar las devoluciones correspondientes. | - El organizador puede cancelar el evento. <br> - El sistema debe permitir devoluciones a través una pasarela de pagos <br> - El sistema debe notificar a los usuarios de la cancelación del evento | I: Dependiente <br> N: Negociable <br> V: Es valiosa <br> E: 8 pts <br> S: pequeña <br> T: Parcialmente comprobable |
| **US-09** | Como administrador me gustaria poder llevar trazabilidad de las boletas vendidas, las horas donde se vende más, cuando se cae la compra y los problemas con los pagos.  | - El administrador puede ver estadisticas de las ventas <br> - El administrador puede ver errores en las solicitudes de pagos <br> - El administrador puede ver la trazabilidad general de la compra de una boleta | I: Dependiente <br> N: Negociable <br> V: Media <br> E: 5 pts <br> S: pequeña <br> T: Parcialmente comprobable |


>  **Instrucciones:**  
> - Completar al menos **6 historias de usuario**.  
> - Asegurar que cada historia tenga criterios de aceptación claros y verificables.  



### **Historias tecnicas**
| **ID**    | **Historia de Usuario**         | **Criterios de Aceptación**         | INVEST                                      |
| --------- | ------------------------------- | ----------------------------------- | ------------------------------------------- |
| **TS-10** | Como equipo de desarrollo, necesitamos implementar un sistema de gestión de usuarios con autenticación OAuth2 vía Keycloak y control de acceso basado en roles (RBAC), para garantizar que cada actor del sistema (Administrador, Organizador, Cliente) solo pueda ejecutar las acciones que le corresponden.  |- Keycloak configurado con un Realm propio para VivaEventos. <br> - Tres roles definidos en Keycloak: ROLE_ADMIN, ROLE_EVENT_CREATOR, ROLE_CLIENT. <br> - Spring Boot configurado como Resource Server validando tokens JWT emitidos por Keycloak. <br> - Endpoint de login funcional que retorna Access Token y Refresh Token. <br> - Los roles viajan como claims dentro del JWT y son leídos por cada microservicio. <br> - Un ROLE_CLIENT no puede acceder a endpoints de organizador ni administrador; el sistema retorna HTTP 403. <br> - Un ROLE_EVENT_CREATOR solo gestiona sus propios eventos, no los de otros organizadores. <br> - Revocación de sesión funcional desde Keycloak (logout invalida el token). <br> - Contraseñas almacenadas con BCrypt, nunca en texto plano. <br> - Secretos y claves RSA gestionados por variables de entorno, no en el repositorio. | I: Dependiente <br> N: Negociable <br> V: Alta <br> E: 15 pts <br> S: grande <br> T: Comprobable |

---

## 3. Requisitos de Calidad  
Los requisitos de calidad se presentan en forma de **historias de calidad**, siguiendo la estructura de Len Bass.

### **Historias de Calidad**
| **ID**    | **Fuente**         | **Estímulo**         | **Artefactos**         | **Entorno**         | **Respuesta**         | **Medida de Respuesta**         |
| --------- | ------------------ | -------------------- | ---------------------- | ------------------- | --------------------- | ------------------------------- |
| **RQ-01 (Performance)** | Usuario | Realiza una búsqueda de eventos disponibles |API Gateway + servicio de catálogo/eventos |Operación normal |El sistema procesa la consulta y devuelve los eventos disponibles |Tiempo de respuesta ≤ 300 ms en las solicitudes. |
| **RQ-02 (Escalabilidad)** | Sistema |Incremento repentino de solicitudes de compra de boletas | Servicios de órdenes y pagos| Hora pico o alta demanda | El sistema escala automáticamente instancias de los microservicios afectados | Capacidad de soportar hasta 5× la carga normal sin degradar el tiempo de respuesta el cual es >= 300 ms |
| **RQ-03 (Disponibilidad)** |Usuario | Intenta acceder a la plataforma para consultar o comprar boletas | API Gateway y microservicios principales | Operación continua | El sistema mantiene disponibilidad del servicio incluso ante fallas parciales |Disponibilidad ≥ 99% mensual |
| **RQ-04 (Seguridad)** | Usuario malicioso o atacante | Realiza múltiples intentos fallidos de autenticación |Servicio de autenticación y gateway | Operación normal | El sistema detecta intentos sospechosos y bloquea temporalmente la cuenta o IP | Bloqueo automático tras 5 intentos fallidos en menos de 1 minuto |
| **RQ-05 (Integrabilidad)** | Sistema externo (pasarela de pagos) | Se procesa un pago de una boleta | Servicio de pagos | Operación normal | El sistema se comunica con la pasarela y actualiza el estado de la orden | Confirmación o rechazo del pago en ≤ 2 segundos |
| **RQ-06 (Confiabilidad / Mensajería)** | Sistema | Se envía un evento de confirmación de pago | Sistema de mensajería (Kafka/RabbitMQ) y servicio de tickets | Operación normal | El mensaje se procesa sin pérdida y se genera la boleta digital | 0 pérdida de mensajes y reintentos automáticos hasta 3 veces |
| **RQ-07 (Usabilidad)** | Usuario | Compra una boleta y recibe su entrada digital | Servicio de generación de tickets | Operación normal | El sistema genera la boleta con QR y la envía al usuario | Entrega de la boleta digital en ≤ 5 segundos después del pago confirmado |


>  **Instrucciones:**  
> - Completar al menos **6 historias de calidad**, alineadas con atributos clave como **rendimiento, escalabilidad y seguridad**.  
> - Definir cómo se medirá la respuesta esperada ante la situación planteada.  

---

## 4. Restricciones del Sistema  
Las restricciones establecen **limitaciones** en la arquitectura del sistema, ya sean tecnológicas, de negocio, regulatorias o de infraestructura.

### **Lista de Restricciones**
| **Tipo de Restricción** | **Descripción**                                                                                                                                                          |
| ----------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| De negocio   | Se debe realizar el backend del MVP como maximo en 13 semanas.|
| De negocio   | El sistema debe estar conectado con una pasarela de pagos real (sin especificar).|
| De infraestructura | La arquitectura debe implementarse bajo un enfoque de microservicios. |
| De negocio   | Las boletas deben tener su QR respectivo.|
| Tecnológia   | El sistema debe poder desplegarse mediante contenedores (dockers) para facilitar su ejecución en entornos de prueba. |
| Tecnológia   | Se debe utilizar Java con su framework SpringBoot. |

>  **Tipos de restricciones:**  
> - **Tecnológicas:** Lenguajes, frameworks o herramientas que deben utilizarse.  
> - **De negocio:** Normativas o estándares de la empresa.  
> - **Regulatorias:** Cumplimiento de normativas legales o de seguridad.  
> - **De infraestructura:** Limitaciones en hardware, red o almacenamiento.  

