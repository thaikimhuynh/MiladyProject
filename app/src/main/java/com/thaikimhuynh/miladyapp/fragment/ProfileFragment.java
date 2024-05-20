package com.thaikimhuynh.miladyapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.EarnPointActivity;
import com.thaikimhuynh.miladyapp.HelpCenterActivity;
import com.thaikimhuynh.miladyapp.MyOrdersActivity;
import com.thaikimhuynh.miladyapp.MyWalletActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.SettingActivity;
import com.thaikimhuynh.miladyapp.WishListActivity;
import com.thaikimhuynh.miladyapp.payment.ViewPaymentMethodActivity;
import com.thaikimhuynh.miladyapp.payment.WalletInformationActivity;
import com.thaikimhuynh.miladyapp.profile.MyProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CardView cardMyProfile, cardMyOrders, cardMyWallet, cardEarnPoints, cardWishList, cardHelpCenter, cardSetting;
    TextView txtProfileName;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        cardMyProfile = view.findViewById(R.id.cardMyProfile);
        cardMyOrders = view.findViewById(R.id.cardMyOrders);
        cardMyWallet = view.findViewById(R.id.cardMyWallet);
        cardEarnPoints = view.findViewById(R.id.cardEarnPoints);
        cardWishList = view.findViewById(R.id.cardWishList);
        cardHelpCenter = view.findViewById(R.id.cardHelpCenter);
        cardSetting = view.findViewById(R.id.cardSetting);
        txtProfileName = view.findViewById(R.id.txtProfileName);

        String userId = getUserId();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(userId).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.getValue(String.class);
                    txtProfileName.setText(name);
                } else {
                    txtProfileName.setText("Name not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                txtProfileName.setText("Error loading name");
            }
        });



        cardMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);            }
        });
        cardMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyOrdersActivity.class);
                startActivity(intent);
            }
        });
        cardEarnPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EarnPointActivity.class);
                startActivity(intent);            }
        });
        cardMyWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyWalletActivity.class);
                startActivity(intent);            }
        });
        cardWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WishListActivity.class);
                startActivity(intent);            }
        });
        cardHelpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HelpCenterActivity.class);
                startActivity(intent);            }
        });
        cardSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);            }
        });
        return view;
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }


}