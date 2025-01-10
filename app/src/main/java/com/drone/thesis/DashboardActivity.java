package com.drone.thesis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.drone.thesis.databinding.ActivityDashboardBinding;
import com.drone.thesis.interfaces.LogoutListener;
import com.drone.thesis.preference.LocalPref;
import com.github.MakMoinee.library.preference.LoginPref;
import com.github.MakMoinee.library.services.Utils;

public class DashboardActivity extends AppCompatActivity implements LogoutListener {

    private ActivityDashboardBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarDashboard.toolbar);
        drawer = binding.drawerLayout;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(DashboardActivity.this, R.id.nav_host_fragment_content_dashboard);
        NavigationUI.setupActionBarWithNavController(DashboardActivity.this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        setNavDetails();
    }

    private void setNavDetails() {
        String firstName = new LoginPref(DashboardActivity.this).getStringItem("firstName");
        String middleName = new LoginPref(DashboardActivity.this).getStringItem("middleName");
        String lastName = new LoginPref(DashboardActivity.this).getStringItem("lastName");
        String username = new LoginPref(DashboardActivity.this).getStringItem("username");

        View mView = Utils.getNavView(binding.navView);
        TextView txtFullName = mView.findViewById(R.id.txtFullName);
        TextView txtUsername = mView.findViewById(R.id.txtUsername);
        txtFullName.setText(String.format("%s %s %s", firstName, middleName, lastName));
        txtUsername.setText(username);
    }


    @Override
    public void onCancelLogout() {
        navController.navigate(R.id.nav_home);
    }

    @Override
    public void onLogout() {
        new LocalPref(DashboardActivity.this).clearLogin();
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(DashboardActivity.this, R.id.nav_host_fragment_content_dashboard);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}