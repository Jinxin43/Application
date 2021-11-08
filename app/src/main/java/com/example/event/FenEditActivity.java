package com.example.event;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DingTu.Base.ICallback;
import com.DingTu.Base.PubVar;
import com.example.event.View.RecyclerViewSpacesItemDecoration;
import com.example.event.db.xEntity.FenAllTableEntity;
import com.example.event.db.xEntity.FenUploadEntity;
import com.example.event.manager.PatrolManager;
import com.example.event.manager.UploadMananger;
import com.example.event.model.FenTableBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FenEditActivity extends AppCompatActivity {

    private FenAllTableEntity mBean;
    private Dialog mSavingingDlg;
    private TextView mLat, mLon, mHigh, mId, mTvSheng, mTvXian, mTvDiaochaZhe, mTianBiaoRen, mTvdate, mEtAddress;
    private Spinner mSpPoxiang, mSpPodu, mSpPowei;
    private Spinner mTreeType, mTuType, mShuType, mQiYuanType;
    private EditText mFenMuYan, mDiarea, mShuAge, mGuanFu, mFenZhiHigh, mFenXiongjin, mFenTreehigh, mYubidu, mMidu, mLinArea, mAvXuji, mZhongYuan, mShuZu, mHealthStatus, mSeed;
    private RecyclerView mFengExamine;
    private ListExamineAdapter mAdapter;
    private ArrayList<FenTableBean> mJson;
    private CardView mCardAddList;
    private Button mSumbit, mBack;
    private TextView mZhuHao, mXiongJin, mTreeHigh, mGuanFus, mShuXing, mHuaTime, mMianji, mStateLevel;
    private String Id;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fen_edit);
        mBean = (FenAllTableEntity) getIntent().getSerializableExtra("StateBean");
        View customView = LayoutInflater.from(this).inflate(R.layout.actionbar_back, new RelativeLayout(this), false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(customView);
        ((TextView) customView.findViewById(R.id.tv_back)).setText("林分编辑");
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((ImageView) customView.findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSavingDlg();
        mLat = (TextView) findViewById(R.id.tvLat);
        mLat.setText(mBean.getLongtitude() + "");
        mLon = (TextView) findViewById(R.id.tvLon);
        mLon.setText(mBean.getLatitude() + "");
        mHigh = (TextView) findViewById(R.id.tvHigh);
        mHigh.setText(mBean.getHight() + "");
        mId = (TextView) findViewById(R.id.tv_Id);
        mId.setText(mBean.getLandOrder());
        mTvSheng = (TextView) findViewById(R.id.tv_sheng);
        mTvSheng.setText(mBean.getSheng());
        mTvXian = (TextView) findViewById(R.id.tv_xian);
        mTvXian.setText(mBean.getXian());
        mTvDiaochaZhe = (TextView) findViewById(R.id.tv_diaocha_zhe);
        mTvDiaochaZhe.setText(mBean.getExmainPerson());
        mTianBiaoRen = (TextView) findViewById(R.id.tv_tianbiao_ren);
        mTianBiaoRen.setText(mBean.getFillPerson());
        mTvdate = (TextView) findViewById(R.id.tv_date);
        mTvdate.setText(mBean.getExamineDate());
        mEtAddress = (TextView) findViewById(R.id.et_address);
        mEtAddress.setText(mBean.getAddress());


        mSpPoxiang = (Spinner) findViewById(R.id.sp_po_xiang);
        mSpPodu = (Spinner) findViewById(R.id.sp_po_du);
        mSpPowei = (Spinner) findViewById(R.id.sp_po_wei);


        mTreeType = (Spinner) findViewById(R.id.sp_tree_type);
        mTuType = (Spinner) findViewById(R.id.sp_tu_type);
        mShuType = (Spinner) findViewById(R.id.sp_shu_type);
        mQiYuanType = (Spinner) findViewById(R.id.sp_qiyuan_type);

        String poxiang = mBean.getPoXiang();
        String[] poXiangType = "东,西,南,北,东南,东北,西南,西北,无".split(",");
        ArrayAdapter<String> poXiangTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                poXiangType);
        poXiangTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpPoxiang.setAdapter(poXiangTypeAdapter);
        for (int i = 0; i < poXiangType.length; i++) {
            if (poxiang.equals(poXiangType[i])) {
                mSpPoxiang.setSelection(i, true);
            }
        }

        String poWei = mBean.getPoWei();
        String[] poWeiType = "山脊,上坡,中坡,下坡,山谷,平地".split(",");
        ArrayAdapter<String> poWeiTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                poWeiType);
        poWeiTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpPowei.setAdapter(poWeiTypeAdapter);
        for (int i = 0; i < poWeiType.length; i++) {
            if (poWei.equals(poWeiType[i])) {
                mSpPowei.setSelection(i, true);
            }
        }

        String poDu = mBean.getPoDu();
        String[] poDuType = "平(0°-5°),缓(6°-15°）,斜(16°-25°）,陡(26°-35°）,急(36°-45°）,险(>46°)".split(",");
        ArrayAdapter<String> poDuTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                poDuType);
        poWeiTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpPodu.setAdapter(poDuTypeAdapter);
        for (int i = 0; i < poDuType.length; i++) {
            if (poDu.equals(poDuType[i])) {
                mSpPodu.setSelection(i, true);
            }
        }

        String treeTypes = mBean.getTreeType();
        String[] treeType = "乔木,灌木,草".split(",");
        ArrayAdapter<String> treeTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                treeType);
        treeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTreeType.setAdapter(treeTypeAdapter);
        for (int i = 0; i < treeType.length; i++) {
            if (treeTypes.equals(treeType[i])) {
                mTreeType.setSelection(i, true);
            }
        }

        String tuTypes = mBean.getTuType();
        String[] tuType = "黄棕壤,棕壤,暗棕壤,灰黑土,漂灰土,燥红土,褐土,塿土,灰褐土,黑垆土,黑钙土,棕钙土,灰钙土,灰漠土,灰棕漠土,棕漠土,沼泽土,水稻土,黑土,白浆土,潮土,砂姜黑土,灌淤土,绿洲土,草甸土,盐土,碱土,紫色土,石灰土,磷质石灰土,黄绵土,风沙土,火山灰土,山地草甸土,亚高山草甸土,高山草甸土,亚高山草原土,高山草原土,亚高山漠土,高山漠土,高山寒冰土,洪积冲积土,石骨土,耕作土".split(",");
        ArrayAdapter<String> tuTypeAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                tuType);
        tuTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTuType.setAdapter(tuTypeAdapter);
        for (int i = 0; i < tuType.length; i++) {
            if (tuTypes.equals(tuType[i])) {
                mTuType.setSelection(i, true);
            }
        }

        String desTree = mBean.getTreeName();
        String[] shuType = "七叶树,红豆杉".split(",");
        ArrayAdapter<String> shuAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                shuType);
        shuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mShuType.setAdapter(shuAdapter);
        for (int i = 0; i < shuType.length; i++) {
            if (desTree.equals(tuType[i])) {
                mShuType.setSelection(i, true);
            }
        }

        String qiYuan = mBean.getQiYuan();
        String[] qiYuanType = "天然林,人工林".split(",");
        ArrayAdapter<String> qiYuanAdapter = new ArrayAdapter<String>(PubVar.m_DoEvent.m_Context,
                android.R.layout.simple_spinner_item,
                qiYuanType);
        shuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mQiYuanType.setAdapter(qiYuanAdapter);
        for (int i = 0; i < qiYuanType.length; i++) {
            if (qiYuan.equals(qiYuanType[i])) {
                mQiYuanType.setSelection(i, true);
            }
        }

        mFenMuYan = (EditText) findViewById(R.id.et_mu_yan);
        mFenMuYan.setText(mBean.getMyMZ());
        mDiarea = (EditText) findViewById(R.id.et_di_area);
        mDiarea.setText(mBean.getArea());
        mShuAge = (EditText) findViewById(R.id.et_shu_edit_age);
        mShuAge.setText(mBean.getLinAge());
        mGuanFu = (EditText) findViewById(R.id.et_guan_fu);
        mGuanFu.setText(mBean.getAvGuanFu());
        mFenZhiHigh = (EditText) findViewById(R.id.et_zhi_hight);
        mFenZhiHigh.setText(mBean.getZhiHigh() + "");
        mFenXiongjin = (EditText) findViewById(R.id.et_average_edit_xiongjin);
        mFenXiongjin.setText(mBean.getAvXiongJin());
        mFenTreehigh = (EditText) findViewById(R.id.et_average_edit_treehight);
        mFenTreehigh.setText(mBean.getAvTreeHigh());
        mYubidu = (EditText) findViewById(R.id.et_yubi_du);
        mYubidu.setText(mBean.getYuBiDu());
        mMidu = (EditText) findViewById(R.id.et_midu);
        mMidu.setText(mBean.getMiDu());
        mLinArea = (EditText) findViewById(R.id.et_lin_area);
        mLinArea.setText(mBean.getLinFenMainJi());
        mAvXuji = (EditText) findViewById(R.id.et_average_xuji);
        mAvXuji.setText(mBean.getAvXuji());
        mZhongYuan = (EditText) findViewById(R.id.et_edit_zhong_yuan);
        mZhongYuan.setText(mBean.getLinZhongYuan());
        mShuZu = (EditText) findViewById(R.id.et_shu_zu);
        mShuZu.setText(mBean.getShuZhong());
        mHealthStatus = (EditText) findViewById(R.id.et_health_status);
        mHealthStatus.setText(mBean.getHealth());
        mSeed = (EditText) findViewById(R.id.et_edit_seed);
        mSeed.setText(mBean.getJieShi());

        mJson = new Gson().fromJson(mBean.getDetailJson(), new TypeToken<List<FenTableBean>>() {
        }.getType());
        mFengExamine = (RecyclerView) findViewById(R.id.list_fen_examine);
        mFengExamine.addItemDecoration(new RecyclerViewSpacesItemDecoration(10));
        mFengExamine.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ListExamineAdapter();
        mFengExamine.setAdapter(mAdapter);
        mAdapter.setDatas(mJson);

        mCardAddList = (CardView) findViewById(R.id.card_add_list);
        mCardAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mSumbit = (Button) findViewById(R.id.text_submit);
        mSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumbitMessage();
            }
        });

        mBack = (Button) findViewById(R.id.text_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void sumbitMessage() {
        boolean okay = true;

        if (mJson.size() == 0) {
            Toast.makeText(getApplicationContext(), "请添加一条调查表信息!", Toast.LENGTH_SHORT).show();
            okay = false;
        }

        String lat = mLat.getText().toString().trim();
        String lon = mLon.getText().toString().trim();
        String high = mHigh.getText().toString().trim();
        String Id = mId.getText().toString().trim();
        String sheng = mTvSheng.getText().toString().trim();
        String xian = mTvXian.getText().toString().trim();
        String diaoChaZhe = mTvDiaochaZhe.getText().toString().trim();
        String fillPerson = mTianBiaoRen.getText().toString().trim();
        String address = mEtAddress.getText().toString().trim();
        String DiaoDate = mTvdate.getText().toString().trim();
        String mPoXiang = mSpPoxiang.getSelectedItem().toString().trim();
        String mPoWei = mSpPowei.getSelectedItem().toString().trim();
        String mPoDu = mSpPodu.getSelectedItem().toString().trim();
        String mTreeTypes = mTreeType.getSelectedItem().toString().trim();
        String mTuTypes = mTuType.getSelectedItem().toString().trim();
        String mMuYan = mFenMuYan.getText().toString().trim();
        String mArea = mDiarea.getText().toString().trim();
        String shuType = mShuType.getSelectedItem().toString().trim();
        String shuAge = mShuAge.getText().toString().trim();
        String mAvGuanFu = mGuanFu.getText().toString().trim();
        String mZhiHigh = mFenZhiHigh.getText().toString().trim();
        String mAvXiongJin = mFenXiongjin.getText().toString().trim();
        String mAvShuGao = mFenTreehigh.getText().toString().trim();
        String mYuBiDu = mYubidu.getText().toString().trim();
        String mMiDu = mMidu.getText().toString().trim();
        String mLinFenArea = mLinArea.getText().toString().trim();
        String mAvXuJi = mAvXuji.getText().toString().trim();
        String mQiYuanTypes = mQiYuanType.getSelectedItem().toString().trim();
        String mLinZhongYuan = mZhongYuan.getText().toString().trim();
        String mShuZhongZuCheng = mShuZu.getText().toString().trim();
        String health = mHealthStatus.getText().toString().trim();
        String seed = mSeed.getText().toString().trim();


        if (okay) {
            final FenAllTableEntity entity = new FenAllTableEntity();
            entity.setLandOrder(Id);
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
            entity.setTreeType(mTreeTypes);
            entity.setTuType(mTuTypes);
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
            entity.setQiYuan(mQiYuanTypes);
            entity.setLinZhongYuan(mLinZhongYuan);
            entity.setShuZhong(mShuZhongZuCheng);
            entity.setHealth(health);
            entity.setJieShi(seed);
            String jsonList = new Gson().toJson(mJson);
            entity.setDetailJson(jsonList);

            if (PatrolManager.getInstance().saveFenEvent(entity)) {

                List<FenUploadEntity> entitys = PatrolManager.getInstance().getFenUpload();
                if (entitys != null && entitys.size() > 0) {
                    for (int i = 0; i < entitys.size(); i++) {
                        if (entitys.get(i).getLandOrder().equals(entity.getLandOrder())) {
                            Id = entitys.get(i).getMessAgeId();
                        }
                    }

                    UploadMananger.getInstance().EditFen(Id, entity, new ICallback() {
                        @Override
                        public void OnClick(String Str, Object ExtraStr) {
                            if (Str.equals("success")) {
                                Toast.makeText(getApplicationContext(), "当前修改的林分调查信息,已上传成功!", Toast.LENGTH_SHORT).show();
                            } else if (Str.equals("failed")) {
                                if (ExtraStr != null) {

                                } else {
                                    Toast.makeText(getApplicationContext(), "当前修改的林分调查信息,已上传失败!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                }


                Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "保存失败!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog() {
        AlertDialog.Builder startRoundDialog = new AlertDialog.Builder(this);
        startRoundDialog.setCancelable(true);
        startRoundDialog.setTitle("每木调查");

        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_meimu_layout, null);
        startRoundDialog.setView(layout);
        mZhuHao = (TextView) layout.findViewById(R.id.tv_zhu_hao);
        Log.d("Tag", mJson.size() + "+++");
        if (mJson != null && mJson.size() > 0) {
            mZhuHao.setText((Integer.parseInt(mJson.get(mJson.size() - 1).getId()) + 1) + "");
        } else {
            mZhuHao.setText(1 + "");
        }
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
                if (!TextUtils.isEmpty(mXiongJin.getText().toString().trim()) && !TextUtils.isEmpty(mTreeHigh.getText().toString().trim()) && !TextUtils.isEmpty(mGuanFus.getText().toString().trim()) && !TextUtils.isEmpty(mShuXing.getText().toString().trim())) {
                    FenTableBean bean = new FenTableBean();
                    bean.setId(mZhuHao.getText().toString().trim());
                    bean.setXiongjin(mXiongJin.getText().toString().trim());
                    bean.setTreeHigh(mTreeHigh.getText().toString().trim());
                    bean.setGuanFu(mGuanFus.getText().toString().trim());
                    bean.setShuXing(mShuXing.getText().toString().trim());
                    bean.setFlowerDate(mHuaTime.getText().toString().trim());
                    bean.setDaiWeiMianJi(mMianji.getText().toString().trim());
                    bean.setStartLevel(mStateLevel.getText().toString().trim());
                    bean.setUUID(UUID.randomUUID().toString());
                    mJson.add(bean);
                    mAdapter.setDatas(mJson);
                } else {
                    Toast.makeText(getApplicationContext(), "请输入需要填写的数据!", Toast.LENGTH_SHORT).show();
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


    private class ListExamineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private List<FenTableBean> mlist;


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_feng_item, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
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
                    deleteNormal(i, mJson, mBean);
                    mJson.remove(i);
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

    private void deleteNormal(int i, ArrayList<FenTableBean> mJson, FenAllTableEntity mBean) {
        List<FenUploadEntity> entitys = PatrolManager.getInstance().getFenUpload();
        if (entitys != null && entitys.size() > 0) {
            for (int j = 0; j < entitys.size(); j++) {
                Log.d("Tag", entitys.get(j).getLandOrder()+"-----"+entitys.get(j).getTreeId()+"------------"+entitys.get(j).getId());
                Log.d("Tag",mBean.getLandOrder()+"========="+mJson.get(i).getId());
                if (entitys.get(j).getLandOrder().equals(mBean.getLandOrder()) && entitys.get(j).getTreeId().equals(mJson.get(i).getId())) {
                    Id = entitys.get(j).getId();
                }
                if(Id!=null) {
                    Log.d("Tag", Id);
                }
            }
            if (Id != null && !TextUtils.isEmpty(Id)) {
                UploadMananger.getInstance().DelateFen(Id, new ICallback() {

                    @Override
                    public void OnClick(String Str, Object ExtraStr) {
                        if (Str.equals("success")) {
                            Toast.makeText(getApplicationContext(), "当前删除的林分单株信息,已上传成功!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "当前删除的林分单株信息,上传失败!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }

        }

    }


    private void initSavingDlg() {

        mSavingingDlg = new Dialog(FenEditActivity.this, R.style.loginingDlg);
        mSavingingDlg.setContentView(R.layout.logindlg);

        Window window = mSavingingDlg.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置

        // 获取屏幕的高宽
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
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

}
