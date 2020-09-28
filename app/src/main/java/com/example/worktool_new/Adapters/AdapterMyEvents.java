package com.example.worktool_new.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worktool_new.Models.MyEventModel;
import com.example.worktool_new.R;
import com.example.worktool_new.Util.SharedPreference.App;
import com.example.worktool_new.Util.SharedPreference.AppConstants;
import com.example.worktool_new.Views.Activities.Add_Event;
import com.example.worktool_new.Views.Activities.ParticipentsList;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AdapterMyEvents extends RecyclerView.Adapter<ViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public ArrayList<MyEventModel.Datum> datamodelArraylist;
    onEventClick onEventClick;

    public interface onEventClick {
        void onEventDelete(int i, int i2);
    }

    public AdapterMyEvents(Context context2, ArrayList<MyEventModel.Datum> datamodelArraylist2, onEventClick onEventClick2) {
        this.context = context2;
        this.datamodelArraylist = datamodelArraylist2;
        this.onEventClick = onEventClick2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_my_events, parent, false), this.onEventClick);
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        System.out.println("jksfjs" + this.datamodelArraylist.get(position).getStatut() + "  " + this.datamodelArraylist.get(position).getEventType());
        holder.llEditEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                App.getAppPreference().saveString("eventscreen", "edit");
                Intent intent = new Intent(AdapterMyEvents.this.context, Add_Event.class);
                intent.putExtra("position", position);
                intent.putExtra("eventlist", AdapterMyEvents.this.datamodelArraylist);
                AdapterMyEvents.this.context.startActivity(intent);
            }
        });
        holder.llParticipents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdapterMyEvents.this.context, ParticipentsList.class);
                intent.putExtra("idEvent", ((MyEventModel.Datum) AdapterMyEvents.this.datamodelArraylist.get(position)).getIdEvent());
                AdapterMyEvents.this.context.startActivity(intent);
            }
        });
        String MyEventImage = AppConstants.IMAGEURL + this.datamodelArraylist.get(position).getFile();
        if (this.datamodelArraylist.get(position).getFile() != null) {
            Picasso.get().load(MyEventImage).placeholder((int) R.drawable.profileplaceholder).error((int) R.drawable.profileplaceholder).into(holder.ivMyEvent);
        } else {
            holder.ivMyEvent.setImageResource(R.drawable.profileplaceholder);
        }
        if (this.datamodelArraylist.get(position).getStatut().equals("Public")) {
            holder.ivEventType.setImageResource(R.drawable.ic_internet);
        } else {
            holder.ivEventType.setImageResource(R.drawable.lock);
        }
        holder.llDeleteEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AdapterMyEvents.this.onEventClick.onEventDelete(position, Integer.parseInt(((MyEventModel.Datum) AdapterMyEvents.this.datamodelArraylist.get(position)).getIdEvent()));
            }
        });
        holder.tvMyEventTitle.setText(this.datamodelArraylist.get(position).getTitre());
        String[] split = this.datamodelArraylist.get(position).getDate().split(" ");
        holder.tvMyEventName.setText(this.datamodelArraylist.get(position).getPrenom() + " " + this.datamodelArraylist.get(position).getNom());
        holder.tvMyEventType.setText(this.datamodelArraylist.get(position).getEventType());
    }

    public int getItemCount() {
        ArrayList<MyEventModel.Datum> arrayList = this.datamodelArraylist;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivEventType;
        ImageView ivMyEvent;
        LinearLayout llDeleteEvent;
        LinearLayout llEditEvent;
        LinearLayout llParticipents;
        onEventClick onEventClick;
        TextView tvDate;
        TextView tvMyEventName;
        TextView tvMyEventTitle;
        TextView tvMyEventType;
        TextView tvTime;

        public ViewHolder(View itemView, onEventClick onEventClick2) {
            super(itemView);
            this.llEditEvent = (LinearLayout) itemView.findViewById(R.id.llEditEvent);
            this.llParticipents = (LinearLayout) itemView.findViewById(R.id.llParticipents);
            this.ivMyEvent = (ImageView) itemView.findViewById(R.id.ivMyEvent);
            this.ivEventType = (ImageView) itemView.findViewById(R.id.ivEventType);
            this.tvMyEventTitle = (TextView) itemView.findViewById(R.id.tvMyEventTitle);
            this.tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            this.tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            this.tvMyEventName = (TextView) itemView.findViewById(R.id.tvMyEventName);
            this.tvMyEventType = (TextView) itemView.findViewById(R.id.tvMyEventType);
            this.llDeleteEvent = (LinearLayout) itemView.findViewById(R.id.ll_deleteEvent);
            this.onEventClick = onEventClick2;
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
        }
    }
}
