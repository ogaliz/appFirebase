package firebase.app.testcrud;

import android.app.Person;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

    private static final String TAG = "ERROR";

    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;

    EditText nomP, appP, correoP, passwdP;
    ListView listV_personas;

    Spinner spinner;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Persona personaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomP = findViewById(R.id.txt_nombrePersona);
        appP = findViewById(R.id.txt_apellidoPersona);
        correoP = findViewById(R.id.txt_correoPersona);
        passwdP = findViewById(R.id.txt_passwordPersona);

//      Inicio código dedicado al Spinner
        spinner = (Spinner) findViewById(R.id.depart_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.spinner_array, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Se ha pulsado el elemento " + adapterView.getItemAtPosition(i),
                        Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//      Final código dedicado al Spinner

        listV_personas = findViewById(R.id.lv_datosPersonas);

        inicializarFirebase();
        listarDatos();

        listV_personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSelected = (Persona) parent.getItemAtPosition(position);
                nomP.setText(personaSelected.getNombre());
                appP.setText(personaSelected.getApellido());
                correoP.setText(personaSelected.getCorreo());
                passwdP.setText(personaSelected.getPassword());
            }
        });
    }

    private void listarDatos() {

        /* No nos referimos al nombre de la clase, sino al nombre del nodo de la BBDD Firebase*/
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
                // Fallo al tomar una Persona, mensaje en el log
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    /*Nos crea una instancia de firebase para posteriormente poder guardar los datos*/
    private void inicializarFirebase() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        /* Habilitamos la persistencia para que se puedan agragar datos sin la necesidad de conexión. */
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* Tomamos lo que tenemos en los editText > los guardamos en una variable de tipo String
         * > almacenamos las variables en el objeto persona > guardamos en FireBase
         * */
        String nombre = nomP.getText().toString();
        String correo = correoP.getText().toString();
        String passwd = passwdP.getText().toString();
        String app = appP.getText().toString();

        switch (item.getItemId()) {
            case R.id.icon_add: {
                if (nombre.equals("")) {
                    validacion();
                } else {
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(app);
                    p.setCorreo(correo);
                    p.setPassword(passwd);

                    /* Creamos un nodo hijo, que llamamos persona, a su vez este tendrá nodos hijo con sus datos
                     * Raiz > Nodo persona > Nodo UUID > Nombre + Apellido + Correo ... */
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);

                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();

                    limpiarcajas();
                }
                break;

            } case R.id.icon_save: {

                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                p.setNombre(nomP.getText().toString().trim());
                p.setApellido(appP.getText().toString().trim());
                p.setCorreo(correoP.getText().toString().trim());
                p.setPassword(passwdP.getText().toString().trim());

                databaseReference.child("Persona").child(p.getUid()).setValue(p);

                Toast.makeText(this, "Guardado", Toast.LENGTH_LONG).show();
                limpiarcajas();
                break;

            } case R.id.icon_delete: {

                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                databaseReference.child("Persona").child(p.getUid()).removeValue();

                Toast.makeText(this, "Registro eliminado", Toast.LENGTH_LONG).show();
                limpiarcajas();
                break;
            }
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

        if (nombre.equals("")) {
            nomP.setError("Campo requerido.");
        }
    }
}
