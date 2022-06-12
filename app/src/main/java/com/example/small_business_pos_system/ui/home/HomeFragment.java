package com.example.small_business_pos_system.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.small_business_pos_system.Connect;
import com.example.small_business_pos_system.Inventory;
import com.example.small_business_pos_system.InventoryModel;
import com.example.small_business_pos_system.MainActivity;
import com.example.small_business_pos_system.R;
import com.example.small_business_pos_system.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Connect conn;
    private ArrayAdapter itemArrayAdapter;
    private ListView lv_itemlist;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        conn = new Connect(this.getContext());
//        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        lv_itemlist = (ListView) root.findViewById(R.id.lv_items);
        transform();
        return root;
    }

    public void transform()
    {
        List<Inventory> itemList = conn.getAllInventory();
        List<InventoryModel> modelList = new ArrayList<>();

        for(Inventory inventory: itemList)
        {
            InventoryModel model = new InventoryModel(inventory.getItem().getName(),inventory.getItem().getPrice(),inventory.getQuantity());
            modelList.add(model);
        }

        itemArrayAdapter = new ArrayAdapter<InventoryModel>(this.getContext(),android.R.layout.simple_list_item_1,modelList);
        lv_itemlist.setAdapter(itemArrayAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}