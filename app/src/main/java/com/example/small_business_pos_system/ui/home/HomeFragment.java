package com.example.small_business_pos_system.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.small_business_pos_system.AddInventory;
import com.example.small_business_pos_system.Connect;
import com.example.small_business_pos_system.EditInventory;
import com.example.small_business_pos_system.Inventory;
import com.example.small_business_pos_system.InventoryModel;
import com.example.small_business_pos_system.R;
import com.example.small_business_pos_system.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Connect conn;
    private ArrayAdapter itemArrayAdapter;
    private AppCompatButton yesButton, cancelButton;
//    private Button yesButton, cancelButton;
    private ListView lv_itemlist;
    private Button addItemButton;
    private Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

//    private EditText editNameInput,editPriceInput,editQuantityInput;
//    AlertDialog.Builder builderDialog;
//    AlertDialog alertDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        conn = new Connect(this.getContext());
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        lv_itemlist = (ListView) root.findViewById(R.id.lv_items);
        addItemButton = (Button) root.findViewById(R.id.addItemButton);
        preferences = this.getActivity().getSharedPreferences("activity",0);
        editor = preferences.edit();
//        editNameInput = (EditText) root.findViewById(R.id.editNameInput);
//        editPriceInput = (EditText) root.findViewById(R.id.editPriceInput);
//        editQuantityInput = (EditText) root.findViewById(R.id.editQuantityInput);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemClicked();
            }
        });
        context = this.getContext();
//        yesButton = root.findViewById(R.id.yesButton);
//        cancelButton = root.findViewById(R.id.cancelButton);
        lv_itemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                InventoryModel model = (InventoryModel) adapterView.getItemAtPosition(position);
                editor.putString("itemName",model.getName());
                editor.putFloat("itemPrice",model.getPrice());
                editor.putInt("itemQuantity",model.getQuantity());
                editor.commit();
//                editNameInput.setText(model.getName());
//                editPriceInput.setText(Float .toString(model.getPrice()));
//                editQuantityInput.setText(Integer.toString(model.getQuantity()));
                Toast.makeText(context, model.toString(), Toast.LENGTH_SHORT).show();
//                showAlertDialog(R.layout.edit_dialog_layout);
                Intent i = new Intent(context, EditInventory.class);
                startActivity(i);
            }
        });
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

    public void addItemClicked()
    {
        Intent i = new Intent(this.getContext(),AddInventory.class);
        startActivity(i);
    }

//    private void showAlertDialog(int myLayout)
//    {
//        builderDialog = new AlertDialog.Builder(context);
//        View layoutView = getLayoutInflater().inflate(myLayout,null);
//        builderDialog.setView(layoutView);
//        alertDialog = builderDialog.create();
//        alertDialog.show();
//        yesButton = (AppCompatButton) rootParent.findViewById(R.id.yesButton);
//        cancelButton = (AppCompatButton) rootParent.findViewById(R.id.cancelButton);
//
//        yesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "here", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}