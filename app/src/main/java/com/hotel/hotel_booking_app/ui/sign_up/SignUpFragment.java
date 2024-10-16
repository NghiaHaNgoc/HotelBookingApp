package com.hotel.hotel_booking_app.ui.sign_up;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentSignUpBinding;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.User;
import com.hotel.hotel_booking_app.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SharedPreferences accountPreferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        NavController navController = Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment_content_main);

        // Prepare alert dialog
        AlertDialog.Builder alertSignInFailed = new AlertDialog.Builder(getActivity());
        alertSignInFailed.setTitle(getResources().getString(R.string.sign_up_failed_alert_title))
                .setMessage(getResources().getString(R.string.sign_up_failed_alert_message));

        alertSignInFailed.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        binding.signUpSubmit.setOnClickListener(view -> {
            String firstname = binding.signUpFirstname.getText().toString().trim();
            String surname = binding.signUpSurname.getText().toString().trim();
            String email = binding.signUpEmail.getText().toString().trim();
            String password = binding.signUpPassword.getText().toString().trim();
            if (checkAllFields(firstname, surname, email, password)) {
                User.SignUpInput signUpInput = new User.SignUpInput(firstname, surname, email, password);
                new ApiService(getContext()).signUp(signUpInput).enqueue(new Callback<ApiResponse<User.SignInOutput>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User.SignInOutput>> call, Response<ApiResponse<User.SignInOutput>> response) {
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
                                    getResources().getString(R.string.sign_in_success_message),
                                    Toast.LENGTH_SHORT).show();
                            navController.popBackStack(R.id.nav_home, false);
                        } else {
                            alertSignInFailed.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User.SignInOutput>> call, Throwable throwable) {

                    }
                });
            }
        });

        return root;

    }

    private boolean checkAllFields(String firstname, String surname, String email, String password) {
        String errorMessage = getContext().getResources().getString(R.string.require_input_message);
        boolean isValid = true;
        if (firstname.isEmpty()) {
            binding.signUpFirstname.setError(errorMessage);
            isValid = false;
        }

        if (surname.isEmpty()) {
            binding.signUpSurname.setError(errorMessage);
            isValid = false;
        }

        if (email.isEmpty()) {
            binding.signUpEmail.setError(errorMessage);
            isValid = false;
        }

        if (password.isEmpty()) {
            binding.signUpPassword.setError(errorMessage);
            isValid = false;
        }

        // after all validation return true.
        return isValid;
    }
}