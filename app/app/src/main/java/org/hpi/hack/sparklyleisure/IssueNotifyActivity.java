package org.hpi.hack.sparklyleisure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import org.hpi.hack.sparklyleisure.model.Issue;
import org.hpi.hack.sparklyleisure.remote.APIService;
import org.hpi.hack.sparklyleisure.remote.ApiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IssueNotifyActivity extends AppCompatActivity implements IssueCreationFragment.OnIssueCreationListener {
    @BindView(R.id.badButton)
    Button badButton;

    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_notify);
        ButterKnife.bind(this);

        badButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBadClicked();
            }
        });
        apiService = ApiUtils.getAPIService();
    }

    public void onBadClicked() {
        IssueCreationFragment dialog = new IssueCreationFragment();
        dialog.show(getSupportFragmentManager(), "issueCreationFragment");
    }


    @Override
    public void onIssueFinished(Issue issue) {
        // never gets called
        Log.wtf(IssueNotifyActivity.class.getSimpleName(), "onIssueFinished was calledß?? plz implement");
    }
}
