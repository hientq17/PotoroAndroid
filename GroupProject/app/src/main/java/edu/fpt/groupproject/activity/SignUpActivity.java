package edu.fpt.groupproject.activity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IUserApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.common.ReturnModel;
import edu.fpt.groupproject.model.user.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton imgBtnBack;
    Button btnSignUp;
    RadioGroup rdGroup;
    EditText txtUsername, txtNewPassword, txtConfirmPassword, txtName, txtAddress, txtPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(this);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        txtUsername = findViewById(R.id.txtUsername);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);
        rdGroup = findViewById(R.id.rdGroup);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnBack:
                SignUpActivity.this.finish();
                break;
            case R.id.btnSignUp:
                if(signUpValidation()){
                    User user = new User();
                    user.setUsername(txtUsername.getText().toString());
                    user.setPassword(txtNewPassword.getText().toString());
                    user.setName(txtName.getText().toString());
                    user.setAddress(txtAddress.getText().toString());
                    user.setPhone(txtPhone.getText().toString());
                    user.setRole(rdGroup.getCheckedRadioButtonId()==R.id.rdAdmin?"ADMIN":"USER");
                    Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                            .addConverterFactory(GsonConverterFactory.create()) .build();
                    IUserApi userApi = retrofit.create(IUserApi.class);
                    userApi.signup(user).enqueue(new Callback<ReturnModel>() {
                        @Override
                        public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                            ReturnModel returnModel = response.body();
                            if(returnModel.isSuccess()){
                                Toast.makeText(SignUpActivity.this, "????ng k?? th??nh c??ng. Vui l??ng ????ng nh???p ????? ti???p t???c", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable(){
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }, 1000);
                            } else if(returnModel.getMessage().equals("EXISTED")){
                                Toast.makeText(SignUpActivity.this, "T??i kho???n ???? t???n t???i", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, "L???i h??? th???ng: "+returnModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ReturnModel> call, Throwable t) {
                            try {
                                throw t;
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }
    }

    public boolean signUpValidation(){
        if(txtUsername.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui l??ng nh???p t??n ????ng nh???p", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtNewPassword.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui l??ng nh???p m???t kh???u", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!String.valueOf(txtNewPassword.getText()).equals(String.valueOf(txtConfirmPassword.getText()))){
            Toast.makeText(SignUpActivity.this, "Nh???p l???i m???t kh???u kh??ng tr??ng kh???p", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtName.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui l??ng nh???p t??n c???a b???n", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtAddress.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui l??ng nh???p ?????a ch???", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtPhone.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui l??ng nh???p s??? ??i???n tho???i", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}