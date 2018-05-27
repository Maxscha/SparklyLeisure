package org.hpi.hack.sparklyleisure;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.hpi.hack.sparklyleisure.model.Issue;
import org.hpi.hack.sparklyleisure.model.IssueType;
import org.hpi.hack.sparklyleisure.remote.ApiUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnIssueCreationListener} interface
 * to handle interaction events.
 * Use the {@link IssueCreationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueCreationFragment extends DialogFragment implements LocationListener {
    @BindView(R.id.badnessLayout)
    FrameLayout badnessLayout;

    RatingBar badnessRatingBar;

    TextView badnessText;

    ProgressBar badnessLoadingBar;

    @BindView(R.id.issueTypeSpinner)
    Spinner issueTypeSpinner;

    @BindView(R.id.locationText)
    TextView locationText;

    @BindView(R.id.photoImageView)
    ImageView photoImageView;



    Issue currentIssue = new Issue();

    private OnIssueCreationListener mListener;
    private Uri imageUri;
    private File imageFile;
    private LocationManager locationManager;

    public IssueCreationFragment() {
        // Required empty public constructor
    }


    public static IssueCreationFragment newInstance() {
        IssueCreationFragment fragment = new IssueCreationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    5
            );

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_issue_creation, null);

        badnessRatingBar = new RatingBar(getContext());
        badnessLoadingBar = new ProgressBar(getContext());
        badnessLoadingBar.setIndeterminate(true);
        badnessText = new TextView(getContext());

        ButterKnife.bind(this, rootView);

        issueTypeSpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, IssueType.values()));
        issueTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IssueType issueType = (IssueType) issueTypeSpinner.getItemAtPosition(position);
                currentIssue.setType(issueType.toString());
                if (issueType == IssueType.AirPollution) {
                    badnessLayout.removeView(badnessLoadingBar);
                    badnessLayout.removeView(badnessText);
                    badnessLayout.removeView(badnessRatingBar);

                    badnessLayout.addView(badnessLoadingBar);

                    Timer timer = new Timer();
                    timer.schedule(new IntroduceBadnessTextTask(), 1400);
                } else {
                    badnessLayout.removeView(badnessLoadingBar);
                    badnessLayout.removeView(badnessText);
                    badnessLayout.removeView(badnessRatingBar);

                    badnessLayout.addView(badnessRatingBar);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        badnessRatingBar.setNumStars(5);
        badnessRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                currentIssue.setRating((int) rating);
            }
        });


        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Environment issue notification");

        return builder
            .setView(rootView)
            .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            })

            .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    postIssue(
                        currentIssue.getType(),
                        currentIssue.getRating(),
                        currentIssue.getLon(),
                        currentIssue.getLat()
                    );
                }
            })
            .create();
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void takePhoto(View view) {
        verifyStoragePermissions(getActivity());
        // good code
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "sparklyLeisure.jpg");
        imageUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                imageUri);
        startActivityForResult(intent, 2);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    ContentResolver cr = getActivity().getContentResolver();
                    cr.notifyChange(selectedImage, null);

                    Bitmap bitmap;
                    try {
                        Toast.makeText(this.getContext(), selectedImage.toString(),
                                Toast.LENGTH_LONG).show();

                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        photoImageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Toast.makeText(this.getContext(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        e.printStackTrace();
                        Log.e("examplePhoto", e.toString());
                    }
                }
        }
    }

    private void postIssue(String type, int rating, double lon, double lat) {

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", imageFile.getName(), reqFile);
        RequestBody typePart = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody ratingPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(rating));
        RequestBody lonPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lon));
        RequestBody latPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lat));

        ApiUtils.getAPIService().saveIssue(body, typePart, ratingPart, lonPart, latPart).enqueue(new Callback<Issue>() {
            @Override
            public void onResponse(@NonNull Call<Issue> call, @NonNull Response<Issue> response) {
                if (response.isSuccessful()) {
                    Log.i("issue poster", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Issue> call, @NonNull Throwable t) {
                Log.e("issue poster", "Unable to submit post to API.");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIssueCreationListener) {
            mListener = (OnIssueCreationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnIssueCreationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        locationText.setText(String.format("%f, %f", location.getLatitude(), location.getLongitude()));
        currentIssue.setLat(location.getLatitude());
        currentIssue.setLon(location.getLongitude());
        Log.i("locchange", "location changed to " + location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("loc.tracker", String.format("status changed %s %d", provider, status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("loc.tracker", "provider enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("loc.tracker", "provider disabled");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnIssueCreationListener {
        void onIssueFinished(Issue issue);
    }

    private class IntroduceBadnessTextTask extends TimerTask {
        public void run() {
            getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        badnessLayout.removeView(badnessLoadingBar);
                        badnessText.setText("AQI: 41.7843");
                        badnessLayout.addView(badnessText);
                    }
                }
            );
        }
    }
}
