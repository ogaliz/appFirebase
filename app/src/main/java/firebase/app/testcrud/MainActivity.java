package firebase.app.testcrud;

import android.app.Person;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import firebase.app.testcrud.model.Persona;

public class MainActivity extends AppCompatActivity {

    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;

    EditText nomP, appP, correoP, passwdP;
    ListView listV_personas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomP = findViewById(R.id.txt_nombrePersona);
        appP = findViewById(R.id.txt_apellidoPersona);
        correoP = findViewById(R.id.txt_correoPersona);
        passwdP = findViewById(R.id.txt_passwordPersona);

        listV_personas = findViewById(R.id.lv_datosPersonas);

        inicializarFirebase();
        listarDatos();
    }

    private void listarDatos() {

        /**No nos referimos al nombre de la clase, sino al nombre del nodo de la BBDD Firebase*/
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listPerson.clear();

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Persona p = objSnapshot.getValue(Persona.class);
                    listPerson.add(p);

                    arrayAdapterPersona =
                            new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, listPerson);

                    listV_personas.setAdapter(arrayAdapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*Nos crea una instancia de firebase para posteriormente poder guardar los datos*/
    private void inicializarFirebase() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /**Tomamos lo que tenemos en los editText y los metemos en una variable de tipo String
         * esta a su vez la almacenamos en el objeto persona, y lo almacenamos en FireBase
         * */
        String nombre = nomP.getText().toString();
        String correo = correoP.getText().toString();
        String passwd = passwdP.getText().toString();
        String app = appP.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:
                if (nombre.equals("")){
                    validacion();
                }else {
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(app);
                    p.setCorreo(correo);
                    p.setPassword(passwd);

                    /**Creamos un nodo hijo, que llamamos persona, a su vez este tendrá nodos hijo con sus datos
                     * Raiz > Nodo persona > Nodo UUID > Nombre + Apellido + Correo ... */
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);
                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                    limpiarcajas();
                }
                break;

            case R.id.icon_save:
                Toast.makeText(this, "Guardar", Toast.LENGTH_LONG).show();
                break;

            case R.id.icon_delete:
                Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }

        return true;
    }

    private void limpiarcajas() {
        nomP.setText("");
        correoP.setText("");
        appP.setText("");
        passwdP.setText("");
    }

    private void validacion() {

        String nombre = nomP.getText().toString();

        if (nombre.equals("")){
            nomP.setError("Campo requerido.");
        }
    }
}
