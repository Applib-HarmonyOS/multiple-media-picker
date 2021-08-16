package com.deveco.multiplemediapicker.slice;

import com.deveco.multiplemediapicker.ResourceTable;
import com.deveco.multiplemediapicker.fraction.OneFraction;
import com.deveco.multiplemediapicker.fraction.TwoFraction;
import com.deveco.multiplemediapicker.interfaces.FractionClickListner;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import java.util.ArrayList;
import java.util.List;

/**
 * HomeAbilitySlice contains TabList and PageSlider.
 *
 * @since 2021-08-03
 */

public class HomeAbilitySlice extends AbilitySlice implements FractionClickListner {

    protected static int selectionTitle;
    protected static String title  = "Select media";
    private int maxSelection = 3;
    TabList tabList;
    PageSlider pageSlider;
    Text appBarTitle;
    Context mContext;
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.DEBUG, 0x00201, "Multiple_media_picker");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_home);
        mContext = this;
        appBarTitle = (Text) findComponentById(ResourceTable.Id_appBar_title);
        tabList = (TabList) findComponentById(ResourceTable.Id_tabs);
        pageSlider = (PageSlider) findComponentById(ResourceTable.Id_page_slider);
        initAppBar();

        if (maxSelection == 0) {
            maxSelection = Integer.MAX_VALUE;
        }
        appBarTitle.setText(title);

        initTabList(tabList);
        setupViewPager(pageSlider);
        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {
                HiLog.info(LABEL, "onPageSliding ");
            }

            @Override
            public void onPageSlideStateChanged(int i) {
                HiLog.info(LABEL, "onPageSlideStateChanged ");
            }

            @Override
            public void onPageChosen(int i) {
                tabList.selectTab(tabList.getTabAt(i));
            }
        });
    }

    private void initTabList(TabList tabList) {
        if (tabList.getTabCount() <= 0) {
            TabList.Tab tab = tabList.new Tab(getContext());
            TabList.Tab tab1 = tabList.new Tab(getContext());
            tab.setText("IMAGES");
            tab1.setText("VIDEOS");
            tabList.addTab(tab);
            tabList.addTab(tab1);
        }
        tabList.selectTabAt(0);
        tabList.setFixedMode(true);
        tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                pageSlider.setCurrentPage(tab.getPosition());
            }

            @Override
            public void onUnselected(TabList.Tab tab) {
                HiLog.info(LABEL, "onUnselected ");
            }

            @Override
            public void onReselected(TabList.Tab tab) {
                HiLog.info(LABEL, "onUnselected");
            }
        });
    }

    //This method set up the tab view for images and videos
    private void setupViewPager(PageSlider viewPager) {
        MultiplePageSliderProvider adapter = new MultiplePageSliderProvider(this);
        adapter.addFragment("Images");
        adapter.addFragment("Videos");
        HiLog.info(LABEL, "Pager Item set done : " + adapter.getCount());
        viewPager.setProvider(adapter);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void itemClicked(String from, List<String> bitmapList, List<String> bucketNames) {
        Intent intent1 = new Intent();
        intent1.setParam("FROM", from);
        intent1.setStringArrayListParam("bucketNames", (ArrayList<String>) bucketNames);
        intent1.setStringArrayListParam("bitmapList", (ArrayList<String>) bitmapList);
        present(new OpenGalleryAbilitySlice(), intent1);
    }

    /**
     * MultiplePageSliderProvider.
     *
     * @since 2021-08-03
     */
    public class MultiplePageSliderProvider extends PageSliderProvider {

        private final ArrayList<Fraction> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        AbilitySlice abilitySlice;

        public MultiplePageSliderProvider(AbilitySlice abilitySlice) {
            this.abilitySlice = abilitySlice;
        }

        @Override
        public int getCount() {
            HiLog.info(LABEL, "getCount : " + mFragmentList.size());
            return this.mFragmentTitleList.size();
        }

        @Override
        public Object createPageInContainer(ComponentContainer componentContainer, int position) {
            Fraction fraction = null;
            if (position == 0) {
                HiLog.info(LABEL, "PageSlider createPageInContainer create 0");
                fraction = new OneFraction(HomeAbilitySlice.this, componentContainer);
            } else if (position == 1) {
                HiLog.info(LABEL, "PageSlider createPageInContainer create 1");
                fraction = new TwoFraction(HomeAbilitySlice.this, componentContainer);
            }
            componentContainer.removeAllComponents();
            if (fraction != null) {
                componentContainer.addComponent(fraction.getComponent());
            }
            return componentContainer;

        }

        @Override
        public void destroyPageFromContainer(
                ComponentContainer componentContainer, int i, Object o) {
            componentContainer.removeComponent((Component) o);
        }

        @Override
        public boolean isPageMatchToObject(Component component, Object o) {

            return true;
        }

        public void addFragment(String title) {
            mFragmentTitleList.add(title);
        }
    }

    private void initAppBar() {
        DirectionalLayout backButton = (DirectionalLayout)
                findComponentById(ResourceTable.Id_appBar_backButton_touchTarget);
        Image backButtonImage = (Image) findComponentById(ResourceTable.Id_appBar_backButton);
        if (backButtonImage.getLayoutDirectionResolved() == Component.LayoutDirection.RTL) {
            Element buttonImage = ElementScatter.getInstance(this).parse(
                    ResourceTable.Graphic_ic_back_mirror);
            backButtonImage.setImageElement(buttonImage);
        }
        backButton.setClickedListener(component -> onBackPressed());
    }
}
