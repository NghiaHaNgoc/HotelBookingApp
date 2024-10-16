package com.hotel.hotel_booking_app.ui.sign_in;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentSignInBinding;
import com.hotel.hotel_booking_app.databinding.NavHeaderMainBinding;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.User;
import com.hotel.hotel_booking_app.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;

    public SignInFragment() {
    }

    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences accountPreferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        NavController navController = Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment_content_main);

        // Prepare alert dialog
        AlertDialog.Builder alertSignInFailed = new AlertDialog.Builder(getActivity());
        alertSignInFailed.setTitle(getResources().getString(R.string.sign_in_failed_alert_title))
                .setMessage(getResources().getString(R.string.sign_in_failed_alert_message));

        alertSignInFailed.setNegativeButton(getResources().getString(R.string.sign_up),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertSignInFailed.setPositiveButton(getResources().getString(R.string.retry),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });


        binding.buttonSignInSubmit.setOnClickListener(view -> {
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPassword.getText().toString();
            User.SignInInput signInInput = new User.SignInInput(
                    email,
                    password
            );
            new ApiService(getContext()).signIn(signInInput).enqueue(new Callback<ApiResponse<User.SignInOutput>>() {
                @Override
                public void onResponse(Call<ApiResponse<User.SignInOutput>> call,
                                       Response<ApiResponse<User.SignInOutput>> response) {
                    ApiResponse<User.SignInOutput> res = response.body();
                    if (res != null && res.status == 200) {

                        SharedPreferences.Editor editor =  accountPreferences.edit();
                        editor.putBoolean("isSignedIn", true);
                        editor.putString("email", email);
                        editor.putString("firstname", res.result.firstname);
                        editor.putString("surname", res.result.surname);
                        editor.putString("linkAvatar", res.result.linkAvatar);
                        editor.putString("token", res.result.token);
                        editor.apply();
                        Toast.makeText(getContext(),
                                getResources().getString(R.string.sign_up_success_message),
                                Toast.LENGTH_SHORT).show();
                        navController.popBackStack(R.id.nav_home, false);

                    } else {
                        alertSignInFailed.show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<User.SignInOutput>> call,
                                      Throwable throwable) {

                }
            });
        });
        return root;
    }
}