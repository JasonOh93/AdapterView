package com.jasonoh.adapterviewex_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView listView; //리스트뷰 참조변수
    TextView tv_empty; //리스트뷰에 멤버가 없을때 나타나는 TextView

    EditText et_name; //alert Dialog 안의 EditText 참조변수
    RadioGroup rg_gender; //라디오 버튼에 반응하는 참조변수
    Button btn_addMember; // 추가 버튼 클릭시 반응하는 참조변수

    Spinner spinner_alert; //alert Dialog 안의 스피너 참조변수


    String spinner_string = ""; //스피너에서 선택된 문자열을 리스트뷰에 출력하기 위해서 가져오는 참조변수

    int nationImag_int; // 나라 이미지를 출력하기 위한 int형 변수

    //EditText et_search; // search를 누를때 나타나게 하려는 참조변수
    SearchView searchView; // search를 누를때 나타나게 하려는 참조변수

    ArrayList<Member> members = new ArrayList<>(); // 멤버를 참조하는 어레이 리스트 변수

    MyAdapter adapter; // 리스트 뷰에 해당하는 어뎁터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);

        adapter = new MyAdapter( members, getLayoutInflater() );

        //리스트뷰에 보여질 객체 생성
        listView.setAdapter( adapter );

        //리스트뷰의 아이템이 비어있을때 보여지는 뷰
        tv_empty = findViewById(R.id.tv_empty);
        listView.setEmptyView( tv_empty );

        //리스트뷰에 롱클릭 리스너 달기
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                PopupMenu popup_listView =new PopupMenu(MainActivity.this, view);
                getMenuInflater().inflate(R.menu.popup_listview, popup_listView.getMenu());

                popup_listView.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch ( item.getItemId() ) {
                            case R.id.popup_modify :

                                alert_Dialog( item.getItemId(), position );
                                adapter.notifyDataSetChanged();
                                listView.setSelection( position );
                                //Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();

                                break;

                            case R.id.popup_delete :

                                members.remove( position );
                                adapter.notifyDataSetChanged();
                                //Toast.makeText(MainActivity.this, "delete", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.popup_info :
                                Toast.makeText(MainActivity.this,  "info", Toast.LENGTH_SHORT).show();
                                break;
                        }//switch

                        return false;
                    }//onMenuItemClick method
                });//popup_listView.setOnMenuItemClickListener

                popup_listView.show();

                return true;//true로 해야 온 클릭은 안되고 롱클릭만 가능
            }
        });//listView.setOnItemLongClickListener 익명클래스

    }//onCreate method

    //옵션버튼 만들기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.option_menu, menu);

        //Search View
        MenuItem item_search =menu.findItem( R.id.search );
        if( item_search.getActionView() instanceof SearchView ) searchView = (SearchView) item_search.getActionView();

        searchView.setQueryHint( "Hint" );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //listView에 같은 글씨 찾기
                for(int i = 0; i < members.size(); i++) {
                    if( query.equals( members.get(i).name ) ) listView.setSelection( i );
                }
                Toast.makeText(MainActivity.this, query + " 을 검색했습니다.", Toast.LENGTH_SHORT).show();
                searchView.setQuery( "", false );
                return false;
            }//onQueryTextSubmit method

            @Override
            public boolean onQueryTextChange(String newText) { return false; }
        });//setOnQueryTextListener

        return super.onCreateOptionsMenu(menu);
    }//onCreateOptionsMenu method

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ) {
            case R.id.plus :

                alert_Dialog( item.getItemId() );

                break;
        }//swtch case

        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected method

    //alert_Dialog 여러번 사용하기 위해 메소드 설정
    void alert_Dialog (final int getSelectedId ){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View v_alert = getLayoutInflater().inflate(R.layout.alert_dialog, null);

        et_name =  v_alert.findViewById(R.id.et_name);
        rg_gender = v_alert.findViewById(R.id.rg_gender);
        btn_addMember = v_alert.findViewById(R.id.btn_addMember);
        Button btn_cancelMember =v_alert.findViewById(R.id.btn_cancel);



        spinner_alert = v_alert.findViewById(R.id.spinner_nation_alert);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(this, R.array.spinner_nation, R.layout.spinner_selected);
        spinner_adapter.setDropDownViewResource( R.layout.spinner_dropdown );
        spinner_alert.setAdapter( spinner_adapter );

        spinner_alert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] spinner_array = getResources().getStringArray(R.array.spinner_nation);

                nationImag_int = position;

                spinner_string = spinner_array[position]; //해당하는 글씨를 가져오기
            }//onItemSelected 메소드

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        }); // spinner_alert.setOnItemSelectedListener 익명클래스

        builder.setView( v_alert );

        final AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();

        btn_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name_addMember = et_name.getText().toString(); // 문자받기
                String nation_addMember = spinner_string;

                //TextView Spinner 확인용
//                TextView tv_aaa = v_alert.findViewById( R.id.tv_nation_alert );
//                String nation_addMember = tv_aaa.toString();

                //날짜와 시간 가져오기
                long now = System.currentTimeMillis();
                Date toDate = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String get_today_time = simpleDateFormat.format(toDate);

                //여자인지 남자인지 확인
                RadioButton rb = v_alert.findViewById( rg_gender.getCheckedRadioButtonId() );
                String gender_addMember = rb.getText().toString();

                if( getSelectedId == R.id.plus ) {
                    if( gender_addMember.equals( "MALE" ) ) members.add(0, new Member( name_addMember, nation_addMember, get_today_time, (R.drawable.flag_australia + nationImag_int), R.drawable.einstein ) );
                    else if( gender_addMember.equals( "FEMALE" ) ) members.add(0, new Member( name_addMember, nation_addMember, get_today_time, (R.drawable.flag_australia + nationImag_int), R.drawable.mileva_maric ) );
                } // plus를 눌렀을때의 반응하도록 하는 if문

                adapter = new MyAdapter( members, MainActivity.this.getLayoutInflater() );

                //AlertDialog가 꺼지도록 하는 메소드 호출
                alertDialog.dismiss();

                //어뎁터의 정보가 변경 된 것을 알리고,
                adapter.notifyDataSetChanged();

                //리스트뷰에 보여질 객체 생성
                listView.setTranscriptMode( listView.TRANSCRIPT_MODE_DISABLED ); // 포커스가 추가될때 포커스가 자동 스크롤 되는 것을 방지
                listView.setAdapter( adapter );

            }
        });//addMember OnclickListener method

        btn_cancelMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //AlertDialog가 꺼지도록 하는 메소드 호출
                alertDialog.dismiss();

            }//onclick method
        });//cancelMember OnclickListener method

    }//alert_Dialog method

    //오버로딩 메소드 -> 수정을 눌렀을때 반응하도록 하는 메소드
    void alert_Dialog (final int getSelectedId, final int getListViewItem ){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View v_alert = getLayoutInflater().inflate(R.layout.alert_dialog, null);

        et_name =  v_alert.findViewById(R.id.et_name);
        //et_name.setText( members.get( getListViewItem ).name ); // 값얻어오기 왜 안될까...
        rg_gender = v_alert.findViewById(R.id.rg_gender);
        btn_addMember = v_alert.findViewById(R.id.btn_addMember);
        Button btn_cancelMember =v_alert.findViewById(R.id.btn_cancel);

        spinner_alert = v_alert.findViewById(R.id.spinner_nation_alert);
        ArrayAdapter spinner_adaper = ArrayAdapter.createFromResource(this, R.array.spinner_nation, R.layout.spinner_selected);
        spinner_adaper.setDropDownViewResource( R.layout.spinner_dropdown );
        spinner_alert.setAdapter( spinner_adaper );

        //

        spinner_alert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] spinner_array = getResources().getStringArray(R.array.spinner_nation);

                nationImag_int = position;

                spinner_string = spinner_array[position]; //해당하는 글씨를 가져오기
            }//onItemSelected 메소드

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        }); // spinner_alert.setOnItemSelectedListener 익명클래스

        builder.setView( v_alert );

        final AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();

        btn_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name_addMember = et_name.getText().toString(); // 문자받기
                String nation_addMember = spinner_string;

                //날짜와 시간 가져오기
                long now = System.currentTimeMillis();
                Date toDate = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String get_today_time = simpleDateFormat.format(toDate);

                //여자인지 남자인지 확인
                RadioButton rb = v_alert.findViewById( rg_gender.getCheckedRadioButtonId() );
                String gender_addMember = rb.getText().toString();

                if( getSelectedId == R.id.popup_modify ) {
                    if( gender_addMember.equals( "MALE" ) ) members.set(getListViewItem , new Member( name_addMember, nation_addMember, get_today_time, (R.drawable.flag_australia + nationImag_int), R.drawable.einstein ) );
                    else if( gender_addMember.equals( "FEMALE" ) ) members.set(getListViewItem , new Member( name_addMember, nation_addMember, get_today_time, (R.drawable.flag_australia + nationImag_int), R.drawable.mileva_maric ) );
                }// modify를 눌렀을 때 반응하도록 하는 if문

                adapter = new MyAdapter( members, MainActivity.this.getLayoutInflater() );

                //AlertDialog가 꺼지도록 하는 메소드 호출
                alertDialog.dismiss();

                //어뎁터의 정보가 변경 된 것을 알리고,
                adapter.notifyDataSetChanged();

                //리스트뷰에 보여질 객체 생성
//                listView.setTranscriptMode( listView.TRANSCRIPT_MODE_NORMAL ); // 포커스가 추가될때 포커스가 자동 스크롤 되는 것을 방지
                listView.setAdapter( adapter );
                listView.setSelection(getListViewItem); // 리스트뷰 수정한 아이템으로 이동

            }
        });//addMember OnclickListener method

        btn_cancelMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //AlertDialog가 꺼지도록 하는 메소드 호출
                alertDialog.dismiss();

            }//onclick method
        });//cancelMember OnclickListener method

    }//alert_Dialog overloading method

}//MainActivity class



//todo : 앞으로 할 것
//          2020.05.25
//      집에서 한것이 에뮬레이터에서는 스피너 글씨가 다이얼로그에서 안나타나지만 폰에서는 나타나는 문제
//      팝업메뉴 나타나는 위치와 크기 설정에 대한 오류
//      롱클릭시 modify를 누를 때 해당하는 값을 가져와서 다이얼 로그에 나타나게 하는 방법
//      롱클릭시 info에 해당하는 정보 값 넣기
//      search버튼 누르면 해당하는 리스트뷰의 값 찾아서 그자리로 포지션 이동하기
//          -
//      2020.05.26
//      다 했고 이제 info 맞추고
//      modify 누를 때 그 안의 값을 찾아와서 넣기


//todo : 2020.05.22
//      하단에 당일의 날짜 나오는 것 해야함(현재 스트링으로 되어있음(tv_todayDate) : 했음
//      다이얼 로그의 NATION의 해당하는 값을 스피너를 사용해서 만들어야함 : 했음
//      더하기 버튼을 누르면 해당하는 값이 밖에 나오도록 하기 : 했음
//      성별과 국가 이미지는 맞는 것에 해당하는 사진들 맞추어 나오도록 만들기 : 했음
//      취소를 누르면 다이얼로그 나가도록 하기 : 했음
//      메뉴 상단의 찾기 버튼 누르면 해당하는 것 찾아오기
//        리스트뷰 길게 누르면 삭제, 다시 정보화면인 다이얼로그로 띄워서 수정 가능하도록, info를 누르면 정보 화면 띄우기