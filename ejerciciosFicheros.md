# Batería de Ejercicios: Acceso a Datos y Manejo de Ficheros en Java

Este documento contiene una serie de ejercicios prácticos sobre el manejo de ficheros en Java (`java.io`), diseñados para reforzar el trabajo con el sistema de archivos, flujos de datos (bytes y caracteres), optimización mediante búferes y acceso aleatorio.

---

## Ejercicio 1: Explorador y Organizador de Directorios

### Objetivo
Gestión y manipulación de la estructura de archivos y carpetas del sistema.

### Enunciado
Desarrolla una aplicación en Java que reciba la ruta de un directorio por teclado o parámetro de entrada. La aplicación debe realizar las siguientes tareas:

1. Comprobar si la ruta introducida existe en el sistema y si efectivamente corresponde a un directorio.
2. Crear dentro de dicho directorio dos subcarpetas llamadas `/Documentos` y `/Imagenes`.
3. Obtener todos los archivos contenidos en el directorio especificado y clasificarlos:
   - Los archivos con extensión `.txt` se moverán a la subcarpeta `/Documentos`.
   - Los archivos con extensión `.png` o `.jpg` se moverán a la subcarpeta `/Imagenes`.
4. Mostrar por consola el nombre, la ruta absoluta y la fecha de última modificación de cada uno de los archivos procesados.

### Investigación en la Documentación Oficial (`java.io`)
- Consulta en la Javadoc oficial qué mecanismo permite filtrar archivos durante la obtención del contenido de un directorio sin necesidad de recorrer todo el vector de resultados manualmente.
- Revisa la diferencia documentada entre obtener la ruta relativa, la ruta absoluta y la ruta canónica de un archivo.

---

## Ejercicio 2: Clonador de Archivos Binarios y Comparativa de Rendimiento

### Objetivo
Análisis del rendimiento entre el acceso secuencial directo y el acceso con memoria intermedia (búfer).

### Enunciado
Desarrolla una utilidad en Java capaz de duplicar un archivo binario de gran tamaño (por ejemplo, una imagen de alta resolución o un archivo comprimido):

1. Implementa un método de copia que lea y escriba el archivo de entrada byte a byte de forma secuencial directa.
2. Implementa un segundo método de copia que utilice flujos con memoria intermedia (búfer) para la lectura y la escritura.
3. Mide y muestra por consola el tiempo exacto que tarda en completarse cada uno de los dos métodos de copia (en milisegundos).
4. Garantiza la liberación adecuada de recursos y el cierre de todos los flujos utilizados.

### Investigación en la Documentación Oficial (`java.io`)
- Consulta en la Javadoc oficial la función del método responsable del vaciado explícito de búferes de salida y explica en qué escenarios es imprescindible invocarlo manualmente antes del cierre del flujo.
- Revisa las sobrecargas de los métodos de escritura en flujos de salida para transferir bloques completos de bytes mediante arrays.

---

## Ejercicio 3: Procesador y Filtro de Archivos de Registro (Logs)

### Objetivo
Tratamiento de archivos de texto plano orientados a caracteres.

### Enunciado
Se dispone de un archivo de texto llamado `app.log` que almacena registros de eventos por línea. Diseña una aplicación que:

1. Lea el archivo de registro línea por línea.
2. Identifique y filtre únicamente aquellas líneas que contengan la palabra clave `"ERROR"`.
3. Guarde todas las líneas filtradas en un nuevo archivo denominado `errores.log`.
4. Si la aplicación se ejecuta en repetidas ocasiones, los nuevos errores detectados deben añadirse al final del archivo `errores.log` sin sobrescribir el contenido existente previamente.

### Investigación en la Documentación Oficial (`java.io`)
- Averigua qué parámetro o configuración en los constructores de flujos de escritura de texto permite activar el modo de anexado (*append mode*).
- Revisa en la Javadoc el comportamiento del método de lectura por líneas para detectar de forma segura la llegada al final del archivo (*EOF*).

---

## Ejercicio 4: Gestor de Registros de Longitud Fija

### Objetivo
Acceso aleatorio y posicionamiento de punteros en ficheros binarios.

### Enunciado
Se requiere gestionar un fichero binario de acceso directo con información de usuarios, donde cada registro tiene un tamaño fijo predefinido (por ejemplo, 100 bytes por registro):

1. Define la estructura de datos considerando el tipo y tamaño de bytes de cada campo (por ejemplo: identificador entero, nombre formateado a longitud fija y salario real).
2. Desarrolla una función que reciba la posición o número de registro solicitado y ubique el puntero de lectura exactamente al inicio de dicho registro para extraer y mostrar sus datos.
3. Desarrolla una función que permita actualizar un dato concreto de un usuario existente modificando directamente su posición en el archivo, sin reescribir la totalidad del fichero.
4. Muestra por consola la posición exacta del puntero antes y después de realizar cada una de las operaciones.

### Investigación en la Documentación Oficial (`java.io`)
- Revisa los modos de acceso soportados por las clases de acceso aleatorio a ficheros y analiza las diferencias en la sincronización con el almacenamiento físico entre los distintos modos.
- Investiga el método que permite modificar o truncar el tamaño total de un fichero mediante la API oficial de Java.

---

## Ejercicio 5: Sistema de Almacén con Cabecera y Modificación *In-Place*

### Objetivo
Integración de metadatos, acceso aleatorio, gestión de estructuras compuestas y mantenimiento de archivos.

### Enunciado
Diseña un sistema de gestión de inventario que opere sobre un único archivo binario estructurado `inventario.dat`:

1. **Estructura del Fichero:**
   - **Cabecera:** Ocupa los primeros bytes del archivo y almacena la información global: número total de registros, tamaño fijo asignado a cada registro y una marca de tiempo de la última modificación.
   - **Cuerpo:** Colección de registros de tamaño fijo. Cada registro consta de: identificador (entero), código de producto (texto de longitud fija), unidades de stock (entero), precio (decimal) y un campo indicador de estado (byte: activo o borrado lógicamente).

2. **Operaciones requeridas:**
   - **Inicialización:** Al arrancar el programa, si el fichero no existe, debe crearse escribiendo la cabecera por defecto. Si ya existe, debe leer los datos de la cabecera para configurar el funcionamiento del sistema.
   - **Inserción con reutilización de espacio:** Al dar de alta un producto, el programa debe comprobar si existen registros marcados como borrados lógicamente para ocupar ese hueco. Si no hay espacio reutilizable, la inserción se realizará al final del fichero y se actualizará el recuento en la cabecera.
   - **Modificación *In-Place*:** Permitir la actualización del stock y precio de un producto mediante su identificador, modificando únicamente sus bytes en la posición exacta del fichero y actualizando la marca de tiempo de la cabecera.
   - **Compactación del Fichero:** Implementa un proceso de mantenimiento que purgue definitivamente del disco los registros borrados lógicamente. Para ello, se generará una estructura limpia en un archivo auxiliar que posteriormente reemplazará al fichero principal, reescribiendo la cabecera con el número real de registros consolidados.

### Investigación en la Documentación Oficial (`java.io`)
- Investiga el comportamiento de las interfaces de lectura y escritura de datos primitivos en Java para el manejo de cadenas de texto de longitud fija y el control exacto de bytes transferidos.
- Analiza las excepciones específicas asociadas al acceso aleatorio y lecturas fuera de límites, e implementa una gestión de recursos adecuada mediante estructuras de control de excepciones que garanticen el cierre de ficheros ante cualquier fallo de ejecución.
