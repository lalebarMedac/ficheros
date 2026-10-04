import java.io.File;
import java.util.Date;

public class App {
    public static void main(String[] args) throws Exception {

        //Instanciamos un objeto de la clase File
        File file = new File("src/words.txt");

        //Creamos el archivo
        file.createNewFile();
        
        System.out.println(new Date(file.lastModified()));

        //Eliminamos el archivo
        file.delete();

        File ficheroOrigen = new File("src/words.txt");
        File ficheroDestino = new File("src/words2.txt");

        if (ficheroOrigen.renameTo(ficheroDestino)) {
            System.out.println("El archivo se ha renombrado correctamente.");
        } else {
            System.out.println("No se pudo renombrar el archivo.");
        }
    }
}
