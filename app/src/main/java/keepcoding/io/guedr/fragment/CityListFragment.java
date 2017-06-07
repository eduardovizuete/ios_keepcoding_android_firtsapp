package keepcoding.io.guedr.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import keepcoding.io.guedr.R;
import keepcoding.io.guedr.model.City;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityListFragment extends Fragment {

    private final static String ARG_CITIES = "cities";

    protected LinkedList<City> mCities;
    protected OnCityselectedListener mOnCityselectedListener;

    public static CityListFragment newInstance(LinkedList<City> cities) {
        CityListFragment fragment = new CityListFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_CITIES, cities);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtener la lista de ciudades
        if (getArguments() != null) {
            mCities = (LinkedList<City>) getArguments().getSerializable(ARG_CITIES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_city_list, container, false);


        // accedemos a la lista
        ListView list = (ListView) root.findViewById(R.id.city_list);

        // creamos el adapter con la lista de ciudades
        ArrayAdapter<City> adapter = new ArrayAdapter<City>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                mCities
                );

        // le pasamos el adapter al listview para que rellene la listas
        list.setAdapter(adapter);

        // le asigno un listener a la lista de cuando se ha pulsado una fila
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // avisamos al listener, si lo tenemos, que el usuario ha seleccionado una ciudad
                if (mOnCityselectedListener != null) {
                    City selectedCity = mCities.get(position);
                    // aviso al listener
                    mOnCityselectedListener.onCitySelected(selectedCity, position);
                }
            }
        });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof OnCityselectedListener) {
            mOnCityselectedListener = (OnCityselectedListener) getActivity();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getActivity() instanceof OnCityselectedListener) {
            mOnCityselectedListener = (OnCityselectedListener) getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mOnCityselectedListener = null;
    }

    public interface OnCityselectedListener {
        void onCitySelected(City city, int position);
    }

}
