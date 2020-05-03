package firebase.app.testcrud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void arrancar(View view) {

        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);

        Toast toast = Toast.makeText(getApplicationContext(), "Bienvenid@", Toast.LENGTH_SHORT);
        toast.show();
    }
}
