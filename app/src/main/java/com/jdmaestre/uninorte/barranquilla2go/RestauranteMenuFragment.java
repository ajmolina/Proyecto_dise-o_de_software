package com.jdmaestre.uninorte.barranquilla2go;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jose on 05/11/2014.
 */
public class RestauranteMenuFragment extends Fragment {

    ExpandableListAdapter mlistAdapter;
    ExpandableListView mexpListView;
    List<String> mlistDataHeader;
    HashMap<String, List<String>> mlistDataChild;
    String cat1;
    String cat2;
    String cat3;
    static String nb;
    List<String> cate1;
    List<String> cate2;
    List<String> cate3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_restaurantes, container, false);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        nb=getActivity().getIntent().getExtras().getString("nr");
        // get the listview
        mexpListView = (ExpandableListView) getView().findViewById(R.id.menuExpandableListView);
        // preparing list data
        prepareListData();
        mlistAdapter = new ExpandableListAdapter(getActivity(), mlistDataHeader, mlistDataChild);
        // setting list adapter
        mexpListView.setAdapter(mlistAdapter);

       mexpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
           @Override
           public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
               Toast.makeText(
                       getActivity(),
                       mlistDataHeader.get(i)
                               + " : "
                               + mlistDataChild.get(
                               mlistDataHeader.get(i)).get(
                               i2), Toast.LENGTH_SHORT)
                       .show();
               return false;
           }
       });

    }

    private void prepareListData() {

        cat1="hamburguesa";
        cat2="bebida";
        cat3="helado";



        mlistDataHeader = new ArrayList<String>();
        mlistDataChild = new HashMap<String, List<String>>();

        // Adding child data
        mlistDataHeader.add(cat1);
        mlistDataHeader.add(cat2);
        mlistDataHeader.add(cat3);
        cate1 = new ArrayList<String>();
        cate2 = new ArrayList<String>();
        cate3 = new ArrayList<String>();

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                       ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Platos");
                                query2.findInBackground(new FindCallback<ParseObject>() {
                                   public void done(List<ParseObject> score, ParseException e) {

                                       if (e == null) {
                                           String name = getActivity().getIntent().getExtras().getString("nr");
                                           nb=name;
                                           for ( int i=0;i<score.size();i++) {

                                               ParseObject ps = score.get(i);

                                               String cat = ps.getString("categoria");
                                               String rev = ps.getString("nombreplato");
                                               String nom = ps.getString("nombreres");

                                               if (name.equals(nom)) {

                                               if (cat1.equals(cat)) {

                                                   cate1.add(rev);

                                               }
                                               if (cat2.equals(cat)) {

                                                   cate2.add(rev);

                                               }
                                               if (cat3.equals(cat)) {

                                                   cate3.add(rev);

                                               }
                                           }


                                           }

                                       }

                                   }

                               });
















        mlistDataChild.put(mlistDataHeader.get(0), cate1);
        mlistDataChild.put(mlistDataHeader.get(1), cate2);
        mlistDataChild.put(mlistDataHeader.get(2), cate3);
    }

}

 class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap< String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.menuListItem);
        final TextView txtListChildReview = (TextView) convertView
                .findViewById(R.id.menuListItemReview);
        final ImageView imgView = (ImageView) convertView.findViewById(R.id.menuListImage);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Platos");


        query.findInBackground(new FindCallback<ParseObject>() {
                                   public void done(List<ParseObject> score, ParseException e) {
                                       String imagen="";
                                       String cont="";
                                       if (e == null) {
                                  String n =RestauranteMenuFragment.nb;
                                           for ( int i=0;i<score.size();i++) {

                                               ParseObject ps = score.get(i);

                                               String ns = ps.getString("nombreres");
                                               String np = ps.getString("nombreplato");
                                               String rev = ps.getString("review");
                                               String imgs = ps.getString("imagen");

                                                if(n.equals(ns)) {


                                                    if (childText.equals(np)) {


                                                      cont=rev;
                                                        imagen=imgs;
                                                    }
                                                }
                                           }
                                           txtListChildReview.setText(cont);
                                           Picasso.with(_context).load(imagen).resize(60, 60).into(imgView);

                                       }
                                   }
                               });
        query.clearCachedResult();




        imgView.setBackgroundColor(Color.BLACK);
        txtListChild.setText(childText);



        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.menuListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
