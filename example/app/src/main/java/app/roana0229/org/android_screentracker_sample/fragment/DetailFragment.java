package app.roana0229.org.android_screentracker_sample.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import app.roana0229.org.android_screentracker_sample.Navigator;
import app.roana0229.org.android_screentracker_sample.R;
import app.roana0229.org.android_screentracker_sample.model.DummyContent;
import app.roana0229.org.screentracker.TrackingMarker;

public class DetailFragment extends Fragment implements TrackingMarker {

    private final static String BUNDLE_KEY_ITEM = "bundle_key_item";

    private DummyContent.DummyItem mItem;

    public static Fragment newInstance(DummyContent.DummyItem item) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY_ITEM, item);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    public DetailFragment() {
    }

    @Override
    public String getScreenName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public HashMap<String, Object> getScreenParameter() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("item_id", mItem.id);
        return hashMap;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = (DummyContent.DummyItem) getArguments().getSerializable(BUNDLE_KEY_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView textView = (TextView) view.findViewById(R.id.description);
        textView.setText(mItem.content);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog();
            }
        });
        return view;
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Buy this?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Navigator(getActivity()).showComplete();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

}
