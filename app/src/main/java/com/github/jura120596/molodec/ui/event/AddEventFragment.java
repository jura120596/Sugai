package com.github.jura120596.molodec.ui.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.jura120596.molodec.CONST.CONST;
import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.ScannerActivity;
import com.github.jura120596.molodec.adapter.AdapterEvents;
import com.github.jura120596.molodec.data.Event;
import com.github.jura120596.molodec.data.database.DataBASE;
import com.github.jura120596.molodec.retrofit.Retrofit;
import com.github.jura120596.molodec.retrofit.call.ValidateCallback;
import com.github.jura120596.molodec.retrofit.responses.ValidationResponse;
import com.github.jura120596.molodec.scanner.CaptureAct;
import com.github.jura120596.molodec.ui.components.AppEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AddEventFragment extends Fragment {

    private AppEditText title, place, point;
    private TextView date, frag_title;
    private Button add;
    private Button qrBtn, nfcBtn;
    protected AdapterEvents adapter;
    private Calendar dateAndTime = Calendar.getInstance();
    Event event;
    private int pos;

    public void checkEvent(View view) {
        if (event == null || (DataBASE.user.getRole() & CONST.CURATOR_ROLE) == 0 && DataBASE.user.getRole() < CONST.ADMIN_ROLE) {
            view.findViewById(R.id.scannersList).setVisibility(View.INVISIBLE);
            return;
        }
        if (!Event.canAddParticipant(event)) view.findViewById(R.id.scannersList).setVisibility(View.INVISIBLE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date c = sdf.parse(event.getDate());
            dateAndTime.setTime(c);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        title.setText(event.getTitle());
        place.setText(event.getPlace());
        point.setText("" + event.getPoints());
        frag_title.setText(R.string.edit_event_fragment_title);
        add.setText(R.string.save_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        Bundle arguments = getArguments();
        event = arguments != null ? DataBASE.EVENT_JSON_LIST.get(pos = arguments.getInt("pos")) : null;
        date = view.findViewById(R.id.textView9);
        title = view.findViewById(R.id.etv_title_event);
        place = view.findViewById(R.id.etv_title_event2);
        point = view.findViewById(R.id.etv_points_event4);
        qrBtn = view.findViewById(R.id.scanner2);
        nfcBtn = view.findViewById(R.id.scanner);
        frag_title = view.findViewById(R.id.textView7);
        add = view.findViewById(R.id.bt_add);
        checkEvent(view);
        date.setOnClickListener((view1 -> {
            setDate(view1);
        }));
        nfcBtn.setOnClickListener(view12 -> startNfc(pos));
        qrBtn.setOnClickListener((v) -> startQr(pos));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                String date = format.format(dateAndTime.getTime());

                Call<ResponseBody> addEvent;
                if (event == null) {
                    addEvent = Retrofit.getInstance().getApi().addEvent("Bearer " + DataBASE.token, title.getText().toString(), place.getText().toString(), date, point.getText().toString());
                } else {
                    event.setTitle(title.getText().toString());
                    event.setPlace(place.getText().toString());
                    event.setPoints(point.getText().toString().length() > 0 ? Integer.parseInt(point.getText().toString()) : 0);
                    event.setDate(date);
                    addEvent = Retrofit.getInstance().getApi().editEvent("Bearer " + DataBASE.token, event.getId(), event);
                }
                addEvent.enqueue(new ValidateCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getContext(), "Успешно", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                    @Override
                    public void on422(Call<ResponseBody> call, Response<ResponseBody> response, ValidationResponse errors) {
                        String e = errors.getError("title");
                        if (e != null) title.setError(e);
                        e = errors.getError("place");
                        if (e != null) place.setError(e);
                        e = errors.getError("points");
                        if (e != null) point.setError(e);
                        e = errors.getError("date");
                        if (e != null) AddEventFragment.this.date.setError(e);

                    }

                    @Override
                    public Context getContext() {
                        return getActivity();
                    }
                });
            }
        });
        setInitialDateTime();
        return view;
    }

    private void setDate(View v) {
        new DatePickerDialog(getContext(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setTime() {
        new TimePickerDialog(getContext(), t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {

        date.setText(DateUtils.formatDateTime(getContext(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    private TimePickerDialog.OnTimeSetListener t = (view, hourOfDay, minute) -> {
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateAndTime.set(Calendar.MINUTE, minute);
        setInitialDateTime();
    };

    // установка обработчика выбора даты
    private DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            dateAndTime.set(Calendar.YEAR, y);
            dateAndTime.set(Calendar.MONTH, m);
            dateAndTime.set(Calendar.DAY_OF_MONTH, d);
            setTime();
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String card = null;
        if (data != null) {
            card = data.getStringExtra("card_id");
        }
        if (resultCode == 1) {
            if (card == null || card.isEmpty()) error();
            else event.addParticipant(getActivity(), card);
            System.out.println(card);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public ActivityResultLauncher<ScanOptions> activityResultLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) return;
        try {
            System.out.println(result.getContents());
            event.addParticipant(getActivity(), Integer.parseInt(result.getContents()));
        } catch (Throwable t) {
            t.printStackTrace();
            error();
        }
    });


    public void startQr(int position) {
        startQr(position, () -> {});
    }
    public void startQr(int position,Runnable run) {
        event = DataBASE.EVENT_JSON_LIST.get(position);
        ScanOptions options = new ScanOptions();
        options.setPrompt("Отсканируйте QR код профиля");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        activityResultLauncher.launch(options);
        run.run();
    }

    public void startNfc(int position) {
        startNfc(position, () -> {});
    }
    public void startNfc(int position, Runnable run) {
        event = DataBASE.EVENT_JSON_LIST.get(position);
        Intent intent = new Intent(getContext(), ScannerActivity.class);
        intent.putExtra("post_id", DataBASE.EVENT_JSON_LIST.get(position).getId());
        startActivityForResult(intent, 1);
        run.run();
    }


    public void error() {
        Toast.makeText(getActivity(), "Пользователь или метка не найдены", Toast.LENGTH_SHORT).show();
    }

    public void deleteItem(Event event) {
        Call<ResponseBody> call = Retrofit.getApi().deleteEvent("Bearer " + DataBASE.token, event.getId());
        call.enqueue(new ValidateCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataBASE.EVENT_JSON_LIST.remove(event);
                if (adapter != null) adapter.notifyDataSetChanged();
                if (this.getContext() == null) return;
                Toast.makeText(this.getContext(), "Мероприятие удалено", Toast.LENGTH_SHORT).show();
            }

            @Override
            public Context getContext() {
                return AddEventFragment.this.getContext();
            }
        });
    }
}