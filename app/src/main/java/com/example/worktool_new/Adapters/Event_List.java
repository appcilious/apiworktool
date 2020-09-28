package com.example.worktool_new.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worktool_new.Models.ListEventsModel;
import com.example.worktool_new.R;
import com.example.worktool_new.Util.SharedPreference.AppConstants;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class Event_List extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private ArrayList<ListEventsModel.Datum> datamodelArraylist;

    public Event_List(Context context2, ArrayList<ListEventsModel.Datum> datamodelArraylist2) {
        this.context = context2;
        this.datamodelArraylist = datamodelArraylist2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_events, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvListEventName.setText(this.datamodelArraylist.get(position).getPrenom() + " " + this.datamodelArraylist.get(position).getNom());
        holder.tvListEventTitle.setText(this.datamodelArraylist.get(position).getTitre());
        holder.tvListEventType.setText("Type: " + this.datamodelArraylist.get(position).getEventType());
        String ListEventImage = AppConstants.IMAGEURL + this.datamodelArraylist.get(position).getFile();
        if (this.datamodelArraylist.get(position).getFile() != null) {
            Picasso.get().load(ListEventImage).placeholder((int) R.drawable.profileplaceholder).error((int) R.drawable.profileplaceholder).into(holder.ivListEventImage);
        } else {
            holder.ivListEventImage.setImageResource(R.drawable.profileplaceholder);
        }
    }

    public int getItemCount() {
        ArrayList<ListEventsModel.Datum> arrayList = this.datamodelArraylist;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivListEventImage;
        TextView tvListEventName;
        TextView tvListEventTitle;
        TextView tvListEventType;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ivListEventImage = (ImageView) itemView.findViewById(R.id.ivListEventImage);
            this.tvListEventName = (TextView) itemView.findViewById(R.id.tvListEventName);
            this.tvListEventTitle = (TextView) itemView.findViewById(R.id.tvListEventTitle);
            this.tvListEventType = (TextView) itemView.findViewById(R.id.tvListEventType);
        }
    }
}
