package com.example.worktool_new.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worktool_new.Models.MyDataModel;
import com.example.worktool_new.R;
import java.util.ArrayList;

public class Adapter_MyData extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    /* access modifiers changed from: private */
    public ArrayList<MyDataModel.Datum> datamodelArraylist;
    /* access modifiers changed from: private */
    public deleteData deleteDatas;
    /* access modifiers changed from: private */
    public editData editDatas;

    public interface deleteData {
        void deleteMyData(ArrayList<MyDataModel.Datum> arrayList, int i);
    }

    public interface editData {
        void editMyData(String str, String str2, String str3);
    }

    public Adapter_MyData(Context context2, ArrayList<MyDataModel.Datum> datamodelArraylist2, deleteData deleteDatas2, editData editDatas2) {
        this.context = context2;
        this.datamodelArraylist = datamodelArraylist2;
        this.deleteDatas = deleteDatas2;
        this.editDatas = editDatas2;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemViewType(int position) {
        return position;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_data, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (this.datamodelArraylist.get(position).getFichier() != null && !this.datamodelArraylist.get(position).getFichier().isEmpty()) {
            holder.tvMyDataFileName.setText(this.datamodelArraylist.get(position).getFichier());
        }
        if (this.datamodelArraylist.get(position).getFichier() != null && !this.datamodelArraylist.get(position).getFichier().isEmpty()) {
            holder.tvMyDataFileType.setText(this.datamodelArraylist.get(position).getType());
        }
        holder.delete_task.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Adapter_MyData.this.deleteDatas.deleteMyData(Adapter_MyData.this.datamodelArraylist, position);
            }
        });
        holder.edit_task.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Adapter_MyData.this.editDatas.editMyData(((MyDataModel.Datum) Adapter_MyData.this.datamodelArraylist.get(position)).getIdFichier(), ((MyDataModel.Datum) Adapter_MyData.this.datamodelArraylist.get(position)).getTable(), ((MyDataModel.Datum) Adapter_MyData.this.datamodelArraylist.get(position)).getFichier());
            }
        });
    }

    public int getItemCount() {
        ArrayList<MyDataModel.Datum> arrayList = this.datamodelArraylist;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout delete_task;
        RelativeLayout edit_task;
        RelativeLayout rlData;
        TextView tvMyDataFileName;
        TextView tvMyDataFileType;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvMyDataFileName = (TextView) itemView.findViewById(R.id.tvMyDataFileName);
            this.tvMyDataFileType = (TextView) itemView.findViewById(R.id.tvMyDataFileType);
            this.rlData = (RelativeLayout) itemView.findViewById(R.id.rlData);
            this.delete_task = (RelativeLayout) itemView.findViewById(R.id.delete_task);
            this.edit_task = (RelativeLayout) itemView.findViewById(R.id.edit_task);
        }
    }
}
