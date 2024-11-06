package com.mattiuzzi.fontana.cristhian.lista.model;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    List<MyItem> itens = new ArrayList<>();

    public List<MyItem> getItens() {
        return itens;
    }
}