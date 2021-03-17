package com.example.event;

import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.DingTu.Base.ICallback;
import com.DingTu.Base.PCallback;
import com.DingTu.Base.PubVar;
import com.DingTu.Base.Tools;
import com.DingTu.Enum.lkGpsFixMode;
import com.DingTu.GPS.LocationEx;
import com.example.event.Video.VideoRecordActivity;
import com.example.event.View.RecyclerViewSpacesItemDecoration;
import com.example.event.db.xEntity.FenAllTableEntity;
import com.example.event.db.xEntity.PatrolEventEntity;
import com.example.event.db.xEntity.PhotoEntity;
import com.example.event.db.xEntity.RoundExamineEntity;
import com.example.event.http.RetrofitHttp;
import com.example.event.manager.PatrolManager;
import com.example.event.manager.PhotoManager;
import com.example.event.model.FenAllTable;
import com.example.event.model.FenTableBean;
import com.example.event.model.ImgTime;
import com.example.event.utils.PhotoCamera;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventActivityFragment extends Fragment {

    public static ICallback photoCallBack = null;
    final PatrolEventEntity roundEventEntity = new PatrolEventEntity();
    //    @BindView(R.id.gvList)
//    GridView gridView;
//    @BindView((R.id.sc_view))
//    ScrollView scrollview;
    //    @BindView(R.id.etEventPOI)
//    EditText eventPOI;
//    @BindView(R.id.etEventTime)
//    EditText eventTime;
    //    @BindView(R.id.event_name)
//    FloatLabelView eventName;
//    @BindView(R.id.etEventDescription)
//    EditText eventDescription;
//    @BindView(R.id.spRoundEventType)
//    Spinner spRoundEventType;
    FragmentActivity mOwner;
    private View mView;
    //    private Date eventTimeDate;
    private Unbinder unbinder;
    private ArrayList<String> mPhotoNameList = new ArrayList<String>();
    private ArrayList<ImgTime> mPhotoTimeList = new ArrayList<>();
    private String photoPath = PubVar.m_SysAbsolutePath + "/Photo";
    private String smallPhotoPath = photoPath + "/samllPhoto";
    private String tempPhotoName = photoPath + "/TempPhoto.jpg";
    private LocationEx mLocation = null;
    private Dialog mSavingingDlg; // 显示正在保存的Dialog
    //    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private boolean mIsSaving = false;
    TextView mId, mTvSheng, mTvXian, mTvDiaoCha, mTvTianBiaoRen, mTvDate;
    public static final String TAG = "diaochasetting";
    public static final String ShengDB = "shengStr";
    public static final String XianDB = "xianStr";
    public static final String DiaoChaZheDB = "diaochazheStr";
    public static final String FillTableDB = "filltableStr";
    private Spinner mPoXiangSp, mPoWeiSp, mPoDuSp, mSpTuType, mZhongCnName, mZhongLaName;
    private Button mCaculate;
    private EditText mHight, mXiong;
    private EditText mTvNumber, mAddress;
    private TextView mLat, mLong, mHaiHight, mXuji, mTakePerson;
    private EditText mTakeDate;
    private EditText mGuanfu, mZhiHigh;
    private EditText mDanzhuDescibe, mTakeBeizhu;
    private String mVideoPath = PubVar.m_SysAbsolutePath + "/Video";
    public static PCallback videoCallBack;
    private List<String> mVideoNameList = new ArrayList<String>();
    private List<String> mThumNameList = new ArrayList<String>();
    private List<HashMap<String, Object>> data_list;
    private GridView mVideoGridView;
    private Spinner mShuZhongType;
    private String[] myZhongType;
    private ArrayAdapter<String> zhongTypeAdapter;
    private String[] mLaType;
    private ArrayAdapter<String> mlaTypeAdapter;
    private TextView mShuCnName, mKeCnName, mShuLaName, mKeLaName;
    private ArrayAdapter<String> shuCnAdapter, keCnAdapter;
    private ArrayAdapter<String> shuLaAdapter, keLaAdapter;
    private List<View> mViews = new ArrayList<View>();
    private PagerAdapter mAdpter;
    private com.example.event.controls.MyViewpager mViewPager;
    private View SingleTreeView, LinFenView;
    private GridView gridView;
    private TextView mFenId, mFenTvSheng, mFenTvXian, mFenAddress;
    private Spinner mFenPoXiangSp, mFenPoWeiSp, mFenPoDuSp, mFenTreeType, mFenTuType, mSpShuType, mQiyuanType;
    private EditText mFenMuYan, mDiarea, mShuAge, mGuanFu, mFenZhiHigh, mFenXiongjin, mFenTreehigh, mYubidu, mMidu, mLinArea, mAvXuji, mZhongYuan, mShuZu, mHealthStatus, mSeed;
    private CardView mCardAddList;
    private TextView mZhuHao;
    private EditText mXiongJin, mTreeHigh, mGuanFus, mShuXing, mHuaTime, mMianji, mStateLevel;
    private RecyclerView mFengExamine;
    private ListExamineAdapter mAdapter;
    private ArrayList<FenTableBean> mTable;
    private int i = 0;
    private TextView mFenLon, mFenLat, mFenHigh, mFenzhe, mFenFillperson, mFenDiaoDate;
    private double mSumGuanfu, mSumXiongJin, mSumTreeHigh, mSumXuji;

    public EventActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_event, container, false);
        initView(mView);
        unbinder = ButterKnife.bind(this, SingleTreeView);
        mOwner = this.getActivity();
        initSavingDlg();
//        refreshLocation();
        return mView;
    }

    private void VideoCallbackResult(String result, String thumPath, String videoPath) {
        if (result.equals("ok")) {
            mVideoNameList.add(0, videoPath);
            mThumNameList.add(0, thumPath);
        }
        showVideo();
    }

    private void showVideo() {
        String[] from = {"image", "text", "check"};
        int[] to = {R.id.iv_video_image, R.id.tv_video_info, R.id.cb_video_select};
        data_list = new ArrayList<HashMap<String, Object>>();
        if (mVideoNameList != null && mVideoNameList.size() > 0) {
            for (int i = 0; i < mVideoNameList.size(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("image", mThumNameList.get(i));
                map.put("text", mVideoNameList.get(i));
                map.put("check", false);
                data_list.add(map);
            }
            if (getActivity() != null) {
                SimpleAdapter sim_adapter = new SimpleAdapter(getActivity(), data_list, R.layout.video_list_item, from, to);
                //配置适配器
                mVideoGridView.setAdapter(sim_adapter);
                mVideoGridView.setOnItemClickListener(new ItemClickListener());
                mVideoGridView.invalidate();
            }
        }

    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                View arg1,//The view within the AdapterView that was clicked
                                int arg2,//The position of the view in the adapter
                                long arg3//The row id of the item that was clicked
        ) {
            //在本例中arg2=arg3
            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
            //显示所选Item的ItemText
            String truePath = item.get("text").toString();
            File file = new File(truePath);
            Intent it = new Intent(Intent.ACTION_VIEW);
            Uri mUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mUri = FileProvider.getUriForFile(PubVar.m_DoEvent.m_Context, PubVar.m_DoEvent.m_Context.getPackageName() + ".fileprovider", file);
                it.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                mUri = Uri.parse("file://" + file.getPath());
            }
            it.setDataAndType(mUri, "video/mp4");
            mOwner.startActivity(it);
        }

    }


    private void initView(View mView) {
        LayoutInflater mInflater = LayoutInflater.from(PubVar.m_DoEvent.m_Context);
        SingleTreeView = mInflater.inflate(R.layout.single_tree_static, null);
        LinFenView = mInflater.inflate(R.layout.lin_fen_static, null);
        mViewPager = (com.example.event.controls.MyViewpager) mView.findViewById(R.id.my_view_pager);
        RadioGroup mRadioGroup = (RadioGroup) mView.findViewById(R.id.rg);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb0:

                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb1:
                        mViewPager.setCurrentItem(1);
                        break;


                }
            }
        });
        mViews.add(SingleTreeView);
        mViews.add(LinFenView);


        mAdpter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }
        };
        mViewPager.setAdapter(mAdpter);
        mViewPager.setCurrentItem(0);
        initOne(SingleTreeView);
        initTwo(LinFenView);
        photoCallBack = new ICallback() {
            @Override
            public void OnClick(String Str, Object ExtraStr) {
                photoCallbackResult(Str, ExtraStr);
            }
        };
        videoCallBack = new PCallback() {

            @Override
            public void OnClick(String Str, String ThumPath, String VideoPath) {
                VideoCallbackResult(Str, ThumPath, VideoPath);
            }
        };

    }

    private void initTwo(View linFenView) {


        mTable = new ArrayList<FenTableBean>();
        mFenLon = (TextView) linFenView.findViewById(R.id.tvLon);
        mFenLat = (TextView) linFenView.findViewById(R.id.tvLat);
        mFenHigh = (TextView) linFenView.findViewById(R.id.tvHigh);
        mFenId = (TextView) linFenView.findViewById(R.id.tv_Id);
        mFenId.setText(UUID.randomUUID().toString().replaceAll("-", ""));
        mFenTvSheng = (TextView) linFenView.findViewById(R.id.tv_sheng);
        mFenTvXian = (TextView) linFenView.findViewById(R.id.tv_xian);
        mFenzhe = (TextView) linFenView.findViewById(R.id.tv_diaocha_zhe);
        mFenFillperson = (TextView) linFenView.findViewById(R.id.tv_tianbiao_ren);
        mFenDiaoDate = (TextView) linFenView.findViewById(R.id.tv_date);
        mFenAddress = (EditText) linFenView.findViewById(R.id.et_address);
        SharedPreferences preferences = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        mFenTvSheng.setText(preferences.getString(ShengDB, ""));
        mFenTvXian.setText(preferences.getString(XianDB, ""));
        mFenzhe.setText(preferences.getString(DiaoChaZheDB, ""));
        mFenFillperson.setText(preferences.getString(FillTableDB, ""));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        mFenDiaoDate.setText(simpleDateFormat.format(date));

        mFenPoXiangSp = (Spinner) linFenView.findViewById(R.id.sp_po_xiang);
        String[] poXiangType = "东,西,南,北,东南,东北,西南,西北,无".split(",");
        ArrayAdapter<String> poXiangTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                poXiangType);
        poXiangTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFenPoXiangSp.setAdapter(poXiangTypeAdapter);

        mFenPoWeiSp = (Spinner) linFenView.findViewById(R.id.sp_po_wei);
        String[] poWeiType = "山脊,上坡,中坡,下坡,山谷,平地".split(",");
        ArrayAdapter<String> poWeiTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                poWeiType);
        poWeiTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFenPoWeiSp.setAdapter(poWeiTypeAdapter);

        mFenPoDuSp = (Spinner) linFenView.findViewById(R.id.sp_po_du);
        String[] poDuType = "平(0°-5°),缓(6°-15°）,斜(16°-25°）,陡(26°-35°）,急(36°-45°）,险(>46°)".split(",");
        ArrayAdapter<String> poDuTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                poDuType);
        poDuTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFenPoDuSp.setAdapter(poDuTypeAdapter);

        mFenTuType = (Spinner) linFenView.findViewById(R.id.sp_tu_type);
        String[] tuType = "黄棕壤,棕壤,暗棕壤,灰黑土,漂灰土,燥红土,褐土,塿土,灰褐土,黑垆土,黑钙土,棕钙土,灰钙土,灰漠土,灰棕漠土,棕漠土,沼泽土,水稻土,黑土,白浆土,潮土,砂姜黑土,灌淤土,绿洲土,草甸土,盐土,碱土,紫色土,石灰土,磷质石灰土,黄绵土,风沙土,火山灰土,山地草甸土,亚高山草甸土,高山草甸土,亚高山草原土,高山草原土,亚高山漠土,高山漠土,高山寒冰土,洪积冲积土,石骨土,耕作土".split(",");
        ArrayAdapter<String> tuTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                tuType);
        tuTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFenTuType.setAdapter(tuTypeAdapter);

        mFenTreeType = (Spinner) linFenView.findViewById(R.id.sp_tree_type);
        String[] treeType = "乔木,灌木,草".split(",");
        ArrayAdapter<String> treeTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                treeType);
        treeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFenTreeType.setAdapter(treeTypeAdapter);

        mSpShuType = (Spinner) linFenView.findViewById(R.id.sp_shu_type);
        String[] shuType = "七叶树,红豆杉".split(",");
        ArrayAdapter<String> shuAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                shuType);
        shuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpShuType.setAdapter(shuAdapter);

        mFenMuYan = (EditText) linFenView.findViewById(R.id.et_mu_yan);
        mDiarea = (EditText) linFenView.findViewById(R.id.et_di_area);
        mShuAge = (EditText) linFenView.findViewById(R.id.et_shu_age);
        mGuanFu = (EditText) linFenView.findViewById(R.id.et_guan_fu);
        mFenZhiHigh = (EditText) linFenView.findViewById(R.id.et_zhi_hight);
        mFenXiongjin = (EditText) linFenView.findViewById(R.id.et_average_xiongjin);
        mFenTreehigh = (EditText) linFenView.findViewById(R.id.et_average_treehight);
        mYubidu = (EditText) linFenView.findViewById(R.id.et_yubi_du);
        mMidu = (EditText) linFenView.findViewById(R.id.et_midu);
        mLinArea = (EditText) linFenView.findViewById(R.id.et_lin_area);
        mAvXuji = (EditText) linFenView.findViewById(R.id.et_average_xuji);

        mQiyuanType = (Spinner) linFenView.findViewById(R.id.sp_qiyuan_type);
        String[] qiYuanType = "天然林,人工林".split(",");
        ArrayAdapter<String> qiYuanAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                qiYuanType);
        shuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mQiyuanType.setAdapter(qiYuanAdapter);


        mZhongYuan = (EditText) linFenView.findViewById(R.id.et_zhong_yuan);
        mShuZu = (EditText) linFenView.findViewById(R.id.et_shu_zu);
        mHealthStatus = (EditText) linFenView.findViewById(R.id.et_health_status);
        mSeed = (EditText) linFenView.findViewById(R.id.et_seed);
        mFengExamine = (RecyclerView) linFenView.findViewById(R.id.list_fen_examine);
        mFengExamine.addItemDecoration(new RecyclerViewSpacesItemDecoration(10));
        mFengExamine.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new ListExamineAdapter();
        mFengExamine.setAdapter(mAdapter);
        mCardAddList = (CardView) linFenView.findViewById(R.id.card_add_list);
        mCardAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        ((Button) linFenView.findViewById(R.id.text_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMess();

            }
        });

        ((Button) linFenView.findViewById(R.id.text_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        ((TextView) linFenView.findViewById(R.id.text_Refresh)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshTwoLocation();
            }
        });

    }

    private void SaveMess() {
        boolean okay = true;

        String lat = mFenLat.getText().toString().trim();
        String lon = mFenLon.getText().toString().trim();
        String high = mFenHigh.getText().toString().trim();
        String Id = mFenId.getText().toString().trim();
        String sheng = mFenTvSheng.getText().toString().trim();
        String xian = mFenTvXian.getText().toString().trim();
        String diaoChaZhe = mFenzhe.getText().toString().trim();
        String fillPerson = mFenFillperson.getText().toString().trim();
        String address = mFenAddress.getText().toString().trim();
        String DiaoDate = mFenDiaoDate.getText().toString().trim();
        String mPoXiang = mFenPoXiangSp.getSelectedItem().toString().trim();
        String mPoWei = mFenPoWeiSp.getSelectedItem().toString().trim();
        String mPoDu = mFenPoDuSp.getSelectedItem().toString().trim();
        String mTreeType = mFenTreeType.getSelectedItem().toString().trim();
        String mTuType = mFenTuType.getSelectedItem().toString().trim();
        String mMuYan = mFenMuYan.getText().toString().trim();
        String mArea = mDiarea.getText().toString().trim();
        String shuType = mSpShuType.getSelectedItem().toString().trim();
        String shuAge = mShuAge.getText().toString().trim();
        String mAvGuanFu = mGuanFu.getText().toString().trim();
        String mZhiHigh = mFenZhiHigh.getText().toString().trim();
        String mAvXiongJin = mFenXiongjin.getText().toString().trim();
        String mAvShuGao = mFenTreehigh.getText().toString().trim();
        String mYuBiDu = mYubidu.getText().toString().trim();
        String mMiDu = mMidu.getText().toString().trim();
        String mLinFenArea = mLinArea.getText().toString().trim();
        String mAvXuJi = mAvXuji.getText().toString().trim();
        String mQiYuanType = mQiyuanType.getSelectedItem().toString().trim();
        String mLinZhongYuan = mZhongYuan.getText().toString().trim();
        String mShuZhongZuCheng = mShuZu.getText().toString().trim();
        String health = mHealthStatus.getText().toString().trim();
        String seeds = mSeed.getText().toString().trim();


        if (mLocation == null) {
            refreshTwoLocation();
        }

        if (mLocation == null) {
            Toast.makeText(mOwner, "GPS信号弱，无法取到位置", Toast.LENGTH_LONG).show();
            okay = false;
            return;
        } else {
            if (Double.parseDouble(lat) == 0 || Double.parseDouble(lon) == 0) {
                Toast.makeText(mOwner, "请刷新位置!", Toast.LENGTH_LONG).show();
                okay = false;
                return;
            }

        }


        if (mTable.size() == 0) {
            Toast.makeText(mOwner, "请添加一条调查表信息!", Toast.LENGTH_SHORT).show();
            okay = false;
        }
        if (okay) {
            if (TextUtils.isEmpty(sheng)) {
                Toast.makeText(getActivity(), "请设置省份信息！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(xian)) {
                Toast.makeText(getActivity(), "请设置县级信息！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(diaoChaZhe)) {
                Toast.makeText(getActivity(), "请设置调查者！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(fillPerson)) {
                Toast.makeText(getActivity(), "请设置填表人！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(address)) {
                Toast.makeText(getActivity(), "请填写调查地点！", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(mArea)) {
                Toast.makeText(getActivity(), "请输入标准地大小！", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(shuAge)){
                Toast.makeText(getActivity(), "请输入林龄！", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(mAvGuanFu)){
                Toast.makeText(getActivity(), "请输入平均冠幅！", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(mAvXiongJin)){
                Toast.makeText(getActivity(), "请输入林分平均胸径！", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(mAvShuGao)){
                Toast.makeText(getActivity(), "请输入平均树高！", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(mAvXuJi)){
                Toast.makeText(getActivity(), "请输入每公顷蓄积量！", Toast.LENGTH_SHORT).show();
            } else {
                FenAllTableEntity entity = new FenAllTableEntity();
                entity.setLandOrder(Id);
                entity.setGpsTime(System.currentTimeMillis() + "");
                entity.setSheng(sheng);
                entity.setXian(xian);
                entity.setExmainPerson(diaoChaZhe);
                entity.setFillPerson(fillPerson);
                entity.setExamineDate(DiaoDate);
                entity.setLatitude(Double.parseDouble(lon));
                entity.setLongtitude(Double.parseDouble(lat));
                entity.setHight(Double.parseDouble(high));
                entity.setPoXiang(mPoXiang);
                entity.setPoWei(mPoWei);
                entity.setPoDu(mPoDu);
                entity.setAddress(address);
                entity.setPoXiang(mPoXiang);
                entity.setTreeType(mTreeType);
                entity.setTuType(mTuType);
                entity.setMyMZ(mMuYan);
                entity.setArea(mArea);
                entity.setTreeName(shuType);
                entity.setLinAge(shuAge);
                entity.setAvGuanFu(mAvGuanFu);
                entity.setZhiHigh(Double.valueOf(mZhiHigh));
                entity.setAvXiongJin(mAvXiongJin);
                entity.setAvTreeHigh(mAvShuGao);
                entity.setYuBiDu(mYuBiDu);
                entity.setMiDu(mMiDu);
                entity.setLinFenMainJi(mLinFenArea);
                entity.setAvXuji(mAvXuJi);
                entity.setQiYuan(mQiYuanType);
                entity.setLinZhongYuan(mLinZhongYuan);
                entity.setShuZhong(mShuZhongZuCheng);
                entity.setHealth(health);
                entity.setJieShi(seeds);
                String jsonList = new Gson().toJson(mTable);
                entity.setDetailJson(jsonList);
                if (PatrolManager.getInstance().saveFenEvent(entity)) {
                    Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "保存失败!", Toast.LENGTH_SHORT).show();
                }

            }


        }

    }

    private class ListExamineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private List<FenTableBean> mlist;


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_feng_item, parent, false);
            ListExamineAdapter.MyViewHolder vh = new ListExamineAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
            MyViewHolder holders = (MyViewHolder) holder;
            final FenTableBean bean = mlist.get(i);
            holders.mOrderNum.setText(bean.getId());
            holders.mXiongJin.setText(bean.getXiongjin());
            holders.mTreeHigh.setText(bean.getTreeHigh());
            holders.mGuanFu.setText(bean.getGuanFu());
            holders.mShuXing.setText(bean.getShuXing());
            holders.mTvFlowerDate.setText(bean.getFlowerDate());
            holders.mDanWeiMianJi.setText(bean.getDaiWeiMianJi());
            holders.mStartLevel.setText(bean.getStartLevel());
            holders.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTable.remove(i);
                    notifyDataSetChanged();


                }
            });

        }

        @Override
        public int getItemCount() {
            if (mlist != null && mlist.size() > 0) {
                return mlist.size();
            } else {
                return 0;
            }
        }

        public void setDatas(ArrayList<FenTableBean> mlist) {
            this.mlist = mlist;
            notifyDataSetChanged();
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            private final TextView mOrderNum, mXiongJin, mTreeHigh, mGuanFu, mShuXing, mTvFlowerDate, mDanWeiMianJi, mStartLevel;
            private ImageView mDelete;

            public MyViewHolder(View itemView) {
                super(itemView);
                mOrderNum = (TextView) itemView.findViewById(R.id.tv_number);
                mXiongJin = (TextView) itemView.findViewById(R.id.tv_xiong_jin);
                mTreeHigh = (TextView) itemView.findViewById(R.id.tv_tree_high);
                mGuanFu = (TextView) itemView.findViewById(R.id.tv_guan_fu);
                mShuXing = (TextView) itemView.findViewById(R.id.tv_shu_xing);
                mTvFlowerDate = (TextView) itemView.findViewById(R.id.tv_flower_date);
                mDanWeiMianJi = (TextView) itemView.findViewById(R.id.tv_danwei_mianji);
                mStartLevel = (TextView) itemView.findViewById(R.id.start_level);
                mDelete = (ImageView) itemView.findViewById(R.id.ic_delete);

            }
        }
    }


    private void showDialog() {
        AlertDialog.Builder startRoundDialog = new AlertDialog.Builder(getActivity());
        startRoundDialog.setCancelable(true);
        startRoundDialog.setTitle("每木调查");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_meimu_layout, null);
        startRoundDialog.setView(layout);
        mZhuHao = (TextView) layout.findViewById(R.id.tv_zhu_hao);
        mZhuHao.setText((i + 1) + "");
        mXiongJin = (EditText) layout.findViewById(R.id.et_xiong_jin);
        mTreeHigh = (EditText) layout.findViewById(R.id.et_tree_high);
        mGuanFus = (EditText) layout.findViewById(R.id.et_guan_fu);
        mShuXing = (EditText) layout.findViewById(R.id.et_shu_xing);
        mHuaTime = (EditText) layout.findViewById(R.id.et_hua_time);
        mMianji = (EditText) layout.findViewById(R.id.single_mian_ji);
        mStateLevel = (EditText) layout.findViewById(R.id.state_level);
        startRoundDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(mXiongJin.getText().toString().trim()) && !TextUtils.isEmpty(mTreeHigh.getText().toString().trim()) && !TextUtils.isEmpty(mGuanFus.getText().toString().trim())   ) {
                    FenTableBean bean = new FenTableBean();
                    bean.setId(mZhuHao.getText().toString().trim());
                    bean.setXiongjin(mXiongJin.getText().toString().trim());
                    bean.setTreeHigh(mTreeHigh.getText().toString().trim());
                    bean.setGuanFu(mGuanFus.getText().toString().trim());
                    bean.setShuXing(mShuXing.getText().toString().trim());
                    bean.setFlowerDate(mHuaTime.getText().toString().trim());
                    bean.setDaiWeiMianJi(mMianji.getText().toString().trim());
                    bean.setStartLevel(mStateLevel.getText().toString().trim());
                    mTable.add(bean);

                    if (mTable != null && mTable.size() > 0) {
                        for (int i = 0; i < mTable.size(); i++) {
                            mSumGuanfu += Double.valueOf(mTable.get(i).getGuanFu());
                            mSumXiongJin += Double.valueOf(mTable.get(i).getXiongjin());
                            mSumTreeHigh += Double.valueOf(mTable.get(i).getTreeHigh());
                            String name = mSpShuType.getSelectedItem().toString().trim();
                            Double xishu = 0.42;
                            if (name.equals("冷杉")) {
                                xishu = 0.45;
                            } else if (name.equals("华山松") || name.equals("铁杉") || name.equals("泡桐")) {
                                xishu = 0.43;
                            } else if (name.equals("油松")) {
                                xishu = 0.42;
                            } else if (name.equals("山杨") || name.equals("光皮桦")) {
                                xishu = 0.41;
                            } else if (name.equals("栎类")) {
                                xishu = 0.4;
                            } else if (name.equals("红桦") || name.equals("白桦") || name.equals("马尾松") || name.equals("杂木") || name.equals("刺槐")) {
                                xishu = 0.39;
                            } else if (name.equals("侧柏")) {
                                xishu = 0.38;
                            }
                            double hight = Double.valueOf(mTable.get(i).getTreeHigh());
                            double xiongJin = Double.valueOf(mTable.get(i).getXiongjin());
                            mSumXuji += (hight + 3) * xishu * xiongJin / 100;
                        }
                        DecimalFormat df = new DecimalFormat("##0.000");
                        mGuanFu.setText(df.format(mSumGuanfu / mTable.size()));
                        mFenXiongjin.setText(df.format(mSumXiongJin / mTable.size()));
                        mFenTreehigh.setText(df.format(mSumTreeHigh / mTable.size()));
                        if (mLinArea != null && mLinArea.length() > 0) {
                            double linarea = Double.valueOf(mLinArea.getText().toString().trim());
                            mAvXuji.setText(df.format(mSumXuji / linarea));
                        }
                        mSumGuanfu=0;
                        mSumXiongJin=0;
                        mSumTreeHigh=0;
                    }

                    mAdapter.setDatas(mTable);
                    i++;
                } else {
                    Toast.makeText(getActivity(), "请填写必须要填写的项目!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        startRoundDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        startRoundDialog.show();


    }

    private void initOne(View SingleTreeView) {
        gridView = (GridView) SingleTreeView.findViewById(R.id.gvList);
        mId = (TextView) SingleTreeView.findViewById(R.id.tv_Id);
        mTvSheng = (TextView) SingleTreeView.findViewById(R.id.tv_sheng);
        mTvXian = (TextView) SingleTreeView.findViewById(R.id.tv_xian);
        mAddress = (EditText) SingleTreeView.findViewById(R.id.et_address);
        mTvDiaoCha = (TextView) SingleTreeView.findViewById(R.id.tv_diaocha_zhe);
        mTvTianBiaoRen = (TextView) SingleTreeView.findViewById(R.id.tv_tianbiao_ren);
        mTvDate = (TextView) SingleTreeView.findViewById(R.id.tv_date);
        SharedPreferences preferences = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        mTvSheng.setText(preferences.getString(ShengDB, ""));
        mTvXian.setText(preferences.getString(XianDB, ""));
        mTvDiaoCha.setText(preferences.getString(DiaoChaZheDB, ""));
        mTvTianBiaoRen.setText(preferences.getString(FillTableDB, ""));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        mTvDate.setText(simpleDateFormat.format(date));

        mShuZhongType = (Spinner) SingleTreeView.findViewById(R.id.sp_shu_type);
        String[] shuType = "七叶树调查,红豆杉调查".split(",");
        ArrayAdapter<String> shuTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                shuType);
        shuTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mShuZhongType.setAdapter(shuTypeAdapter);
        mShuZhongType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mShuCnName.setText("七叶树属");
                    mKeCnName.setText("七叶树科");
                    mShuLaName.setText("Aesculus");
                    mKeLaName.setText("Hippocastanaceae");
                    myZhongType = "浙江七叶树,大果七叶树,长柄七叶树,大叶七叶树,欧洲七叶树,日本七叶树,云南七叶树,天师栗,小果七叶树,澜沧七叶树,多脉七叶树".split(",");
                    mLaType = "Aesculus chinensis Bunge var.chekiangensis (Hu et Fang)Fang,Aesculus chuniana Hu et Fang,Aesculus assamica Griff.,Aesculus megaphylla Hu et Fang,Aesculus hippocastanum L.,Aesculus turbinata Bl.,Aesculus wangii Hu,Aesculus wilsonii Rehd.,Aesculus tsiangii,Aesculus lantsangensis Hu & W. P. Fang,Aesculus polyneura Hu et Fang".split(",");
                } else if (position == 1) {
                    mShuCnName.setText("红豆杉属");
                    mKeCnName.setText("红豆杉科");
                    mShuLaName.setText("Taxus Linn");
                    mKeLaName.setText("Taxaceae Gray (1822).");
                    myZhongType = "欧洲红豆杉,太平洋紫衫,加拿大红豆杉,南方红豆杉,中国红豆杉,东北红豆杉,佛罗里达红豆杉,墨西哥红豆杉,南洋红豆杉,西藏红豆杉,云南红豆杉".split(",");
                    mLaType = "Taxus baccata L.,Taxus brevifolia,Taxus canadensis Marshall,Taxus wallichiana var. mairei (Leme & H. Lv.) L. K. Fu & Nan Li,Taxus chinensis（Pilger）Rehd,Taxus cuspidata Siebold & Zucc.,Taxus floridana Nutt. ex Chapm.,Taxus globosa Schltdl.,Taxus sumatrana,Taxus wallichiana Zucc.,Taxus yunnanensis Cheng et L. K. Fu".split(",");
                }
                zhongTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context, android.R.layout.simple_spinner_item, myZhongType);
                zhongTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mZhongCnName.setAdapter(zhongTypeAdapter);

                mlaTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context, android.R.layout.simple_spinner_item, mLaType);
                zhongTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mZhongLaName.setAdapter(mlaTypeAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mZhongCnName = (Spinner) SingleTreeView.findViewById(R.id.et_zhong_cn_name);
        String type = mShuZhongType.getSelectedItem().toString();
        mShuCnName = (TextView) SingleTreeView.findViewById(R.id.et_shu_cn_name);
        mKeCnName = (TextView) SingleTreeView.findViewById(R.id.et_ke_cn_name);
        mShuLaName = (TextView) SingleTreeView.findViewById(R.id.et_shu_la_name);
        mKeLaName = (TextView) SingleTreeView.findViewById(R.id.et_ke_la_name);
        if (type.equals("七叶树调查")) {
            mShuCnName.setText("七叶树属");
            mKeCnName.setText("七叶树科");
            mShuLaName.setText("Aesculus");
            mKeLaName.setText("Hippocastanaceae");
            myZhongType = "浙江七叶树,大果七叶树,长柄七叶树,大叶七叶树,欧洲七叶树,日本七叶树,云南七叶树,天师栗,小果七叶树,澜沧七叶树,多脉七叶树".split(",");
            mLaType = "Aesculus chinensis Bunge var.chekiangensis (Hu et Fang)Fang,Aesculus chuniana Hu et Fang,Aesculus assamica Griff.,Aesculus megaphylla Hu et Fang,Aesculus hippocastanum L.,Aesculus turbinata Bl.,Aesculus wangii Hu,Aesculus wilsonii Rehd.,Aesculus tsiangii,Aesculus lantsangensis Hu & W. P. Fang,Aesculus polyneura Hu et Fang".split(",");
        } else if (type.equals("红豆杉调查")) {
            mShuCnName.setText("红豆杉属");
            mKeCnName.setText("红豆杉科");
            mShuLaName.setText("Taxus Linn");
            mKeLaName.setText("Taxaceae Gray (1822).");
            myZhongType = "欧洲红豆杉,太平洋紫衫,加拿大红豆杉,南方红豆杉,中国红豆杉,东北红豆杉,佛罗里达红豆杉,墨西哥红豆杉,南洋红豆杉,西藏红豆杉,云南红豆杉".split(",");
            mLaType = "Taxus baccata L.,Taxus brevifolia,Taxus canadensis Marshall,Taxus wallichiana var. mairei (Leme & H. Lv.) L. K. Fu & Nan Li,Taxus chinensis（Pilger）Rehd,Taxus cuspidata Siebold & Zucc.,Taxus floridana Nutt. ex Chapm.,Taxus globosa Schltdl.,Taxus sumatrana,Taxus wallichiana Zucc.,Taxus yunnanensis Cheng et L. K. Fu".split(",");
        }

        zhongTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, myZhongType);
        zhongTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mZhongCnName.setAdapter(zhongTypeAdapter);


        mZhongLaName = (Spinner) SingleTreeView.findViewById(R.id.et_zhong_la_name);
        mlaTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context, android.R.layout.simple_spinner_item, mLaType);
        zhongTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mZhongLaName.setAdapter(mlaTypeAdapter);

        mZhongCnName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mZhongLaName.setSelection(0, true);
                } else if (position == 1) {
                    mZhongLaName.setSelection(1, true);
                } else if (position == 2) {
                    mZhongLaName.setSelection(2, true);
                } else if (position == 3) {
                    mZhongLaName.setSelection(3, true);
                } else if (position == 4) {
                    mZhongLaName.setSelection(4, true);
                } else if (position == 5) {
                    mZhongLaName.setSelection(5, true);
                } else if (position == 6) {
                    mZhongLaName.setSelection(6, true);
                } else if (position == 7) {
                    mZhongLaName.setSelection(7, true);
                } else if (position == 8) {
                    mZhongLaName.setSelection(8, true);
                } else if (position == 9) {
                    mZhongLaName.setSelection(9, true);
                } else if (position == 10) {
                    mZhongLaName.setSelection(10, true);
                } else if (position == 11) {
                    mZhongLaName.setSelection(11, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mZhongLaName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    mZhongCnName.setSelection(0, true);
                } else if (position == 1) {
                    mZhongCnName.setSelection(1, true);
                } else if (position == 2) {
                    mZhongCnName.setSelection(2, true);
                } else if (position == 3) {
                    mZhongCnName.setSelection(3, true);
                } else if (position == 4) {
                    mZhongCnName.setSelection(4, true);
                } else if (position == 5) {
                    mZhongCnName.setSelection(5, true);
                } else if (position == 6) {
                    mZhongCnName.setSelection(6, true);
                } else if (position == 7) {
                    mZhongCnName.setSelection(7, true);
                } else if (position == 8) {
                    mZhongCnName.setSelection(8, true);
                } else if (position == 9) {
                    mZhongCnName.setSelection(9, true);
                } else if (position == 10) {
                    mZhongCnName.setSelection(10, true);
                } else if (position == 11) {
                    mZhongCnName.setSelection(11, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLat = (TextView) SingleTreeView.findViewById(R.id.tvLat);
        mLong = (TextView) SingleTreeView.findViewById(R.id.tvLon);
        mHaiHight = (TextView) SingleTreeView.findViewById(R.id.tvHigh);

        mPoXiangSp = (Spinner) SingleTreeView.findViewById(R.id.sp_po_xiang);
        mPoWeiSp = (Spinner) SingleTreeView.findViewById(R.id.sp_po_wei);
        mPoDuSp = (Spinner) SingleTreeView.findViewById(R.id.sp_po_du);
        mSpTuType = (Spinner) SingleTreeView.findViewById(R.id.sp_tu_type);
        mCaculate = (Button) SingleTreeView.findViewById(R.id.btn_cacuate);
        mHight = (EditText) SingleTreeView.findViewById(R.id.et_high);
        mXiong = (EditText) SingleTreeView.findViewById(R.id.et_xiong);
        mGuanfu = (EditText) SingleTreeView.findViewById(R.id.et_guan_fu);
        mZhiHigh = (EditText) SingleTreeView.findViewById(R.id.et_zhi_hight);
        mXuji = (TextView) SingleTreeView.findViewById(R.id.tv_xuji_result);

        mDanzhuDescibe = (EditText) SingleTreeView.findViewById(R.id.et_danzhu_descibe);
        mTakePerson = (TextView) SingleTreeView.findViewById(R.id.tv_take_person);
        mTakePerson.setText(preferences.getString(DiaoChaZheDB, ""));
        mTakeDate = (EditText) SingleTreeView.findViewById(R.id.et_take_date);
        mTakeBeizhu = (EditText) SingleTreeView.findViewById(R.id.et_bei_zhu);

        mTvNumber = (EditText) SingleTreeView.findViewById(R.id.tv_number);
        mCaculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mHight.getText().toString())) {

                    if (!TextUtils.isEmpty(mXiong.getText().toString())) {

                        String name = mZhongCnName.getSelectedItem().toString().trim();
                        if (!TextUtils.isEmpty(name)) {
                            Double xishu = 0.42;
                            if (name.equals("冷杉")) {
                                xishu = 0.45;
                            } else if (name.equals("华山松") || name.equals("铁杉") || name.equals("泡桐")) {
                                xishu = 0.43;
                            } else if (name.equals("油松")) {
                                xishu = 0.42;
                            } else if (name.equals("山杨") || name.equals("光皮桦")) {
                                xishu = 0.41;
                            } else if (name.equals("栎类")) {
                                xishu = 0.4;
                            } else if (name.equals("红桦") || name.equals("白桦") || name.equals("马尾松") || name.equals("杂木") || name.equals("刺槐")) {
                                xishu = 0.39;
                            } else if (name.equals("侧柏")) {
                                xishu = 0.38;
                            }
                            DecimalFormat df = new DecimalFormat("##0.000");
                            double hight = Double.valueOf(mHight.getText().toString().trim());
                            double xiongJin = Double.valueOf(mXiong.getText().toString().trim());
                            String XujiStr = df.format((hight + 3) * xishu * xiongJin / 100);
                            mXuji.setText(XujiStr + "m³");
                        } else {
                            Toast.makeText(getActivity(), "请输入树种名称", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "请输入胸径！", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getActivity(), "请输入树高！", Toast.LENGTH_SHORT).show();
                }


            }
        });
        String[] poXiangType = "东,西,南,北,东南,东北,西南,西北,无".split(",");
        ArrayAdapter<String> poXiangTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                poXiangType);
        poXiangTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPoXiangSp.setAdapter(poXiangTypeAdapter);
        String[] poWeiType = "山脊,上坡,中坡,下坡,山谷,平地".split(",");
        ArrayAdapter<String> poWeiTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                poWeiType);
        poWeiTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPoWeiSp.setAdapter(poWeiTypeAdapter);

        String[] poDuType = "平(0°-5°),缓(6°-15°）,斜(16°-25°）,陡(26°-35°）,急(36°-45°）,险(>46°)".split(",");
        ArrayAdapter<String> poDuTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                poDuType);
        poWeiTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPoDuSp.setAdapter(poDuTypeAdapter);

        String[] tuType = "砖红壤,赤红壤,红壤,黄壤,黄红壤,黄棕壤,棕壤,暗棕壤,灰黑土,漂灰土,燥红土,褐土,塿土,灰褐土,黑垆土,黑钙土,栗钙土,棕钙土,灰钙土,灰漠土,灰棕漠土,棕漠土,沼泽土,水稻土,黑土,白浆土,潮土,砂姜黑土,灌淤土,绿洲土,草甸土,盐土,碱土,紫色土,石灰土,磷质石灰土,黄绵土,风沙土,火山灰土,山地草甸土,亚高山草甸土,高山草甸土,亚高山草原土,高山草原土,亚高山漠土,高山漠土,高山寒冰土,洪积冲积土,石骨土,耕作土".split(",");
        ArrayAdapter<String> tuTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                tuType);
        poWeiTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpTuType.setAdapter(tuTypeAdapter);
        mId.setText(System.currentTimeMillis() + "");
        mVideoGridView = (GridView) SingleTreeView.findViewById(R.id.video_List);
    }


    private void refreshOneLocation() {
        try {
            if (PubVar.m_GPSLocate.m_LocationEx != null && PubVar.m_GPSLocate.m_LocationEx.GetGpsFixMode() == lkGpsFixMode.en3DFix &&
                    PubVar.m_GPSLocate.m_LocationEx.GetGpsLatitude() > 0.0001 && PubVar.m_GPSLocate.m_LocationEx.GetGpsLongitude() > 0.0001) {
                mLocation = PubVar.m_GPSLocate.m_LocationEx;//TODO:还是一个引用
                Toast.makeText(mOwner, "位置已刷新!", Toast.LENGTH_SHORT).show();
                ((TextView) SingleTreeView.findViewById(R.id.tvLon)).setText(Tools.ConvertToDigi(mLocation.GetGpsLatitude() + "", 6));
                ((TextView) SingleTreeView.findViewById(R.id.tvLat)).setText(Tools.ConvertToDigi(mLocation.GetGpsLongitude() + "", 6));
                ((TextView) SingleTreeView.findViewById(R.id.tvHigh)).setText(mLocation.GetGpsAltitude() + "");
            } else {
                Toast.makeText(mOwner, "GPS信号弱,请到开阔地带刷新位置!", Toast.LENGTH_LONG).show();
                //TODO：save to log
            }

        } catch (Exception ex) {
//            Toast.makeText(mOwner, ex.getMessage(), Toast.LENGTH_SHORT).show();
            //TODO：save to log
        }
    }


    private void refreshTwoLocation() {
        try {
            if (PubVar.m_GPSLocate.m_LocationEx != null && PubVar.m_GPSLocate.m_LocationEx.GetGpsFixMode() == lkGpsFixMode.en3DFix &&
                    PubVar.m_GPSLocate.m_LocationEx.GetGpsLatitude() > 0.0001 && PubVar.m_GPSLocate.m_LocationEx.GetGpsLongitude() > 0.0001) {
                mLocation = PubVar.m_GPSLocate.m_LocationEx;//TODO:还是一个引用
                Toast.makeText(mOwner, "位置已刷新!", Toast.LENGTH_SHORT).show();
                ((TextView) LinFenView.findViewById(R.id.tvLon)).setText(Tools.ConvertToDigi(mLocation.GetGpsLatitude() + "", 6));
                ((TextView) LinFenView.findViewById(R.id.tvLat)).setText(Tools.ConvertToDigi(mLocation.GetGpsLongitude() + "", 6));
                ((TextView) LinFenView.findViewById(R.id.tvHigh)).setText(mLocation.GetGpsAltitude() + "");
            } else {
                Toast.makeText(getActivity(), "GPS信号弱,请到开阔地带刷新位置!", Toast.LENGTH_LONG).show();
                //TODO：save to log
            }

        } catch (Exception ex) {
//            Toast.makeText(mOwner, ex.getMessage(), Toast.LENGTH_SHORT).show();
            //TODO：save to log
        }
    }


//    private void bindingToView() {
//        String[] arrRoundEventType = "资源安全巡查、保护设施检查、森林防火、野生动物活动情况、社区宣传、其他".split("、");
//        ArrayAdapter<String> roundEventTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
//                android.R.layout.simple_spinner_item,
//                arrRoundEventType);
//        roundEventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spRoundEventType.setAdapter(roundEventTypeAdapter);
//    }

    @OnClick({R.id.btn_photo, R.id.text_back, R.id.text_submit, R.id.btn_deletephoto, R.id.text_Refresh, R.id.btn_add_photo, R.id.btn_video, R.id.btn_delete_video})
    public void clickBtn(View view) {
        switch (view.getId()) {
            case R.id.btn_photo:
                String maddress = mAddress.getText().toString().trim();
                String mDiaoPerson = mTvDiaoCha.getText().toString().trim();
                if (TextUtils.isEmpty(maddress)) {
                    Toast.makeText(getActivity(), "请填写调查地点!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mDiaoPerson)) {
                    Toast.makeText(getActivity(), "请设置调查人!", Toast.LENGTH_SHORT).show();
                } else {
                    takePhoto();
                }
                break;
            case R.id.text_back:
                this.getActivity().finish();
                break;
            case R.id.text_submit:
                saveMessage();
//                saveEvent();
                break;
            case R.id.btn_deletephoto:
                deletePhoto();
                break;
            case R.id.text_Refresh:
                refreshOneLocation();
                break;

            case R.id.btn_add_photo:
                AddPhoto();
                break;
            case R.id.btn_video:
                OpenVideo();
                break;
            case R.id.btn_delete_video:
                deleteVideo();
                break;
        }
    }

    private void deleteVideo() {
        ListAdapter adapter = mVideoGridView.getAdapter();
        List<String> delVideos = new ArrayList<String>();
        List<String> delPhotos = new ArrayList<>();
        if (adapter != null && adapter.getCount() > 0) {
            for (int i = 0; i < adapter.getCount(); i++) {
                HashMap<String, Object> map = (HashMap<String, Object>) adapter.getItem(i);
                View view = mVideoGridView.getChildAt(i);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_video_select);
                if (checkBox.isChecked()) {
                    File file = new File(map.get("text").toString());
                    if (delFile(file.getParentFile())) {
                        Log.d("TAG", "删除成功");
                    }
                    for (String f : mVideoNameList) {
                        if (f.equals(map.get("text").toString())) {
                            delVideos.add(f);
                        }
                    }
                    for (String f : mThumNameList) {
                        if (f.equals(map.get("image").toString())) {
                            delPhotos.add(f);
                        }
                    }


                }
            }
        }
        for (String f : delPhotos) {
            mThumNameList.remove(f);
        }
        for (String f : delVideos) {
            mVideoNameList.remove(f);
        }
        showVideo();
    }

    static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

    private void OpenVideo() {
        File file = new File(mVideoPath);
        if (!file.exists()) {
            file.mkdir();
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String path = mVideoPath + "/" + sdf.format(date);
        File files = new File(path);
        if (!files.exists()) {
            files.mkdir();
        }
        PubVar.mPath = path;
        Intent intent = new Intent(PubVar.m_DoEvent.m_Context, VideoRecordActivity.class);
        PubVar.m_DoEvent.m_Context.startActivity(intent);
    }

    private void AddPhoto() {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
        /* 取得相片后返回本画面 */
        startActivityForResult(intent, 0x110);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x110 && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            String img_url = uri.getPath();//这是本机的图片路径
            if (img_url != null && !TextUtils.isEmpty(img_url) && img_url.contains("优良树调查")) {
                img_url = img_url.replace("/root" + photoPath + "/", "");
                mPhotoNameList.add(img_url);
                ImgTime time = new ImgTime();
                time.setName(img_url);
                try {
                    Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(img_url.replace(".jpg", ""));
                    String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    time.setTime(now);
                    Log.d("TAG", now);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mPhotoTimeList.add(time);

                showPhotos();
                if (mPhotoNameList != null && mPhotoNameList.size() > 0) {
                    mTvNumber.setText("");
                    for (int i = 0; i < mPhotoNameList.size(); i++) {
                        mTvNumber.append(mPhotoNameList.get(i).substring(0, mPhotoNameList.get(i).indexOf(".")) + "_" + (i + 1) + ",");
                    }
                }
                if (mPhotoTimeList != null && mPhotoTimeList.size() > 0) {
                    mTakeDate.setText("");
                    for (int j = 0; j < mPhotoTimeList.size(); j++) {
                        mTakeDate.append(mPhotoTimeList.get(j).getTime() + ";");
                    }
                }

            } else {
                Toast.makeText(getActivity(), "暂不支持其他路径下的照片!", Toast.LENGTH_SHORT).show();
            }


        }
    }


    private void saveMessage() {
        boolean okay = true;
        String id = mId.getText().toString();
        String sheng = mTvSheng.getText().toString().trim();
        String xian = mTvXian.getText().toString().trim();
        String maddress = mAddress.getText().toString().trim();
        String mDiaoPerson = mTvDiaoCha.getText().toString().trim();
        String mTianBiaoRen = mTvTianBiaoRen.getText().toString().trim();
        String mFillDate = mTvDate.getText().toString().trim();
        String mCNzhong = mZhongCnName.getSelectedItem().toString().trim();
        String mCNshu = mShuCnName.getText().toString().trim();
        String mCNke = mKeCnName.getText().toString().trim();
        String mLaZhong = mZhongLaName.getSelectedItem().toString().trim();
        String mLaShu = mShuLaName.getText().toString().trim();
        String mLaKe = mKeLaName.getText().toString().trim();
        String lat = mLat.getText().toString().trim();
        String lot = mLong.getText().toString().trim();
        String haiba = mHaiHight.getText().toString().trim();
        String mPoxiang = mPoXiangSp.getSelectedItem().toString().trim();
        String mPoDu = mPoDuSp.getSelectedItem().toString().trim();
        String mPoWei = mPoWeiSp.getSelectedItem().toString().trim();
        String treeHight = mHight.getText().toString().trim();
        String xiongD = mXiong.getText().toString().trim();
        String guanfu = mGuanfu.getText().toString().trim();
        String zhiGao = mZhiHigh.getText().toString().trim();
        String xuji = mXuji.getText().toString().trim();
        if (xuji != null && xuji.length() > 0) {
            xuji = xuji.substring(0, xuji.indexOf("m³"));
        }
        String tuType = mSpTuType.getSelectedItem().toString().trim();
        String mdescibe = mDanzhuDescibe.getText().toString().trim();
        String mPhotoNumber = mTvNumber.getText().toString().trim();
        String mTakePersons = mTakePerson.getText().toString().trim();
        String mTakeDates = mTakeDate.getText().toString().trim();
        String mBeiZhu = mTakeBeizhu.getText().toString().trim();
        if (mLocation == null) {
            refreshOneLocation();
        }

        if (mLocation == null) {
            Toast.makeText(mOwner, "GPS信号弱，无法取到位置", Toast.LENGTH_LONG).show();
            okay = false;
            return;
        } else {
            if (Double.parseDouble(lat) == 0 || Double.parseDouble(lot) == 0) {
                Toast.makeText(mOwner, "请刷新位置!", Toast.LENGTH_LONG).show();
                okay = false;
                return;
            }

        }
        if (mPhotoNameList.size() == 0) {
            Toast.makeText(mOwner, "请拍摄照片", Toast.LENGTH_SHORT).show();
            okay = false;
        }

        if (okay) {
            if (TextUtils.isEmpty(sheng)) {
                Toast.makeText(getActivity(), "请设置省份信息！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(xian)) {
                Toast.makeText(getActivity(), "请设置县级信息！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mDiaoPerson)) {
                Toast.makeText(getActivity(), "请设置调查者！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mTianBiaoRen)) {
                Toast.makeText(getActivity(), "请设置填表人！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(maddress)) {
                Toast.makeText(getActivity(), "请填写调查地点！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(treeHight)) {
                Toast.makeText(getActivity(), "请填写树高！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(xiongD)) {
                Toast.makeText(getActivity(), "请填写胸径！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(xuji)) {
                Toast.makeText(getActivity(), "请计算生成蓄积量！", Toast.LENGTH_SHORT).show();
            } else {
                RoundExamineEntity exam = new RoundExamineEntity();
                exam.setOrderNumber(id);
                exam.setSheng(sheng);
                exam.setXian(xian);
                exam.setAddress(maddress);
                exam.setExmainPerson(mDiaoPerson);
                exam.setFillPerson(mTianBiaoRen);
                exam.setExamineDate(mFillDate);
                exam.setZhongCName(mCNzhong);
                exam.setShuCName(mCNshu);
                exam.setKeCName(mCNke);
                exam.setZhongLaName(mLaZhong);
                exam.setShuLaName(mLaShu);
                exam.setKeLaName(mLaKe);
                exam.setLatitude(Double.parseDouble(lat));
                exam.setLongtitude(Double.parseDouble(lot));
                exam.setHight(Double.parseDouble(haiba));
                exam.setPoXiang(mPoxiang);
                exam.setPoDu(mPoDu);
                exam.setPoWei(mPoWei);
                if (mPhotoNameList != null && mPhotoNameList.size() > 0) {
                    StringBuffer txt = new StringBuffer();
                    for (int i = 0; i < mPhotoNameList.size(); i++) {
                        txt.append(mPhotoNameList.get(i) + ",");
                    }
                    exam.setPhotoList(txt.toString());
                }

                if (treeHight != null && !TextUtils.isEmpty(treeHight)) {
                    double treeHigh = Double.valueOf(treeHight);
                    BigDecimal bd = new BigDecimal(treeHigh).setScale(0, BigDecimal.ROUND_HALF_UP);
                    exam.setTreeHight(Integer.valueOf(bd.toString()));
                }
                if (xiongD != null && !TextUtils.isEmpty(xiongD)) {
                    double xiong = Double.valueOf(xiongD);
                    BigDecimal bd = new BigDecimal(xiong).setScale(0, BigDecimal.ROUND_HALF_UP);
                    exam.setXiongJin(Integer.valueOf(bd.toString()));
                }
                if (guanfu != null && !TextUtils.isEmpty(guanfu)) {
                    double fu = Double.valueOf(guanfu);
                    BigDecimal bd = new BigDecimal(fu).setScale(0, BigDecimal.ROUND_HALF_UP);
                    exam.setGuanFu(Integer.valueOf(bd.toString()));
                }
                if (zhiGao != null && !TextUtils.isEmpty(zhiGao)) {
                    exam.setZhiHight(Double.valueOf(zhiGao));
                }
                if (xuji != null && !TextUtils.isEmpty(xuji)) {
                    exam.setXuji(Double.valueOf(xuji));
                }
                exam.setTuType(tuType);
                exam.setImportDescribe(mdescibe);
                exam.setPhotoOrderNum(mPhotoNumber);
                exam.setTakePerson(mTakePersons);
                exam.setTakeDate(mTakeDates);
                exam.setStateDescribe(mBeiZhu);
                if (mVideoNameList != null && mVideoNameList.size() > 0) {
                    StringBuffer video = new StringBuffer();
                    StringBuffer videoThum = new StringBuffer();
                    for (int i = 0; i < mVideoNameList.size(); i++) {
                        video.append(mVideoNameList.get(i) + ",");
                        videoThum.append(mThumNameList.get(i) + ",");
                    }
//                    exam.setVideoList(video.toString());
//                    exam.setVideoThumList(videoThum.toString());
                }
                if (PatrolManager.getInstance().saveExaminEvent(exam)) {
                    Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    private void deletePhoto() {
        GridView gridView = (GridView) SingleTreeView.findViewById(R.id.gvList);
        ListAdapter adapter = gridView.getAdapter();
        List<String> delPhotos = new ArrayList<String>();
        if (adapter != null && adapter.getCount() > 0) {
            for (int i = 0; i < adapter.getCount(); i++) {
                HashMap<String, Object> map = (HashMap<String, Object>) adapter.getItem(i);
                View view = gridView.getChildAt(i);
                if (view != null) {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_select);
                    if (checkBox.isChecked()) {
                        String fileName = map.get("image") + "";
                        File file = new File(fileName);
                        if (file.exists()) {
                            file.delete();
                        }

                        String bigFileName = fileName.replace("/samllPhoto", "");
                        File bigfile = new File(bigFileName);
                        if (bigfile.exists()) {
                            bigfile.delete();
                        }
                        bigfile.delete();

                        for (String f : mPhotoNameList) {
                            if (f.equals(map.get("text") + "")) {
                                delPhotos.add(f);
                            }
                        }
                    }
                }
            }
            if (delPhotos != null && delPhotos.size() > 0) {
                for (String f : delPhotos) {
                    mPhotoNameList.remove(f);
                }
            }

            if (delPhotos != null && delPhotos.size() > 0) {
                for (String f : delPhotos) {
                    for (int i = 0; i < mPhotoTimeList.size(); i++) {
                        if (f.equals(mPhotoTimeList.get(i).getName())) {
                            mPhotoTimeList.remove(i);
                        }
                    }
                }
            }
            if (mPhotoNameList != null && mPhotoNameList.size() >= 0) {
                mTvNumber.setText("");
                for (int i = 0; i < mPhotoNameList.size(); i++) {
                    mTvNumber.append(mPhotoNameList.get(i).substring(0, mPhotoNameList.get(i).indexOf(".")) + "_" + (i + 1) + ",");
                }
            }

            if (mPhotoTimeList != null && mPhotoTimeList.size() >= 0) {
                mTakeDate.setText("");
                for (int i = 0; i < mPhotoTimeList.size(); i++) {
                    mTakeDate.append(mPhotoTimeList.get(i).getTime() + ";");
                }
            }
            showPhotos();
        }

    }


//    private int getEventType() {
//        String eventType = spRoundEventType.getSelectedItem() + "";
//        if (eventType.equals("资源安全巡查")) {
//            return 21;
//        } else if (eventType.equals("保护设施检查")) {
//            return 22;
//        } else if (eventType.equals("森林防火")) {
//            return 23;
//        } else if (eventType.equals("野生动物活动情况")) {
//            return 24;
//        } else if (eventType.equals("社区宣传")) {
//            return 25;
//        } else {
//            return 26;
//        }
//    }

//    private void saveEvent() {
//        boolean okay = true;
//        if(mIsSaving)
//        {
//            Toast.makeText(mOwner, "点击过于频繁，请稍后点击", Toast.LENGTH_LONG).show();
//            return;
//        }
//        else
//        {
//            mIsSaving = true;
//        }
//        String strEventName = eventName.getEditText().getText().toString().trim();
//
//        String strEventPOI = eventPOI.getText().toString().trim();
//        String strEventDescrption = eventDescription.getText().toString().trim();
//
//        if (mLocation == null) {
//            refreshLocation();
//        }
//        if (mLocation == null) {
//            mIsSaving = false;
//            Toast.makeText(mOwner, "GPS信号弱，无法取到位置", Toast.LENGTH_LONG).show();
////            okay = false;
//            return;
//        }
//
////        if (mPhotoNameList.size() == 0) {
////            Toast.makeText(mOwner, "巡护记录要拍摄照片", Toast.LENGTH_SHORT).show();
////            okay = false;
////        }
//
//        if (okay) {
//
//            try {
//                roundEventEntity.setId(simpleDateFormat.format(eventTimeDate) + "_1");//为什么要加后缀
//                roundEventEntity.setEventName(strEventName);
//                roundEventEntity.setEventPOI(strEventPOI);
//                roundEventEntity.setEventDescription(strEventDescrption);
//                roundEventEntity.setEventType(getEventType());
//                roundEventEntity.setRoundId(AppSetting.curRound.getId());
//
//                roundEventEntity.setEventTime(eventTimeDate);
//                if (mLocation != null) {
//                    roundEventEntity.setEventLat(mLocation.GetGpsLatitude());
//                    roundEventEntity.setEventLon(mLocation.GetGpsLongitude());
//                    roundEventEntity.setAltitude(mLocation.GetGpsAltitude());
//                }
//
//                String strPhotoList = "";
//                for (String photo : mPhotoNameList) {
//                    if (strPhotoList.length() > 0) {
//                        strPhotoList += ";" + photo;
//                    } else {
//                        strPhotoList += photo;
//                    }
//                }
//                roundEventEntity.setEventPhotos(strPhotoList);
//                PatrolManager.getInstance().savePatrolEvent(roundEventEntity);
//                if (mPhotoNameList.size() > 0) {
//                    for (String photoName : mPhotoNameList) {
//                        PhotoEntity photoEntity = new PhotoEntity();
//                        photoEntity.setBelongTo(roundEventEntity.getId());
//                        photoEntity.setPhotoName(photoName);
//                        photoEntity.setPhotoType("发现");
//                        photoEntity.setSaveTime(new Date());
//                        photoEntity.setUserID(AppSetting.curUserKey);
//                        try {
//                            PhotoManager.getInstance().savePhoto(photoEntity);
//                        } catch (Exception ex) {
//                            Toast.makeText(mOwner, "保存照片信息：" + ex.getMessage(), Toast.LENGTH_LONG).show();
//                            //TODO:save error message to logfile
//                        }
//
//                    }
//                }
//                if (AppSetting.curRound.getServerId() == null || AppSetting.curRound.getServerId().isEmpty()) {
//                    //没有ServerId不上传
//                    mIsSaving = false;
//                    getActivity().finish();
//                    return;
//                } else {
//                    roundEventEntity.setServerId(AppSetting.curRound.getServerId());
//                }
//                Handler handler = new Handler();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpEventModel eventModel = new HttpEventModel();
//                        eventModel.setUserId(AppSetting.curUserKey);
//                        eventModel.setDescription(roundEventEntity.getEventDescription());
//                        if (AppSetting.curRound == null) {
//                            return;
//                        } else {
//                            eventModel.setRoundId(AppSetting.curRound.getServerId());
//                        }
//                        eventModel.setEventTime(roundEventEntity.getEventTime().getTime());
//                        if(roundEventEntity.getEventLat()>0&&roundEventEntity.getEventLon()>0) {
//                            eventModel.setLatitude(roundEventEntity.getEventLat() + "");
//                            eventModel.setLongitude(roundEventEntity.getEventLon() + "");
//                        }
//                        eventModel.setEventPOI(roundEventEntity.getEventPOI());
//                        eventModel.setHeight(roundEventEntity.getAltitude() + "");
//                        eventModel.setType(roundEventEntity.getEventType() + "");
//                        eventModel.setGpsTime(roundEventEntity.getEventTime().getTime());
//                        String name= StaticObject.soProjectSystem.GetCoorSystem().GetName();
//                        if(name.equals("西安80坐标")){
//                            eventModel.setSrid("2381");
//                        }else if(name.equals("北京54坐标")){
//                            eventModel.setSrid("2433");
//                        }else if(name.equals("2000国家大地坐标系")){
//                            eventModel.setSrid("4545");
//                        }else if(name.equals("WGS-84坐标")){
//                            eventModel.setSrid("4326");
//                        }
//                        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                        prompt(RetrofitHttp.getRetrofit(builder.build()).CreateEvent("CreateEvent", eventModel));
//                    }
//                });
//
//                mIsSaving = false;
//                getActivity().finish();
//            }
//            catch (Exception ex)
//            {
//                mIsSaving = false;
//                Toast.makeText(mOwner,"保存发现失败："+ex.getMessage(),Toast.LENGTH_LONG).show();
//                //TODO:save file
//            }
//        }
//    }

    private void takePhoto() {

        if (!Tools.ExistFile(photoPath)) {
            boolean createPath = (new File(photoPath)).mkdirs();
            Log.i("createPath", createPath + "");
        }

        if (!Tools.ExistFile(smallPhotoPath)) {
            (new File(smallPhotoPath)).mkdirs();
        }

        Intent photoCamera = new Intent(mOwner, PhotoCamera.class);
        Log.i("拍照", "打开相机");
        photoCamera.putExtra("PhotoPath", photoPath);
        photoCamera.putExtra("TempPhoto", "TempPhoto.jpg");
        photoCamera.putExtra("from", "eventActivity");
        this.startActivity(photoCamera);
    }


    //拍照完成后回调
    public void photoCallbackResult(String resultCode, final Object ExtraStr) {
        if (!resultCode.equals("1")) {
            Log.i("事件拍照回调", "回调校验码：" + resultCode);
            return;
        }

        if (!Tools.ExistFile(tempPhotoName)) {
            Log.i("事件拍照回调", "没有写入文件：" + resultCode);
            return;
        }

        try {
            if (mSavingingDlg != null) {
                mSavingingDlg.show();
            }
        } catch (Exception ex) {
            Toast.makeText(mOwner, "打开SavingDlg" + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String PhotoFileName = simpleDateFormat.format(new java.util.Date()) + ".jpg";
                File f1 = new File(tempPhotoName.toString());

                boolean isFailed = true;
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPurgeable = true;
                    options.inMutable = true;
                    options.inInputShareable = true;
                    FileInputStream iSteam = new FileInputStream(tempPhotoName);
                    Bitmap bitmap = BitmapFactory.decodeStream(iSteam, null, options);
                    iSteam.close();
                    Log.v("tag", "读取照片。。。");

                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();


                    Canvas canvasTemp = new Canvas(bitmap);

                    Paint p = new Paint();
                    String familyName = "宋体";
                    Typeface font = Typeface.create(familyName, Typeface.BOLD);
                    p.setColor(Color.RED);
                    p.setTypeface(font);
                    p.setTextSize(50);

                    String maddress = mAddress.getText().toString().trim();
                    String mDiaoPerson = mTvDiaoCha.getText().toString().trim();

                    String examPerson = "调查人:" + mDiaoPerson;
                    canvasTemp.drawText(examPerson, 8, h - 250, p);
                    String address = "调查地点:" + maddress;
                    canvasTemp.drawText(address, 8, h - 200, p);
                    String dt = Tools.GetSystemDate();
                    String strCamerTime = "拍摄时间：" + dt;
                    canvasTemp.drawText(strCamerTime, 8, h - 100, p);

                    if (AppSetting.curUser != null && AppSetting.curUser.getLoginName() != null) {
                        canvasTemp.drawText("拍摄者：" + AppSetting.curUser.getLoginName(), 8, h - 50, p);
                    }

                    String pswz = "拍摄位置：未定位";
                    if (PubVar.m_GPSLocate.m_LocationEx != null && PubVar.m_GPSLocate.m_LocationEx.GetGpsFixMode() == lkGpsFixMode.en3DFix &&
                            PubVar.m_GPSLocate.m_LocationEx.GetGpsLatitude() > 0.0001 && PubVar.m_GPSLocate.m_LocationEx.GetGpsLongitude() > 0.0001) {
                        try {
                            String[] Coor = PubVar.m_GPSLocate.getJWGPSCoordinate().split(",");
                            String jd = Tools.GetDDMMSS(Tools.ConvertToDouble(Coor[0]));
                            String wd = Tools.GetDDMMSS(Tools.ConvertToDouble(Coor[1]));
                            pswz = "拍摄位置：" + wd + "," + jd + "," + PubVar.m_GPSLocate.getGPSCoordinate().getZ();

                            Log.v("tag", "添加拍摄位置。。。");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                    canvasTemp.drawText(pswz, 8, h - 150, p);

                    canvasTemp.save();
                    canvasTemp.restore();

                    FileOutputStream fos = new FileOutputStream(f1);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    fos = null;
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    FileInputStream iSteam2 = new FileInputStream(tempPhotoName);
                    Bitmap bitmap2 = BitmapFactory.decodeStream(iSteam2);
                    File smallF = new File(AppSetting.smallPhotoPath + "/" + PhotoFileName);
                    FileOutputStream f = new FileOutputStream(smallF);
                    Bitmap b = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth() / 8, bitmap2.getHeight() / 8, false);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, f);

                    isFailed = false;
                    f.flush();
                    f.close();
                    f = null;

                    if (!b.isRecycled()) {
                        b.recycle();
                    }

                    f1.renameTo(new File(photoPath + "/" + PhotoFileName));
                    mPhotoNameList.add(PhotoFileName);
                    ImgTime time = new ImgTime();
                    time.setName(PhotoFileName);
                    time.setTime(dt);
                    mPhotoTimeList.add(time);

                    gridView.post(new Runnable() {
                        @Override
                        public void run() {
                            showPhotos();
                            if (mPhotoNameList != null && mPhotoNameList.size() > 0) {
                                mTvNumber.setText("");
                                for (int i = 0; i < mPhotoNameList.size(); i++) {
                                    mTvNumber.append(mPhotoNameList.get(i).substring(0, mPhotoNameList.get(i).indexOf(".")) + "_" + (i + 1) + ",");
                                }
                            }
                            if (mPhotoTimeList != null && mPhotoTimeList.size() > 0) {
                                mTakeDate.setText("");
                                for (int j = 0; j < mPhotoTimeList.size(); j++) {
                                    mTakeDate.append(mPhotoTimeList.get(j).getTime() + ";");
                                }
                            }
                        }
                    });
                    String fileName = AppSetting.photoPath + "/" + PhotoFileName;
                    JSONObject exif = (JSONObject) ExtraStr;
                    //存储exif信息

                    try {
                        if (PubVar.m_GPSLocate != null && PubVar.m_GPSLocate.m_LocationEx != null) {
                            String[] Coor = PubVar.m_GPSLocate.getJWGPSCoordinate().split(",");

                            ExifInterface exifInfo = new ExifInterface(fileName);
                            exifInfo.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
                            exifInfo.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
                            exifInfo.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, Tools.ConvertToSexagesimal(Coor[0]));
                            exifInfo.setAttribute(ExifInterface.TAG_GPS_LATITUDE, Tools.ConvertToSexagesimal(Coor[1]));
                            String[] GPSDateTime = PubVar.m_GPSLocate.getGPSDateForPhotoFormat();
                            exifInfo.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, GPSDateTime[1]);
                            exifInfo.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, GPSDateTime[0]);

                            if (PubVar.m_GPSLocate.m_LocationEx != null) {
                                exif.put("lat:", PubVar.m_GPSLocate.m_LocationEx.GetGpsLatitude());
                                exif.put("lon:", PubVar.m_GPSLocate.m_LocationEx.GetGpsLongitude());
                                exif.put("gpsTime", PubVar.m_GPSLocate.m_LocationEx.GetGpsDate() + " " + PubVar.m_GPSLocate.m_LocationEx.GetGpsTime());
                            }

                            Log.d("exif save", exif.toString());
                            exifInfo.setAttribute(ExifInterface.TAG_USER_COMMENT, exif.toString());
                            exifInfo.saveAttributes();
                        }


                    } catch (Exception io) {
                        //TODO:save to log
                        io.printStackTrace();
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (mSavingingDlg != null && mSavingingDlg.isShowing()) {
                        mSavingingDlg.dismiss();
                    }
                } catch (Exception ex) {
                    Toast.makeText(mOwner, "关闭SavingDlg" + ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }).start();
    }

    private void showPhotos() {
        String[] from = {"image", "text", "check"};
        int[] to = {R.id.iv_image, R.id.tv_info, R.id.cb_select};
        ArrayList<HashMap<String, Object>> data_list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < mPhotoNameList.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", smallPhotoPath + "/" + mPhotoNameList.get(i));
            map.put("text", mPhotoNameList.get(i));
            map.put("check", false);
            data_list.add(map);
        }
        //sim_adapter = new ImageListAdapter(mOwnActivity, data_list, R.layout.photolistitem, from, to);
        SimpleAdapter sim_adapter = new SimpleAdapter(mOwner, data_list, R.layout.photolistitem, from, to);

        Log.i("gridView", "gridView item:" + data_list.size());
        gridView.setAdapter(sim_adapter);
        gridView.setOnItemClickListener(new photoItemClickListener());
        gridView.invalidate();
    }

    private void prompt(Call<ResponseBody> newRound) {

        newRound.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> reg, Response<ResponseBody> response) {

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    if (result.get("success").equals(Boolean.TRUE)) {
                        Log.e("finish Event upload", " result: " + result);
                        roundEventEntity.setUploadStatus(1);//已上传
                        roundEventEntity.setServerId(result.get("data").toString());
                        PatrolManager.getInstance().savePatrolEvent(roundEventEntity);
                        Toast.makeText(mOwner, "巡护事件上传成功，继续上传照片", Toast.LENGTH_SHORT).show();
                        if (mPhotoNameList.size() > 0) {
                            uploadPhotoOneByOne(0);
                        }
                    } else {
                        Toast.makeText(mOwner, "巡护事件失败：" + result.get("msg"), Toast.LENGTH_SHORT).show();
                        Log.e("Event upload failed", " result: " + result);
                    }
                } catch (Exception io) {
                    Toast.makeText(mOwner, io.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Event upload failed", " exception: " + io.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> reg, Throwable t) {
                Log.e("finish Round upload", " exception: " + t.getMessage());
                Toast.makeText(mOwner, "巡护完成状态未上传到服务器，待网络恢复后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadPhotoOneByOne(final int photoIndex) {
        final boolean hasNext = photoIndex < (mPhotoNameList.size() - 2);
        final String fileName = mPhotoNameList.get(photoIndex);
        try {
            File photo = new File(AppSetting.photoPath + "/" + fileName);
            RequestBody eventId = RequestBody.create(MediaType.parse("text/plain"), roundEventEntity.getServerId());
            Map<String, RequestBody> map = new HashMap<>();
            map.put("eventId", eventId);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), photo);
            map.put("uploadedFiles\"; filename=\"" + photo.getName(), fileBody);

            String imageInfo = "";
            try {
                ExifInterface exifInfo = new ExifInterface(AppSetting.photoPath + "/" + fileName);
                imageInfo = exifInfo.getAttribute(ExifInterface.TAG_USER_COMMENT);
                Log.d("exif read", imageInfo);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (imageInfo == null) {
                imageInfo = "";
            }
            RequestBody imageExif = RequestBody.create(MediaType.parse("text/plain"), imageInfo);
            map.put("imageInfo", imageExif);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Call<ResponseBody> newPhoto = RetrofitHttp.getRetrofit(builder.build()).uploadEventPicture("UploadEventFile", map);
            newPhoto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        if (hasNext) {
                            uploadPhotoOneByOne(photoIndex + 1);
                        }
                        if (response == null || response.body() == null || response.body().string() == null) {
                            Log.e("uploadPhotos faild", "response.body() is null");
                            return;
                        }

                        JSONObject result = new JSONObject(response.body().string());
                        if (result.get("success").equals(Boolean.TRUE)) {

                            try {
                                PhotoEntity photoEntity = PhotoManager.getInstance().getPhotoEntity(fileName);
                                photoEntity.setUploadStatus(1);
                                photoEntity.setUploadTime(new Date());
                                PhotoManager.getInstance().savePhoto(photoEntity);
                            } catch (Exception ex) {
                                Toast.makeText(AppSetting.applicaton.getApplicationContext(), "更新照片失败：" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(mOwner, "上传照片成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mOwner, "上传照片失败1：" + result.get("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(mOwner, "照片上传失败2" + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(mOwner, "上传照片失败：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(mOwner, "上传照片失败3：" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            //TODO:log to file
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initSavingDlg() {

        mSavingingDlg = new Dialog(this.getActivity(), R.style.loginingDlg);
        mSavingingDlg.setContentView(R.layout.logindlg);

        Window window = mSavingingDlg.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置

        // 获取屏幕的高宽
        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int cxScreen = dm.widthPixels;
        int cyScreen = dm.heightPixels;

        int height = (int) getResources().getDimension(
                R.dimen.loginingdlg_height);// 高42dp
        int lrMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_lr_margin); // 左右边沿10dp
        int topMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_top_margin); // 上沿20dp

        params.y = (-(cyScreen - height) / 2) + topMargin; // -199
        /* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */
        ((TextView) mSavingingDlg.findViewById(R.id.tv_loading_show)).setText("正在处理照片");
        params.width = cxScreen;
        params.height = height;
        // width,height表示mLoginingDlg的实际大小
        mSavingingDlg.setCanceledOnTouchOutside(false);
//        mSavingingDlg.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog
    }

    //照片点击展示
    class photoItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                View arg1,//The view within the AdapterView that was clicked
                                int arg2,//The position of the view in the adapter
                                long arg3//The row id of the item that was clicked
        ) {
            //在本例中arg2=arg3
            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
            //显示所选Item的ItemText

            Intent it = new Intent(Intent.ACTION_VIEW);
            String photoName = item.get("image") + "";
            photoName = photoName.replace("/samllPhoto", "");
            File file = new File(photoName);
            Uri fileName = FileProvider.getUriForFile(mOwner, "com.example.event.fileprovider", file);
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            it.setDataAndType(fileName, "image/*");

            mOwner.startActivity(it);
        }
    }

}
