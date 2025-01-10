package com.drone.thesis.ui.logout;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.drone.thesis.databinding.FragmentLogoutBinding;
import com.drone.thesis.interfaces.LogoutListener;

public class LogoutFragment extends Fragment {

    FragmentLogoutBinding binding;
    LogoutListener listener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLogoutBinding.inflate(LayoutInflater.from(requireContext()), container, false);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
        DialogInterface.OnClickListener dListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE -> {
                    listener.onLogout();
                }
                default -> {
                    listener.onCancelLogout();
                    dialog.dismiss();
                }
            }
        };
        mBuilder.setMessage("Are You Sure You Want Logout?")
                .setNegativeButton("Yes", dListener)
                .setPositiveButton("Cancel", dListener)
                .setCancelable(false)
                .show();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the parent activity implements the LogoutListener interface
        if (context instanceof LogoutListener) {
            listener = (LogoutListener) context;

        } else {
            throw new ClassCastException(context.toString() + " must implement LogoutListener");
        }
    }
}
