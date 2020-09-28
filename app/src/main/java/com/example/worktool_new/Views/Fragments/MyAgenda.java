package com.example.worktool_new.Views.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worktool_new.Adapters.AdapterAgenda;
import com.example.worktool_new.Models.MyAgendaModel;
import com.example.worktool_new.R;
import com.example.worktool_new.Retrofit.Apis;
import com.example.worktool_new.Util.SharedPreference.App;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyAgenda extends Fragment {
    /* access modifiers changed from: private */
    public AdapterAgenda mAdapter;
    private ProgressDialog progress;
    /* access modifiers changed from: private */
    public RecyclerView recyclerAgenda;
    /* access modifiers changed from: private */
    public TextView tvNoDataFound;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.view == null) {
            View inflate = inflater.inflate(R.layout.fragment_my_agenda, container, false);
            this.view = inflate;
            this.recyclerAgenda = (RecyclerView) inflate.findViewById(R.id.recyclerAgenda);
            this.tvNoDataFound = (TextView) this.view.findViewById(R.id.tvNoDataFound);
            AgendaPosts();
        }
        return this.view;
    }

    private void AgendaPosts() {
        showLoadingDialog();
        ((Apis) new Retrofit.Builder().baseUrl("http://devworktools.fr/contenu/conseiller/").addConverterFactory(GsonConverterFactory.create()).client(new OkHttpClient.Builder().addInterceptor(provideHttpLoggingInterceptor()).readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).build()).build().create(Apis.class)).myAgenda(App.getAppPreference().getString("LoginId")).enqueue(new Callback<MyAgendaModel>() {
            public void onResponse(Call<MyAgendaModel> call, Response<MyAgendaModel> response) {
                if (!response.isSuccessful()) {
                    MyAgenda.this.dismissLoadingDialog();
                    MyAgenda.this.recyclerAgenda.setVisibility(8);
                    MyAgenda.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(MyAgenda.this.getContext(), "Data Not Found", 0).show();
                } else if (response.body() == null || response.body().getStatusCode() == null || response.body().getStatusCode().intValue() != 200) {
                    MyAgenda.this.dismissLoadingDialog();
                    MyAgenda.this.recyclerAgenda.setVisibility(8);
                    MyAgenda.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(MyAgenda.this.getContext(), "Data Not Found", 0).show();
                } else if (response.body() == null || response.body().getData().size() <= 0) {
                    MyAgenda.this.dismissLoadingDialog();
                    MyAgenda.this.recyclerAgenda.setVisibility(8);
                    MyAgenda.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(MyAgenda.this.getContext(), "Data Not Found", 0).show();
                } else {
                    MyAgenda.this.dismissLoadingDialog();
                    MyAgenda.this.recyclerAgenda.setVisibility(0);
                    MyAgenda.this.tvNoDataFound.setVisibility(8);
                    AdapterAgenda unused = MyAgenda.this.mAdapter = new AdapterAgenda(MyAgenda.this.getActivity(), response.body().getData());
                    MyAgenda.this.recyclerAgenda.setAdapter(MyAgenda.this.mAdapter);
                }
            }

            public void onFailure(Call<MyAgendaModel> call, Throwable t) {
                MyAgenda.this.dismissLoadingDialog();
                MyAgenda.this.recyclerAgenda.setVisibility(8);
                MyAgenda.this.tvNoDataFound.setVisibility(0);
                Toast.makeText(MyAgenda.this.getContext(), t.getLocalizedMessage(), 1).show();
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

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            public void log(String message) {
                Log.d("Injector", message);
            }
        });
    }
}
