# Proyecto_Desarrollo3

## Desarrolladores:
- Daniel Gomez Cano
- Edward Stivens Pinto Granados
- Santiago Avalo Monsalve
- Moises Uriel Medina Villa

## Requisitos previos
 
| Herramienta    | Versión mínima |
|----------------|----------------|
| Docker         | 24+            |
| Docker Compose | v2 (integrado) |
 
---
 
## Primer arranque (una sola vez)
 
```bash
# 1. Clonar / posicionarse en la raíz del proyecto
cd viva-eventos/
 
# 2. Crear el archivo de entorno a partir del ejemplo
cp .env.example .env
 
# 3. Editar .env y cambiar las contraseñas
#    (KC_DB_PASSWORD y KC_ADMIN_PASSWORD como mínimo)
nano .env   # o el editor de tu preferencia
 
# 4. Crear el directorio de importación de realms
mkdir -p keycloak/realms
 
# 5. Levantar todo el stack
docker compose --env-file .env up -d
```
 
---
 
## Arranque diario del equipo
 
```bash
docker compose --env-file .env up -d
```
 
---
 
## Verificar que está listo
 
```bash
# Ver estado de los contenedores
docker compose ps
 
# Esperar a que Keycloak reporte "healthy"
docker compose --env-file .env up -d --wait
 
# Acceder a la consola de administración
open http://localhost:8080   # o el KC_HTTP_PORT que hayas configurado
```
 
Iniciar sesión con las credenciales definidas en `.env` (`KC_ADMIN_USER` / `KC_ADMIN_PASSWORD`).
 
---



## Pruebas del endpoint ClientController

**Pasos**

- Revisa que el puerto que estas utilizando en la linea 6 del application.properties es igual 
al del .env del keycloak

- Luego inicia keycloak y verifica que esten creado los rols ROLE_CLIENT, ROLE_ADMIN y ROLE_EVENT_CREATOR, 
y que este creado el client: spring-boot-client

- Crear un usuario en el keycloak, con todos los datos y asignarle el rol de ROLE_CLIENT.
Para crearlo van a: Users - CreateUser, despues de poner los datos entran al usuario y se dirigen a
Role mapping -> Assing Role -> Arriba a la izquierda cambia filtrar por clientes a filtrar por roles y 
seleccionan el Rol de cliente en la parte de abajo. 

- Efecuta este comando en la bash cambiando los datos a los del usuario que se creo.
```bash
curl -X POST http://localhost:8387/realms/viva-eventos/protocol/openid-connect/token \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "client_id=spring-boot-client" \
-d "client_secret=EpMx9ekvZemdjofbsUoDFhRDaQ4Iaqw0" \
-d "grant_type=password" \
-d "username=EL_USERNAME_DEL_USUARIO_CREADO" \
-d "password=LA_CONTRASEÑA_DEL_USUARIO_CREADO"
```

- Posteriormente te dara un access_token y lo copiaras y lo utilizaras en este comando
remplazando el TOKEN por el que copiaste.
```bash
curl http://localhost:8081/client/test \
-H "Authorization: Bearer TOKEN"
```
Puedes cambiar el puerto por que estas utilizando