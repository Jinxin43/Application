package com.example.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.FillEventHistory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DingTu.Base.PubVar;
import com.example.event.View.RecyclerViewSpacesItemDecoration;
import com.example.event.controls.MyViewpager;
import com.example.event.db.xEntity.FenAllTableEntity;
import com.example.event.db.xEntity.RoundExamineEntity;
import com.example.event.manager.PatrolManager;
import com.example.event.model.FenAllTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListExamineActivity extends AppCompatActivity {

    private RecyclerView mRecyView;
    private ListExamineAdapter mAdapter;
    private View conentView;
    private MenuPopWindow mPopupWindow;
    private List<View> mViews = new ArrayList<View>();
    private View SingleTreeView, LinFenView;
    private MyViewpager mViewPager;
    private PagerAdapter mAdpter;
    private ListFenEventAdapter mFengAdapter;
    private RecyclerView mFenRecyView;
    private boolean isFirstPage=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_examine);
        View customView = LayoutInflater.from(this).inflate(R.layout.actionbar_back, new RelativeLayout(this), false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(customView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((TextView) customView.findViewById(R.id.tv_back)).setText("调查记录");
        ((ImageView) customView.findViewById(R.id.iv_tools)).setVisibility(View.VISIBLE);
        ((ImageView) customView.findViewById(R.id.iv_tools)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow = new MenuPopWindow(ListExamineActivity.this,isFirstPage);
                mPopupWindow.showPopupWindow(v);
            }
        });
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.menu_popup_window, null);
        ((ImageView) customView.findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();


    }

    private void init() {
        LayoutInflater mInflater = LayoutInflater.from(PubVar.m_DoEvent.m_Context);
        SingleTreeView = mInflater.inflate(R.layout.single_history, null);
        LinFenView = mInflater.inflate(R.layout.feng_history, null);
        mViews.add(SingleTreeView);
        mViews.add(LinFenView);
        mViewPager = (com.example.event.controls.MyViewpager) findViewById(R.id.my_view_pager_list);
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
        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.rg_history);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_single_history:
                        isFirstPage=true;
                        mViewPager.setCurrentItem(0);
                        List<RoundExamineEntity> mRoundlist = PatrolManager.getInstance().getExam();
                        mAdapter.setDatas(mRoundlist);
                        break;
                    case R.id.rb_linfen_history:
                        isFirstPage=false;
                        mViewPager.setCurrentItem(1);
                        List<FenAllTableEntity> mlist = PatrolManager.getInstance().getfenEvent();
                        mFengAdapter.setDatas(mlist);
                        break;


                }
            }
        });

        initOne(SingleTreeView);
        initTwo(LinFenView);

    }

    private void initTwo(View linFenView) {
        mFenRecyView = (RecyclerView) linFenView.findViewById(R.id.fen_examine);
        mFenRecyView.addItemDecoration(new RecyclerViewSpacesItemDecoration(10));
        mFenRecyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFengAdapter = new ListFenEventAdapter();
        mFenRecyView.setAdapter(mFengAdapter);

    }

    private void initOne(View singleTreeView) {
        mRecyView = (RecyclerView) singleTreeView.findViewById(R.id.list_examine);
        mRecyView.addItemDecoration(new RecyclerViewSpacesItemDecoration(10));
        mRecyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ListExamineAdapter();
        mRecyView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<RoundExamineEntity> mlist = PatrolManager.getInstance().getExam();
        mAdapter.setDatas(mlist);
        List<FenAllTableEntity> mfenlist = PatrolManager.getInstance().getfenEvent();
        mFengAdapter.setDatas(mfenlist);

    }

    private class ListExamineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private List<RoundExamineEntity> mlist;


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_examine_item, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
            MyViewHolder holders = (MyViewHolder) holder;
            final RoundExamineEntity bean = mlist.get(i);
            holders.mTv.setText("编号:");
            holders.mOrderNum.setText(bean.getOrderNumber());
            holders.mSheng.setText(bean.getSheng());
            holders.mXian.setText(bean.getXian());
            holders.mDiaoPerson.setText(bean.getExmainPerson());
            holders.mFillPerson.setText(bean.getFillPerson());
            holders.mTvDate.setText(bean.getExamineDate());
            holders.mAddress.setText(bean.getAddress());
            holders.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean != null) {
                        Intent intent = new Intent(ListExamineActivity.this, EventEditActivity.class);
                        intent.putExtra("StateBean", (Serializable) bean);
                        startActivity(intent);
                    }
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

        public void setDatas(List<RoundExamineEntity> mlist) {
            this.mlist = mlist;
            notifyDataSetChanged();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private final TextView mTv,mOrderNum, mSheng, mXian, mDiaoPerson, mFillPerson, mTvDate, mAddress;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTv =(TextView)itemView.findViewById(R.id.tv_biao_zhun_order);
                mOrderNum = (TextView) itemView.findViewById(R.id.tv_number);
                mSheng = (TextView) itemView.findViewById(R.id.tv_sheng);
                mXian = (TextView) itemView.findViewById(R.id.tv_xian);
                mDiaoPerson = (TextView) itemView.findViewById(R.id.tv_diao_person);
                mFillPerson = (TextView) itemView.findViewById(R.id.tv_fiil_person);
                mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
                mAddress = (TextView) itemView.findViewById(R.id.tv_address);

            }
        }
    }


    private class ListFenEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private List<FenAllTableEntity> mlist;


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_examine_item, parent, false);
            MyHolder vh = new MyHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
            MyHolder holders = (MyHolder) holder;
            final FenAllTableEntity bean = mlist.get(i);
            holders.mTv.setText("标准地编号:");
            holders.mOrderNum.setText(bean.getLandOrder());
            holders.mSheng.setText(bean.getSheng());
            holders.mXian.setText(bean.getXian());
            holders.mDiaoPerson.setText(bean.getExmainPerson());
            holders.mFillPerson.setText(bean.getFillPerson());
            holders.mTvDate.setText(bean.getExamineDate());
            holders.mAddress.setText(bean.getAddress());
            holders.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bean!=null) {
                        Intent intent = new Intent(ListExamineActivity.this, FenEditActivity.class);
                        intent.putExtra("StateBean", (Serializable) bean);
                        startActivity(intent);
                    }
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

            public void setDatas (List < FenAllTableEntity > mlist) {
                this.mlist = mlist;
                notifyDataSetChanged();
            }

        class MyHolder extends RecyclerView.ViewHolder {
                private final TextView mTv,mOrderNum, mSheng, mXian, mDiaoPerson, mFillPerson, mTvDate, mAddress;

            public MyHolder(View itemView) {
                super(itemView);
                mTv =(TextView)itemView.findViewById(R.id.tv_biao_zhun_order);
                mOrderNum = (TextView) itemView.findViewById(R.id.tv_number);
                mSheng = (TextView) itemView.findViewById(R.id.tv_sheng);
                mXian = (TextView) itemView.findViewById(R.id.tv_xian);
                mDiaoPerson = (TextView) itemView.findViewById(R.id.tv_diao_person);
                mFillPerson = (TextView) itemView.findViewById(R.id.tv_fiil_person);
                mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
                mAddress = (TextView) itemView.findViewById(R.id.tv_address);

            }
        }
    }

}
