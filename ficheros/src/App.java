import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.util.Date;
import java.util.Scanner;
import java.util.stream.Stream;
import java.io.BufferedOutputStream;

public class App {
    public static void main(String[] args) throws Exception {
        
        InputStream is = new FileInputStream("README.md");
        OutputStream os = new FileOutputStream("README.md.bk");
        long startTime = System.currentTimeMillis();
        int info;
        while ((info = is.read()) != -1) {
            os.write(info);
            //System.out.print((char) is.read());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("\nTiempo de lectura sin BufferedInputStream: " + (endTime - startTime) + " ms");
        is.close();
        os.close();

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("README.md"));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("README2.md.bk"));
        startTime = System.currentTimeMillis();
        info=0;
        while ((info = bis.read()) != -1) {
            bos.write(info);
            //System.out.print((char) is.read());
        }
        endTime = System.currentTimeMillis();
        System.out.println("\nTiempo de lectura con BufferedInputStream: " + (endTime - startTime) + " ms");
        is.close();
        os.close();



        /*
        BufferedReader br = new BufferedReader(new FileReader("README.md"));
        String linea;
        long startTime = System.currentTimeMillis();
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
        br.close();
        long endTime = System.currentTimeMillis();  
        System.out.println("\nTiempo de lectura con BufferedReader: " + (endTime - startTime) + " ms");
        */
        /*
        int bufferSize = 4 * 1024; // Tamaño del buffer en bytes (4 KB)
        FileInputStream bis = new FileInputStream("README.md");
        
        long startTime = System.currentTimeMillis();
        int info = bis.read();
        while (info != -1) {
            System.out.print((char) info);
            info = bis.read();
        }
        bis.close();
        long endTime = System.currentTimeMillis();
        System.out.println("\nTiempo de lectura con BufferedInputStream: " + (endTime - startTime) + " ms");
        */        
        /*
        //Los 3 primeros ID
        //Siguiente ID 001;HOLA      ;  (10 bytes)

        RandomAccessFile raf = new RandomAccessFile("datos/respaldos/notas.txt", "rw");
//        RandomAccessFile raf = new RandomAccessFile(new file("datos/respaldos/notas.txt"), "rw");

        raf.seek(4);
        raf.write(46);
        raf.seek(12);
        raf.write(46);
        raf.seek(15);
        raf.write(46);
        raf.seek(17);
        raf.write(46);

        raf.seek(0);
        
        int carac;
        while((carac = raf.read()) != -1) {
            System.out.println((char) carac);
        }

        raf.seek(0);
        while(raf.getFilePointer() < raf.length()) {
            System.out.println((char) raf.read());
        }
        raf.seek(0);
        System.out.println("La línea completa: " + raf.readLine());
        System.out.println("La línea completa: " + raf.readLine());

        raf.close();
        */
        //System.out.println((char) raf.read());
        //raf.write(72);
        //raf.write(72);
        //System.out.println((char) raf.read());
        //raf.close();

        //FileWriter fw = new FileWriter("datos/respaldos/notas2.txt");
        //fw.write("Hola Mundo");
        //fw.close();




        /*
        BufferedReader br = new BufferedReader(new FileReader("datos/respaldos/notas.txt"));
        String linea;
        int i=1;
        while ((linea = br.readLine()) != null) {
            System.out.println(Integer.toString(i) + " " + linea);
            i++;
        }

        br.close();
        */



        /*
        RandomAccessFile raf = new RandomAccessFile("datos/respaldos/notas.txt", "rw");

        raf.seek(10);
        System.out.println((char) raf.read());
        raf.write(72);
        raf.write(72);
        System.out.println((char) raf.read());
        raf.close();
        */

        /* 
        FileWriter escritura = new FileWriter("datos/respaldos/notas.txt");
        escritura.write("Hola alumnos de 2º DAM");
        escritura.close(); 
        */
    
        /*
        FileReader fr = new FileReader("datos/respaldos/notas.txt");

        int numero;

        while ((numero = fr.read()) != -1) {
            System.out.println((char) numero);
        }

        fr.close();
        */

        /*
    File estructura = new File("datos/respaldos");
    estructura.mkdirs();
    File fichero = new File(estructura, "notas.txt");
    fichero.createNewFile();*/

        
/*         Scanner sc = new Scanner(System.in);

        System.out.print("Introduce el fichero/directorio: ");
        String nombre = sc.nextLine();

        File fichero = new File(nombre);

        if (fichero.exists()) {
            System.out.println("El fichero/directorio existe.");
            System.out.println("Nombre: " + fichero.getName());
            System.out.println("Ruta absoluta: " + fichero.getAbsolutePath());
            System.out.println("Última modificación: " + new Date(fichero.lastModified()));
            System.out.println("Permisos: " + (fichero.canRead() ? "lectura " : "") + (fichero.canWrite() ? "escritura " : "") + (fichero.canExecute() ? "ejecución" : ""));
            if (fichero.isDirectory()) {
                System.out.println("Es un directorio.");
            } else if (fichero.isFile()) {
                System.out.println("Es un fichero.");
                System.out.println("Tamaño: " + fichero.length() + " bytes");
            }
        } else {
            System.out.println("El fichero/directorio no existe.");
        } */

        //File hola = new File("holaMundo.txt");
        
        //hola.createNewFile();

        //System.out.println(hola.lastModified());

        //File hola2 = new File("holamundo.txt.bk");
        //hola.renameTo(hola2);














        //File hola = new File("holamundo.txt");
        //hola.createNewFile();

        //System.out.println(hola.lastModified());

        //File hola2 = new File("holamundo.txt.bk");

        //hola.renameTo(hola2);

        //Instanciamos un objeto de la clase File
        //File file = new File("src/words.txt");

        //Creamos el archivo
        //file.createNewFile();
        
        //System.out.println(new Date(file.lastModified()));

        //Eliminamos el archivo
        //file.delete();

        //File ficheroOrigen = new File("src/words.txt");
        //File ficheroDestino = new File("src/words2.txt");

        //if (ficheroOrigen.renameTo(ficheroDestino)) {
        //    System.out.println("El archivo se ha renombrado correctamente.");
        //} else {
        //    System.out.println("No se pudo renombrar el archivo.");
        //}
    }
}
