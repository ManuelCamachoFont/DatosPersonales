package es.studium.datospersonales;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnShow, btnClear;

    EditText txtName, txtSurname, txtAge;

    TextView txtError;

    RadioGroup groupRadio;
    RadioButton maleOption, femaleOption;

    Spinner spinnerLang, spinnerStatus;

    Switch chooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtName = findViewById(R.id.textName);

        txtSurname = findViewById(R.id.textSurname);

        txtAge = findViewById(R.id.textAge);

        txtError = findViewById(R.id.textError);

        groupRadio = findViewById(R.id.radioGroup);

        spinnerLang = findViewById(R.id.langSpinner);
        String[] langOptions = getResources().getStringArray(R.array.idiomas);

        spinnerStatus = findViewById(R.id.statusSpinner);

        btnShow = findViewById(R.id.buttonShow);

        btnClear = findViewById(R.id.buttonClear);

        chooser = findViewById(R.id.parentsChooser);

        // Idiomas

        spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String langCode = "";
                switch (position) {
                    case 0:
                        langCode = "es";
                        break;
                    case 1:
                        langCode = "en";
                        break;
                    case 2:
                        langCode = "fr";
                        break;
                    default:
                        Snackbar.make(parent, "Error: Opción no válida", Snackbar.LENGTH_LONG).show();
                        return;
                }
                LocaleListCompat locales = LocaleListCompat.forLanguageTags(langCode);
                AppCompatDelegate.setApplicationLocales(locales);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Validación

        btnShow.setOnClickListener(view ->{
            String name = txtName.getText().toString().trim();
            String surnames = txtSurname.getText().toString().trim();
            String age = txtAge.getText().toString().trim();
            String checkedAge = "";

            List<String> options = new ArrayList<>();

            if (name.isEmpty()){
                options.add(getString(R.string.nombreErr));
                txtName.setError("Completa el campo");
            }

            if (surnames.isEmpty()){
                options.add(getString(R.string.apellidosErr));
                txtSurname.setError("Completa el campo");
            }

            if (age.isEmpty()){
                options.add(getString(R.string.edadErr));
                txtAge.setError("Completa el campo");
            }
            else{
                if ((Integer.parseInt(age)> 150) || (Integer.parseInt(age)<0)){
                    options.add("Edad");
                    txtAge.setError("Escriba una edad válida");
                }
                checkedAge = checkAge(Integer.parseInt(age));

            }

            if (groupRadio.getCheckedRadioButtonId() == -1){
                options.add(getString(R.string.generoErr));
            }

            if (spinnerStatus.getSelectedItemPosition() == 0){
                options.add(getString(R.string.estadoErr));
            }

            if (!options.isEmpty()){
                txtError.setText("Falta por completar: " + android.text.TextUtils.join(", ", options) + ".");
            }
            else{
                txtError.setText("");

               String gender = checkGender(groupRadio);

                String status = spinnerStatus.getSelectedItem().toString();

                String parents = checkParents(chooser);

                String infoMessage = surnames + ", " +  name + ", " + checkedAge + ", " + gender.toLowerCase() + ", " + status.toLowerCase() + " y " + parents;
                Toast.makeText(this, infoMessage, Toast.LENGTH_LONG).show();

            }
        });

        // Limpiar textos

        btnClear.setOnClickListener(view->{
            txtName.setText("");
            txtSurname.setText("");
            txtAge.setText("");
            spinnerStatus.setSelection(0);
            groupRadio.clearCheck();
            chooser.setChecked(false);
        });



    }

    public String checkAge(int age){
        if (age>=18){
            return getString(R.string.mayorEdad);
        }
        else{
            return getString(R.string.menorEdad);
        }
    }

    public String checkParents(Switch chooser) {
        if (chooser.isChecked()) {
            return getString(R.string.conHijos);
        } else {
            return getString(R.string.sinHijos);
        }
    }

    public String checkGender(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radioMale) {
            return getString(R.string.hombre);
        } else  {
            return getString(R.string.mujer);
        }
    }
}