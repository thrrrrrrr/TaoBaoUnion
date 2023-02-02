package com.thr.taobaounion.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.SearchResult;
import com.thr.taobaounion.presenter.ISearchPresenter;
import com.thr.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.thr.taobaounion.ui.adapter.SearchResultContentAdapter;
import com.thr.taobaounion.ui.custom.TextFlowLayout;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.PresenterManager;
import com.thr.taobaounion.utils.TicketUtil;
import com.thr.taobaounion.utils.ToastUtils;
import com.thr.taobaounion.view.ISearchCallback;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends BaseFragment implements ISearchCallback, TextFlowLayout.OnFlowTextItemClickListener, SearchResultContentAdapter.OnListItemClickListener {

    private ISearchPresenter searchPresenter;

    private String mKeyWord;

    @BindView(R.id.search_flow_text_layout)
    public TextFlowLayout textFlowLayout;

    @BindView(R.id.search_history_flow)
    public TextFlowLayout searchHistoryFlow;

    @BindView(R.id.search_edit_view)
    public EditText searchEditView;

    @BindView(R.id.search_history_bar)
    public RelativeLayout searchHistoryBar;

    @BindView(R.id.search_recommend_bar2)
    public LinearLayout search_recommend_bar2;

    @BindView(R.id.search_result_content)
    public RecyclerView searchResultContent;

    @BindView(R.id.search_refresh_more)
    public SmartRefreshLayout search_refresh_more;

    private SearchResultContentAdapter mContentListAdapter;

    @OnClick(R.id.search_history_del)
    public void deleteClick() {
        searchPresenter.delHistories();
    }

    @OnClick(R.id.search_icon)
    public void searchClick() {
        String s = searchEditView.getText().toString();
        if (!TextUtils.isEmpty(s)) {
            searchPresenter.doSearch(s);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            // 隐藏软键盘
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        setUpState(State.SUCCESS);
        //设置布局管理器
        searchResultContent.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //创建适配器
        mContentListAdapter = new SearchResultContentAdapter();
        //设置适配器
        searchResultContent.setAdapter(mContentListAdapter);

        //loadmore
        search_refresh_more.setEnableRefresh(false);
        search_refresh_more.setEnableLoadMore(true);
        search_refresh_more.setEnableAutoLoadMore(true);
        search_refresh_more.setEnableOverScrollDrag(true);
    }

    @Override
    protected void initListener() {
        textFlowLayout.setOnFlowTextItemClickListener(this);
        searchHistoryFlow.setOnFlowTextItemClickListener(this);
        mContentListAdapter.setOnListItemClickListener(this);
        searchEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchHistoryBar.setVisibility(View.VISIBLE);
                searchHistoryFlow.setVisibility(View.VISIBLE);
                search_recommend_bar2.setVisibility(View.VISIBLE);
                searchResultContent.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_refresh_more.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                searchPresenter.loadMore();
            }
        });
    }

    @Override
    protected void initPresenter() {
        searchPresenter = PresenterManager.getInstance().getSearchPresenter();
        searchPresenter.registerViewCallback(this);
        //获取推荐词
        searchPresenter.getRecommendWords();
        searchPresenter.getHistories();
    }

    @Override
    protected void release() {
        if (searchPresenter != null) {
            searchPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        searchPresenter.doSearch(searchEditView.getText().toString());
    }

    @Override
    public void onHistoriesLoaded(List<String> list) {
        if (list == null || list.size() == 0) {
            searchHistoryFlow.setVisibility(View.GONE);
            searchHistoryBar.setVisibility(View.GONE);
        } else {
            Collections.reverse(list);
            searchHistoryFlow.setTextList(list);
            searchHistoryFlow.setVisibility(View.VISIBLE);
            searchHistoryBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHistoriesDeleted() {

    }

    @Override
    public void onRecommendWordsLoaded(List<String> recommendWords) {
        //回显推荐词
        LogUtils.d(this, recommendWords.toString());
        textFlowLayout.setTextList(recommendWords);
        searchEditView.setHint(recommendWords.get(0));

    }

    @Override
    public void onSearchSuccess(SearchResult searchResult) {

        setUpState(State.SUCCESS);
        LogUtils.d(this, searchResult.toString());
        mContentListAdapter.setData(searchResult.getList());

        searchHistoryBar.setVisibility(View.GONE);
        searchHistoryFlow.setVisibility(View.GONE);
        search_recommend_bar2.setVisibility(View.GONE);
        searchResultContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoadMoreLoaded(SearchResult searchResult) {
        ToastUtils.show("加载了" + searchResult.getList().size() + "条数据");
        mContentListAdapter.addData(searchResult.getList());
        search_refresh_more.finishLoadMore();

    }

    @Override
    public void onLoadMoreError() {
        ToastUtils.show("加载更多失败");
    }

    @Override
    public void onLoadMoreEmpty() {
        ToastUtils.show("没有更多数据了");
    }

    @Override
    public void onFlowItemClick(String text) {
        searchPresenter.doSearch(text);
        searchEditView.setText(text);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

    }

    @Override
    public void onItemClick(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item) {
        TicketUtil.toTicketPage(getContext(), item);
    }
}
