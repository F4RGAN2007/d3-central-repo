# Documento de Arquitectura del Sistema de Gestión de Órdenes y Entregas

## 1. Introducción

Este documento describe la arquitectura inicial del sistema de gestión de órdenes y entregas, incluyendo requisitos
funcionales, requisitos de calidad y restricciones que deben ser consideradas en el diseño del software.

**Equipo:** Los informantes
**Integrantes:**

- Moises Uriel Medina Villa 2459709
- Santiago Avalo Monsalve 2359442
- Daniel Gómez Cano 2359396
- Edward Stivens Pinto 2359431

**Fecha:** 01/03/2026

---

## 2. Requisitos Funcionales

Los requisitos funcionales se presentan en forma de **historias de usuario**, especificando los **criterios de
aceptación**.

### **Historias de Usuario**

| **ID**    | **Historia de Usuario**                                                                                                                                    | **Criterios de Aceptación**                                                                                                                                                                                                                        | INVEST                                                                                                                                                                                      |
|-----------|------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **US-01** | Como administrador quiero un sistema enfocado en microservicios para que pueda desplegarse independiente y ser escalable.                                  | Cada dominio funcional (catálogo, pedidos, inventario, reparto) se implementa como servicio independiente. <br> Cada servicio puede desplegarse sin afectar a los demás.<br> El sistema permite escalar instancias de un servicio bajo alta carga. | Cumple: Independiente (servicios desacoplados), Valiosa (mejora escalabilidad y disponibilidad), Estimable y Testeable (se valida por despliegues y escalado), Pequeña (arquitectura base). |
| **US-02** | Como administrador, quiero que el stock se actualice automáticamente al recibir un pedido para evitar ventas de productos agotados.                        | El stock nunca puede quedar en negativo. <br> La actualización ocurre en menos de 5 segundos tras registrar el pedido.                                                                                                                             | Cumple: Clara y Valiosa, Independiente, Testeable (validar stock), Estimable y de tamaño manejable.                                                                                         |
| **US-03** | Como administrador quiero un gateway para controlar el acceso, seguridad y enrutamiento.                                                                   | Existe un único punto de entrada a la plataforma. <br> El acceso está controlado por roles (cliente, admin, repartidor). <br> Se detectan y bloquean intentos sospechosos (tokens inválidos, múltiples fallos).                                    | Cumple: Valiosa (seguridad), Independiente, Estimable, Testeable (roles, accesos), de tamaño adecuado.                                                                                      |
| **US-04** | Como cliente quiero una plataforma que tenga un flujo de compra, para saber qué productos hay, cuánto cuestan, si están disponibles y poder pedir y pagar. | El cliente puede consultar catálogo con precio y disponibilidad. <br> Puede agregar productos al carrito y confirmar pedido. <br> El pago es simulado y confirma el pedido exitosamente.                                                           | Cumple: Valiosa para el negocio, Negociable, Estimable, Testeable (flujo completo), aunque puede dividirse en futuras historias.                                                            |
| **US-05** | Como cliente, quiero consultar el estado de mi pedido en línea para saber si está aprobado, empacado, en ruta o entregado.                                 | El sistema muestra estados: aprobado, empacado, en ruta, entregado, cancelado.<br> El cliente recibe notificación automática al cambiar de estado.                                                                                                 | Cumple: Independiente, Valiosa (reduce reclamos), Estimable y Testeable mediante cambios de estado.                                                                                         |
| **US-06** | Como repartidor, quiero recibir asignaciones claras de pedidos con dirección y estado para organizar mis entregas.                                         | Cada pedido asignado muestra dirección, contacto y estado. <br> El sistema registra novedades (dirección errada, cliente ausente).                                                                                                                 | Cumple: Clara, Valiosa, Testeable, Estimable y enfocada en un solo rol.                                                                                                                     |
| **US-07** | Como operador, quiero visualizar en tiempo casi real el avance de las entregas para monitorear el cumplimiento.                                            | El sistema actualiza ubicación/estado cada 10–30 segundos.<br> Si hay demora o novedad, se registra y se notifica.                                                                                                                                 | Cumple: Valiosa para operación, Independiente, Testeable (frecuencia), Estimable y adecuada para un sprint.                                                                                 |

> **Instrucciones:**
> - Completar al menos **6 historias de usuario**.
> - Asegurar que cada historia tenga criterios de aceptación claros y verificables.

---

## 3. Requisitos de Calidad

Los requisitos de calidad se presentan en forma de **historias de calidad**, siguiendo la estructura de Len Bass.

### **Historias de Calidad**

| **ID**                                        | **Fuente**                                  | **Estímulo**                                              | **Artefactos**                                 | **Entorno**                     | **Respuesta**                                                                          | **Medida de Respuesta**                                                                    |
|-----------------------------------------------|---------------------------------------------|-----------------------------------------------------------|------------------------------------------------|---------------------------------|----------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| **RQ-01 (Disponibilidad)**                    | Falla de infraestructura                    | Una instancia de un microservicio crítico se cae          | Microservicios (Pedidos, Inventario, Entregas) | Operación normal o en hora pico | El gateway redirige automáticamente las solicitudes a instancias sanas y genera alerta | Redirección en ≤ 5 segundos. Disponibilidad ≥ 99.5% mensual. Alerta generada en ≤ 1 minuto |
| **RQ-02 (Rendimiento)**                       | Usuario (cliente/admin)                     | Realiza una solicitud (consulta catálogo o pedido)        | API Gateway + Microservicios                   | Hora pico                       | El sistema procesa y responde la solicitud sin degradación perceptible                 | Tiempo de respuesta ≤ 300 ms en el 95% de las solicitudes                                  |
| **RQ-03 (Escalabilidad)**                     | Incremento sostenido de usuarios            | Aumento del 50% en la carga durante 10 minutos            | Clúster en Kubernetes                          | Hora pico                       | El sistema levanta nuevas instancias automáticamente sin interrumpir usuarios          | Nuevas instancias activas en ≤ 2 minutos. 0% de caída de servicio durante escalado         |
| **RQ-04 (Seguridad - Autenticación)**         | Usuario malintencionado                     | Múltiples intentos fallidos de autenticación              | Servicio de autenticación (OAuth2 + JWT)       | Operación normal                | El sistema bloquea temporalmente la cuenta y registra evento sospechoso                | Bloqueo tras 5 intentos fallidos en 5 minutos. Evento registrado en logs inmediatamente    |
| **RQ-05 (Seguridad - Autorización)**          | Usuario autenticado                         | Intenta acceder a recurso sin permisos                    | API Gateway / Control de roles                 | Operación normal                | El sistema deniega acceso y registra intento                                           | Respuesta HTTP 403 en ≤ 200 ms. Registro del evento en logs centralizados                  |
| **RQ-06 (Confiabilidad de Mensajería)**       | Falla en procesamiento de mensaje           | Un mensaje de actualización de stock no puede procesarse  | Sistema de mensajería (Kafka/RabbitMQ)         | Operación normal                | El mensaje se reintenta automáticamente o pasa a cola de pendientes (DLQ)              | 0% de pérdida de mensajes. Máximo 3 reintentos antes de enviarlo a DLQ                     |
| **RQ-07 (Consistencia de Inventario)**        | Creación de pedido                          | Se descuenta stock de un producto                         | Servicio de Inventario                         | Alta concurrencia               | El sistema evita stock negativo y mantiene consistencia                                | Stock nunca < 0. Actualización reflejada en ≤ 5 segundos                                   |
| **RQ-08 (Observabilidad)**                    | Error repetitivo o tiempo alto de respuesta | Servicio supera umbral de latencia                        | Sistema completo (logs, métricas, monitoreo)   | Hora pico                       | Se genera alerta automática y queda trazabilidad                                       | Alerta en ≤ 1 minuto si latencia > 300 ms por 3 minutos consecutivos                       |
| **RQ-09 (Actualización casi en tiempo real)** | Repartidor actualiza estado                 | Cambio de estado a “En ruta” o actualización de ubicación | Servicio de Entregas                           | Pedido en proceso de entrega    | El sistema refleja el cambio al cliente y operador                                     | Actualización visible en ≤ 30 segundos                                                     |
| **RQ-10 (Recuperación ante fallos)**          | Reinicio inesperado de servicio             | Servicio vuelve a estar disponible                        | Microservicio afectado                         | Entorno degradado               | El servicio se reintegra al clúster automáticamente                                    | Reintegración en ≤ 2 minutos sin intervención manual                                       |

> **Instrucciones:**
> - Completar al menos **6 historias de calidad**, alineadas con atributos clave como **rendimiento, escalabilidad y
    seguridad**.
> - Definir cómo se medirá la respuesta esperada ante la situación planteada.

---

## 4. Restricciones del Sistema

Las restricciones establecen **limitaciones** en la arquitectura del sistema, ya sean tecnológicas, de negocio,
regulatorias o de infraestructura.

### Lista de Restricciones Identificadas

| Tipo de Restricción | Descripción                                                                                 | Evidencia (frase textual)                                                             |
|---------------------|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| Tecnológica         | La arquitectura debe implementarse bajo un enfoque de microservicios                        | "debemos dividir la solución en servicios (microservicios)"                           |
| Tecnológica         | La solución debe incorporar un API Gateway como punto único de entrada                      | "debe existir un punto único de entrada a la plataforma (un gateway)"                 |
| Tecnológica         | El sistema debe utilizar un stack específico definido por la dirección                      | "La dirección de tecnología también pidió un stack concreto"                          |
| Tecnológica         | El sistema debe implementar trazabilidad mediante métricas, logs y alertas                  | "el sistema debe dejar trazabilidad. Necesitamos métricas, logs y alertas."           |
| Tecnológica         | No se permite la pérdida ni duplicación de mensajes en los flujos de los mensajes           | "no podemos permitir pérdida de mensajes ni duplicaciones que dañen el negocio"       |
| Tecnológica         | El desarrollo debe realizarse con integración continua y despliegue automatico              | "con integración continua y despliegue automatizado"                                  |
| Infraestructura     | La plataforma debe redirigir solicitudes a instancias sanas en caso de fallar               | "redirigiendo a instancias sanas"                                                     |
| Infraestructura     | Los servicios críticos no deben presentar caídas                                            | "no quiero caídas en servicios críticos"                                              |
| Infraestructura     | Los servicios deben mantener tiempos de respuesta menores a 300 ms                          | "si un servicio tarda más de 300 ms en responder"                                     |
| Infraestructura     | Debe ser posible escalar levantando nuevas instancias sin interrumpir a los usuarios        | "debe ser posible levantar más instancias sin interrumpir a los usuarios"             |
| Infraestructura     | El sistema debe actualizar el estado de pedidos en intervalos frecuentes (10 a 30 segundos) | "actualización frecuente (por ejemplo, cada 10 a 30 segundos)"                        |
| De negocio          | Los pedidos deben asignarse de manera ordenada a los repartidores                           | "los pedidos se asignen de manera ordenada a repartidores"                            |
| De negocio          | Las demoras o novedades en pedidos deben quedar registradas y ser visibles                  | "Si un pedido se demora o se presenta una novedad, debe quedar registrado y visible." |
| De negocio          | El equipo debe trabajar bajo un proceso tipo SCRUM                                          | "un proceso de trabajo tipo SCRUM con sprints cortos, dailies y revisiones"           |
| Regulatoria         | Los accesos al sistema deben gestionarse por roles                                          | "Los accesos deben ser por roles"                                                     |
| Regulatoria         | El sistema debe detectar y reaccionar ante intentos sospechosos de ingreso                  | "se debe detectar y reaccionar ante intentos sospechosos"                             |

> **Tipos de restricciones:**
> - **Tecnológicas:** Lenguajes, frameworks o herramientas que deben utilizarse.
> - **De negocio:** Normativas o estándares de la empresa.
> - **Regulatorias:** Cumplimiento de normativas legales o de seguridad.
> - **De infraestructura:** Limitaciones en hardware, red o almacenamiento.  



