package ru.sugai.village.ui.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ru.sugai.village.CONST.CONST;
import ru.sugai.village.R;
import ru.sugai.village.data.Cords;
import ru.sugai.village.data.MapObject;
import ru.sugai.village.data.ServerListResponse;
import ru.sugai.village.retrofit.Retrofit;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.LinearRing;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polygon;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolygonMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.ui_view.ViewProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapFragment extends Fragment {
    private MapView mapview;
    public static final double lat = 55.535667;
    public static final double lon = 47.504093;
    public static final double OBJECT_SIZE =0.001;

    private MapObjectCollection mapObjects;


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        mapview = view.findViewById(R.id.mapview);
        mapview.getMap().move(
                new CameraPosition(new Point(55.535667, 47.504093), 13.5f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        Call<ServerListResponse<MapObject>> getObject = Retrofit.getInstance().getApi().getMapObject();
        getObject.enqueue(new Callback<ServerListResponse<MapObject>>() {
            @Override
            public void onResponse(Call<ServerListResponse<MapObject>> call, Response<ServerListResponse<MapObject>> response) {
                if (response.code()==200){
                    List<MapObject> data = response.body().getData();
                    Log.d(CONST.SERVER_LOG, data.toString());
                    for (MapObject mo: data) {
                        if (mo.getType().equals("marker")) addPlacemark(mo);
                        else addPolygon(mo);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerListResponse<MapObject>> call, Throwable t) {
                t.printStackTrace();
            }
        });


        return view;
    }


    private void addPlacemark(MapObject mo){

        View view = new View(getActivity());
        view.setBackground(getActivity().getDrawable(R.drawable.ic_baseline_location_on_24));
        view.setMinimumWidth(100);
        view.setMinimumHeight(100);
        ((VectorDrawable) view.getBackground()).setTint(mo.getIntColor(false));
        mapview.getMap().getMapObjects()
                .addPlacemark(new Point(mo.getLat(),mo.getLng()), new ViewProvider(view));
    }


    private void addPolygon(MapObject mapObject){
        mapObjects = mapview.getMap().getMapObjects().addCollection();
        ArrayList<ArrayList<Cords>> cords=mapObject.getCoords();
        ArrayList<Point> points=new ArrayList<>();
        for (int i = 0; i < cords.size() ;i++) {
            ArrayList<Cords> item = cords.get(i);
            for (int j = 0; j < item.size(); j++) {
                points.add(new Point(item.get(j).getLat(), item.get(j).getLng()));
            }
        }

        PolygonMapObject polygon = mapObjects.addPolygon(new Polygon(new LinearRing(points), new ArrayList<LinearRing>()));
        mapObject.setPolygonMapObject(polygon);
        polygon.setUserData(mapObject);
        polygon.setFillColor(mapObject.getIntColor());
        polygon.setStrokeColor(mapObject.getIntColor());
        polygon.setStrokeWidth(0.5F);
        polygon.addTapListener((mapObject1, point) -> {
            LayoutInflater inflater = ((AppCompatActivity) getContext()).getLayoutInflater();
            LinearLayout dialoglayout = (LinearLayout) inflater.inflate(R.layout.dialog_comment, null);
            MapObject mo = (MapObject) mapObject1.getUserData();
            Toast toast = Toast.makeText(getActivity().getBaseContext(), mo.getName(), Toast.LENGTH_SHORT);
            if (mo.getPoints() == 0) {
                toast.show();
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(mo.getName());
            builder.setView(dialoglayout);
            TextView tv =  new TextView(getContext());
            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setText(mo.getPoints() > 0 ? "Тут вы можете потратить заработанные баллы.\n Для посещения нужно " + mo.getPoints() + " баллов" : "");
            dialoglayout.addView(tv);
            builder.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
            return true;
        });
    }




    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapview.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        MapKitFactory.getInstance().onStop();
        mapview.onStop();
    }
}