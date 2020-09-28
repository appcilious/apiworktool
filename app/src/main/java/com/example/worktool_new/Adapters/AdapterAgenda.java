package com.example.worktool_new.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worktool_new.Models.MyAgendaModel;
import com.example.worktool_new.R;
import java.util.ArrayList;

public class AdapterAgenda extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private ArrayList<MyAgendaModel.Datum> datamodelArraylist;

    public AdapterAgenda(Context context2, ArrayList<MyAgendaModel.Datum> datamodelArraylist2) {
        this.context = context2;
        this.datamodelArraylist = datamodelArraylist2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_agenda, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (this.datamodelArraylist.get(position).getTitre() != null) {
            holder.tvAgendaTitle.setText(this.datamodelArraylist.get(position).getTitre());
        }
        if (this.datamodelArraylist.get(position).getDate() != null || !this.datamodelArraylist.get(position).getDate().isEmpty()) {
            String[] separated = this.datamodelArraylist.get(position).getDate().split(" ");
            holder.tvAgendaDate.setText(separated[0]);
            holder.tvAgendaTime.setText(separated[1]);
        }
    }

    public int getItemCount() {
        ArrayList<MyAgendaModel.Datum> arrayList = this.datamodelArraylist;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAgendaDate;
        TextView tvAgendaTime;
        TextView tvAgendaTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvAgendaTitle = (TextView) itemView.findViewById(R.id.tvAgendaTitle);
            this.tvAgendaTime = (TextView) itemView.findViewById(R.id.tvAgendaTime);
            this.tvAgendaDate = (TextView) itemView.findViewById(R.id.tvAgendaDate);
        }
    }
}
