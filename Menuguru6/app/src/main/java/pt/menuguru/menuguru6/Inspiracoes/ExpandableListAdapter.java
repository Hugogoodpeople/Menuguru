package pt.menuguru.menuguru6.Inspiracoes;

/**
 * Created by hugocosta on 14/08/14.
 */
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Inspiracao;
import pt.menuguru.menuguru6.Utils.InspiracaoItem;

public class ExpandableListAdapter extends BaseExpandableListAdapter {



    public ImageLoader imageLoader;
    private Context _context;
    private List<Inspiracao> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Inspiracao, List<InspiracaoItem>> _listDataChild;

    public ExpandableListAdapter(Context context, List<Inspiracao> listDataHeader,
                                 HashMap<Inspiracao, List<InspiracaoItem>> listChildData) {

        imageLoader=new ImageLoader(context);
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final InspiracaoItem childText = (InspiracaoItem) getChild(groupPosition, childPosition);

        // o codigo comentado causa incongruencias visuais ajustando os itens a esquerda
        //if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        //}

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText.getNome());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Inspiracao headerTitle = (Inspiracao) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_inspiracao, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.nomeInspiracao);
        lblListHeader.setText(headerTitle.getNome());

        ImageView imagem=(ImageView)convertView.findViewById(R.id.imagem_inspiracao);
        imageLoader.DisplayImage("http://menuguru.pt/"+headerTitle.getUrlImagem(), imagem);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}