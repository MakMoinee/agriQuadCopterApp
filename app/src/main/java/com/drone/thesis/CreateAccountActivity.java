package com.drone.thesis;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drone.thesis.commons.LocalDefaults;
import com.drone.thesis.databinding.ActivityCreateAccountBinding;
import com.drone.thesis.models.Users;
import com.drone.thesis.repository.LocalSQL;
import com.drone.thesis.services.LocalSqliteManager;
import com.drone.thesis.services.UserService;
import com.github.MakMoinee.library.interfaces.DefaultBaseListener;
import com.github.MakMoinee.library.services.HashPass;
import com.github.MakMoinee.library.services.SqlLiteServiceManager;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    HashPass hashPass = new HashPass();
    LocalSqliteManager sm;
    UserService userService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sm = LocalSqliteManager.getInstance(CreateAccountActivity.this);
        userService = new UserService(sm.getSQLiteService());
        setListeners();
    }

    private void setListeners() {
        binding.btnCreateAccount.setOnClickListener(v -> {
            String firstName = binding.editFN.getText().toString().trim();
            String middleName = binding.editMN.getText().toString().trim();
            String lastName = binding.editLN.getText().toString().trim();
            String username = binding.editUsername.getText().toString().trim();
            String password = binding.editPassword.getText().toString().trim();
            String confirmPass = binding.editConfirm.getText().toString().trim();

            if (firstName.equals("") || lastName.equals("") || username.equals("") || password.equals("") || confirmPass.equals("")) {
                Toast.makeText(CreateAccountActivity.this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                if (confirmPass.equals(password)) {
                    if (LocalDefaults.isBound) {
                        String pw = hashPass.makeHashPassword(password);
                        Users users = new Users.UserBuilder()
                                .setFirstName(firstName)
                                .setMiddleName(middleName)
                                .setLastName(lastName)
                                .setUsername(username)
                                .setPassword(pw)
                                .build();
                        userService.insertUser(users, new DefaultBaseListener() {
                            @Override
                            public <T> void onSuccess(T any) {
                                Toast.makeText(CreateAccountActivity.this, "Successfully Added Account", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onError(Error error) {
                                Toast.makeText(CreateAccountActivity.this, "Failed To Create Account, Please Try Again Later", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(CreateAccountActivity.this, "Something Wrong Happened Internally, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CreateAccountActivity.this, "Passwords Doesn't Match", Toast.LENGTH_SHORT).show();
                }
            }

        });
        binding.btnCancel.setOnClickListener(v -> finish());
    }
}
