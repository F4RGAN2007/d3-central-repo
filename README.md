# VivaEventos — Documentación de Arquitectura de Microservicios


## 📁 Estructura del Repositorio (Monorepo de momento)

Los microservicios convivirán en este repositorio, cada uno en su propia carpeta con responsabilidades bien definidas. Esto puede cambiar en la próxima sesión con el profesor:

| Carpeta | Descripción |
|---|---|
| [/auth-service](https://github.com/daniel-gomez-cano/d3-auth-service) | Gestión de usuarios, roles y permisos. Emisión y validación de tokens JWT. |
| [/event-service](https://github.com/daniel-gomez-cano/d3-event-service) | Creación de eventos, tipos de boleta, cupos, precios y códigos promocionales. |
| [/order-service](https://github.com/daniel-gomez-cano/d3-order-service) | Flujo de compra: reserva temporal de cupos, descuentos y estados de orden. |
| [/payment-service](https://github.com/daniel-gomez-cano/d3-payment-service) | Integración con pasarela de pagos, webhooks, reconciliación y reembolsos. |
| [/ticket-service](https://github.com/daniel-gomez-cano/d3-ticket-service) | Generación de boletas digitales con QR único y validación en puerta. |
| [/notification-service](https://github.com/daniel-gomez-cano/d3-notification-service) | Envío asíncrono de correos y mensajes (confirmación, recordatorio, cancelación). |
| [/analytics-audit-service](https://github.com/daniel-gomez-cano/d3-analytics-audit-service) | Dashboard en tiempo real para el gerente y log inmutable de auditoría. |


---

## 🏛️ Descripción General de la Arquitectura

Se empleará una arquitectura de microservicios con los principios de base de datos por servicio, comunicación mixta (síncrona y asíncrona) y escalabilidad horizontal independiente por componente.

>Este diagrama de componentes lo hicimos a modo de boceto, Claude nos ayudó a generar la imagen. Este cambiará según recomendaciones del profesor.

![alt text](image.png)

---

## 👥 Equipo

| Nombre    | Correo |
|----------------|----------------|
| Daniel Gómez Cano         | daniel.gomez.cano@correounivalle.edu.co |
|Santiago Avalo Monsalve | avalo.santiago@correounivalle.edu.co |
|Edward Stivens Pinto Granados| edward.pinto@correounivalle.edu.co |
|Moises Uriel Medina Villa | moises.medina@correounivalle.edu.co |
