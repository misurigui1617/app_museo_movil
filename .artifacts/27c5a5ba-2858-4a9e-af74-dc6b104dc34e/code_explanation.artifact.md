# Explicación General del Código de App Museo

Este documento está diseñado exclusivamente para que puedas **aprender y comprender** cómo está estructurado todo el código fuente de la aplicación, módulo por módulo, sin modificar ningún archivo.

---

## 1. Configuración Global e Inicialización (`MuseoApplication.kt`)
- **Qué hace:** Hereda de `Application` y se ejecuta antes que cualquier actividad.
- **Concepto clave:** Configura la librería **OSMDroid** (mapas) asignando un `userAgentValue` requerido por los servidores de OpenStreetMap y limpiando la caché de tiles antiguos para evitar errores de carga.

---

## 2. Capa de Datos y Base de Datos Local (`data/`)
La app utiliza **Room** (el ORM oficial de Android sobre SQLite) para la persistencia local de usuarios.

- **[Usuario.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/data/Usuario.kt):** Entidad (`@Entity`) que define la tabla `usuarios` en la base de datos (id, nombre, email, contrasena, tipoCliente, fotoUri).
- **[UsuarioDao.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/data/UsuarioDao.kt):** Interfaz DAO (`@Dao`) con operaciones suspendidas (`suspend fun`) para registrar usuarios y validar credenciales de inicio de sesión (`login`).
- **[AppDatabase.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/data/AppDatabase.kt):** Base de datos abstracta que implementa el patrón **Singleton** (`getDatabase`) para garantizar que solo exista una instancia de la base de datos en toda la app.
- **[DatosPruebaMuseos.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/data/DatosPruebaMuseos.kt):** Un objeto estático (`object`) que actúa como repositorio en memoria con una lista predefinida de museos en Iberoamérica (Chile, Colombia, México, Perú, España) con sus coordenadas geográficas (`GeoPoint`).

---

## 3. Pantallas y Módulos de la Aplicación (`ui/`)

### A. Autenticación y Bienvenida (`bienvenida/`, `login/`, `registro/`)
- **[BienvenidaActivity.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/bienvenida/BienvenidaActivity.kt):** Pantalla de presentación con animaciones de fundido (`AnimationUtils`) que da la bienvenida al usuario.
- **[LoginActivity.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/login/LoginActivity.kt):** Gestiona el inicio de sesión. Utiliza corrutinas (`lifecycleScope.launch`) para consultar la base de datos Room. Además, incluye cuentas de prueba automáticas (Admin, Cliente, Investigador) para facilitar demostraciones.
- **[RegistroActivity.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/registro/RegistroActivity.kt):** Permite registrar nuevos usuarios e incluye un selector de galería (`ActivityResultContracts.GetContent()`) para cargar una foto de perfil localmente.

### B. Panel Principal (`main/`)
- **[MainActivity.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/main/MainActivity.kt):** La pantalla central tras iniciar sesión. Lee el rol del usuario mediante `Intent` extras y adapta la interfaz dinámicamente (`visibility = View.VISIBLE / GONE`). Controla la navegación inferior (`BottomNavigationView`) y el menú superior.

### C. Mapa Interactivo y Geolocalización (`mapa/`, `components/`)
- **[MapaActivity.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/mapa/MapaActivity.kt):** Coordina la lógica del mapa, los estados de búsqueda (`textoBusqueda`), los filtros por categoría/país y el museo seleccionado.
- **[VistaMapa.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/components/VistaMapa.kt):** Componente que utiliza un `AndroidView` para renderizar el `MapView` de OSMDroid, dibujando marcadores interactivos (`Marker`) por cada museo filtrado.
- **[BarraBusqueda.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/components/BarraBusqueda.kt) y [FiltrosMapa.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/components/FiltrosMapa.kt):** Componentes visuales de Jetpack Compose para buscar texto y filtrar elementos horizontalmente con `FilterChip`.
- **[TarjetaVistaPreviaMuseo.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/components/TarjetaVistaPreviaMuseo.kt):** Tarjeta flotante que aparece al hacer clic en un pin del mapa, mostrando un resumen y un botón para abrir el detalle completo.

### D. Detalle del Museo (`detalle/`)
- **[DetalleMuseoActivity.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/detalle/DetalleMuseoActivity.kt):** Recibe los datos del museo seleccionado, muestra información detallada, permite marcarlo como favorito (con cambio de ícono reactivo) y abrir enlaces externos para recorridos virtuales 360°.

### E. Catálogo Cultural y E-commerce (`catalogo/`)
- **[CatalogoActivity.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/catalogo/CatalogoActivity.kt):** Muestra una cuadrícula (`LazyVerticalGrid`) con cursos, libros y materiales patrimoniales, filtrables por categoría y texto.
- **[CatalogoDetalle.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/catalogo/CatalogoDetalle.kt):** Pantalla de detalle del producto/curso. Incluye un selector de cantidad interactivo (`+` / `-`), indicador de disponibilidad y validación de stock.

### F. Favoritos (`favorito/`)
- **[FavoritosActivity.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/favorito/FavoritosActivity.kt):** Muestra una lista filtrable (`LazyColumn`) de los elementos guardados por el usuario, permitiendo iniciar visitas virtuales o eliminar elementos con listas mutables reactivas (`mutableStateListOf`).

### G. Escáner de Códigos QR (`qr/`)
- **[QrActivity.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/qr/QrActivity.kt) y [QrScreen.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/qr/QrScreen.kt):** Integran **Google Play Services ML Kit Barcode Scanning** (`GmsBarcodeScanning`). Al escanear un código, la app lee el texto sin formato (`rawValue`), intenta interpretarlo como un objeto JSON (`JSONObject`) y extrae títulos, descripciones y enlaces multimedia navegables.

---

## 4. Estilos y Temas (`theme/`)
- **[Theme.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/theme/Theme.kt), [Color.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/theme/Color.kt) y [Type.kt](file:///C:/Users/javie/Desktop/app_museo-master/app/src/main/java/com/example/app_museo/ui/theme/Type.kt):** Configuración de **Material Design 3** para Compose, soportando temas claros/oscuros y colores dinámicos en Android 12+.
