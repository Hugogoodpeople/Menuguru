package pt.menuguru.menuguru6;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CriarUser extends Activity {

    EditText edit_pnome;
    EditText edit_snome;
    Button edit_sexo;
    EditText edit_cidade;
    Button edit_data_nasc;
    EditText edit_email;
    EditText edit_pass;

    private Calendar cal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_user);

        edit_pnome  = (EditText)findViewById(R.id.edit_pnome);
        edit_snome  = (EditText)findViewById(R.id.edit_snome);
        edit_sexo  = (Button)findViewById(R.id.edit_sexo);
        edit_cidade  = (EditText)findViewById(R.id.edit_cidade);
        edit_data_nasc  = (Button)findViewById(R.id.edit_data_nasc);
        edit_email  = (EditText)findViewById(R.id.edit_email);
        edit_pass  = (EditText)findViewById(R.id.edit_pass);

        edit_data_nasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tenho de adicionar cenas
                Log.v("coiso", "Procurar");

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(CriarUser.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();

            }
        });
        edit_sexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("coiso", "Sexo");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CriarUser.this);
                // set dialog message
                alertDialogBuilder
                        .setTitle("Sexo")
                        .setItems(R.array.sexo, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0) {
                                    edit_sexo.setText("Homem");
                                }else if(which==1){
                                    edit_sexo.setText("Mulher");
                                }
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;

            String dataSelecionada= "Ano " + Integer.toString(mYear) +
                    " Mes " + Integer.toString(mMonth+1) +
                    " Dia " + Integer.toString(mDay);

            Log.v("selecionado",dataSelecionada);

            cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, mYear);
            cal.set(Calendar.MONTH, mMonth);
            cal.set(Calendar.DAY_OF_MONTH, mDay);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(cal.getTime());

            edit_data_nasc.setText(formattedDate);


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.criar_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
