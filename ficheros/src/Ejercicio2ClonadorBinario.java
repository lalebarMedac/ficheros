import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Ejercicio2ClonadorBinario {

    public static void main(String[] args) {
        File origen = new File("archivo_origen.dat"); // Reemplazar por un archivo binario existente
        File destinoSinBuffer = new File("copia_sin_buffer.dat");
        File destinoConBuffer = new File("copia_con_buffer.dat");

        if (!origen.exists()) {
            System.err.println("El archivo de origen no existe. Por favor, especifica un archivo válido.");
            return;
        }

        System.out.println("Iniciando pruebas de copia...");

        // 1. Copia Byte a Byte sin Buffer
        long tiempoInicioSin = System.currentTimeMillis();
        copiarByteAByte(origen, destinoSinBuffer);
        long tiempoFinSin = System.currentTimeMillis();
        long duracionSin = tiempoFinSin - tiempoInicioSin;
        System.out.println("Tiempo copia byte a byte (sin buffer): " + duracionSin + " ms");

        // 2. Copia con Buffered Streams y bloque de bytes
        long tiempoInicioCon = System.currentTimeMillis();
        copiarConBuffer(origen, destinoConBuffer);
        long tiempoFinCon = System.currentTimeMillis();
        long duracionCon = tiempoFinCon - tiempoInicioCon;
        System.out.println("Tiempo copia con buffer (bloques): " + duracionCon + " ms");
    }

    public static void copiarByteAByte(File origen, File destino) {
        try (FileInputStream fis = new FileInputStream(origen);
             FileOutputStream fos = new FileOutputStream(destino)) {

            int byteLeido;
            while ((byteLeido = fis.read()) != -1) {
                fos.write(byteLeido);
            }
        } catch (IOException e) {
            System.err.println("Error durante la copia byte a byte: " + e.getMessage());
        }
    }

    public static void copiarConBuffer(File origen, File destino) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(origen));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destino))) {

            byte[] buffer = new byte[8192]; // Buffer de 8 KB
            int bytesLeidos;
            while ((bytesLeidos = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesLeidos);
            }
            bos.flush(); // Asegura el vaciado del buffer al flujo de salida
        } catch (IOException e) {
            System.err.println("Error durante la copia con buffer: " + e.getMessage());
        }
    }
}