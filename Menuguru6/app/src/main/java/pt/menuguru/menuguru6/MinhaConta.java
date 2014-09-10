package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import pt.menuguru.menuguru6.Utils.Globals;

/**
 * Created by hugocosta on 06/08/14.
 */
public class MinhaConta extends Activity {

    EditText edit_pnome;
    EditText edit_snome;
    EditText edit_data_nascimento;
    EditText edit_cidade;
    EditText edit_telefone;
    EditText edit_email;
    EditText edit_pass;
    Switch edit_news;

    String pnome;
    String snome;
    String data_nascimento;
    String cidade;
    String telefone;
    String email;
    String pass;
    Boolean news;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Minha conta");
        actionBar.setDisplayHomeAsUpEnabled(true);

        pnome = Globals.get_instance().getUser().getPnome();
        snome = Globals.get_instance().getUser().getSnome();
        data_nascimento = Globals.get_instance().getUser().getData_nasc();
        cidade = Globals.get_instance().getUser().getCidade();
        telefone = Globals.get_instance().getUser().getTelefone_user();
        email = Globals.get_instance().getUser().getEmail();
        pass = Globals.get_instance().getUser().getPass();
        news = Globals.get_instance().getUser().getNews();


        edit_pnome   = (EditText)findViewById(R.id.edit_pnome);
        edit_snome   = (EditText)findViewById(R.id.edit_snome);
        edit_data_nascimento   = (EditText)findViewById(R.id.edit_data_nascimento);
        edit_cidade   = (EditText)findViewById(R.id.edit_cidade);
        edit_telefone   = (EditText)findViewById(R.id.edit_telefone);
        edit_email   = (EditText)findViewById(R.id.edit_email);
        edit_pass   = (EditText)findViewById(R.id.edit_pass);
        edit_news   = (Switch)findViewById(R.id.news);

        edit_pnome.setText(pnome);
        edit_snome.setText(snome);
        edit_data_nascimento.setText(data_nascimento);
        edit_cidade.setText(cidade);
        edit_telefone.setText(telefone);
        edit_email.setText(email);
        edit_pass.setText(pass);
        edit_news.setChecked(news);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
        }

        return false;
    }
}
