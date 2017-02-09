package de.templum.routplaner.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.templum.routplaner.R;
import de.templum.routplaner.model.RoutePoint;

public class RouteFormActivity extends AppCompatActivity {

    @Bind(R.id.form_route_list)
    RecyclerView mList;
    @Bind(R.id.form_input)
    EditText mInput;

    private List<RoutePoint> mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_form);
        ButterKnife.bind(this);
        // Maybe restore old state

        mList.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.form_submit)
    public void addLocation(){
        if(mInput.getText() != null)
            Toast.makeText(this, "Addresse: " + mInput.getText().toString() + " hinzugefügt", Toast.LENGTH_LONG).show();
    }
}