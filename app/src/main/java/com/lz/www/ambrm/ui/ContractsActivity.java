package com.lz.www.ambrm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lz.www.ambrm.ContentProvider.ContactsResolver;
import com.lz.www.ambrm.R;
import com.lz.www.ambrm.model.Contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-06-29.
 */
public class ContractsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userlist);

       // getActionBar().show();

        ListView lvContacts=(ListView)findViewById(R.id.lvUserList);

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        List<Contacts> contactsArrayList = new ContactsResolver().getContacts(this);
        for (int i=0;i<contactsArrayList.size();i++){
            Map<String,String> items=new HashMap<String, String>();
            items.put("name",contactsArrayList.get(i).getName());
            items.put("number",contactsArrayList.get(i).getNumber());
            list.add(items);
        }

        SimpleAdapter adapter=new SimpleAdapter(this,list,R.layout.news_list_item,new String[]{"name","number"},new int[]{R.id.tvTitle,R.id.tvContent});

        lvContacts.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        return true;
    }
}
