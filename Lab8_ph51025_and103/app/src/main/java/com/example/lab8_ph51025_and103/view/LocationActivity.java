package com.example.lab8_ph51025_and103.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab8_ph51025_and103.adapter.Adapter_Item_District_Select_GHN;
import com.example.lab8_ph51025_and103.adapter.Adapter_Item_Province_Select_GHN;
import com.example.lab8_ph51025_and103.adapter.Adapter_Item_Ward_Select_GHN;
import com.example.lab8_ph51025_and103.databinding.ActivityLocationBinding;
import com.example.lab8_ph51025_and103.model.District;
import com.example.lab8_ph51025_and103.model.DistrictRequest;
import com.example.lab8_ph51025_and103.model.Province;
import com.example.lab8_ph51025_and103.model.ResponseGHN;
import com.example.lab8_ph51025_and103.model.Ward;
import com.example.lab8_ph51025_and103.services.GHNRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    ActivityLocationBinding binding;
    GHNRequest ghnRequest;
    Adapter_Item_Province_Select_GHN adapterItemProvinceSelectGhn;
    Adapter_Item_District_Select_GHN adapterItemDistrictSelectGhn;
    Adapter_Item_Ward_Select_GHN adapterItemWardSelectGhn;
    ArrayList<District> list_District = new ArrayList<>();
    ArrayList<Province> list_Province = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());


        ghnRequest = new GHNRequest();
        configGHN();
        userListener();
    }
    private void userListener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configGHN(){
        Call<ResponseGHN<ArrayList<Province>>> call = ghnRequest.getApiService().getListProvince();
        call.enqueue(new Callback<ResponseGHN<ArrayList<Province>>>() {
            @Override
            public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getCode() == 200){
                        list_Province = new ArrayList<>(response.body().getData());
                        Log.e("TAG", "onResponse: "+"Call thành công");
                        adapterItemProvinceSelectGhn = new Adapter_Item_Province_Select_GHN(LocationActivity.this,list_Province);
                        binding.spProvince.setAdapter(adapterItemProvinceSelectGhn);

                    }else{
                        Log.e("TAG", "onResponse: "+"Call thất bại");
                    }
                }else{
                    Log.e("TAG", "onResponse: "+"Call thất bại");
                    Log.e("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {

            }
        });
        binding.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int provinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(provinceID);
                ghnRequest.getApiService().getListDistrict(districtRequest).enqueue(new Callback<ResponseGHN<ArrayList<District>>>() {
                    @Override
                    public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
                        if(response.isSuccessful()){
                            list_District = response.body().getData();
                            adapterItemDistrictSelectGhn = new Adapter_Item_District_Select_GHN(LocationActivity.this,list_District);
                            binding.spDistrict.setAdapter(adapterItemDistrictSelectGhn);
                        }else{
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {
                        Log.e("TAG", "onFailure: "+t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // district
        binding.spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                District district = list_District.get(position);
                ghnRequest.getApiService().getListWard(district.getDistrictID()).enqueue(new Callback<ResponseGHN<ArrayList<Ward>>>() {
                    @Override
                    public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
                        if(response.isSuccessful()){
                            ArrayList<Ward> list = new ArrayList<>(response.body().getData());
                            adapterItemWardSelectGhn = new Adapter_Item_Ward_Select_GHN(LocationActivity.this,list);
                            binding.spWard.setAdapter(adapterItemWardSelectGhn);
                        }else{
                            Toast.makeText(LocationActivity.this, "Xẩy ra lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {
                        Log.e("TAG", "onFailure: "+t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }




}