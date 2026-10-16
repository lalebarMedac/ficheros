import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

public class Ejercicio5BaseDatosBinaria {

    // Estructura Cabecera (16 bytes):
    // 0-3  : int totalRegistros (4 bytes)
    // 4-7  : int tamanoRegistro (4 bytes) -> 128 bytes
    // 8-15 : long timestamp (8 bytes)
    private static final int TAMANO_CABECERA = 16;
    private static final int TAMANO_REGISTRO = 128;
    private static final int LONGITUD_CODIGO = 20; // 20 chars = 40 bytes

    // Estructura Registro (128 bytes):
    // ID (int)            : 4 bytes
    // Codigo (20 chars)   : 40 bytes
    // Stock (int)         : 4 bytes
    // Precio (double)     : 8 bytes
    // Estado (byte)       : 1 byte (1=activo, 0=borrado)
    // Relleno/Padding     : 71 bytes
    // Total               : 4 + 40 + 4 + 8 + 1 + 71 = 128 bytes

    public static void main(String[] args) {
        File archivo = new File("inventario.dat");

        try {
            // 1. Inicialización
            inicializarArchivo(archivo);

            // 2. Inserciones de prueba
            insertarProducto(archivo, 101, "PROD-A10", 50, 19.99);
            insertarProducto(archivo, 102, "PROD-B20", 30, 49.50);
            insertarProducto(archivo, 103, "PROD-C30", 100, 5.25);

            // 3. Borrado lógico del ID 102 (estado = 0)
            marcarBorradoLogico(archivo, 102);

            // 4. Inserción inteligente (reutiliza el hueco del ID 102)
            insertarProducto(archivo, 104, "PROD-D40", 15, 99.99);

            // 5. Modificación in-place
            modificarProductoInPlace(archivo, 101, 75, 17.50);

            // 6. Compactación del fichero (borrado físico)
            compactarFichero(archivo);

            // Mostrar estado final
            mostrarInventario(archivo);

        } catch (IOException e) {
            System.err.println("Error crítico en la base de datos binaria: " + e.getMessage());
        }
    }

    public static void inicializarArchivo(File archivo) throws IOException {
        if (!archivo.exists()) {
            try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
                raf.writeInt(0);                           // Total registros = 0
                raf.writeInt(TAMANO_REGISTRO);              // Tamaño registro = 128
                raf.writeLong(System.currentTimeMillis()); // Timestamp
                System.out.println("Base de datos inicializada con cabecera de 16 bytes.");
            }
        }
    }

    public static void insertarProducto(File archivo, int id, String codigo, int stock, double precio) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.seek(0);
            int totalRegistros = raf.readInt();
            int tamanoReg = raf.readInt();

            long posicionInsercion = -1;
            boolean reutilizandoHueco = false;

            // Buscar hueco libre con borrado lógico (estado == 0)
            for (int i = 0; i < totalRegistros; i++) {
                long posReg = TAMANO_CABECERA + ((long) i * tamanoReg);
                // Byte de estado en offset 56 dentro del registro
                raf.seek(posReg + 56);
                byte estado = raf.readByte();

                if (estado == 0) {
                    posicionInsercion = posReg;
                    reutilizandoHueco = true;
                    System.out.println("Reutilizando hueco borrado en registro #" + i);
                    break;
                }
            }

            // Si no hay huecos, insertar al final
            if (posicionInsercion == -1) {
                posicionInsercion = TAMANO_CABECERA + ((long) totalRegistros * tamanoReg);
            }

            // Escribir datos
            raf.seek(posicionInsercion);
            raf.writeInt(id);

            StringBuffer sb = new StringBuffer(codigo);
            sb.setLength(LONGITUD_CODIGO);
            raf.writeChars(sb.toString()); // 40 bytes

            raf.writeInt(stock);
            raf.writeDouble(precio);
            raf.writeByte((byte) 1); // Estado 1 = Activo

            // Relleno / Padding
            byte[] padding = new byte[71];
            raf.write(padding);

            // Actualizar total en cabecera si es nuevo al final
            if (!reutilizandoHueco) {
                raf.seek(0);
                raf.writeInt(totalRegistros + 1);
            }

            // Actualizar timestamp en cabecera
            raf.seek(8);
            raf.writeLong(System.currentTimeMillis());

            System.out.println("Producto ID " + id + " insertado correctamente.");
        }
    }

    public static void modificarProductoInPlace(File archivo, int id, int nuevoStock, double nuevoPrecio) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.seek(0);
            int totalRegistros = raf.readInt();
            int tamanoReg = raf.readInt();

            for (int i = 0; i < totalRegistros; i++) {
                long posReg = TAMANO_CABECERA + ((long) i * tamanoReg);
                raf.seek(posReg);
                int regId = raf.readInt();

                raf.seek(posReg + 56);
                byte estado = raf.readByte();

                if (regId == id && estado == 1) {
                    // Desplazarse al campo stock (offset 44)
                    raf.seek(posReg + 44);
                    raf.writeInt(nuevoStock);
                    raf.writeDouble(nuevoPrecio);

                    // Actualizar timestamp cabecera
                    raf.seek(8);
                    raf.writeLong(System.currentTimeMillis());

                    System.out.println("Producto ID " + id + " modificado in-place con éxito.");
                    return;
                }
            }
            System.out.println("Producto ID " + id + " no encontrado o no activo.");
        }
    }

    public static void marcarBorradoLogico(File archivo, int id) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.seek(0);
            int totalRegistros = raf.readInt();
            int tamanoReg = raf.readInt();

            for (int i = 0; i < totalRegistros; i++) {
                long posReg = TAMANO_CABECERA + ((long) i * tamanoReg);
                raf.seek(posReg);
                int regId = raf.readInt();

                if (regId == id) {
                    raf.seek(posReg + 56); // Offset byte estado
                    raf.writeByte((byte) 0); // Estado borrado
                    System.out.println("Producto ID " + id + " marcado como borrado lógicamente.");
                    return;
                }
            }
        }
    }

    public static void compactarFichero(File archivo) throws IOException {
        File archivoTmp = new File("inventario.tmp");

        try (RandomAccessFile rafOrigen = new RandomAccessFile(archivo, "r");
             RandomAccessFile rafTmp = new RandomAccessFile(archivoTmp, "rw")) {

            rafOrigen.seek(0);
            int totalOrigen = rafOrigen.readInt();
            int tamanoReg = rafOrigen.readInt();

            // Cabecera provisional en temporal
            rafTmp.writeInt(0);
            rafTmp.writeInt(tamanoReg);
            rafTmp.writeLong(System.currentTimeMillis());

            int registrosValidos = 0;

            for (int i = 0; i < totalOrigen; i++) {
                long posReg = TAMANO_CABECERA + ((long) i * tamanoReg);
                rafOrigen.seek(posReg + 56);
                byte estado = rafOrigen.readByte();

                if (estado == 1) { // Copiar solo activos
                    byte[] bufferRegistro = new byte[tamanoReg];
                    rafOrigen.seek(posReg);
                    rafOrigen.readFully(bufferRegistro);

                    rafTmp.write(bufferRegistro);
                    registrosValidos++;
                }
            }

            // Actualizar recuento en archivo temporal
            rafTmp.seek(0);
            rafTmp.writeInt(registrosValidos);
            rafTmp.seek(8);
            rafTmp.writeLong(System.currentTimeMillis());
        }

        // Sustituir archivo original
        if (archivo.delete()) {
            if (archivoTmp.renameTo(archivo)) {
                System.out.println("Compactación completada con éxito. Registros borrados eliminados físicamente.");
            } else {
                System.err.println("Error al renombrar el archivo temporal.");
            }
        } else {
            System.err.println("Error al eliminar el archivo original previo a la compactación.");
        }
    }

    public static void mostrarInventario(File archivo) throws IOException {
        System.out.println("========== ESTADO DEL INVENTARIO ==========");
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            raf.seek(0);
            int totalRegistros = raf.readInt();
            int tamanoReg = raf.readInt();
            long timestamp = raf.readLong();

            System.out.println("Cabecera -> Registros Totales: " + totalRegistros +
                               " | Tamaño Registro: " + tamanoReg + "b | Última mod: " + new Date(timestamp));

            for (int i = 0; i < totalRegistros; i++) {
                long posReg = TAMANO_CABECERA + ((long) i * tamanoReg);
                raf.seek(posReg);

                int id = raf.readInt();

                char[] codChars = new char[LONGITUD_CODIGO];
                for (int c = 0; c < LONGITUD_CODIGO; c++) codChars[c] = raf.readChar();
                String codigo = new String(codChars).trim();

                int stock = raf.readInt();
                double precio = raf.readDouble();
                byte estado = raf.readByte();

                System.out.printf("Reg #%d [Offset %d]: ID=%d | Cod=%s | Stock=%d | Precio=%.2f€ | Estado=%s",
                        i, posReg, id, codigo, stock, precio, (estado == 1 ? "ACTIVO" : "BORRADO"));
            }
        }
    }
}