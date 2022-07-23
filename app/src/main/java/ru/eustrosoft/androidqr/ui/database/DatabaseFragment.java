package ru.eustrosoft.androidqr.ui.database;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.sql.ResultSet;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.database.apipostgres.DBConnector;

import static ru.eustrosoft.androidqr.database.apipostgres.DBConnector.KEY_PASSWORD;
import static ru.eustrosoft.androidqr.database.apipostgres.DBConnector.KEY_USER;

public class DatabaseFragment extends Fragment {
    private EditText databaseUrl;
    private EditText databaseUser;
    private EditText databasePassword;
    //private EditText databaseQuery;
    private Button executeQueryButton;
    private TextView queryAnswer;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_database, container, false);
        initElements(root);
        executeQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBConnector connector = getDBConnectorInstance(
                        databaseUrl.getText().toString(),
                        databaseUser.getText().toString(),
                        databasePassword.getText().toString()
                );
                try {
                    connector.connectToDatabase();
                    queryAnswer.setText(String.format("%s", "Connection successfully established!"));
                } catch (Exception ex) {
                    queryAnswer.setText(String.format(
                            "%s: %s\n%s\n",
                            ex.getMessage(),
                            ex.getCause(),
                            String.format(
                                    "URL: %s\nUSER: %s\nPASSWORD: %s",
                                    connector.getUrl(),
                                    connector.getDbProps().getProperty(KEY_USER),
                                    connector.getDbProps().getProperty(KEY_PASSWORD)
                            )
                    ));
                } finally {
                    connector.closeConnection();
                }
            }
        });
        return root;
    }

    private DBConnector getDBConnectorInstance(String database, String user, String password) {
        return new DBConnector(database, user, password);
    }

    private ResultSet executeQuery(String database, String user, String password, String query) throws Exception {
        DBConnector connector = new DBConnector(database, user, password);
        return connector.getData(query);
    }

    private void initElements(View root) {
        databaseUrl = root.findViewById(R.id.database_url_edit_field);
        databaseUser = root.findViewById(R.id.database_user_edit_field);
        databasePassword = root.findViewById(R.id.database_password_edit_field);
        //databaseQuery = root.findViewById(R.id.database_query_edit_field);
        executeQueryButton = root.findViewById(R.id.execute_query_button);
        queryAnswer = root.findViewById(R.id.query_text_view);
    }
}
