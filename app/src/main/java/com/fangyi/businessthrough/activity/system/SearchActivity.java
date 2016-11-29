package com.fangyi.businessthrough.activity.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.bean.search.CharacterParser;
import com.fangyi.businessthrough.bean.search.ClearEditText;
import com.fangyi.businessthrough.bean.search.ClearSideBar;
import com.fangyi.businessthrough.bean.search.GroupMemberBean;
import com.fangyi.businessthrough.bean.search.PinyinComparator;
import com.fangyi.businessthrough.bean.search.SortGroupMemberAdapter;
import com.fangyi.businessthrough.dao.DBBusiness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by FANGYI on 2016/9/19.
 */

public class SearchActivity extends BaseActivity implements SectionIndexer {

    @BindView(R.id.action_bar_back)
    ImageView actionBarBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @BindView(R.id.filter_edit)
    ClearEditText mClearEditText;
    @BindView(R.id.country_lvcountry)
    ListView sortListView;
    @BindView(R.id.title_layout_no_friends)
    TextView tvNofriends;
    @BindView(R.id.title_layout_catalog)
    TextView title;
    @BindView(R.id.title_layout)
    LinearLayout titleLayout;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidrbar)
    ClearSideBar sideBar;

    private static int RESULT_CODE;//结果码
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;

    private int search_code;//查询对象编码
    private String loginUserSysID;//登陆用户ID

    private Map<String, String> infoMap;// 信息
    private CharacterParser characterParser;//汉字转换成拼音的类
    private List<GroupMemberBean> SourceDateList;


    private PinyinComparator pinyinComparator;//根据拼音来排列ListView里面的数据类

    private InputMethodManager imm; // 系统键盘

    private SortGroupMemberAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        selectDataSource();
        setListener();


    }

    /**
     * 选择数据来源
     */
    private void selectDataSource() {

        search_code = this.getIntent().getIntExtra("searchType", -1);//接受前文传过来的intent;赋值给search_code;
        loginUserSysID = this.getIntent().getStringExtra("loginUserSysID");


        characterParser = CharacterParser.getInstance();//返回的是CharacterParser的对象;
        pinyinComparator = new PinyinComparator();//实例化类对象;这个类用来排序;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //接受软键盘输入的编辑文本或其它视图；


        DBBusiness dbBusiness = new DBBusiness(FYApplication.getContext());
        switch (search_code)//判断语句;
        {

            case 0:
                tvTitle.setText("客户选择");
                infoMap = dbBusiness.getCustomers(new String[]{loginUserSysID});
                RESULT_CODE = 0;
                break;
            case 1:
                tvTitle.setText("商品选择");
                infoMap = dbBusiness.getGoods();
                RESULT_CODE = 1;
                break;
            case 4:
                tvTitle.setText("订单客户");
                infoMap = dbBusiness.getOrderedCustomers(loginUserSysID);
                RESULT_CODE = 4;
                break;

            case 6:
                tvTitle.setText("商品选择");
                infoMap = dbBusiness.getOrderedCustomers(loginUserSysID);
                RESULT_CODE = 6;
                break;

            case 7:
                tvTitle.setText("供应商选择");
                infoMap = dbBusiness.getSupplier(loginUserSysID);
                RESULT_CODE = 7;
                break;
//            case 3:
//                title.setText("      选择用户");
//                this.SourceDateList=app.OrderedClientSourceList;
//                break;
        }
        dbBusiness.closeDB();

        LoadingDataTask task = new LoadingDataTask();
        task.execute(infoMap, characterParser);
    }


    private void setListener() {
        actionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCallbackData();
                SearchActivity.this.finish();
            }
        });
    }

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        setCallbackData();
        super.onBackPressed();
    }

    /**
     * 回调默认数据给上一个Activity
     */
    private void setCallbackData() {

        Bundle bundle = new Bundle();
        bundle.putString("sysid", "");
        switch (search_code)//判断语句;
        {

            case 0:
                bundle.putString("name","请点击选择客户名称");
                break;
            case 1:
                bundle.putString("name","必须选择");
                break;
            case 4:
                bundle.putString("name","");
                break;
//            case 3:
//                title.setText("      选择用户");
//                this.SourceDateList=app.OrderedClientSourceList;
//                break;
        }



        setResult(-1, SearchActivity.this.getIntent().putExtras(bundle));//执行回调事件;
    }


    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     *
     * @param sectionIndex
     * @return
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     *
     * @param position
     * @return
     */
    @Override
    public int getSectionForPosition(int position) {
        if (position >= SourceDateList.size())
            return -1;
        return SourceDateList.get(position).getSortLetters().charAt(0);
    }


    private ProgressDialog progressDialog;


    /**
     * <-----------------------异步线程--------------------------->
     */
    class LoadingDataTask extends AsyncTask<Object, Integer, List<GroupMemberBean>> {


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SearchActivity.this, null, "加载中，请稍后……");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<GroupMemberBean> result) {

            initViews(result);

            progressDialog.dismiss();
            super.onPostExecute(result);
        }


        @Override
        protected List<GroupMemberBean> doInBackground(Object... params) {//传过来两个值;一个为查询的map集合params[0]一个为拼音转化类的对象;
            Map<String, String> dataMap = (Map<String, String>) params[0];//dataMap赋值;
            List<GroupMemberBean> SourceDateList = filledData(dataMap, (CharacterParser) params[1]);//将两个参数加传到fillData方法里去;
            return SourceDateList;
        }

        /**
         * 为ListView填充数据
         *
         * @param dataMap
         * @param cp
         * @return
         */
        private List<GroupMemberBean> filledData(Map<String, String> dataMap, CharacterParser cp) {
            List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

            for (Map.Entry<String, String> kv : dataMap.entrySet()) {//遍历集合

                GroupMemberBean sortModel = new GroupMemberBean();//实例化这个类的对象;
                sortModel.setSysId(kv.getKey());
                sortModel.setName(kv.getValue());
                // 汉字转换成拼音
                String pinyin = cp.getSelling(kv.getValue());//将value也就是名字进行
                String sortString = pinyin.substring(0, 1).toUpperCase();//将拼音的第一个字母转换为大写;

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }

                mSortList.add(sortModel);//将这个GroupMemberBean类的对象sortModel存到mSortList中;
            }
            return mSortList;
        }

    }


    private void initViews(List<GroupMemberBean> result) {


        SourceDateList = result;
        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new ClearSideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //设置item的点击监听;
                //position代表的是指针指的第几行;
                GroupMemberBean bean = (GroupMemberBean) adapter.getItem(position);//连接adapter;
                //GroupMemberBean 的类对象bean
                Bundle bundle = new Bundle();
                bundle.putString("sysid", bean.getSysId());
                bundle.putString("name", bean.getName());

                setResult(RESULT_CODE, SearchActivity.this.getIntent().putExtras(bundle));//执行回调事件;

                SearchActivity.this.finish();//当前事件结束;
            }
        });



        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortGroupMemberAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int section = getSectionForPosition(firstVisibleItem);
                int nextSection = getSectionForPosition(firstVisibleItem + 1);
                int nextSecPosition = getPositionForSection(+nextSection);
                if (firstVisibleItem != lastFirstVisibleItem) {

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                            .getLayoutParams();
                    params.topMargin = 0;
                    titleLayout.setLayoutParams(params);

                    title.setText(SourceDateList.get(getPositionForSection(section)).getSortLetters());

                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = titleLayout.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            titleLayout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                titleLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });

        mClearEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 这个时候不需要挤压效果 就把他隐藏掉
                    titleLayout.setVisibility(View.GONE);
                    // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    FilterDataTask fTask = new FilterDataTask();
                    fTask.execute(v.getText().toString());
                    imm.hideSoftInputFromWindow(mClearEditText.getWindowToken(), 0);
                }
                return false;
            }
        });

        // 根据输入框输入值的改变来过滤搜索

        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 这个时候不需要挤压效果 就把他隐藏掉
                titleLayout.setVisibility(View.GONE);
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                FilterDataTask fTask = new FilterDataTask();
                if ((s.toString() == null || s.toString().equals("")) && SearchActivity.this.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    //imm.hideSoftInputFromWindow(mClearEditText.getWindowToken(), 0);
                    fTask.execute(s.toString());
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }


    class FilterDataTask extends AsyncTask<Object, Integer, List<GroupMemberBean>> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SearchActivity.this, null, "查询中，请稍后……");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<GroupMemberBean> result) {

            adapter.updateListView(result);
            if (result.size() == 0) {
                tvNofriends.setVisibility(View.VISIBLE);
            }
            progressDialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected List<GroupMemberBean> doInBackground(Object... params) {
            String filterStr = (String) params[0];
            List<GroupMemberBean> filterDateList = this.filterData(filterStr);
            if (search_code == 2) {
                if (filterDateList != SourceDateList) {
//                    app.goodSourceFilterStr=filterStr;
//                    app.GoodSourceFilterList=filterDateList;
                } else {
//                    app.goodSourceFilterStr=null;
//                    app.GoodSourceFilterList=null;
                }
            }
            return filterDateList;
        }

        private List<GroupMemberBean> filterData(String filterStr) {
            List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = SourceDateList;
                tvNofriends.setVisibility(View.GONE);
            } else {
                filterDateList.clear();
                for (GroupMemberBean sortModel : SourceDateList) {
                    String name = sortModel.getName();
                    if (name.indexOf(filterStr.toString()) != -1) {  //|| characterParser.getSelling(name).startsWith(						                                         //filterStr.toString())
                        filterDateList.add(sortModel);
                    }
                }
            }

            // 根据a-z进行排序
            Collections.sort(filterDateList, pinyinComparator);
            return filterDateList;
        }

    }

}
