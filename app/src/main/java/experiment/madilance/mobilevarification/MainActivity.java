package experiment.madilance.mobilevarification;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    EditText mobileNumber;
    Button verify;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mphoneNumber;
    FirebaseAuth mAuth;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    //PhoneAuthCredential Credential = PhoneAuthProvider.getCredential(mVerificationId, String.valueOf(mResendToken));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobileNumber=findViewById(R.id.editTextMobile);
        verify=findViewById(R.id.buttonGetCode);
        mAuth=FirebaseAuth.getInstance();

       verify.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String phoneNumber = mobileNumber.getText().toString();
               PhoneAuthProvider.getInstance().verifyPhoneNumber(
                       phoneNumber,
                       60,
                       TimeUnit.SECONDS,
                       MainActivity.this,
                       mphoneNumber);

           }
       });



        mphoneNumber = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential Credential) {


                Toast.makeText(MainActivity.this,"verifyed",Toast.LENGTH_SHORT).show();


                signInWithPhoneAuthCredential(Credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(getApplicationContext(),e+"...",Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {



                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText(getApplicationContext(),"code send",Toast.LENGTH_SHORT).show();

                // ...
            }
        };


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential Credential) {
        mAuth.signInWithCredential(Credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),"sign in success",Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();
                            Intent intent=new Intent(getApplicationContext(),Profile.class);
                            startActivity(intent);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(),"sign in Failed",Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
