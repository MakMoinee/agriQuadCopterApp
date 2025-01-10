package com.drone.thesis;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drone.thesis.commons.LocalDefaults;
import com.drone.thesis.databinding.ActivityCreateAccountBinding;
import com.drone.thesis.models.Users;
import com.drone.thesis.preference.LocalPref;
import com.drone.thesis.services.UserService;
import com.github.MakMoinee.library.interfaces.DefaultBaseListener;
import com.github.MakMoinee.library.services.HashPass;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    HashPass hashPass = new HashPass();
    UserService userService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userService = new UserService(CreateAccountActivity.this);
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
                    String pw = hashPass.makeHashPassword(password);
                    Users users = new Users.UserBuilder()
                            .setFirstName(firstName)
                            .setMiddleName(middleName)
                            .setLastName(lastName)
                            .setUsername(username)
                            .setPassword(pw)
                            .build();
                    userService.insertUniqueUser(users, new DefaultBaseListener() {
                        @Override
                        public <T> void onSuccess(T t) {
                            Toast.makeText(CreateAccountActivity.this, "Successfully Added An Account", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(Error error) {
                            Toast.makeText(CreateAccountActivity.this, "Failed To Add Account, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(CreateAccountActivity.this, "Passwords Doesn't Match", Toast.LENGTH_SHORT).show();
                }
            }

        });
        binding.btnCancel.setOnClickListener(v -> finish());
    }
}
