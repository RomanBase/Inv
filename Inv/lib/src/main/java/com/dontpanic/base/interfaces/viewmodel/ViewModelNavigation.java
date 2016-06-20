package com.dontpanic.base.interfaces.viewmodel;


public interface ViewModelNavigation {

    void setViewModel(ViewModel viewModel, Boolean clearBackStack);

    void addViewModel(ViewModel viewModel);

    void navigateBack();

    void setPreviousViewModel();

    <T extends ViewModelObserver> T getObserver();

}
