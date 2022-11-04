package com.example.findfriends.ui.home;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.findfriends.R;
import com.example.findfriends.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Intent lancement d'une activité
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.btn.setOnClickListener(v->
                {
                    String numero=binding.nb.getText().toString();
                    //envoie sms
                    SmsManager manager=SmsManager.getDefault();
                    manager.sendTextMessage(numero,null,"FindFriends:envoyer moi votre position",null,null);
                }
                );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void Onclick(View view){
       // String numero=binding.nb.
    }
}