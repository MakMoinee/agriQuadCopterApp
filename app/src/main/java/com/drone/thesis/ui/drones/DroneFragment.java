package com.drone.thesis.ui.drones;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.drone.thesis.DroneActivity;
import com.drone.thesis.databinding.FragmentDronesBinding;
import com.drone.thesis.interfaces.DroneFragmentListener;

public class DroneFragment extends Fragment {

    FragmentDronesBinding binding;
    DroneFragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDronesBinding.inflate(LayoutInflater.from(requireContext()), container, false);
        listener.setParentTitle();
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.btnAddDrone.setOnClickListener(v -> requireContext().startActivity(new Intent(requireContext(), DroneActivity.class)));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof DroneFragmentListener) {
            listener = (DroneFragmentListener) context;

        } else {
            throw new ClassCastException(context.toString() + " must implement DroneFragmentListener");
        }
    }
}
