package com.example.compuspeed;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<Recorrido> {
	 
    Context context;
 
    public CustomListViewAdapter(Context context, int resourceId,
            List<Recorrido> items) {
        super(context, resourceId, items);
        this.context = context;
    }
 
    /*private view holder class*/
    private class ViewHolder {
        TextView imageView;
        TextView txtTitle;
        TextView txtDesc;
        TextView txtDato_1;
        TextView txtDato_2;
        TextView txtDato_3;
        TextView txtHoraInicio;
        TextView txtHoraTermino;
        LinearLayout filaLayout;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Recorrido rowItem = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            
            holder.filaLayout = (LinearLayout)convertView.findViewById(R.id.fila_layout);
            if (position % 2 == 0)
            	holder.filaLayout.setBackgroundColor(Color.rgb(58, 1, 223));
            else
            	holder.filaLayout.setBackgroundColor(Color.rgb(179, 202, 249));
            
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (TextView) convertView.findViewById(R.id.icon);
            holder.txtDato_1 = (TextView) convertView.findViewById(R.id.txtDato1);
            holder.txtDato_2 = (TextView) convertView.findViewById(R.id.txtDato2);
            holder.txtDato_3 = (TextView) convertView.findViewById(R.id.txtDato3);
            holder.txtHoraInicio = (TextView) convertView.findViewById(R.id.txtHoraIni);
            holder.txtHoraTermino = (TextView) convertView.findViewById(R.id.txtHoraTer);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        holder.txtDesc.setText(rowItem.getDIRECCION_INICIO());
        holder.txtTitle.setText(rowItem.getDIRECCION_TERMINO());
        holder.imageView.setText(rowItem.getFECHA());
        holder.txtDato_1.setText(rowItem.getVELOCIDAD_MAXIMA());
        holder.txtDato_2.setText(rowItem.getDISTANCIA_RECORRIDA());
        holder.txtDato_3.setText(rowItem.getTIEMPO_TRANSCURRIDO());
        holder.txtHoraInicio.setText(rowItem.getHORA_INICIO());
        holder.txtHoraTermino.setText(rowItem.getHORA_TERMINO());
        return convertView;
    }
}