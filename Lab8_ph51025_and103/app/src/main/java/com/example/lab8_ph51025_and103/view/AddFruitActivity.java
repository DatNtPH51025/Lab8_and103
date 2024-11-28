package com.example.lab8_ph51025_and103.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lab8_ph51025_and103.R;
import com.example.lab8_ph51025_and103.databinding.ActivityAddFruitBinding;
import com.example.lab8_ph51025_and103.model.Distributor;
import com.example.lab8_ph51025_and103.model.Fruit;
import com.example.lab8_ph51025_and103.model.Response;
import com.example.lab8_ph51025_and103.services.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddFruitActivity extends AppCompatActivity {
    ActivityAddFruitBinding binding;
    private HttpRequest httpRequest;
    private String id_Distributor;
    private ArrayList<Distributor> distributorArrayList;
    private ArrayList<File> ds_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddFruitBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        ds_image = new ArrayList<>();
        httpRequest = new HttpRequest();
        configSpinner();
        userListener();

    }
    private void userListener() {
binding.avatar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        chooseImage();
    }
});

    binding.btnRegister.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String , RequestBody> mapRequestBody = new HashMap<>();
            String _name = binding.edName.getText().toString().trim();
            String _quantity = binding.edQuantity.getText().toString().trim();
            String _price = binding.edPrice.getText().toString().trim();
            String _status = binding.edStatus.getText().toString().trim();
            String _description = binding.edDescription.getText().toString().trim();

            mapRequestBody.put("name", getRequestBody(_name));
            mapRequestBody.put("quantity", getRequestBody(_quantity));
            mapRequestBody.put("price", getRequestBody(_price));
            mapRequestBody.put("status", getRequestBody(_status));
            mapRequestBody.put("description", getRequestBody(_description));
            mapRequestBody.put("id_distributor", getRequestBody(id_Distributor));
            ArrayList<MultipartBody.Part> _ds_image = new ArrayList<>();
            ds_image.forEach(file1 -> {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file1);
                MultipartBody.Part multipartBodyPart = MultipartBody.Part.createFormData("image", file1.getName(),requestFile);
                _ds_image.add(multipartBodyPart);
            });
            httpRequest.callAPI().addFruitWithFileImage(mapRequestBody, _ds_image).enqueue(responseFruit);


        }
    });
    }

    Callback<Response<Fruit>> responseFruit = new Callback<Response<Fruit>>() {
        @Override
        public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
            if (response.isSuccessful()) {
                Log.d("123123", "onResponse: " + response.body().getStatus());
                if (response.body().getStatus()==200) {
                    Toast.makeText(AddFruitActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Fruit>> call, Throwable t) {
            Toast.makeText(AddFruitActivity.this, "Thêm  thành công", Toast.LENGTH_SHORT).show();
            Log.e("zzzzzzzzzz", "onFailure: "+t.getMessage());
        }
    };

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"),value);
    }
    private void chooseImage() {
        Log.d("123123", "chooseAvatar: " + 123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getImage.launch(intent);
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        ds_image.clear();
                        Intent data = o.getData();
                        if (data != null) {
                            // Xử lý khi chọn nhiều hình ảnh
                            if (data.getClipData() != null) {
                                int count = data.getClipData().getItemCount();
                                for (int i = 0; i < count; i++) {
                                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                    File file = createFileFromUri(imageUri, "image" + i);
                                    if (file != null) {
                                        ds_image.add(file);
                                    }
                                }
                            } else if (data.getData() != null) {
                                // Xử lý khi chỉ chọn một hình ảnh
                                Uri imageUri = data.getData();
                                File file = createFileFromUri(imageUri, "image");
                                if (file != null) {
                                    ds_image.add(file);
                                }
                            }

                            // Hiển thị hình ảnh đầu tiên nếu danh sách không rỗng
                            if (!ds_image.isEmpty()) {
                                Glide.with(AddFruitActivity.this)
                                        .load(ds_image.get(0))
                                        .thumbnail(Glide.with(AddFruitActivity.this).load(R.drawable.baseline_broken_image_24))
                                        .centerCrop()
                                        .circleCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(binding.avatar);
                            } else {
                                Log.d("123123", "No images selected");
                            }
                        }
                    }
                }
            });

    private File createFileFromUri(Uri uri, String name) {
        File file = new File(AddFruitActivity.this.getCacheDir(), name + ".png");
        try (InputStream in = AddFruitActivity.this.getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(file)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            Log.d("123123", "createFileFromUri: " + file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("123123", "Error creating file from URI", e);
        }
        return null;
    }
    private void configSpinner() {
        httpRequest.callAPI().getListDistributor().enqueue(getDistributorAPI);
        binding.spDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                id_Distributor = distributorArrayList.get(position).getId();
                Log.d("123123", "onItemSelected: " + id_Distributor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spDistributor.setSelection(0);
    }

    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    distributorArrayList = response.body().getData();
                    String[] items = new String[distributorArrayList.size()];

                    for (int i = 0; i< distributorArrayList.size(); i++) {
                        items[i] = distributorArrayList.get(i).getName();
                    }
                    ArrayAdapter<String> adapterSpin = new ArrayAdapter<>(AddFruitActivity.this, android.R.layout.simple_spinner_item, items);
                    adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spDistributor.setAdapter(adapterSpin);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            t.getMessage();
        }

    };
    }