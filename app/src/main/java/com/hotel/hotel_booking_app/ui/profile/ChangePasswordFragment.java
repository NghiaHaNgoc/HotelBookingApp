package com.hotel.hotel_booking_app.ui.profile;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentChangePasswordBinding;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.User;
import com.hotel.hotel_booking_app.service.ApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;
    private AlertDialog alertDialogLoading;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        alertDialogLoading = new AlertDialog.Builder(
                getActivity(),
                R.style.TransparentDialog
        ).setView(R.layout.loading_progress).setCancelable(false).create();

        binding.oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.oldPasswordInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.newPasswordInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.confirmPasswordInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btnChangePassword.setOnClickListener(v -> {
            String oldPassword = Objects.requireNonNull(binding.oldPassword.getText()).toString();
            String newPassword = Objects.requireNonNull(binding.newPassword.getText()).toString();
            String confirmPassword = Objects.requireNonNull(binding.confirmPassword.getText()).toString();
            boolean isValidForm = validateFormChangePassword(
                    oldPassword,
                    newPassword,
                    confirmPassword
            );
            if (isValidForm) {
                alertDialogLoading.show();
                User.FormChangePassword formChangePassword = new User.FormChangePassword(
                        oldPassword,
                        newPassword
                );
                new ApiService(getContext()).changePassword(formChangePassword).enqueue(new Callback<ApiResponse<User.FormChangePassword>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User.FormChangePassword>> call, Response<ApiResponse<User.FormChangePassword>> response) {
                        ApiResponse<User.FormChangePassword> res = response.body();
                        if (res != null && res.status == 200) {
                            Toast.makeText(getContext(),
                                    getResources().getString(R.string.change_password_success),
                                    Toast.LENGTH_SHORT).show();
                            binding.oldPassword.setText(null);
                            binding.newPassword.setText(null);
                            binding.confirmPassword.setText(null);
                        } else {
                            // hiện api này chỉ có 1 case 400 là wrong old password !
                            Toast.makeText(getContext(),
                                    getResources().getString(R.string.wrong_old_password),
                                    Toast.LENGTH_SHORT).show();
                            binding.oldPasswordInputLayout.setError(getString(R.string.wrong_old_password));
                        }
                        alertDialogLoading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User.FormChangePassword>> call, Throwable throwable) {
                        Toast.makeText(getContext(),
                                getResources().getString(R.string.change_password_failed),
                                Toast.LENGTH_SHORT).show();
                        alertDialogLoading.dismiss();
                    }
                });
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private boolean validateFormChangePassword(
            String oldPassword,
            String newPassword,
            String confirmPassword
    ) {
        String messageErrorOldPassword = "";
        String messageErrorNewPassword = "";
        String messageErrorConfirmPassword = "";

        // validate Old password
        if (TextUtils.isEmpty(oldPassword)) {
            messageErrorOldPassword = getString(R.string.old_password_required);
        }
        if (!messageErrorOldPassword.isEmpty()) {
            binding.oldPasswordInputLayout.setError(messageErrorOldPassword);
        }

        // validate new password
        if (TextUtils.isEmpty(newPassword)) {
            messageErrorNewPassword = getString(R.string.new_password_required);
        }
        if (newPassword.length() < 6 && messageErrorNewPassword.isEmpty()) {
            messageErrorNewPassword = getString(R.string.min_length_password);
        }
        if (!messageErrorNewPassword.isEmpty()) {
            binding.newPasswordInputLayout.setError(messageErrorNewPassword);
        }

        // validate confirm password
        if (!confirmPassword.equals(newPassword)) {
            messageErrorConfirmPassword = getString(R.string.match_confirm_password);
        }
        if (!messageErrorConfirmPassword.isEmpty()) {
            binding.confirmPasswordInputLayout.setError(messageErrorConfirmPassword);
        }

        return messageErrorOldPassword.isEmpty() && messageErrorNewPassword.isEmpty() && messageErrorConfirmPassword.isEmpty();
    }
}