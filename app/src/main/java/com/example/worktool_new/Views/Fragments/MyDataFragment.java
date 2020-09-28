package com.example.worktool_new.Views.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worktool_new.Adapters.Adapter_MyData;
import com.example.worktool_new.Models.MyDataModel;
import com.example.worktool_new.Models.MyDeleteDataModel;
import com.example.worktool_new.R;
import com.example.worktool_new.Retrofit.Apis;
import com.example.worktool_new.Util.SharedPreference.App;
import com.example.worktool_new.Views.Activities.RecyclerTouchListener;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyDataFragment extends Fragment implements Adapter_MyData.deleteData, Adapter_MyData.editData {
    /* access modifiers changed from: private */
    public Adapter_MyData mAdapter;
    private ProgressDialog progress;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            MyDataFragment.this.getMyData();
        }
    };
    /* access modifiers changed from: private */
    public RecyclerView rv_data;
    private RecyclerTouchListener touchListener;
    /* access modifiers changed from: private */
    public TextView tvNoDataFound;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.view == null) {
            this.view = inflater.inflate(R.layout.fragment_my__data, container, false);
        }
        return this.view;
    }

    public void onViewCreated(View view2, Bundle savedInstanceState) {
        super.onViewCreated(view2, savedInstanceState);
        init();
    }

    public void init() {
        this.rv_data = (RecyclerView) this.view.findViewById(R.id.rv_data);
        this.tvNoDataFound = (TextView) this.view.findViewById(R.id.tvNoDataFound);
        getMyData();
        RecyclerTouchListener recyclerTouchListener = new RecyclerTouchListener(getActivity(), this.rv_data);
        this.touchListener = recyclerTouchListener;
        recyclerTouchListener.setClickable((RecyclerTouchListener.OnRowClickListener) new RecyclerTouchListener.OnRowClickListener() {
            public void onRowClicked(int position) {
            }

            public void onIndependentViewClicked(int independentViewID, int position) {
            }
        }).setSwipeOptionViews(Integer.valueOf(R.id.delete_task), Integer.valueOf(R.id.edit_task)).setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
            public void onSwipeOptionClicked(int viewID, int position) {
            }
        });
    }

    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(this.receiver, new IntentFilter("updateData"));
        this.rv_data.addOnItemTouchListener(this.touchListener);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.receiver != null) {
            getActivity().unregisterReceiver(this.receiver);
        }
    }

    private void deleteData(final ArrayList<MyDataModel.Datum> datamodelArraylists, final int position) {
        showLoadingDialog();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://devworktools.fr/contenu/conseiller/").addConverterFactory(GsonConverterFactory.create()).build();
        ((Apis) retrofit.create(Apis.class)).deleteMyData(datamodelArraylists.get(position).getIdFichier(), datamodelArraylists.get(position).getTable()).enqueue(new Callback<MyDeleteDataModel>() {
            public void onResponse(Call<MyDeleteDataModel> call, Response<MyDeleteDataModel> response) {
                if (!response.isSuccessful()) {
                    MyDataFragment.this.dismissLoadingDialog();
                    Toast.makeText(MyDataFragment.this.getContext(), "Data Not Found", 0).show();
                } else if (response.body() != null && response.body().getStatusCode() != null && response.body().getStatusCode().intValue() == 200) {
                    MyDataFragment.this.dismissLoadingDialog();
                    Toast.makeText(MyDataFragment.this.getActivity(), response.body().getMessage(), 0).show();
                    datamodelArraylists.remove(position);
                    MyDataFragment.this.mAdapter.notifyItemRemoved(position);
                    MyDataFragment.this.mAdapter.notifyItemRangeChanged(position, datamodelArraylists.size());
                }
            }

            public void onFailure(Call<MyDeleteDataModel> call, Throwable t) {
                MyDataFragment.this.dismissLoadingDialog();
                Toast.makeText(MyDataFragment.this.getContext(), t.getLocalizedMessage(), 1).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void getMyData() {
        showLoadingDialog();
        ((Apis) new Retrofit.Builder().baseUrl("http://devworktools.fr/contenu/conseiller/").addConverterFactory(GsonConverterFactory.create()).build().create(Apis.class)).getMyData(App.getAppPreference().getString("LoginId")).enqueue(new Callback<MyDataModel>() {
            public void onResponse(Call<MyDataModel> call, Response<MyDataModel> response) {
                if (!response.isSuccessful()) {
                    MyDataFragment.this.dismissLoadingDialog();
                    MyDataFragment.this.rv_data.setVisibility(8);
                    MyDataFragment.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(MyDataFragment.this.getContext(), "Data Not Found", 0).show();
                } else if (response.body() == null || response.body().getStatusCode() == null || response.body().getStatusCode().intValue() != 200) {
                    MyDataFragment.this.dismissLoadingDialog();
                    MyDataFragment.this.rv_data.setVisibility(8);
                    MyDataFragment.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(MyDataFragment.this.getContext(), "Data Not Found", 0).show();
                } else if (response.body() == null || response.body().getData().size() <= 0) {
                    MyDataFragment.this.dismissLoadingDialog();
                    MyDataFragment.this.rv_data.setVisibility(8);
                    MyDataFragment.this.tvNoDataFound.setVisibility(0);
                    Toast.makeText(MyDataFragment.this.getContext(), "Data Not Found", 0).show();
                } else {
                    MyDataFragment.this.dismissLoadingDialog();
                    MyDataFragment.this.rv_data.setVisibility(0);
                    MyDataFragment.this.tvNoDataFound.setVisibility(8);
                    MyDataFragment myDataFragment = MyDataFragment.this;
                    FragmentActivity activity = MyDataFragment.this.getActivity();
                    ArrayList<MyDataModel.Datum> data = response.body().getData();
                    MyDataFragment myDataFragment2 = MyDataFragment.this;
                    Adapter_MyData unused = myDataFragment.mAdapter = new Adapter_MyData(activity, data, myDataFragment2, myDataFragment2);
                    MyDataFragment.this.rv_data.setAdapter(MyDataFragment.this.mAdapter);
                }
            }

            public void onFailure(Call<MyDataModel> call, Throwable t) {
                MyDataFragment.this.dismissLoadingDialog();
                MyDataFragment.this.rv_data.setVisibility(8);
                MyDataFragment.this.tvNoDataFound.setVisibility(0);
                Toast.makeText(MyDataFragment.this.getContext(), t.getLocalizedMessage(), 1).show();
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

    public void deleteMyData(ArrayList<MyDataModel.Datum> datamodelArraylists, int position) {
        deleteData(datamodelArraylists, position);
    }

    public void editMyData(String dataId, String table, String name) {
        Fragment fr = new EditData();
        Bundle bundle = new Bundle();
        bundle.putString("dataId", dataId);
        bundle.putString("table", table);
        bundle.putString("dataName", name);
        fr.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add((int) R.id.container, fr);
        fragmentTransaction.addToBackStack((String) null);
        fragmentTransaction.commit();
    }
}
