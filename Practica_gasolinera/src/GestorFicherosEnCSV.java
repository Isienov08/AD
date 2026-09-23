import java.io.BufferedWriter;
import java.nio.file.*;
import java.util.List;
import java.util.function.Function;

public class GestorFicherosEnCSV {
    Path ruta;
    private Function<Cliente, String> serializadorCliente;
    private Function<Pagos, String> serializadorPagos;



    public GestorFicherosEnCSV() {
//        this.path=
        this.serializadorCliente=cliente -> cliente.getId() + "," + cliente.getNombre() + "," + cliente.getTelefono() + "," + cliente.getMatricula() + ";\n";
        this.serializadorPagos=pagos -> pagos.getId() + "," + pagos.getIdCliente() + "," + pagos.getImporte() + "," + pagos.getLitros() + "," + pagos.getCombustible() + "," + pagos.getFecha() + ";\n";

    }


    public void guadarEnFichero(List objetos) {
      for (Object o:objetos){
          String objetoEnCSV;
          if (o instanceof Cliente){
              objetoEnCSV=this.serializadorCliente.apply((Cliente) o);
          }
          if (o instanceof Pagos){
              objetoEnCSV=this.serializadorPagos.apply((Pagos) o);
          }

//          try (BufferedWriter bw=Files.newBufferedWriter(this.path)){
//
//          }

      }


    }




    public void leerFichero() {}


}

