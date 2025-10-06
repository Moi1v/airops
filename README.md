# Sistema de Planificación de Vuelos - Airops

## 👥 Integrantes
- **Lourdes Alvarado**
- **Moisés Cabrera**

## 📋 Descripción del Proyecto
Sistema de planificación y gestión de vuelos desarrollado con Jakarta EE que permite la administración completa de operaciones aéreas mediante una interfaz calendario interactiva.

## 🚀 Funcionalidades Implementadas

### ✅ Gestión Completa de Vuelos
- **Visualización en Calendario** - Interfaz intuitiva con PrimeFaces Schedule
- **Creación de Vuelos** - Formulario completo con validaciones
- **Edición en Tiempo Real** - Modificación directa desde el calendario
- **Eliminación Segura** - Confirmación antes de eliminar registros
- **Validaciones Avanzadas** - Cumplimiento de reglas de negocio y restricciones del modelo

### 🔧 Características Técnicas
- **Persistencia Robusta** - Conexión con PostgreSQL en contenedor Docker
- **Validaciones** - Implementación de Bean Validation con mensajes personalizados
- **Interfaz Responsiva** - Diseño adaptativo con CSS Grid
- **Operaciones AJAX** - Actualizaciones sin recarga de página
- **Manejo de Errores** - Sistema completo de notificaciones al usuario

## 🗄️ Configuración de Base de Datos

### Ejecutar Contenedor PostgreSQL:
```bash
docker run --name postgres-airops \
  -e POSTGRES_PASSWORD=admin123 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=airops \
  -p 5432:5432 \
  -d postgres
Estructura de la Base de Datos:
Tabla: Vuelos (Flights)

Campos: Número de vuelo, origen, destino, fechas, piloto, aeronave, pasajeros

Relaciones: Pilotos y Aeronaves como entidades relacionadas

🛠️ Tecnologías Utilizadas
Backend
Jakarta EE 9+ - Plataforma empresarial

CDI (Contexts and Dependency Injection) - Inyección de dependencias

Bean Validation - Validación de datos

JPA (Java Persistence API) - Persistencia de datos

Frontend
JSF (Jakarta Server Faces) - Framework de interfaz de usuario

PrimeFaces - Componentes UI ricos

CSS Grid - Layout responsivo

JavaScript - Interactividad del calendario

Base de Datos e Infraestructura
PostgreSQL - Base de datos relacional

Docker - Contenedorización de servicios

Maven - Gestión de dependencias y build
📁 Estructura del Proyecto
airops/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── darwinruiz/
│   │   │           └── airops/
│   │   │               ├── controllers/     # Managed Beans
│   │   │               ├── models/          # Entidades JPA
│   │   │               ├── services/        # Lógica de negocio
│   │   │               └── repositories/    # Acceso a datos
│   │   ├── resources/
│   │   │   ├── META-INF/
│   │   │   │   └── persistence.xml         # Configuración JPA
│   │   │   └── ValidationMessages.properties # Mensajes validación
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml                 # Configuración web
│   │       ├── templates/
│   │       │   └── layout.xhtml            # Template principal
│   │       └── *.xhtml                     # Vistas JSF
└── pom.xml                                 # Configuración Maven

🎯 Criterios de Evaluación Cumplidos
✅ Funcionalidad de Edición
Diálogo modal al hacer clic en evento del calendario

Formulario completo para modificación de datos del vuelo

Validaciones conforme al modelo Flight y ValidationMessages.properties

Actualización automática del calendario tras guardar cambios

✅ Funcionalidad de Eliminación
Opción de eliminar en diálogo de detalles

Confirmación previa a la eliminación

Desaparición inmediata del evento del calendario

Eliminación persistente en base de datos

✅ Persistencia y Servicios
Uso de FlightService y FlightRepository

Conexión con PostgreSQL en contenedor Docker

Operaciones CRUD completas en base de datos

✅ Calidad de Código
Código ordenado y comentado

Convenciones consistentes con el proyecto

Manejo apropiado de excepciones

Separación clara de responsabilidades

🚀 Instrucciones de Ejecución
Iniciar Base de Datos:

bash
docker run --name postgres-airops -e POSTGRES_PASSWORD=admin123 -e POSTGRES_USER=postgres -e POSTGRES_DB=airops -p 5432:5432 -d postgres

Compilar Proyecto:
bash
mvn clean package
