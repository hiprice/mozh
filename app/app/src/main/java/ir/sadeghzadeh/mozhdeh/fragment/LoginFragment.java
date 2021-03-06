package ir.sadeghzadeh.mozhdeh.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import ir.sadeghzadeh.mozhdeh.ApplicationController;
import ir.sadeghzadeh.mozhdeh.Const;
import ir.sadeghzadeh.mozhdeh.R;
import ir.sadeghzadeh.mozhdeh.entity.AuthResponse;
import ir.sadeghzadeh.mozhdeh.utils.Util;
import ir.sadeghzadeh.mozhdeh.volley.GsonRequest;

/**
 * Created by reza on 1/11/17.
 */
public class LoginFragment extends BaseFragment {

    public static final String TAG = LoginFragment.class.getName();
    EditText email;
    EditText password;
    Button next;
    Button register;

    boolean redirectToNew =  false;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = layoutInflater.inflate(R.layout.login_fragment, container, false);
        Bundle args = getArguments();
        if (args != null && args.getBoolean(Const.REDIRECT_TO_NEW)) {
            redirectToNew =  true;
        }

        initEmail(view);
        initPassword(view);
        initLogin(view);
        initRegister(view);
        animate(view.findViewById(R.id.main_layout));

        return view;
    }

    private void initRegister(View view) {
        register = (Button) view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment fragment = new RegisterFragment();
                activity.addFragmentToContainer(fragment,RegisterFragment.TAG);
            }
        });
    }

    private void initLogin(View view) {
        activity.closeKeyboard();
        next = (Button) view.findViewById(R.id.login);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().trim().equals("")) {
                    email.setError(getString(R.string.invalid_email));
                    email.requestFocus();
                    return;
                }

                if (password.getText().toString().trim().equals("")) {
                    password.setError(getString(R.string.invalid_password));
                    password.requestFocus();
                    return;
                }

                Map<String, String> params = new HashMap<>();
                params.put(Const.PASSWORD, password.getText().toString());
                params.put(Const.EMAIL, email.getText().toString());
                params.put(Const.FIREBASE_TOKEN, Util.fetchFromPreferences(Const.FIREBASE_TOKEN));

                activity.showProgress();
                GsonRequest<AuthResponse> request = new GsonRequest<AuthResponse>(Const.AUTH_USER_URL, AuthResponse.class, params, null, new Response.Listener<AuthResponse>() {
                    @Override
                    public void onResponse(AuthResponse response) {
                        if (response.Status == 1) {
                            Util.saveInPreferences(Const.TOKEN, response.Token);
                            activity.hideProgress();
                            if(redirectToNew){
                                NewFragment fragment = new NewFragment();
                                activity.addFragmentToContainer(fragment, NewFragment.TAG);
                            }else {
                                MyItemsFragment fragment = new MyItemsFragment();
                                activity.addFragmentToContainer(fragment, MyItemsFragment.TAG);
                            }
                        } else if (response.Status == 0) {
                            activity.hideProgress();
                            Toast.makeText(getContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.hideProgress();
                        Toast.makeText(getContext(), getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                    }
                });
                ApplicationController.getInstance().addToRequestQueue(request);

            }
        });
    }

    private void initPassword(View view) {
        password = (EditText) view.findViewById(R.id.password);
    }

    private void initEmail(View view) {
        email = (EditText) view.findViewById(R.id.email);
    }


}
