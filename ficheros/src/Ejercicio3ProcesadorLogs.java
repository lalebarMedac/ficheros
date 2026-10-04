import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Ejercicio3ProcesadorLogs {

    public static void main(String[] args) {
        File archivoEntrada = new File("app.log");
        File archivoSalida = new File("errores.log");

        filtrarErroresLog(archivoEntrada, archivoSalida);
    }

    public static void filtrarErroresLog(File origen, File destino) {
        if (!origen.exists()) {
            System.err.println("El archivo de log origen no existe: " + origen.getName());
            return;
        }

        int lineasProcesadas = 0;
        int erroresEncontrados = 0;

        // El parámetro 'true' en FileWriter habilita el modo append (anexar)
        try (BufferedReader br = new BufferedReader(new FileReader(origen));
             BufferedWriter bw = new BufferedWriter(new FileWriter(destino, true))) {

            String linea;
            // readLine() devuelve null al alcanzar el final del archivo (EOF)
            while ((linea = br.readLine()) != null) {
                lineasProcesadas++;
                if (linea.contains("ERROR")) {
                    bw.write(linea);
                    bw.newLine(); // Salto de línea independiente del S.O.
                    erroresEncontrados++;
                }
            }
            bw.flush();

            System.out.println("Proceso finalizado con éxito.");
            System.out.println("Líneas analizadas: " + lineasProcesadas);
            System.out.println("Errores registrados en '" + destino.getName() + "': " + erroresEncontrados);

        } catch (IOException e) {
            System.err.println("Ocurrió un error al procesar el archivo de logs: " + e.getMessage());
        }
    }
}