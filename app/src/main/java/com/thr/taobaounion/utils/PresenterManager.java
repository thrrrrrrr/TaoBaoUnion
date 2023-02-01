package com.thr.taobaounion.utils;

import com.thr.taobaounion.presenter.ICategoryPagerPresenter;
import com.thr.taobaounion.presenter.IHomePresenter;
import com.thr.taobaounion.presenter.ISalePresenter;
import com.thr.taobaounion.presenter.ISearchPresenter;
import com.thr.taobaounion.presenter.ITicketPresenter;
import com.thr.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.thr.taobaounion.presenter.impl.HomePresenterImpl;
import com.thr.taobaounion.presenter.impl.SalePresenterImpl;
import com.thr.taobaounion.presenter.impl.SearchPresenterImpl;
import com.thr.taobaounion.presenter.impl.TicketPresenterImpl;

public class PresenterManager {

    private static final PresenterManager ourInstance = new PresenterManager();
    private final ICategoryPagerPresenter categoryPagerPresenter;
    private final IHomePresenter homePresenter;
    private final ITicketPresenter ticketPresenter;
    private final ISalePresenter salePresenter;
    private final ISearchPresenter searchPresenter;

    private PresenterManager() {
        categoryPagerPresenter = new CategoryPagerPresenterImpl();
        homePresenter = new HomePresenterImpl();
        ticketPresenter = new TicketPresenterImpl();
        salePresenter = new SalePresenterImpl();
        searchPresenter = new SearchPresenterImpl();
    }

    public static PresenterManager getInstance() { //PresenterManager单例
        return ourInstance;
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return categoryPagerPresenter;
    }

    public IHomePresenter getHomePresenter() {
        return homePresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return ticketPresenter;
    }

    public ISalePresenter getSalePresenter() {
        return salePresenter;
    }

    public ISearchPresenter getSearchPresenter() {
        return searchPresenter;
    }
}
