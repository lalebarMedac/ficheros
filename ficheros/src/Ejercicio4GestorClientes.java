import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Ejercicio4GestorClientes {

    // Estructura del registro (100 bytes en total):
    // ID (int)            : 4 bytes
    // Nombre (String)     : 44 caracteres -> 88 bytes en writeChars
    // Salario (double)    : 8 bytes
    // Total               : 4 + 88 + 8 = 100 bytes
    private static final int TAMANO_REGISTRO = 100;
    private static final int LONGITUD_NOMBRE = 44; // 44 caracteres = 88 bytes

    public static void main(String[] args) {
        File archivo = new File("clientes.dat");

        try {
            // Escribir clientes de prueba
            escribirCliente(archivo, 1, "Juan Perez", 2500.50);
            escribirCliente(archivo, 2, "Maria Lopez", 3100.00);
            escribirCliente(archivo, 3, "Carlos Gomez", 1800.75);

            // Leer cliente #2
            System.out.println("--- LECTURA CLIENTE #2 ---");
            leerCliente(archivo, 2);

            // Modificar salario del cliente #2
            System.out.println("--- ACTUALIZACIÓN SALARIO CLIENTE #2 ---");
            actualizarSalario(archivo, 2, 3500.00);

            // Re-leer cliente #2 para verificar
            System.out.println("--- LECTURA CLIENTE #2 TRAS MODIFICACIÓN ---");
            leerCliente(archivo, 2);

        } catch (IOException e) {
            System.err.println("Error en operaciones de acceso aleatorio: " + e.getMessage());
        }
    }

    public static void escribirCliente(File archivo, int id, String nombre, double salario) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            long posicion = (long) (id - 1) * TAMANO_REGISTRO;
            raf.seek(posicion);

            raf.writeInt(id);

            // Escribir nombre ajustado a longitud fija (44 caracteres)
            StringBuffer sb = new StringBuffer(nombre);
            sb.setLength(LONGITUD_NOMBRE);
            raf.writeChars(sb.toString());

            raf.writeDouble(salario);
        }
    }

    public static void leerCliente(File archivo, int id) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long posicion = (long) (id - 1) * TAMANO_REGISTRO;

            if (posicion >= raf.length()) {
                System.out.println("El cliente con ID " + id + " no existe.");
                return;
            }

            raf.seek(posicion);
            System.out.println("Posición inicial del puntero (seek): " + raf.getFilePointer() + " bytes");

            int clienteId = raf.readInt();

            char[] nombreChars = new char[LONGITUD_NOMBRE];
            for (int i = 0; i < LONGITUD_NOMBRE; i++) {
                nombreChars[i] = raf.readChar();
            }
            String nombre = new String(nombreChars).trim();

            double salario = raf.readDouble();

            System.out.println("Posición final del puntero: " + raf.getFilePointer() + " bytes");
            System.out.println("Cliente ID: " + clienteId + " | Nombre: " + nombre + " | Salario: " + salario + " €");
        }
    }

    public static void actualizarSalario(File archivo, int id, double nuevoSalario) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            long posicion = (long) (id - 1) * TAMANO_REGISTRO;

            if (posicion >= raf.length()) {
                System.out.println("No se puede actualizar: El ID " + id + " está fuera del rango.");
                return;
            }

            // Desplazamiento directo al campo 'salario':
            // ID (4 bytes) + Nombre (88 bytes) = 92 bytes offset
            long offsetSalario = posicion + 4 + (LONGITUD_NOMBRE * 2);

            raf.seek(offsetSalario);
            System.out.println("Puntero posicionado exactamente en el campo salario: " + raf.getFilePointer() + " bytes");
            raf.writeDouble(nuevoSalario);
            System.out.println("Salario actualizado exitosamente in-place.");
        }
    }
}