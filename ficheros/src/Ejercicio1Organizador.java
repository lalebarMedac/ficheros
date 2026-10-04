import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ejercicio1Organizador {

    public static void main(String[] args) {
        String rutaDirectorio = "./ficheros"; // Cambiar por la ruta deseada
        organizarDirectorio(rutaDirectorio);
    }

    public static void organizarDirectorio(String ruta) {
        File dir = new File(ruta);

        // 1. Comprobar existencia y si es un directorio válido
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("La ruta especificada no existe o no es un directorio válido.");
            return;
        }

        // 2. Crear subcarpetas /Documentos y /Imagenes
        File carpetaDocs = new File(dir, "Documentos");
        File carpetaImgs = new File(dir, "Imagenes");

        if (!carpetaDocs.exists()) carpetaDocs.mkdirs();
        if (!carpetaImgs.exists()) carpetaImgs.mkdirs();

        // 3. Listar y filtrar archivos (.txt, .png, .jpg) usando FilenameFilter
        FilenameFilter filtroFicheros = (dirPadre, nombre) -> {
            String minuscula = nombre.toLowerCase();
            return minuscula.endsWith(".txt") || minuscula.endsWith(".png") || minuscula.endsWith(".jpg");
        };

        File[] archivos = dir.listFiles(filtroFicheros);

        if (archivos == null || archivos.length == 0) {
            System.out.println("No se encontraron archivos .txt, .png o .jpg para clasificar.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (File archivo : archivos) {
            // Imprimir metadatos antes de mover
            System.out.println("----------------------------------------");
            System.out.println("Nombre: " + archivo.getName());
            System.out.println("Ruta absoluta: " + archivo.getAbsolutePath());
            System.out.println("Última modificación: " + sdf.format(new Date(archivo.lastModified())));

            // Clasificar y mover
            String nombreLower = archivo.getName().toLowerCase();
            File destino = null;

            if (nombreLower.endsWith(".txt")) {
                destino = new File(carpetaDocs, archivo.getName());
            } else if (nombreLower.endsWith(".png") || nombreLower.endsWith(".jpg")) {
                destino = new File(carpetaImgs, archivo.getName());
            }

            if (destino != null) {
                boolean exito = archivo.renameTo(destino);
                if (exito) {
                    System.out.println("-> Movido con éxito a: " + destino.getParent());
                } else {
                    System.err.println("-> Error al mover el archivo: " + archivo.getName());
                }
            }
        }
    }
}