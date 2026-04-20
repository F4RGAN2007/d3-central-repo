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
 