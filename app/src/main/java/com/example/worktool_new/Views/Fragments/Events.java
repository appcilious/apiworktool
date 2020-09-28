package com.example.worktool_new.Views.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worktool_new.Adapters.AdapterMyEvents;
import com.example.worktool_new.Adapters.Event_List;
import com.example.worktool_new.Models.DeleteEventModel;
import com.example.worktool_new.Models.ListEventsModel;
import com.example.worktool_new.Models.MyEventModel;
import com.example.worktool_new.R;
import com.example.worktool_new.Retrofit.Apis;
import com.example.worktool_new.Util.SharedPreference.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Events extends Fragment implements AdapterMyEvents.onEventClick {
    private LinearLayout listEvents;
    /* access modifiers changed from: private */
    public Event_List mAdapter;
    /* access modifiers changed from: private */
    public AdapterMyEvents mEventAdapter;
    private LinearLayout myEvents;
    private ProgressDialog progress;
    /* access modifiers changed from: private */
    public RecyclerView rv_event_list;
    /* access modifiers changed from: private */
    public TextView tvNoDataFound;
    private View view;
    /* access modifiers changed from: private */
    public View viewListEvents;
    /* access modifiers changed from: private */
    public View viewMyEvents;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.view == null) {
            View inflate = inflater.inflate(R.layout.fragment_events, container, false);
            this.view = inflate;
            this.rv_event_list = (RecyclerView) inflate.findViewById(R.id.rv_event_list);
            this.myEvents = (LinearLayout) this.view.findViewById(R.id.myEvents);
            this.listEvents = (LinearLayout) this.view.findViewById(R.id.listEvents);
            this.tvNoDataFound = (TextView) this.view.findViewById(R.id.tvNoDataFound);
            this.viewListEvents = this.view.findViewById(R.id.viewListEvents);
            this.viewMyEvents = this.view.findViewById(R.id.viewMyEvents);
            this.viewListEvents.setVisibility(0);
            listEvents();
            this.myEvents.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Events.this.viewMyEvents.setVisibility(0);
                    Events.this.viewListEvents.setVisibility(8);
                    Events.this.myEvents();
                }
            });
            this.listEvents.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Events.this.viewMyEvents.setVisibility(8);
                    Events.this.viewListEvents.setVisibility(0);
                    Events.this.listEvents();
                }
            });
        }
        return this.view;
    }

    /* access modifiers changed from: private */
    public void listEvents() {
        showLoadingDialog();
        ((Apis) new Retrofit.Builder().baseUrl("http://devworktools.fr/contenu/conseiller/").addConverterFactory(GsonConverterFactory.create()).build().create(Apis.class)).listEvents(App.getAppPreference().getString("LoginId")).enqueue(new Callback<ListEventsModel>() {
            public void onResponse(Call<ListEventsModel> call, Response<ListEventsModel> response) {
                if (!response.isSuccessful()) {
                    Events.this.dismissLoadingDialog();
                    Events.this.rv_event_list.setVisibility(8);
                    Events.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(Events.this.getContext(), "Data Not Found", 0).show();
                } else if (response.body() == null || response.body().getStatusCode() == null || response.body().getStatusCode().intValue() != 200) {
                    Events.this.dismissLoadingDialog();
                    Events.this.rv_event_list.setVisibility(8);
                    Events.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(Events.this.getContext(), "Data Not Found", 0).show();
                } else if (response.body() == null || response.body().getData().size() <= 0) {
                    Events.this.dismissLoadingDialog();
                    Events.this.rv_event_list.setVisibility(8);
                    Events.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(Events.this.getContext(), "Data Not Found", 0).show();
                } else {
                    Events.this.dismissLoadingDialog();
                    Events.this.rv_event_list.setVisibility(0);
                    Events.this.tvNoDataFound.setVisibility(8);
                    Event_List unused = Events.this.mAdapter = new Event_List(Events.this.getActivity(), response.body().getData());
                    Events.this.rv_event_list.setAdapter(Events.this.mAdapter);
                }
            }

            public void onFailure(Call<ListEventsModel> call, Throwable t) {
                Events.this.dismissLoadingDialog();
                Events.this.rv_event_list.setVisibility(8);
                Events.this.tvNoDataFound.setVisibility(0);
                Toast.makeText(Events.this.getContext(), t.getLocalizedMessage(), 1).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void myEvents() {
        showLoadingDialog();
        ((Apis) new Retrofit.Builder().baseUrl("http://devworktools.fr/contenu/conseiller/").addConverterFactory(GsonConverterFactory.create()).build().create(Apis.class)).myEvents(App.getAppPreference().getString("LoginId")).enqueue(new Callback<MyEventModel>() {
            public void onResponse(Call<MyEventModel> call, Response<MyEventModel> response) {
                if (!response.isSuccessful()) {
                    Events.this.dismissLoadingDialog();
                    Events.this.rv_event_list.setVisibility(8);
                    Events.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(Events.this.getContext(), "Data Not Found", 0).show();
                } else if (response.body() == null || response.body().getMessage() == null || response.body().getStatusCode().intValue() != 200) {
                    Events.this.dismissLoadingDialog();
                    Events.this.rv_event_list.setVisibility(8);
                    Events.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(Events.this.getContext(), response.body().getMessage(), 0).show();
                } else if (response.body() == null || response.body().getData().size() <= 0) {
                    Events.this.dismissLoadingDialog();
                    Events.this.rv_event_list.setVisibility(8);
                    Events.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(Events.this.getContext(), response.body().getMessage(), 0).show();
                } else {
                    Events.this.dismissLoadingDialog();
                    Events.this.rv_event_list.setVisibility(0);
                    Events.this.tvNoDataFound.setVisibility(8);
                    AdapterMyEvents unused = Events.this.mEventAdapter = new AdapterMyEvents(Events.this.getActivity(), response.body().getData(), Events.this);
                    Events.this.rv_event_list.setAdapter(Events.this.mEventAdapter);
                }
            }

            public void onFailure(Call<MyEventModel> call, Throwable t) {
                Events.this.dismissLoadingDialog();
                Events.this.rv_event_list.setVisibility(8);
                Events.this.tvNoDataFound.setVisibility(0);
                Toast.makeText(Events.this.getContext(), t.getLocalizedMessage(), 1).show();
            }
        });
    }

    public void showLoadingDialog() {
        if (this.progress == null) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            this.progress = progressDialog;
            progressDialog.setTitle(getString(R.string.loading_title));
            this.progress.setMessage(getString(R.string.loading_message));
            this.progress.setCancelable(false);
        }
        this.progress.show();
    }

    public void dismissLoadingDialog() {
        ProgressDialog progressDialog = this.progress;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.progress.dismiss();
        }
    }

    public void onEventDelete(int position, int idevent) {
        showLoadingDialog();
        ((Apis) new Retrofit.Builder().baseUrl("http://devworktools.fr/contenu/conseiller/").addConverterFactory(GsonConverterFactory.create()).build().create(Apis.class)).deleteEvent(idevent).enqueue(new Callback<DeleteEventModel>() {
            public void onResponse(Call<DeleteEventModel> call, Response<DeleteEventModel> response) {
                Events.this.dismissLoadingDialog();
                if (!response.isSuccessful() || response.body().getStatusCode().intValue() != 200) {
                    Events.this.dismissLoadingDialog();
                    Toast.makeText(Events.this.getContext(), response.body().getStatusMessage(), 0).show();
                    return;
                }
                Events.this.dismissLoadingDialog();
                Toast.makeText(Events.this.getContext(), response.body().getStatusMessage(), 0).show();
                Events.this.myEvents();
            }

            public void onFailure(Call<DeleteEventModel> call, Throwable t) {
                Events.this.dismissLoadingDialog();
                Toast.makeText(Events.this.getContext(), t.getMessage().toString(), 0).show();
            }
        });
    }
}
