package ru.sugai.village.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import ru.sugai.village.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class ScannerFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        ScanOptions options = new ScanOptions();
        options.setPrompt("");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        activityResultLauncher.launch(options);
        return view;
    }
    ActivityResultLauncher<ScanOptions> activityResultLauncher = registerForActivityResult(new ScanContract(), result -> {
            Toast.makeText(getContext(), "Успешно", Toast.LENGTH_SHORT).show();
    });
}