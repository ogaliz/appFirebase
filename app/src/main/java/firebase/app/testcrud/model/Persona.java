package firebase.app.testcrud.model;

public class Persona {

    private String uid;
    private String Apellido;
    private String Correo;
    private String Nombre;
    private String Password;

    public Persona(){

    }

    public Persona(String uid, String apellido, String correo, String nombre, String password) {
        this.uid = uid;
        Apellido = apellido;
        Correo = correo;
        Nombre = nombre;
        Password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return Nombre;
    }
}
