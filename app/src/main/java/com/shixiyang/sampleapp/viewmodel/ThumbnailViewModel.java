package com.shixiyang.sampleapp.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ThumbnailViewModel extends ViewModel {
    public ArrayList<HashMap<String, String>> albumsList = new ArrayList<>();
    public String userId;
}
