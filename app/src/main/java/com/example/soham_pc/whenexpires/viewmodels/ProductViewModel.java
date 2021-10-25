package com.example.soham_pc.whenexpires.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.soham_pc.whenexpires.database.db.ProductsDatabase;
import com.example.soham_pc.whenexpires.database.entity.ProductEntity;

import java.util.List;



public class ProductViewModel extends AndroidViewModel {

    //creates liveData instance
    private final LiveData<List<ProductEntity>> productsList;
    Context context;
    //creates database instance
    private final ProductsDatabase appDatabase;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        appDatabase = ProductsDatabase.getDatabase(this.getApplication());
        productsList = appDatabase.productDao().getAllProducts();
        //allproducts = appDatabase.productDao().getProductCount();
    }

    //expose liveData to other classes
    public LiveData<List<ProductEntity>> getItems() {
        return productsList;
    }


    //executes the  save async task
    public long saveProduct(final ProductEntity productModel) {
        productModel.setDaysRemaining(""+getProductCount(productModel.getProductBcode()));
        //new insertProductAsync(appDatabase).execute(productModel);
        return  appDatabase.productDao().insertProduct(productModel);
    }

    //executes the  save async task
    public int deleteProduct(final ProductEntity productModel) {
        return  appDatabase.productDao().delete(productModel);
    }

    //executes the  save async task
    public int getProductCount(final String barcode) {
        return  appDatabase.productDao().getProductCount(barcode);
    }


}
