public class Cliente {
    //Atributos
    private int id;
    private String nombre;
    private String telefono;
    private String matricula;

    public Cliente(int id, String nombre, String telefono, String matricula) {
        this.id=id;
        setNombre(nombre);
        setTelefono(telefono);
        setMatricula(matricula);
    }


    //Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        String nomLimpio=nombre.trim();

        if (nomLimpio.isEmpty()){
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }else {
            this.nombre = nomLimpio;
        }
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        String telefLimpio= telefono.trim();

        if (telefLimpio.isEmpty()){ //validar que sea el nº de cifras de un telef
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }else {
            this.telefono=telefLimpio;
        }
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        String matLimpio=matricula.trim().toUpperCase();

        if (matLimpio.isEmpty()){
            throw new IllegalArgumentException("La matrícula no puede estar vacío");
        }else {
            this.matricula = matLimpio;
        }
    }

//    @Override
//    public String toString() {
//        return "Cliente{" +
//                "id=" + id +
//                ", nombre='" + nombre + '\'' +
//                ", telefono='" + telefono + '\'' +
//                ", matricula='" + matricula + '\'' +
//                '}';
//    }
}



