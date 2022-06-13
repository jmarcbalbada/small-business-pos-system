package com.example.small_business_pos_system.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.small_business_pos_system.Connect;
import com.example.small_business_pos_system.Inventory;
import com.example.small_business_pos_system.InventoryModel;
import com.example.small_business_pos_system.R;
import com.example.small_business_pos_system.Transaction;
import com.example.small_business_pos_system.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter itemArrayAdapter;
    private String selectedItem;
    private Connect conn;
    private EditText textToSearch;
    private Button search;
    private ListView lv_transaction;
    private Context context;
    private TextView searchResult;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        conn = new Connect(this.getContext());
        search = (Button) root.findViewById(R.id.searchButton);
        spinner = (Spinner) root.findViewById(R.id.spinner);
        textToSearch = (EditText) root.findViewById(R.id.searchInput);
        lv_transaction = (ListView) root.findViewById(R.id.lv_transaction);
        context = root.getContext();
        searchResult = (TextView) root.findViewById(R.id.searchResultLabel);
        adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.search_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        generateList();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = spinner.getSelectedItem().toString().trim();
                afterSearchWasClicked();
            }
        });
        return root;
    }

    public void afterSearchWasClicked()
    {
        List<Transaction> transactionList = conn.searchTransaction(textToSearch.getText().toString(),selectedItem);
        itemArrayAdapter = new ArrayAdapter<Transaction>(this.getContext(),android.R.layout.simple_list_item_1,transactionList);
        lv_transaction.setAdapter(itemArrayAdapter);
        searchResult.setText(transactionList.size() + " results found.");
    }

    public void generateList()
    {
        List<Transaction> transactionList = conn.firstFiveInTransaction();
        itemArrayAdapter = new ArrayAdapter<Transaction>(this.getContext(),android.R.layout.simple_list_item_1,transactionList);
        lv_transaction.setAdapter(itemArrayAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}