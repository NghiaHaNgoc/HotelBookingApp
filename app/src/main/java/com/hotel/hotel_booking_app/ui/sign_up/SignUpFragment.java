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

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
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
        binding.buttonSignUpToSignIn.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_sign_up_to_nav_sign_in);
        });

        TextInputLayout firstnameInputLayout = binding.firstnameInputLayout;
        TextInputLayout surnameInputLayout = binding.surnameInputLayout;
        TextInputLayout emailInputLayout = binding.emailInputLayout;
        TextInputLayout passwordInputLayout = binding.passwordInputLayout;

        binding.signUpFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                firstnameInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.signUpSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                surnameInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.signUpEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.signUpPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.signUpSubmit.setOnClickListener(view -> {
            String firstname = binding.signUpFirstname.getText().toString().trim();
            String surname = binding.signUpSurname.getText().toString().trim();
            String email = binding.signUpEmail.getText().toString().trim();
            String password = binding.signUpPassword.getText().toString().trim();
            if (validateFormSignUp(firstname, surname, email, password)) {
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

    private boolean validateFormSignUp(
            String firstname,
            String surname,
            String email,
            String password
    ) {
        String messageErrorFirstname = "";
        String messageErrorSurname = "";
        String messageErrorEmail = "";
        String messageErrorPassword = "";

        //validate firstname
        if (TextUtils.isEmpty(firstname)) {
            messageErrorFirstname = "Firstname is required !";
        }
        if (!messageErrorFirstname.isEmpty()) {
            binding.firstnameInputLayout.setError(messageErrorFirstname);
        }

        //validate surname
        if (TextUtils.isEmpty(surname)) {
            messageErrorSurname = "Surname is required !";
        }
        if (!messageErrorSurname.isEmpty()) {
            binding.surnameInputLayout.setError(messageErrorSurname);
        }

        // validate email
        if (TextUtils.isEmpty(email)) {
            messageErrorEmail = "Email is required !";
        }
        if (
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            messageErrorEmail.isEmpty()
        ) {
            messageErrorEmail = "Email is wrong format !";
        }
        if (!messageErrorEmail.isEmpty()) {
            binding.emailInputLayout.setError(messageErrorEmail);
        }

        //validate password
        if (TextUtils.isEmpty(password)) {
            messageErrorPassword = "Password is required !";
        }
        if (password.length() < 6 && messageErrorPassword.isEmpty()) {
            messageErrorPassword = "Password must be at least 6 characters !";
        }
        if (!messageErrorPassword.isEmpty()) {
            binding.passwordInputLayout.setError(messageErrorPassword);
        }

        return messageErrorEmail.isEmpty() && messageErrorPassword.isEmpty() &&
                messageErrorFirstname.isEmpty() && messageErrorSurname.isEmpty();
    }
}